package Motherboard;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This is the Device Class that will be used to send and
 * receive messages across different devices within the motherboard.
 */
public final class Device{
    private final Logger logger = Logger.getLogger(Device.class.getName());

    //identifier of the string, could be the recipient identifier.
    private final String identifier;

    //each device has a port or multiple ports.
    private final Map<Integer, Device> devicePorts;


    //each device will have a message received from a different device
    private final Map<Message, Device> deviceMessages;

    private Device(String identifier) {
        assert identifier != null;

        devicePorts = new HashMap<>();
        deviceMessages = new HashMap<>();

        this.identifier = identifier;
    }

    /**
     * Factory method to create a new device
     * @param identifier the identifier of the device
     * @return Device object
     */
    public static Device createDevice(String identifier) {
        MotherboardException.checkNullId(identifier);

        String newId = MotherboardException.identifierCheck(identifier);

        return new Device(newId);
    }

    /**
     * This will add a port to the device
     * @param port the port to add
     */
    public void addPort(int port) {
        //make sure port can be in range.
        int portAssign = MotherboardException.checkPortNum(port);
        logger.info("port assignment: " + portAssign);

        devicePorts.put(portAssign, this);
    }


    /**
     * This will remove a port from the device
     * @param port the port to remove
     */
    public void removePort(int port) {
        if (devicePorts.containsKey(port))
            devicePorts.remove(port);
        else
            logger.info("couldn't find the application");
    }

    /**
     * This will forward the message from current app to another application
     * @param m the message to forward
     * @param recipient the device to forward the message to
     */
    public void forward(Message m, Device recipient) {
        MotherboardException.checkNullDevice(recipient);
        MotherboardException.checkNullMessage(m);

        if (recipient.hasPort(m, recipient))
            this.send(m, recipient);
    }

    /**
     * This device will receive the message from a different device
     * @param m the message
     * @param sender the device sending the message
     */
    public void receive(Message m, Device sender) {
        MotherboardException.checkNullDevice(sender);
        MotherboardException.checkNullMessage(m);

        this.accept(m, sender);
    }

    //the actual send operation used for the forward method.
    private void send(Message m, Device device) {
        assert m != null : "null message object";
        assert m.payload() != null : "message contents is null";
        assert m.identifier() != null : "id is null";
        assert device != null : "app is null";

        //put messages into the recipient and designate the sender
        //add the port connection from the sender.
        device.deviceMessages.put(m, this);
    }

    //the actual receive operation used for the receive method.
    private void accept(Message m, Device device) {
        assert m != null : "null message object";
        assert m.payload() != null : "message contents is null";
        assert m.identifier() != null : "id is null";
        assert device != null : "app is null";

        if (this.hasPort(m, this)) {
            //designates the sender of the message
            deviceMessages.put(m, device);
        }
    }

    /**
     * This will return the device ports associated with each device, it is immutable.
     * @return device ports map.
     */
     public Map<Integer, Device> devicePorts() {
        return Collections.unmodifiableMap(devicePorts);
    }


    /**
     * This will return the device messages associated with each device, it is immutable.
     * @return device messages map.
     */
     public Map<Message, Device> deviceMessages() {
        return Collections.unmodifiableMap(deviceMessages);
    }

    //checks if the device has the port
    private boolean hasPort(Message m, Device recipient){
        assert m != null : "null message object";
        assert m.payload() != null : "message contents is null";
        assert m.identifier() != null : "id is null";
        assert recipient != null : "app is null";

        return recipient.devicePorts.containsKey(m.port());
    }

    /**
     * This will return the string representation of the device, using its identifier.
     * @return the string representation
     */
    @Override
    public String toString() {
        return identifier;
    }
}
