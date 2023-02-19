package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

enum UIState {
    AcceptingMessages, AwaitingResponse, GuessingTime, AwaitingFirstContact;
}

// Chat client GUI
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

        // Set up the menu bar
    JMenuBar menuBar = new JMenuBar();
    JMenu optionsMenu = new JMenu("Options");
    menuBar.add(optionsMenu);
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });
    optionsMenu.add(exitMenuItem);
    this.setJMenuBar(menuBar);

        // Set up the chat area
        chatTextArea = new JTextArea();
        chatTextArea.setForeground(Color.BLACK);
        chatTextArea.setBackground(Color.WHITE);
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        this.add(scrollPane, BorderLayout.CENTER);

        // Set up the message area
        messageTextField = new JTextField();
        messageTextField.setForeground(Color.BLACK);
        messageTextField.setBackground(Color.WHITE);
        messageTextField.setCaretColor(Color.WHITE);
        // Set up the send button
        sendButton = new JButton("Submit");
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(Color.BLACK);
        sendButton.addActionListener(this);
        sendButton.setBackground(new Color(59, 89, 182));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Tahoma", Font.BOLD, 12));


        messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
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
            chatTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
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
        label = new JLabel(getLabelMessage());
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        sendButton.setEnabled(getSendButtonState());
        messagePanel.add(label, BorderLayout.NORTH);
        messagePanel.revalidate();
    }
    
    private String getLabelMessage() {
        switch (uiState) {
            case AcceptingMessages:
                return "Enter Message: ";
            case AwaitingResponse:
                return "Message Sent, awaiting response.";
            case GuessingTime:
                return "Guessing Time: ";
            case AwaitingFirstContact:
                return "Awaiting contact from client 1";
            default:
                throw new IllegalArgumentException("Invalid UI state");
        }
    }
    
    private boolean getSendButtonState() {
        return uiState == UIState.AcceptingMessages || uiState == UIState.GuessingTime;
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

