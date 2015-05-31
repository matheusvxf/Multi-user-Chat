import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) throws IOException {
		Run();
	}

	@SuppressWarnings("resource")
	private static void Run() throws IOException {
		ServerSocket server = new ServerSocket(12345);
		System.out.println("Door 12345 open!");

		while (true) {
			Socket client = server.accept();

			System.out.println("New connection with the client "
					+ client.getInetAddress().getHostAddress());
			
			ClientHandler clientHandler = new ClientHandler(client.getInputStream());
			Thread thread = new Thread(clientHandler);
			thread.run();
		}
	}

	public static class ClientHandler implements Runnable {
		private InputStream mInputStream;
		
		public ClientHandler(InputStream client){
			mInputStream = client;
		}
		
		@Override
		public void run() {
			Scanner scanner = new Scanner(mInputStream);
			
			while (scanner.hasNextLine()) {
				System.out.println("Server: " + scanner.nextLine());
			}
			
			scanner.close();
		}
	}
}