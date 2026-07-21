# Set up guide

This is a simple guide on how to do the basics

## Editing config

The config lives at `/logic/server/LogicConfig.java`

Most config options pretty straight forward, only thing worth mentioning is pepper keys are stored as a hexadecimal string

## Adding messages

### Client messages

It is recommended that client messages are put in `/protocol/messages/client/`

They must follow this structure:
```java
public class ExampleClientMessage extends PiranhaMessage {
    public int exampleInt;

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

Then in `LogicMessageFactory.createMessageByType`, add your message to the factory:
```java
public static Class<? extends PiranhaMessage> createMessageByType(int id) {
    return switch (id) {
        case 10100 -> ClientHelloMessage.class;
        case 12345 -> ExampleClientMessage.class;
        default -> null;
    };
}
```

### Server messages

Server messages are very similar, except going in `/protocol/messages/server/`

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

No factory is needed for them, as they are created in the `execute()` of a client message instead

## Adding CSV

Adding CSV files allows for you to refer to their values, here's how you add them:

1. Paste the 2 csv folders into `root/assets/` (you may wish to put all assets, for the patcher)
2. `LogicData.LogicDataType` add each csv in the form of `public static final int CSVNAME = 0;`, where 0 is the class/csv id
3. `LogicResources.createDataTableResourcesArray()` for each above csv add this line `DataTables.add(new LogicDataTableResource("PATH", LogicDataType.IDX, 0));`. PATH is relative to the assets folder (e.g. `"csv_logic/characters.csv"`, IDX should be the same as above
4. `LogicDataTable.createItem` add your table to the switch statement:
```java
switch (this.tableIndex) {
    case LogicData.LogicDataType.CHARACTERS:
        data = new LogicCharacterData(row, this);
        break;
    // ...
}
```


In order to access the data in the csv, you must make a class to access it, see `LogicExampleData.java`, or use `csv2java.py` to make your own. (NOTE: this script works on the most common behaviour, some keys which should be arrays may be treated as regular columns, and camelCase is not used)

## Patcher

Patcher hasn't been implemented yet, sorry :/

## Crypto

Crypto is still untested, once implemented this will be refined

### RC4

*todo*

### Pepper

1. Set `LogicConfig.ACTIVATED` to true and `LogicConfig.TYPE` to `CryptoTypes.PEPPER`
2. Fill `SERVER_PUBLIC_KEY` and `CLIENT_SECRET_KEY`
3. Ensure salsa rounds are correct, see `TweetNaclFast`