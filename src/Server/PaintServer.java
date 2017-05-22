//package com.unn;
package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class PaintServer {
    private static final int PORT = 2551;

    // private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {

        private Socket socket;
        //private ObjectInputStream in;
        //private ObjectOutputStream out;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                //out = new ObjectOutputStream(this.socket.getOutputStream());
                //in = new ObjectInputStream( this.socket.getInputStream());
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                writers.add(out);
                System.out.println(writers.isEmpty() ? "yes" : "no");
                //while (!Thread.currentThread().isInterrupted()) {
                while (true) {
                    String json = in.readLine();
                    //System.out.println(json);
                    //for (ObjectOutputStream writer : writers) {
                    for (PrintWriter writer : writers) {
                        System.out.println(writer.toString());
                        writer.write(json);
                        writer.flush();
                    }
                    // in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    writers.remove(out);
                    if (in != null) try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
