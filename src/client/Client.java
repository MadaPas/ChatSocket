package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {
//defining the arguments
    public Client(final String host, final int port) {
        //creating the socket
        Socket socket;
        //checking if the connection can be fulfilled
        while (true) {
            try {
                socket = new Socket(host, port);
                break;
                //if not, we catch the exception and display the error-message
            } catch (IOException e) {
                System.err.println("Could not connect to " + host + " at port " + port + ".");
                System.exit(0);

            }
        }
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            //initially using runnable method but intelij suggests to use lambda hmmmmmmmmmmm WHY?
            new Thread(() -> {
                String inputLine;
                try {
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Server down.");
                System.exit(0);
            }).start();

            String input;
            while ((input = stdIn.readLine()) != null) {
                out.println(input);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Disconnected.");
        System.exit(0);
    }


    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Client <host> <port number>");
            System.exit(1);
        }
        new Client(args[0], Integer.parseInt(args[1]));
    }
}