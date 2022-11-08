package hse.ce.jameskok.jigsawmultiplayer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Part of the server that registers new users.
 */
final class ServerRegistrar extends Thread {
    ServerSocket serverSocket;
    Server server;

    ServerRegistrar(ServerSocket serverSocket, Server server) {
        this.serverSocket = serverSocket;
        this.server = server;
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (server.getClientHandlers().size() >= server.maxPlayers()) {
                    clientSocket.close();
                    continue;
                }
                ClientHandler handler = new ClientHandler(clientSocket, server);
                server.getClientHandlers().add(handler);
                handler.start();
            } catch (IOException e) {
                break;
            }
        }
        for (var handler : server.getClientHandlers()) {
            handler.interrupt();
        }
    }
}
