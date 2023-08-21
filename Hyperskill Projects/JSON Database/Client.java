package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {

    @Parameter(names = "-t", description = "Type of the request")
    private String type = "";

    @Parameter(names = "-i", description = "Index of the cell")
    private int index = -1;

    @Parameter(names = "-m", description = "Value to save in the database")
    private String message = "";

    public static void main(String[] args) {
        Main main = new Main();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(main)
                .build();

        jCommander.parse(args);

        if (!main.validateArguments()) {
            jCommander.usage();
            return;
        }

        main.run();
    }

    private boolean validateArguments() {
        if (type.isEmpty() || index <= 0) {
            return false;
        }
        return !"set".equalsIgnoreCase(type) || !message.isEmpty();
    }

    public void run() {
        String address = "127.0.0.1";
        int port = 23456;

        try (Socket socket = new Socket(address, port)) {
            System.out.println("Client started!");

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String messageToSend = type + " " + index;
            if ("set".equalsIgnoreCase(type)) {
                messageToSend += " " + message;
            }
            output.writeUTF(messageToSend);
            System.out.println("Sent: " + messageToSend);

            String response = input.readUTF();
            System.out.println("Received: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
