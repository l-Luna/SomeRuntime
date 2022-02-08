package some_runtime.interpreter;

import some_runtime.model.Type;

import java.util.HashMap;
import java.util.Map;

public final class InterpObjects{
	
	// this counts as a heap, right?
	private static final HashMap<Long, ObjectData> OBJECTS = new HashMap<>();
	private static long curObj = 0;
	
	public static final ObjectRefValue NULL = new ObjectRefValue(-1);
	
	public record ObjectRefValue(long code) implements InterpValue{
		public InterpValue copy(){
			return new ObjectRefValue(code);
		}
		
		public int intValue(){
			throw new IllegalStateException("Tried to perform a comparison on an object!");
		}
	}
	
	public record ObjectData(Type type, Map<String, InterpValue> fields){
	
	}
	
	public static ObjectData dereference(ObjectRefValue ref){
		if(ref.equals(NULL))
			throw new NullRefException("Tried to reference a null value!");
		return OBJECTS.get(ref.code());
	}
	
	public static ObjectRefValue allocate(ObjectData data){
		OBJECTS.put(curObj, data);
		var ret = new ObjectRefValue(curObj);
		curObj++;
		return ret;
	}
}