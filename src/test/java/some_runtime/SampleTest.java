package some_runtime;

import org.junit.jupiter.api.Test;
import some_runtime.interpreter.FunctionInterpreter;

import java.util.List;

public class SampleTest{
	
	@Test
	void simpleParseTest(){
		IrParser.parseIr("""
				void empty(){
					nop
					return
				}
				""");
		
		IrParser.parseIr("""
				void empty1(){
					nop
					return
				}
				
				int empty2(){
					i_const 3
					return_val
				}
				
				decimal empty3(){
					d_const 32.7
					return_val
				}
				""");
		
		var fns = IrParser.parse("""
				void main(){
					i_const 10
					dup
					call_sys print
					i_const 2
					i_add
					i_neg
					call_sys print
					d_const 44.5
					dup
					call_sys print
					d2i
					dup
					call_sys print
					i2d
					call_sys print
				}
				
				void main2(){
					d_const 4
					d_store 0
					d_const 8
					d_load 0
					d_div
					call_sys print
					d_load 0
					call_sys print
				}
				
				void main3(){
					call_static other
					call_sys print
				}
				
				decimal other(){
					i_const 99
					dup
					call_sys print
					return_val
				}
				""");
		FunctionInterpreter.execute(fns.functions.get("main"), List.of(), fns);
		FunctionInterpreter.execute(fns.functions.get("main2"), List.of(), fns);
		FunctionInterpreter.execute(fns.functions.get("main3"), List.of(), fns);
	}
}