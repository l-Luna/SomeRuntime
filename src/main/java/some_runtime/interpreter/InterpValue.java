package some_runtime.interpreter;

import static some_runtime.interpreter.InterpObjects.*;
import static some_runtime.interpreter.InterpValue.*;

public sealed interface InterpValue permits ObjectRefValue, ArrayValue, DecimalValue, IntValue{
	
	InterpValue copy();
	int intValue();
	
	record IntValue(long value) implements InterpValue{
		public InterpValue copy(){
			return new IntValue(value());
		}
		
		public int intValue(){
			return (int)value();
		}
	}
	
	record DecimalValue(double value) implements InterpValue{
		public InterpValue copy(){
			return new DecimalValue(value());
		}
		
		public int intValue(){
			return (int)value();
		}
	}
	
	record ArrayValue(InterpValue... elements) implements InterpValue{
		public InterpValue copy(){
			return new ArrayValue(elements);
		}
		
		public int intValue(){
			throw new IllegalStateException("Tried to perform a comparison on an array!");
		}
	}
}
