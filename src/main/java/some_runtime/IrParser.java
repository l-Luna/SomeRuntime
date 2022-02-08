package some_runtime;

import some_runtime.model.Function;
import some_runtime.model.Instruction;
import some_runtime.model.Opcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IrParser{
	
	private static int linePointer = 0;
	private static List<String> lines;
	
	public static RuntimeContext parse(String ir){
		var context = new RuntimeContext();
		var fns = parseIr(ir);
		context.functions = fns.stream().collect(Collectors.toMap(x -> x.desc().name(), x -> x));
		return context;
	}
	
	public static List<Function> parseIr(String ir){
		List<Function> ret = new ArrayList<>();
		lines = ir.lines()
				.map(String::strip)
				.map(x -> x.split("#"))
				.map(x -> x[0])
				.filter(x -> !x.isEmpty())
				.toList();
		
		for(linePointer = 0; linePointer < lines.size(); linePointer++){
			String line = lines.get(linePointer);
			if(line == null || line.isBlank())
				continue;
			ret.add(parseFunc());
		}
		
		return ret;
	}
	
	private static Function parseFunc(){
		// type[str] name[str] '(' (type[str] name[str])* ')' '{'
		StringBuilder type = new StringBuilder(), name = new StringBuilder();
		List<Function.NameAndType> params = new ArrayList<>();
		List<Instruction> instrs = new ArrayList<>();
		var chars = cur().toCharArray();
		
		int i = 0;
		char c = chars[i];
		while(!Character.isWhitespace(c)){
			type.append(c);
			c = chars[++i];
		}
		while(c != '('){
			if(!Character.isWhitespace(c))
				name.append(c);
			c = chars[++i];
		}
		c = chars[++i];
		StringBuilder pType = new StringBuilder(), pName = new StringBuilder();
		boolean typing = true;
		while(c != ')' && c != ','){
			if(Character.isWhitespace(c)){
				if(!pType.toString().isBlank())
					typing = false;
			}else
				(typing ? pType : pName).append(c);
			c = chars[++i];
			if(c == ',' || c == ')'){
				if(c == ',')
					c = chars[++i];
				params.add(new Function.NameAndType(pName.toString(), pType.toString()));
				typing = true;
			}
		}
		while(c != ')')
			c = chars[++i];
		while(c != '{')
			c = chars[++i];
		
		linePointer++;
		
		while(!cur().equals("}")){
			chars = cur().toCharArray();
			i = 0;
			StringBuilder word = new StringBuilder();
			c = chars[i];
			while(i < chars.length && !Character.isWhitespace(c)){
				word.append(c);
				i++;
				if(i < chars.length)
					c = chars[i];
			}
			Opcode opcode = Opcode.OPCODES.stream().filter(x -> x.name().equalsIgnoreCase(word.toString())).findFirst().orElseThrow();
			List<Object> args = new ArrayList<>(opcode.operands().length);
			for(String operand : opcode.operands()){
				c = chars[++i];
				StringBuilder arg = new StringBuilder();
				while(i < chars.length && !Character.isWhitespace(c)){
					arg.append(c);
					i++;
					if(i < chars.length)
						c = chars[i];
				}
				if(operand.endsWith(":int"))
					args.add(Long.parseLong(arg.toString()));
				else if(operand.endsWith(":decimal"))
					args.add(Double.parseDouble(arg.toString()));
				else
					args.add(arg.toString());
			}
			instrs.add(new Instruction(opcode, args));
			linePointer++;
		}
		
		return new Function(new Function.NameAndType(name.toString(), type.toString()), params, instrs);
	}
	
	private static String cur(){
		return lines.get(linePointer);
	}
}