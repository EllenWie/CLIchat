package com.db.chat;

import java.io.*;
import java.net.Socket;
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
        String s = in.readLine();
        lock.unlock();
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
