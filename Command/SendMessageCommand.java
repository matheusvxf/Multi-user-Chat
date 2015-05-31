
public class SendMessageCommand extends Command {
	private static final long serialVersionUID = 1L;
	private String mMessage;
	
	public SendMessageCommand() {
		this(null);
	}
	
	public SendMessageCommand(String message){
		super(Type.SEND_MESSAGE);
		mMessage = message;
	}
	
	public SendMessageCommand setMessage(String message){
		mMessage = message;
		return this;
	}

	public String getMessage() {
		return mMessage;
	}

}
