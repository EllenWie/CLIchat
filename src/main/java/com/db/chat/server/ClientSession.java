package com.db.chat.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientSession {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public Lock lock;

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
        lock = new ReentrantLock();
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public String readMessage() throws IOException {
        lock.lock();
        String readLine = null;
        try {
            if (in != null) {
                readLine = in.readLine();
            } else {
                System.out.println("in is null");
            }
        } catch (SocketException e) {
            System.out.println("could not read message");
        }
        lock.unlock();
        return readLine;
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
