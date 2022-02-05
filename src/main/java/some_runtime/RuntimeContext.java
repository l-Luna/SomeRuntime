package some_runtime;

import some_runtime.model.Function;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext{
	
	public Map<String, Function> functions = new HashMap<>();
}