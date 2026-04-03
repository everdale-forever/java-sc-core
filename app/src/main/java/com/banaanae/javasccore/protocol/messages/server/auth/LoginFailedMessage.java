package com.banaanae.javasccore.protocol.messages.server.auth;

import com.banaanae.javasccore.Server.Client;
import com.banaanae.javasccore.protocol.PiranhaMessage;
import com.banaanae.javasccore.titan.datastream.DataStream;

public class LoginFailedMessage extends PiranhaMessage {
    public int errorCode;
    public String fingerprint;
    public String redirectUri;
    public String contentUri;
    public String updateUri;
    public String reason;
    public int maintenanceTime;
    
    public LoginFailedMessage(Client session) {
        super(session);
        this.stream = DataStream.getByteStream(new byte[0]);
    }
    
    @Override
    public void encode() {
        stream.writeInt(this.errorCode);
        stream.writeString(this.fingerprint);
        stream.writeString(this.redirectUri);
        stream.writeString(this.contentUri);
        stream.writeString(this.updateUri);
        stream.writeString(this.reason);
        stream.writeVInt(this.maintenanceTime);
    }
    
    @Override
    public int getMessageType() {
        return 20103;
    }
}
