package some_runtime.interpreter;

import some_runtime.RuntimeContext;
import some_runtime.model.Function;
import some_runtime.model.Instruction;

import java.util.*;
import java.util.function.DoubleSupplier;
import java.util.function.LongSupplier;

import static some_runtime.interpreter.InterpValue.DecimalValue;
import static some_runtime.interpreter.InterpValue.IntValue;

public class FunctionInterpreter{
	
	public static Optional<InterpValue> execute(Function fn, List<InterpValue> params, RuntimeContext ctx){
		List<InterpValue> locals = new ArrayList<>(params);
		
		int maxLocals = (int)fn.body().stream()
				.filter(x -> x.opcode().name().endsWith("_STORE"))
				.mapToLong(x -> (long)x.arguments().get(0) + 1)
				.max()
				.orElse(0);
		while(locals.size() < maxLocals)
			locals.add(null);
		
		Deque<InterpValue> stack = new LinkedList<>();
		LongSupplier lTop = () -> ((IntValue)stack.pop()).value();
		DoubleSupplier dTop = () -> ((DecimalValue)stack.pop()).value();
		
		List<Instruction> body = fn.body();
		for(int i = 0; i < body.size(); i++){
			Instruction instr = body.get(i);
			
			// can i have non-enum constant switches please :3
			switch(instr.opcode().name()){
				case "NOP" -> {}
				
				case "DUP" -> stack.push(stack.element().copy());
				case "POP" -> stack.pop();
				// TODO: UP* codes
				
				case "I_CONST" -> stack.push(new IntValue((long)instr.arguments().get(0)));
				case "D_CONST" -> stack.push(new DecimalValue((double)instr.arguments().get(0)));
				// TODO: S_CONST
				
				case "I_ADD" -> stack.push(new IntValue(lTop.getAsLong() + lTop.getAsLong()));
				case "I_SUB" -> stack.push(new IntValue(lTop.getAsLong() - lTop.getAsLong()));
				case "I_MUL" -> stack.push(new IntValue(lTop.getAsLong() * lTop.getAsLong()));
				case "I_DIV" -> stack.push(new IntValue(lTop.getAsLong() / lTop.getAsLong()));
				case "I_MOD" -> stack.push(new IntValue(lTop.getAsLong() % lTop.getAsLong()));
				case "I_AND" -> stack.push(new IntValue(lTop.getAsLong() & lTop.getAsLong()));
				case "I_OR" -> stack.push(new IntValue(lTop.getAsLong() | lTop.getAsLong()));
				case "I_XOR" -> stack.push(new IntValue(lTop.getAsLong() ^ lTop.getAsLong()));
				case "I_NEG" -> stack.push(new IntValue(-lTop.getAsLong()));
				case "I_NOT" -> stack.push(new IntValue(~lTop.getAsLong()));
				
				case "D_ADD" -> stack.push(new DecimalValue(dTop.getAsDouble() + dTop.getAsDouble()));
				case "D_SUB" -> stack.push(new DecimalValue(dTop.getAsDouble() - dTop.getAsDouble()));
				case "D_MUL" -> stack.push(new DecimalValue(dTop.getAsDouble() * dTop.getAsDouble()));
				case "D_DIV" -> stack.push(new DecimalValue(dTop.getAsDouble() / dTop.getAsDouble()));
				case "D_MOD" -> stack.push(new DecimalValue(dTop.getAsDouble() % dTop.getAsDouble()));
				case "D_NEG" -> stack.push(new DecimalValue(-dTop.getAsDouble()));
				case "D_BITS" -> stack.push(new IntValue(Double.doubleToLongBits(dTop.getAsDouble())));
				
				case "D2I" -> stack.push(new IntValue((long)dTop.getAsDouble()));
				case "I2D" -> stack.push(new DecimalValue(lTop.getAsLong()));
				
				case "JUMP" -> i = (int)instr.arguments().get(0) - 1;
				case "JUMP_IF_EQZ" -> {
					if(stack.pop().intValue() == 0)
						i = (int)(long)instr.arguments().get(0) - 1;
				}
				case "JUMP_IF_NEQZ" -> {
					if(stack.pop().intValue() != 0)
						i = (int)(long)instr.arguments().get(0) - 1;
				}
				case "JUMP_IF_GTZ" -> {
					if(stack.pop().intValue() > 0)
						i = (int)(long)instr.arguments().get(0) - 1;
				}
				case "JUMP_IF_LTZ" -> {
					if(stack.pop().intValue() < 0)
						i = (int)(long)instr.arguments().get(0) - 1;
				}
				
				case "CALL_STATIC" -> {
					var target = (String)instr.arguments().get(0);
					if(!ctx.functions.containsKey(target))
						throw new IllegalStateException("no function: " + target);
					var targetFn = ctx.functions.get(target);
					var amnt = targetFn.parameters().size();
					List<InterpValue> tParams = new ArrayList<>(amnt);
					for(int j = 0; j < amnt; j++)
						tParams.add(stack.pop());
					execute(targetFn, tParams, ctx).ifPresent(stack::push);
				}
				// TODO: CALL_VIRT
				case "CALL_SYS" -> { // TODO: replace with lib/""native"" functions
					var target = (String)instr.arguments().get(0);
					if(target.equals("print")){
						var top = stack.pop();
						if(top instanceof IntValue iv)
							System.out.println(iv.value());
						else if(top instanceof DecimalValue dv)
							System.out.println(dv.value());
						else
							System.out.println(top);
					}
					if(target.equals("stack"))
						System.out.println(stack);
				}
				
				case "RETURN" -> {
					return Optional.empty();
				}
				case "RETURN_VAL" -> {
					return Optional.of(stack.pop());
				}
				
				case "I_CMP" -> stack.push(new IntValue(Long.compare(lTop.getAsLong(), lTop.getAsLong())));
				case "D_CMP" -> stack.push(new IntValue(Double.compare(dTop.getAsDouble(), dTop.getAsDouble())));
				
				case "I_STORE", "D_STORE", "O_STORE" -> locals.set((int)(long)instr.arguments().get(0), stack.pop());
				case "I_LOAD", "D_LOAD", "O_LOAD" -> stack.push(locals.get((int)(long)instr.arguments().get(0)));
			}
		}
		
		return Optional.empty();
	}
}