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
                
                String[] temp = message.split("Gj2Hc5PqKl9nFtRm");
                chatClient.prevResponse = temp[0];
                chatClient.chatTextArea.append("Received: " + temp[1] + "\n");

                chatClient.updateUIState(UIState.GuessingTime);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
