package com.example;
import java.io.IOException;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.ArrayList;

import java.util.List;


public class Server {

    private ServerSocket serverSocket;

    private List<Connection> clients;


    public Server(int port) {

        try {

            serverSocket = new ServerSocket(port);

            clients = new ArrayList<>();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public void startServer() {

        System.out.println("Server is running and waiting for connections...");

        try {

            while (true) {

                Socket clientSocket = serverSocket.accept();

                Connection connection = new Connection(this, clientSocket);

                clients.add(connection);

                connection.start();

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public void forwardMessageToAll(String sender, String message) {

        for (Connection client : clients) {

            client.sendMessage("FORWARD_TO_ALL", sender, null, message);

        }

    }


    public void forwardMessageToOne(String sender, String recipient, String message) {

        for (Connection client : clients) {

            if (client.getClientName().equals(recipient)) {

                client.sendMessage("FORWARD_TO_ONE", sender, recipient, message);

                return;

            }

        }

        // gestire quando il recipient e' vuoto 

    }


    public void handleSingleClient(Connection client) {

        if (clients.size() == 1) {

            client.sendMessage("SINGLE_CLIENT_WARNING", null, null, "You are the only connected client. Cannot forward messages.");

        }

    }


    public void removeClient(Connection client) {

        clients.remove(client);

    }


    public static void main(String[] args) {

        Server server = new Server(12345);

        server.startServer();

    }

}


