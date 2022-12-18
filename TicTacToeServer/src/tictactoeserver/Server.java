package tictactoeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    ServerSocket serverSocket;
    Socket socket;
    private static Server instance;
    private int indexHandlerMax = -1;
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private Server() {
        System.out.println("Server inside constructor outside try");
        try {
            System.out.println("Server inside constructor inside try");
            serverSocket = new ServerSocket(5005);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            socket = serverSocket.accept();
                            System.out.println("An acception happened !!!!!!!!!");
                            indexHandlerMax++;
                            new Handler(socket, indexHandlerMax);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            });
        } catch (IOException ex) {
            System.out.println("server at catch");
            ex.printStackTrace();
        }

    }

    public int getIndexHandlerMax() {
        return indexHandlerMax;
    }

    public void setIndexHandlerMax(int indexHandlerMax) {
        this.indexHandlerMax = indexHandlerMax;
    }

    void stopAllClients() {
        for (Handler client : Handler.handlerList) {
            client.stop();
        }
    }

}
