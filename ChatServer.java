import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// test comment ??rjuuhf h

public class ChatServer {

    private static final int PORT = 5000;
    private static final int MAX_CLIENTS = 2;

    static int connections = 0;
    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started");

                while (connections < MAX_CLIENTS) {
                    Socket socket1 = serverSocket.accept();
                    System.out.println("Client 1 connected");
                    connections++;
                    Socket socket2 = serverSocket.accept();
                    System.out.println("Client 2 connected");
                    connections++;

                    new Thread(new ClientHandler(socket1, socket2)).start();
                    new Thread(new ClientHandler(socket2, socket1)).start();
                }

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Rejected connection from additional client");

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Sorry, there are already two clients connected to the server.");
                    clientSocket.close();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket from;
    private Socket to;

    public ClientHandler(Socket from, Socket to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(from.getInputStream()));
            PrintWriter out = new PrintWriter(to.getOutputStream(), true);

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Received message: " + message);
                out.println(message);
            }

            from.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}