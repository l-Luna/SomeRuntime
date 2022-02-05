package some_runtime.interpreter;

public sealed interface InterpValue{
	
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
	
	// TODO: objects
	record ObjectValue(/* type, fields, identity? */) implements InterpValue{
		public InterpValue copy(){
			return new ObjectValue();
		}
		
		public int intValue(){
			return 0;
		}
	}
}
