package Motherboard;

/**
 * This is an interface for the Application tester class.
 * Application will use Devices to send and receive messages
 */
public interface Application {

    /**
     * This method will show the messages received by the device and application
     */
    void showMessages();

    /**
     * This will send a message to a device for the Applications test
     * @param m the message to send
     * @param d the device to send the message to
     */
     void forward(Message m, Device d);

     /**
      * This will receive a message from a device for the Applications test
      * @param m the message to receive
      * @param d the device to receive the message from
      */
     void receive(Message m, Device d);


    /**
     * This will broadcast a message to a device for the Applications test
     * @param m the message to broadcast
     * @param d the device that broadcast the message
     */
     void broadcast(Message m, Device d);



}
