
public class Logger {

	public Logger() {
		
	}
	
	public static void clientOnline(String name){
		System.out.println(name + " is online.");
	}
	
	public static void clientLeaved(String name){
		System.out.println(name + " leaved the chat.");
	}

	public static void d(String TAG, String message){
		System.out.println(TAG + ": " + message);
	}
}
