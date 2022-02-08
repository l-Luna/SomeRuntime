package some_runtime.model;

import some_runtime.RuntimeContext;
import some_runtime.interpreter.FunctionInterpreter;
import some_runtime.interpreter.InterpValue;

import java.util.List;
import java.util.Optional;

public record Function(NameAndType desc, List<NameAndType> parameters, List<Instruction> body) implements Executable{
	
	public Optional<InterpValue> execute(List<InterpValue> arguments, RuntimeContext ctx){
		return FunctionInterpreter.execute(this, arguments, ctx);
	}
}