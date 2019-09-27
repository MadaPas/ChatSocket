package server;

import java.io.IOException;
import java.util.Map;

public class ClientHandler extends Thread {
    private User user;
    private Map<String, User> users;
    private static String[] commands = {"/quit", "/q"};

    public ClientHandler(User user, Map<String, User> users) {
        super("Chat server thread");
        this.user = user;
        this.users = users;
    }

    private void sendAll(String message) {
        for (User user : users.values()) {
            System.out.println("Sending to port " + user.getSocket().getPort());
            user.sendMessage(message);
        }
    }

    private void getUniqueName() {
        boolean unique = false;
        while (!unique) {
            user.sendMessage("Please introduce yourself:");
            user.setName(user.getMessage());
            if (user.getName().isEmpty())
                user.sendMessage("Username cannot be empty");
            else if (users.containsKey(user.getName()))
                user.sendMessage("This username has already been taken.");
            else
                unique = true;
        }
    }

    private void introduceNewUser() {
        System.out.println("New user is " + user + ".");
        user.getOutput().println("Hello, " + user + ".");
        sendAll("User " + user + " has joined the chat.");
    }

    private void registerUser() {
        getUniqueName();
        introduceNewUser();
        users.put(user.getName(), user);
    }

    @Override
    public void run() {
        try {
            String inputLine;
            boolean quit = false;
            registerUser();

            while (!quit && (inputLine = user.getInput().readLine()) != null) {
                int command;
                for (command = 0; command < commands.length; ++command)
                    if (inputLine.startsWith(commands[command]))
                        break;

                switch (command) {
                    case 0:
                    case 1:
                        quit = true;
                        break;
                    default:
                        System.out.println(inputLine);
                        sendAll(user + ": " + inputLine);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        users.remove(user.getName());
        sendAll("User " + user + " has disconnected.");

    }
}