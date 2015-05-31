import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Run();
	}

	private static void Run() throws UnknownHostException, IOException {
		Socket client = new Socket("127.0.0.1", 12345);

		System.out.println("The client connected to the server!");

		ServerHandler serverHandler = new ServerHandler(client.getInputStream());
		Thread thread = new Thread(serverHandler);
		thread.run();

		Scanner keyboard = new Scanner(System.in);
		PrintStream output = new PrintStream(client.getOutputStream());

		while (keyboard.hasNextLine()) {
			output.println(keyboard.nextLine());
		}

		output.close();
		keyboard.close();
		client.close();
	}

	public static class ServerHandler implements Runnable {
		InputStream mInputStream;
		
		public ServerHandler(InputStream inputStream){
			mInputStream = inputStream;
		}
		
		@Override
		public void run() {
			Scanner scanner = new Scanner(mInputStream);
			
			while(scanner.hasNextLine()){
				System.out.println("Client: " + scanner.nextLine());
			}
			scanner.close();
		}

	}
}