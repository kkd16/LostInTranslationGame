import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClient extends JFrame implements ActionListener {

    JTextArea chatTextArea;
    private JTextField messageTextField;
    private PrintWriter out;

    public ChatClient() {
        // Set up the chat window
        this.setTitle("Chat Client");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the chat area
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        this.add(scrollPane, BorderLayout.CENTER);

        // Set up the message area
        messageTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageTextField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        this.add(messagePanel, BorderLayout.SOUTH);

        // Show the chat window
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = messageTextField.getText();
        chatTextArea.append("You: " + message + "\n");
        out.println("You: " + message);
        messageTextField.setText("");
    }

    public static void main(String[] args) {

        ChatClient chatClient = new ChatClient();

        try {
            try (Socket socket = new Socket("localhost", 5000)) {
                System.out.println("Connected to server");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                chatClient.out = new PrintWriter(socket.getOutputStream(), true);

                // Start a new thread to handle incoming messages from the server
                new Thread(new ServerHandler(in, chatClient)).start();

                // Read messages from the user and send them to the server
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String message = userInput.readLine();
                    chatClient.out.println(message);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ServerHandler implements Runnable {
    private BufferedReader in;
    private ChatClient chatClient;

    public ServerHandler(BufferedReader in, ChatClient chatClient) {
        this.in = in;
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                chatClient.chatTextArea.append("Server: " + message + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}