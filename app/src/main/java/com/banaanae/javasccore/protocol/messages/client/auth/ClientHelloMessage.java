package com.banaanae.javasccore.protocol.messages.client.auth;

import com.banaanae.javasccore.Server.Client;
import com.banaanae.javasccore.logic.server.LogicVersion;
import com.banaanae.javasccore.protocol.PiranhaMessage;
import com.banaanae.javasccore.protocol.messages.server.auth.LoginFailedMessage;
import com.banaanae.javasccore.protocol.messages.server.auth.ServerHelloMessage;
import com.banaanae.javasccore.titan.datastream.DataStream;
import com.banaanae.javasccore.titan.enums.LoginFailedReason;

public class ClientHelloMessage extends PiranhaMessage {
    int protocol;
    int keyVersion;
    int major;
    int minor;
    int build;
    String fingerprintSha;
    int deviceType;
    int appStore;
    
    public ClientHelloMessage(byte[] payload, Client session) {
        super(session);
        this.stream = DataStream.getByteStream(payload);
    }
    @Override
    public void decode() {
        this.protocol = this.stream.readInt();
        this.keyVersion = this.stream.readInt();
        this.major = this.stream.readInt();
        this.minor = this.stream.readInt();
        this.build = this.stream.readInt();
        this.fingerprintSha = this.stream.readString();
        this.deviceType = this.stream.readInt();
        this.appStore = this.stream.readInt();
    }
    
    @Override
    public void execute() {
        if (this.major != LogicVersion.major) {
            final LoginFailedMessage loginFailed = new LoginFailedMessage(this.session);
            loginFailed.errorCode = LoginFailedReason.UPDATE.getCode();
            loginFailed.updateUri = "https://github.com/everdale-forever/java-sc-core";
            loginFailed.reason = "Yippee there's an update!";
            loginFailed.send(true);
            return;
        }
        
        new ServerHelloMessage(session).send(true);
    }
    
    @Override
    public int getMessageType() {
        return 10100;
    }
}
