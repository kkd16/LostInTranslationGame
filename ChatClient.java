import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try {
            try (Socket socket = new Socket("localhost", 5000)) {
                System.out.println("Connected to server");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Start a new thread to handle incoming messages from the server
                new Thread(new ServerHandler(in)).start();

                // Read messages from the user and send them to the server
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String message = userInput.readLine();
                    out.println(message);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ServerHandler implements Runnable {
    private BufferedReader in;

    public ServerHandler(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Received message: " + message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}