import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

                String ret = message + " SEPERATE " + ChangeLanguage.randomizer(message);
                out.println(ret);
            }

            from.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class Translator {
    static String translate(String langFrom, String langTo, String text) throws IOException {
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbwh1Pro09OkcKpjc9f-8alntvl4lTwQoAhTFKRreLo8TtutxfNqmV6oky1jGYnygeEtSw/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}

class ChangeLanguage{
    static String randomizer(String message) throws IOException {
        String[] FirstGrouplanguages = {"it", "fr", " pt", "es", "ro","ca", "rm", "de", "nl", " sv"};
        String[] SecondGrouplanguages = {"zh", "ja", "sm", "ko", "te", "th", "ug", "vi", "ru", "sr"};
        String[] ThirdGrouplanguages = {"az", "ar", "hy", "hi", "he", "fa", "ht", "af", "sw", "tg"};

        String tracer = "en";

        String translated = message;
        for (int i = 0; i < 3; i++) {
            for (String lang : FirstGrouplanguages) {
                translated = Translator.translate(tracer, lang, translated);
                tracer = lang;
                //out.println(ret);
            }
            for (String lang : SecondGrouplanguages) {
                translated = Translator.translate(tracer, lang, translated);
                tracer = lang;
                //out.println(ret);
            }
            for (String lang : ThirdGrouplanguages) {
                translated = Translator.translate(tracer, lang, translated);
                tracer = lang;
                //out.println(ret);
            }
        }
        String ret = Translator.translate(tracer, "en", translated);
        return ret;
    }


}
