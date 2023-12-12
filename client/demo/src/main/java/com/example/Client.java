package com.example;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.Socket;


public class Client {

    private Socket socket;

    private BufferedReader input;

    private PrintWriter output;

    private BufferedReader userInput;


    public Client(String serverAddress, int serverPort) {

        try {

            socket = new Socket(serverAddress, serverPort);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(socket.getOutputStream(), true);

            userInput = new BufferedReader(new InputStreamReader(System.in));

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public void connectToServer(String name) {

        try {

            output.println(name);

            String response = input.readLine();

            System.out.println(response);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public void sendMessageToAll(String message) {

        output.println("MSG_TO_ALL");

        output.println(message);

    }


    public void sendMessageToOne(String recipient, String message) {

        output.println("MSG_TO_ONE");

        output.println(recipient);

        output.println(message);

    }


    public void closeConnection() {

        output.println("CLOSE_CONNECTION");

        try {

            socket.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public void startListening() {

        new Thread(() -> {

            try {

                while (true) {

                    String message = input.readLine();

                    if (message != null) {

                        System.out.println(message);

                    }

                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }).start();

    }


    public static void main(String[] args) {

        Client client = new Client("localhost", 12345);

        client.startListening();


        // Connection

        System.out.print("Enter your name: ");

        String name = client.getUserInput();

        client.connectToServer(name);


        // Chat

        while (true) {

            System.out.print("Enter your message (type 'exit' to close): ");

            String message = client.getUserInput();

            if (message.equalsIgnoreCase("exit")) {

                client.closeConnection();

                break;

            } else {

                System.out.print("Send to all (A) or to one person (O)? ");

                String choice = client.getUserInput();

                if (choice.equalsIgnoreCase("A")) {

                    client.sendMessageToAll(message);

                } else if (choice.equalsIgnoreCase("O")) {

                    System.out.print("Enter recipient's name: ");

                    String recipient = client.getUserInput();

                    client.sendMessageToOne(recipient, message);

                } else {

                    System.out.println("Invalid choice.");

                }

            }

        }

    }


    private String getUserInput() {

        try {

            return userInput.readLine();

        } catch (IOException e) {

            e.printStackTrace();

            return null;

        }

    }

}
