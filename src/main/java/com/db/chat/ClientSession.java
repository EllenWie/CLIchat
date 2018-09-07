package com.db.chat;

import java.io.*;
import java.net.Socket;

public class ClientSession {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                socket.getOutputStream())));
        in = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                socket.getInputStream())));

    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public String readMessage() throws IOException {
        String s = in.readLine();
        System.out.println("readed message: "+ s);
        return s;
    }

    public boolean isNewMessageAvailable() {
        try {
            return in.ready();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
