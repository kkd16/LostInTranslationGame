package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
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

                String ret = message + "Gj2Hc5PqKl9nFtRm" + ChangeLanguage.randomizer(message);
                out.println(ret);
            }

            from.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}