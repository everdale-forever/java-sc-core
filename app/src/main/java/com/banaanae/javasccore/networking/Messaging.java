package com.banaanae.javasccore.networking;

import com.banaanae.javasccore.titan.ArrayUtils;
import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Messaging {
    protected Socket session;
    protected ByteStream stream; 
    
    public abstract void encode();
    public abstract void decode();
    public abstract void execute();
    public abstract int getMessageType();
    public abstract int getMessageVersion();
    
    public Messaging(Socket session) {
        this.session = session;
    }
    
    public void send(boolean doNotEncrypt) {
        try {
            byte[] payload = getPayload(doNotEncrypt);
            
            OutputStream out = session.getOutputStream();
            out.write(payload, 0, 0);
        } catch (IOException ex) {
            System.getLogger(Messaging.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void send() {
        try {
            byte[] payload = getPayload(false);
            
            OutputStream out = session.getOutputStream();
            out.write(payload, 0, 0);
        } catch (IOException ex) {
            System.getLogger(Messaging.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private byte[] getPayload(boolean doNotEncrypt) throws IOException {
        if (this.getMessageType() < 20000) {
            throw new IOException("Attempted to send client message");
        }
        
        this.encode();
        byte[] header = writeHeader(this.getMessageType(), this.stream.length, this.getMessageVersion());
        byte[] payload = ArrayUtils.concat(header, this.stream.buffer);
        
        return payload;
    }
    
    private byte[] writeHeader(int id, int length, int version) {
        byte[] header = new byte[7];

        header[0] = (byte) ((id >>> 8) & 0xFF);
        header[1] = (byte) (id & 0xFF);

        header[2] = (byte) ((length >>> 16) & 0xFF);
        header[3] = (byte) ((length >>> 8) & 0xFF);
        header[4] = (byte) (length & 0xFF);

        header[5] = (byte) ((version >>> 8) & 0xFF);
        header[6] = (byte) (version & 0xFF);

        return header;
    }
}
