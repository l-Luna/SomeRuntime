package some_runtime.model;

import some_runtime.RuntimeContext;
import some_runtime.interpreter.InterpValue;

import java.util.List;
import java.util.Optional;

public interface Executable{
	
	// I don't like having Interp* leaking into model stuff
	Optional<InterpValue> execute(List<InterpValue> arguments, RuntimeContext ctx);
	
	default List<NameAndType> parameters(){
		return List.of();
	}
}