package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final List<String> database = new ArrayList<>(1000);

    static {
        for (int i = 0; i < 1000; i++) {
            database.add("");
        }
    }

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started!");

            while (true) {
                Socket socket = server.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                String message = input.readUTF();
                String response = handleRequest(message);
                output.writeUTF(response);

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String handleRequest(String message) {
        String[] parts = message.split(" ", 3);
        String command = parts[0];
        int index = Integer.parseInt(parts[1]);

        switch (command.toLowerCase()) {
            case "get":
                return get(index);
            case "set":
                return set(index, parts[2]);
            case "delete":
                return delete(index);
            case "exit":
                return "OK";
            default:
                return "ERROR";
        }
    }

    private static String get(int index) {
        if (index < 1 || index > 1000 || database.get(index - 1).isEmpty()) {
            return "ERROR";
        }
        return database.get(index - 1);
    }

    private static String set(int index, String value) {
        if (index < 1 || index > 1000) {
            return "ERROR";
        }
        database.set(index - 1, value);
        return "OK";
    }

    private static String delete(int index) {
        if (index < 1 || index > 1000) {
            return "ERROR";
        }
        database.set(index - 1, "");
        return "OK";
    }
}
