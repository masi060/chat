package com.example;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.Socket;


public class Connection extends Thread {

    private Server server;

    private Socket socket;

    private BufferedReader input;

    private PrintWriter output;

    private String clientName;


    public Connection(Server server, Socket socket) {

        this.server = server;

        this.socket = socket;

        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public String getClientName() {

        return clientName;

    }


    public void sendMessage(String type, String sender, String recipient, String content) {

        StringBuilder message = new StringBuilder();

        message.append("Type: ").append(type).append("\n");

        message.append("Sender: ").append(sender).append("\n");

        if (recipient != null) {

            message.append("Recipient: ").append(recipient).append("\n");

        }

        message.append("Content: ").append(content).append("\n");

        output.println(message.toString());

    }


    @Override

    public void run() {

        try {


            clientName = input.readLine();

            System.out.println(clientName + " connected.");


            while (true) {

                String message = input.readLine();

                if (message != null) {

                    handleMessage(message);

                }

            }

        } catch (IOException e) {

            //gestire disconnessione client

            System.out.println(clientName + " disconnected.");

            server.removeClient(this);

        }

    }


    private void handleMessage(String message) {


    }

}