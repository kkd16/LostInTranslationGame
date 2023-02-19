package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

enum UIState {
    AcceptingMessages, AwaitingResponse, GuessingTime, AwaitingFirstContact;
}

public class ChatClient extends JFrame implements ActionListener {

    JTextArea chatTextArea;
    private JTextField messageTextField;
    private PrintWriter out;
    private JLabel label;
    private JPanel messagePanel;
    private JButton sendButton;
    private UIState uiState = UIState.AcceptingMessages;
    String prevResponse;


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
        sendButton = new JButton("Submit");
        sendButton.addActionListener(this);

        messagePanel = new JPanel(new BorderLayout());
        
        messagePanel.add(messageTextField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        this.add(messagePanel, BorderLayout.SOUTH);

        // Show the chat window
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = messageTextField.getText();

        if (uiState == UIState.AcceptingMessages) {
            chatTextArea.append("You: " + message + "\n");
            out.println(message);
            messageTextField.setText("");
            updateUIState(UIState.AwaitingResponse);
        } else if (uiState == UIState.GuessingTime) {
            if ( prevResponse.replaceAll("[^a-zA-Z0-9\\s]","").equals(message.replaceAll("[^a-zA-Z0-9\\s]","")) ) {
                chatTextArea.append("Guessed correct\n");
            } else {
                chatTextArea.append("Guessed incorrect\n");
            }
            
            messageTextField.setText("");
            updateUIState(UIState.AcceptingMessages);
        }

    }

    void updateUIState(UIState newState) {

        uiState = newState;

        if (label != null)
            messagePanel.remove(label);
        // Accepting messages
        if (uiState == UIState.AcceptingMessages) {
            label = new JLabel("Enter Message: ");
            sendButton.setEnabled(true);
            messagePanel.add(label, BorderLayout.NORTH);
        // Awaiting response
        } else if (uiState == UIState.AwaitingResponse) {
            label = new JLabel("Message Sent, awaiting response.");
            sendButton.setEnabled(false);
            messagePanel.add(label, BorderLayout.NORTH);
        // Guessing time
        } else if (uiState == UIState.GuessingTime) {
            label = new JLabel("Guesing Time: ");
            sendButton.setEnabled(true);
            messagePanel.add(label, BorderLayout.NORTH);
        // Awaiting First Contact
        } else if (uiState == UIState.AwaitingFirstContact) {
            label = new JLabel("Awaiting contact from client 1");
            sendButton.setEnabled(false);
            messagePanel.add(label, BorderLayout.NORTH);
        }
        messagePanel.revalidate();
    }

    public static void main(String[] args) {

        ChatClient chatClient = new ChatClient();

        try {
            try (Socket socket = new Socket("localhost", 5000)) {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                chatClient.out = new PrintWriter(socket.getOutputStream(), true);

                chatClient.updateUIState(UIState.AcceptingMessages);

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

