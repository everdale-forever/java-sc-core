package com.banaanae.javasccore;

import com.banaanae.javasccore.logic.server.LogicConfig;
import com.banaanae.javasccore.protocol.LogicMessageFactory;
import com.banaanae.javasccore.networking.MessageHandler;
import com.banaanae.javasccore.networking.Packet;
import com.banaanae.javasccore.networking.Queue;
import com.banaanae.javasccore.titan.crypto.StreamEncrypter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Server {
    private ServerSocket server;
    public static HashMap<Integer, Client> sessions = new HashMap<>();
    
    public class Client {
        private final Socket socket;

        public Client(Socket socket) { this.socket = socket; }
        
        public int id;
        public Queue queue;
        public StreamEncrypter crypto;
        public MessageHandler handler;
        
        public void log(String text) {
            final String log = String.format(
                    "\u001B[34m[%s]\u001B[0m %s",
                    this.getRemoteSocketAddress(),
                    text
            );
            System.out.println(log);
        }
        public void warn(String text) {
            final String log = String.format(
                    "\u001B[33m[%s]\u001B[0m %s",
                    this.getRemoteSocketAddress(),
                    text
            );
            System.out.println(log);
        }
        public void error(String text) {
            final String log = String.format(
                    "\u001B[31m[%s]\u001B[0m %s",
                    this.getRemoteSocketAddress(),
                    text
            );
            System.out.println(log);
        }
        
        public InputStream getInputStream() throws IOException { return socket.getInputStream(); }
        public OutputStream getOutputStream() throws IOException { return socket.getOutputStream(); }
        public SocketAddress getRemoteSocketAddress() { return socket.getRemoteSocketAddress(); }
        public void setTcpNoDelay(boolean on) throws SocketException { socket.setTcpNoDelay(on); }
        public void setKeepAlive(boolean on) throws SocketException { socket.setKeepAlive(on); }
        public void setSoTimeout(int timeout) throws SocketException { socket.setSoTimeout(timeout); }
        public void close() throws IOException { socket.close(); }
        public boolean isClosed() { return socket.isClosed(); }
    }
    
    public void start(int port) throws IOException {
        LogicMessageFactory.loadMessages();
        
        server = new ServerSocket(port);
        System.out.println("Server started at " + server.getLocalSocketAddress());
        
        while (!server.isClosed()) {
            Socket session = server.accept();
            Client client = new Client(session);
            
            client.setTcpNoDelay(true);
            client.setKeepAlive(true);
            client.setSoTimeout(LogicConfig.Session.TIMEOUT_SECONDS * 1000);
            
            client.crypto = LogicConfig.Crypto.ACTIVATED
                    ? new StreamEncrypter(LogicConfig.Crypto.TYPE)
                    : null;
            client.id = getLastSessionId() + 1;
            client.queue = new Queue(LogicConfig.Queue.MAX_SIZE, false);
            sessions.put(client.id, client);
            
            client.log("Client connected (sessionId: " + client.id + ")");
            
            client.handler = new MessageHandler(client);
            
            new Thread(() -> {
                try (InputStream in = client.getInputStream()) {
                    byte[] buf = new byte[4096];
                    int read;
                    while ((read = in.read(buf)) != -1) {
                        byte[] bytes = Arrays.copyOf(buf, read);
                        onData(client, bytes);
                    }
                    onClose(client);
                } catch (IOException e) {
                    onError(client, e);
                } finally {
                    try { client.close(); } catch (IOException ignored) {}
                }
            }).start();
        }
    }
    
    private void onData(Client client, byte[] bytes) {
        client.queue.push(bytes);
        
        switch (client.queue.state) {
            case Queue.QUEUE_OVERFILLED -> {
                if (LogicConfig.Queue.WARN_ON_OVERFILL)
                    client.warn("Queue is overfilled! Queue size:" + client.queue.size());
                if (LogicConfig.Queue.DISCONNECT_ON_OVERFILL) {
                    destroySession(client, "warn", "Queue is overfilled! Disconnecting...");
                    return;
                }
            }
            case Queue.QUEUE_PUSHED_MORE_THAN_EXPECTED -> client.warn(
                    String.format("Queue got more bytes than expected! Expected: %d size. Got: %d size."
                        , client.queue.getQueueExpectedSize(), client.queue.size()));
            case Queue.QUEUE_DETECTED_MERGED_PACKETS -> client.warn("Queue detected merged packets!");
        }

        if (!client.queue.isBusy()) {
            final Object queueBytes = client.queue.release();

            if (queueBytes instanceof Packet[] packets) {
                client.log("Handling merged packets...");
                for(Packet packet : packets) {
                    if (LogicConfig.Crypto.ACTIVATED)
                        packet.bytes = client.crypto.decrypt(packet.id, packet.bytes);

                    client.handler.handle(packet.id, packet.bytes);
                }

                client.log("Merged packets was handled.");
                return;
            }

            final Packet message = new Packet((byte[]) queueBytes);

            if (LogicConfig.Crypto.ACTIVATED)
                message.bytes = client.crypto.decrypt(message.id, message.bytes);

            client.handler.handle(message.id, message.bytes);
        }
    }
    
    private void onClose(Client client) {
        destroySession(client, "log", "Client disconnected.");
    }
    
    private void onError(Client client, IOException error) {
        if (error.getMessage().contains("ECONNRESET")) {
            destroySession(client, "log", "Client disconnected.");
        }

        try {
            destroySession(client, "err", "A wild error!\n" + error.getMessage());
        } catch (Exception err) {}
    }

    public void stop() throws IOException {
        if (server != null && !server.isClosed()) server.close();
    }
    
    private int getLastSessionId() {
        Set sessionIds = sessions.keySet();
        
        return (int) (sessions.isEmpty() ? 0 : Collections.max(sessionIds));
    }
    
    public void destroySession(Client session, String logType, String reason) {
        if (session == null)
            return;
        
        switch (logType) {
            case "warn" -> session.warn(reason);
            case "error" -> session.error(reason);
            default -> session.log(reason);
        }
        
        try {
            session.close();
        } catch (IOException ex) {
            System.getLogger(Server.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sessions.remove(session.id);
    }
}
