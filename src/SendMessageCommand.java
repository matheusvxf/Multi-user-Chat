
public class SendMessageCommand extends Command {
	private static final long serialVersionUID = 1L;
	private String mMessage;
	private Integer mSource;
	private Integer mDestination;
	
	public final Integer BROADCAST_ADDRESS = 0xffffffff;
	
	public SendMessageCommand() {
		super(Type.SEND_MESSAGE);
	}
	
	public SendMessageCommand setMessage(String message){
		mMessage = message;
		return this;
	}

	public String getMessage() {
		return mMessage;
	}
	
	public SendMessageCommand setSource(Integer source){
		mSource = source;
		return this;
	}
	
	public Integer getSource(){
		return mSource;
	}
	
	public SendMessageCommand setDestination(Integer destination){
		mDestination = destination;
		return this;
	}
	
	public Integer getDestination(){
		return mDestination;
	}
}
