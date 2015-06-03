
public class CommandErrorNotification extends Event {
	private static final long serialVersionUID = 1L;

	public enum Type{
		REGISTER_CLIENT
	}
	
	Type mType;
	
	public CommandErrorNotification(Type type) {
		super(Event.Type.COMMAND_ERROR);
		mType = type;
	}
	
	public Type getErrorType(){
		return mType;
	}
	
	public void setType(Type type){
		mType = type;
	}
}
