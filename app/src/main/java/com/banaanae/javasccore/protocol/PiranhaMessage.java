package com.banaanae.javasccore.protocol;

import com.banaanae.javasccore.networking.Messaging;
import com.banaanae.javasccore.titan.datastream.DataStream;
import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import java.net.Socket;

public class PiranhaMessage extends Messaging {
    Socket session;
    ByteStream stream;
    public PiranhaMessage(Socket session) {
        super(session);
        this.session = session;
        this.stream = DataStream.getByteStream(new byte[0]);
    }
    
    public void encode() {}
    public void decode() {}
    public void execute() {}
    public void getMessageType() {}
    public void getMessageVersion() {}
}
