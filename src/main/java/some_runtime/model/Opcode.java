package some_runtime.model;

import java.util.List;

public record Opcode(int code, String name, String... operands){
	
	private static int cur = 0;
	
	public static final Opcode
			NOP = new Opcode(cur++, "NOP");
	
	public static final Opcode
			DUP = new Opcode(cur++, "DUP"),
			POP = new Opcode(cur++, "POP"),
			UP = new Opcode(cur++, "UP"),
			UP2 = new Opcode(cur++, "UP2"),
			UP3 = new Opcode(cur++, "UP3");
	
	public static final Opcode
			I_CONST = new Opcode(cur++, "I_CONST", "value:int"),
			D_CONST = new Opcode(cur++, "D_CONST", "value:decimal"),
			S_CONST = new Opcode(cur++, "S_CONST", "value");
	
	public static final Opcode
			I_ADD = new Opcode(cur++, "I_ADD"),
			I_SUB = new Opcode(cur++, "I_SUB"),
			I_MUL = new Opcode(cur++, "I_MUL"),
			I_DIV = new Opcode(cur++, "I_DIV"),
			I_NEG = new Opcode(cur++, "I_NEG"),
			I_MOD = new Opcode(cur++, "I_MOD"),
			I_AND = new Opcode(cur++, "I_AND"),
			I_OR = new Opcode(cur++, "I_OR"),
			I_XOR = new Opcode(cur++, "I_XOR"),
			I_NOT = new Opcode(cur++, "I_NOT");
	
	public static final Opcode
			D_ADD = new Opcode(cur++, "D_ADD"),
			D_SUB = new Opcode(cur++, "D_SUB"),
			D_MUL = new Opcode(cur++, "D_MUL"),
			D_DIV = new Opcode(cur++, "D_DIV"),
			D_NEG = new Opcode(cur++, "D_NEG"),
			D_MOD = new Opcode(cur++, "D_MOD"),
			D_BITS = new Opcode(cur++, "D_BITS");
	
	public static final Opcode
			D2I = new Opcode(cur++, "D2I"),
			I2D = new Opcode(cur++, "I2D");
	
	public static final Opcode
			JUMP = new Opcode(cur++, "JUMP", "target:int"),
			JUMP_IF_EQZ = new Opcode(cur++, "JUMP_IF_EQZ", "target_instr:int"),
			JUMP_IF_NEQZ = new Opcode(cur++, "JUMP_IF_NEQZ", "target_instr:int"),
			JUMP_IF_GTZ = new Opcode(cur++, "JUMP_IF_GTZ", "target_instr:int"),
			JUMP_IF_LTZ = new Opcode(cur++, "JUMP_IF_LTZ", "target_instr:int");
	
	public static final Opcode
			CALL_STATIC = new Opcode(cur++, "CALL_STATIC", "target_fn"),
			CALL_VIRT = new Opcode(cur++, "CALL_VIRT", "target_fn"),
			CALL_SYS = new Opcode(cur++, "CALL_SYS", "target_fn");
	
	public static final Opcode
			RETURN = new Opcode(cur++, "RETURN"),
			RETURN_VAL = new Opcode(cur++, "RETURN_VAL");
	
	public static final Opcode
			I_CMP = new Opcode(cur++, "I_CMP"),
			D_CMP = new Opcode(cur++, "D_CMP");
	
	public static final Opcode
			I_STORE = new Opcode(cur++, "I_STORE", "idx:int"),
			D_STORE = new Opcode(cur++, "D_STORE", "idx:int"),
			O_STORE = new Opcode(cur++, "O_STORE", "idx:int");
	
	public static final Opcode
			I_LOAD = new Opcode(cur++, "I_LOAD", "idx:int"),
			D_LOAD = new Opcode(cur++, "D_LOAD", "idx:int"),
			O_LOAD = new Opcode(cur++, "O_LOAD", "idx:int");
	
	public static List<Opcode> OPCODES = List.of(
			NOP,
			DUP, POP, UP, UP2, UP3,
			I_CONST, D_CONST, S_CONST,
			I_ADD, I_SUB, I_MUL, I_DIV, I_NEG, I_MOD, I_AND, I_OR, I_XOR, I_NOT,
			D_ADD, D_SUB, D_MUL, D_DIV, D_NEG, D_MOD, D_BITS,
			D2I, I2D,
			JUMP, JUMP_IF_EQZ, JUMP_IF_NEQZ, JUMP_IF_GTZ, JUMP_IF_LTZ,
			CALL_STATIC, CALL_VIRT, CALL_SYS,
			RETURN, RETURN_VAL,
			I_CMP, D_CMP,
			I_STORE, D_STORE, O_STORE,
			I_LOAD, D_LOAD, O_LOAD);
}