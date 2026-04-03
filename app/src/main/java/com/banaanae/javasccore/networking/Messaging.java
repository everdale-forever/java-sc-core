package com.banaanae.javasccore.networking;

import com.banaanae.javasccore.Debugger;
import com.banaanae.javasccore.Server.Client;
import com.banaanae.javasccore.titan.ArrayUtils;
import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Messaging {
    protected Client session;
    protected ByteStream stream; 
    
    public abstract void encode();
    public abstract void decode();
    public abstract void execute();
    public abstract int getMessageType();
    public abstract int getMessageVersion();
    
    public Messaging(Client session) {
        this.session = session;
    }
    
    public void send(boolean doNotEncrypt) {
        try {
            byte[] payload = getPayload(doNotEncrypt);
            
            OutputStream out = session.getOutputStream();
            out.write(payload, 0, payload.length);
            out.flush();
        } catch (IOException ex) {
            System.getLogger(Messaging.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void send() {
        try {
            byte[] payload = getPayload(false);
            
            OutputStream out = session.getOutputStream();
            out.write(payload, 0, payload.length);
            out.flush();
        } catch (IOException ex) {
            System.getLogger(Messaging.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private byte[] getPayload(boolean doNotEncrypt) {
        if (this.getMessageType() < 20000) {
            Debugger.error("Attempted to send client message");
        }
        
        this.encode();
        byte[] header = writeHeader(this.getMessageType(), this.stream.buffer.length, this.getMessageVersion());
        byte[] payload = ArrayUtils.concat(header, this.stream.buffer);
        payload = ArrayUtils.concat(payload, new byte[] {(byte) 0xFF, (byte) 0xFF
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});
        
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
