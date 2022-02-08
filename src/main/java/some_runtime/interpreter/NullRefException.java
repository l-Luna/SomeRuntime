package some_runtime.interpreter;

public class NullRefException extends RuntimeException{
	
	public NullRefException(String message){
		super(message);
	}
}