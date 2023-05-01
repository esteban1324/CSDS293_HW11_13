package Motherboard;

import java.util.*;

/**
 * MotherBoard class that holds all the connected devices.
 */
public final class Motherboard {

    //identifier associated with the device.
    private final Map<String, Device> deviceLookup;

    /**
     * Constructor for the Motherboard class.
     */
    public Motherboard() {
        deviceLookup = new HashMap<>();
    }

    /**
     * Returns the device associated with the given identifier.
     *
     * @param device the identifier of the device to be returned.
     * @return the device associated with the given identifier.
     */
    public Device getDevice(String device) {
        MotherboardException.checkNullId(device);
        return deviceLookup.get(device);
    }

    /**
     * Connects the device by adding it to the motherboard.
     *
     * @param device the device to be added to the motherboard.
     */
    public void add(Device device) {
        MotherboardException.checkNullDevice(device);
        MotherboardException.checkNullId(device.toString());

        deviceLookup.put(device.toString(), device);
    }

    /**
     * Disconnects the device by removing it from the motherboard.
     *
     * @param id the identifier of the device to be removed.
     */
    public void remove(String id) {
        MotherboardException.checkNullId(id);

        deviceLookup.remove(id);
    }

    /**
     * Sends a message to all the devices as long as they're connected to the motherboard.
     * @param a device sending the message to others.
     * @param m message to be sent.
     */
    public void broadcast(Device a, Message m) {

        MotherboardException.checkNullDevice(a);
        MotherboardException.checkNullMessage(m);

        for (Device recipients : deviceLookup.values()) {
            a.forward(m, recipients);
        }
    }

    /**
     * @return a string representation of the motherboard.
     */
    public String toString() {
        return deviceLookup.toString();
    }

}