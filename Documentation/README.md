# Set up guide

This is a simple guide on how to do the basics

## Editing config

The config lives at /logic/server/LogicConfig.java

Most config options pretty straight forward, only thing worth mentioning is pepper keys are stored as a hexadecimal string

## Adding messages

### Client messages

Client messages must go within /protocol/messages/client/ for the factory to find them

They must also follow this structure:
```java
public class ExampleClientMessage extends PiranhaMessage {
    int exampleInt;

    public ExampleClientMessage(byte[] payload, Client session) {
        super(session);
        this.stream = DataStream.getByteStream(payload);
    }

    @Override
    public void decode() {
        // Decode stream here
        this.exampleInt = this.stream.readInt();
    }
    
    @Override
    public void execute() {
        // Logic is handled here
        // This to send a server message, use
        new ExampleServerMessage(session).send(true);
        // true means doNotEncrypt, not needed if LogicConfig.Crypto.ACTIVATED is false
    }
    
    @Override
    public int getMessageType() {
        return 12345;
    }

    // This is only needed if the message version is not 0
    @Override
    public int getMessageVersion() {
        return 1;
    }
}
```

### Server messages

Server messages are very similar, except going in /protocol/messages/server/

Their structure is below:
```java
public class ExampleServerMessage extends PiranhaMessage {
    public ExampleServerMessage(Client session) {
        super(session);
        this.stream = DataStream.getByteStream(new byte[0]);
    }

    @Override
    public void encode() {
        // Encode stream here
        this.stream.writeInt(1);
    }
    
    @Override
    public int getMessageType() {
        return 23456;
    }

    // This is only needed if the message version is not 0
    @Override
    public int getMessageVersion() {
        return 1;
    }
}
```

## Adding CSV

Adding CSV files allows for you to refer to their values, here's how you add them:

1. Paste the 2 csv folders into /logic/assets/ (you may wish to put all assets, for the patcher)
2. `LogicData.LogicDataType` add each csv in the form of `public static final int CSVNAME = 0;`, where 0 is the class/csv id
3. `LogicResources.createDataTableResourcesArray()` for each above csv add this line `DataTables.add(new LogicDataTableResource("PATH", LogicDataType.IDX, 0));`. PATH is relative to the assets folder (e.g. `"csv_logic/characters.csv"`, IDX should be the same as above
4. `LogicDataTables.TABLE_COUNT`: set to the number of tables with a class id


In order to access the data in the csv, you must make a class to access it, see `LogicExampleData.java`, or use `csv2java.py` to make your own. (NOTE: this script works on the most common behaviour, some keys which should be arrays may be treated as regular columns)

## Patcher

Patcher hasn't been implemented yet, sorry :/