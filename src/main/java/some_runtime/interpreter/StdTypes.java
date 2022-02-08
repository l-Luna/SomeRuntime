package some_runtime.interpreter;

import some_runtime.RuntimeContext;
import some_runtime.model.Executable;
import some_runtime.model.NameAndType;
import some_runtime.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static some_runtime.interpreter.InterpObjects.*;
import static some_runtime.interpreter.InterpValue.*;

// speedrunning print(string) lets go
public class StdTypes{
	
	public static final Type OBJECT;
	public static final Type STRING;
	
	static{
		OBJECT = new Type("Object", null, Map.of());
		
		Executable print = (args, ctx) -> {
			System.out.println(fromInterp(args.get(0)));
			return Optional.empty();
		};
		Executable concat = new Executable(){
			public Optional<InterpValue> execute(List<InterpValue> args, RuntimeContext ctx){
				return Optional.of(string(fromInterp(args.get(0)) + fromInterp(args.get(1))));
			}
			public List<NameAndType> parameters(){
				return List.of(new NameAndType("other", "String"));
			}
		};
		STRING = new Type("String", OBJECT, Map.of("print", print, "concat", concat));
	}
	
	private static String fromInterp(InterpValue v){
		return fromInterp((ArrayValue)dereference((ObjectRefValue)v).fields().get("characters"));
	}
	
	private static String fromInterp(ArrayValue v){
		StringBuilder builder = new StringBuilder(v.elements().length);
		for(InterpValue element : v.elements())
			if(element instanceof IntValue i)
				builder.append((char)i.value());
		return builder.toString();
	}
	
	public static InterpValue string(String s){
		var array = s.toCharArray();
		List<InterpValue> chars = new ArrayList<>(array.length);
		for(char c : array)
			chars.add(new IntValue(c));
		var arrayVal = new ArrayValue(chars.toArray(InterpValue[]::new));
		return InterpObjects.allocate(new ObjectData(STRING, Map.of("characters", arrayVal)));
	}
}