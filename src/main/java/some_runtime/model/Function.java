package some_runtime.model;

import java.util.List;

public record Function(NameAndType desc, List<NameAndType> parameters, List<Instruction> body){
	
	public record NameAndType(String name, String type){}
}