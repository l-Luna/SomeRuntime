package some_runtime.model;

import java.util.Map;

// TODO: function overloading
public record Type(String name, Type supertype, Map<String, Executable> methods){
	
	public Executable resolve(String name){
		if(methods.containsKey(name))
			return methods.get(name);
		if(supertype != null)
			return supertype.resolve(name);
		throw new IllegalStateException("No virtual method " + name);
	}
}