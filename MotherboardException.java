package Motherboard;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Motherboard Exception class that checks for any bad inputs.
 */
public final class MotherboardException extends Exception {

    private static final Logger logger = Logger.getLogger(MotherboardException.class.getName());

    /**
     * Constructor for the MotherboardException class.
     */
    public MotherboardException() {
    }

    /**
     * Checking for null messages and invalid messages.
     * @param m message to be checked.
     * @throws IllegalArgumentException if message is null or invalid.
     */
    public static void checkNullMessage(Message m) throws IllegalArgumentException {
        if (hasNullMessage(m)) {
            logger.log(Level.SEVERE, "message can't be null");
            throw new IllegalArgumentException("message is null");
        } else if (hasInvalidMessage(m)) {
            logger.log(Level.SEVERE, "invalid message inputted, please try again");
            throw new IllegalArgumentException("message has empty id or payload is invalid");
        }
    }

    //checking for a null message obj or null identifier passed in
    private static boolean hasNullMessage(Message m){
        return m == null || m.identifier() == null;
    }

    //checking for an empty identifier or null payload
    private static boolean hasInvalidMessage(Message m){
        return m.identifier().isEmpty() || m.payload() == null;
    }


    /**
     * Checking for null devices.
     * @param device device to be checked.
     * @throws IllegalArgumentException if device is null.
     */
    public static void checkNullDevice(Device device) throws IllegalArgumentException {
        if (device == null) {
            logger.log(Level.SEVERE, "device can't be null");
            throw new IllegalArgumentException("device is null");
        }
    }

    /**
     * Checking for null identifiers.
     * @param identifier identifier to be checked.
     * @throws IllegalArgumentException if identifier is null.
     */
    public static void checkNullId(String identifier) {
        if (identifier == null) {
            logger.log(Level.SEVERE, "id can't be null");
            throw new IllegalArgumentException("id is null");
        }
    }


    /**
     * returning a positive port number.
     * @param port port number to be checked.
     * @return a positive port number.
     */
    public static int checkPortNum(int port) {
        return Math.abs(port);
    }


    /**
     * checking the device identifier and if it's less than 3 characters,
     * it'll return an identifier with all lowercase letters.
     * @param id identifier to be checked.
     * @return an identifier with all lowercase letters and length of 3.
     */
    public static String identifierCheck(String id) {
        MotherboardException.checkNullId(id);

        if (id.length() < 3)
            return id.toLowerCase();
        else
            return id.substring(0, 3).toLowerCase();
    }
}
