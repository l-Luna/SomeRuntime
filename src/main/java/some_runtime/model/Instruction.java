package some_runtime.model;

import java.util.List;

public record Instruction(Opcode opcode, List<Object> arguments){
}