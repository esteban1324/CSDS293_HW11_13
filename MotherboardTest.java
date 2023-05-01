package Motherboard;

import static org.junit.Assert.*;

import org.junit.Test;


import java.util.logging.Logger;


public class MotherboardTest {

    public static class TestApp implements Application {

        //logger
        private final Logger logger = Logger.getLogger(TestApp.class.getName());

        //device to test
        private Device device;

        //motherboard to test
        private Motherboard mb;

        /**
         * Constructor for the TestApp class
         * @param device the device to test
         * @param mb the motherboard to test
         */
        public TestApp(Device device, Motherboard mb) {
            assert device != null;
            assert mb != null;

            this.device = device;
            this.mb = mb;
        }

        /**
         * This will handle a message for the Applications test
         */
        @Override
        public void showMessages() {
            logger.info("Messages received: " + device.deviceMessages());
        }

        /**
         * This will send a message to a device for the Applications test
         * @param m the message to send
         * @param d the device to send the message to
         */
        @Override
        public void forward(Message m, Device d) {
            device.forward(m, d);
        }

        /**
         * This will receive a message from a device for the Applications test
         * @param m the message to receive
         * @param d the device to receive the message from
         */
        @Override
        public void receive(Message m, Device d) {
            device.receive(m, d);
        }

        /**
         * This will broadcast a message to a device for the Applications test
         * @param m the message to broadcast
         * @param d the device that broadcast the message
         */
        @Override
        public void broadcast(Message m, Device d) {
            mb.broadcast(d, m);
        }

    }

    //error handling test cases
    @Test
    public void testNullMessage() {
        //code coverage for MotherboardException
        MotherboardException m = new MotherboardException();

        Motherboard mb = new Motherboard();
        Device d = Device.createDevice("d");

        mb.add(d);

        Message message = null;

        //test null message object
        assertThrows(IllegalArgumentException.class, () -> mb.broadcast(d, message));

        //test null message contents (payload)
        Message message2 = new Message("d", 12, null);
        assertThrows(IllegalArgumentException.class, () -> mb.broadcast(d, message2));

        //test empty message id
        Message message3 = new Message("", 12, "hello");
        assertThrows(IllegalArgumentException.class, () -> mb.broadcast(d, message3));

        //valid message
        Message message4 = new Message("d", 12, "hello");
        mb.broadcast(d, message4);

        //test null message id
        Message message5 = new Message(null, 12, "hello");
        assertThrows(IllegalArgumentException.class, () -> mb.broadcast(d, message5));

    }

    @Test
    public void testNullApp() {
        Motherboard mb = new Motherboard();
        Device c = Device.createDevice("c");
        mb.add(c);

        mb.add(Device.createDevice("d"));

        Device d2 = null;

        //test null app
        assertThrows(IllegalArgumentException.class, () -> mb.add(d2));
    }

    @Test
    public void testNullId() {
        Motherboard mb = new Motherboard();
        Device d = null;

        //test null app
        assertThrows(IllegalArgumentException.class, () -> mb.add(d));

        //test null id
        assertThrows(IllegalArgumentException.class, () -> Device.createDevice(null));
    }

    @Test
    public void testValidPort() {

        Device d = Device.createDevice("d");
        d.addPort(-165);

        assertTrue(d.devicePorts().containsKey(165));

    }

    @Test
    public void testIdCheck() {

        Device d = Device.createDevice("Esteban");

        assertEquals("est", d.toString());

        Device b = Device.createDevice("s");
        assertEquals("s", b.toString());

    }


    //test MotherBoard class (nominal)
    @Test
    public void testDeviceID() {
        Motherboard mb = new Motherboard();

        Device d = Device.createDevice("d");
        mb.add(d);

        assertEquals("d", mb.getDevice("d").toString());

    }

    @Test
    public void testAddRemove() {
        Motherboard mb = new Motherboard();

        Device a = Device.createDevice("a");
        Device b = Device.createDevice("b");
        Device c = Device.createDevice("c");

        mb.add(a);
        mb.add(b);
        mb.add(c);

        assertEquals("{a=a, b=b, c=c}", mb.toString());

        mb.remove("a");

        assertEquals("{b=b, c=c}", mb.toString());

    }

    //test application/device level
    @Test
    public void testDevicePort() {
        Motherboard mb = new Motherboard();

        Device d = Device.createDevice("d");
        mb.add(d);

        d.addPort(12);

        assertTrue(d.devicePorts().containsKey(12));

        d.removePort(12);
        //logs a statement
        d.removePort(1);

        assertFalse(d.devicePorts().containsKey(12));
    }

    @Test
    public void testForward() {

        Device d = Device.createDevice("d");

        Device c = Device.createDevice("c");

        d.addPort(12);
        c.addPort(13);

        Message message = new Message("d", 13, "hello");

        d.forward(message, c);

        assertTrue(c.deviceMessages().containsKey(message));

    }

    @Test
    public void testReceive() {

        Device d = Device.createDevice("d");

        Device c = Device.createDevice("c");

        d.addPort(12);
        c.addPort(13);

        Message message = new Message("d", 13, "hello");

        c.receive(message, d);

        // expected message from d {Message[identifier=Optional[d], port=13, payload=hello]=d}
        assertTrue(c.deviceMessages().containsKey(message));

        Device a = Device.createDevice("a");
        Device b = Device.createDevice("b");

        a.addPort(12);

        Message message1 = new Message("a", 12, "hello");

        b.receive(message1, a);

        // expected message from a {Message[identifier=Optional[a], port=12, payload=hello]=a}
        assertFalse(b.deviceMessages().containsKey(message1));

    }

    @Test
    public void testBroadcastAll() {
        Motherboard mb = new Motherboard();

        Device a = Device.createDevice("a");
        Device b = Device.createDevice("b");
        Device c = Device.createDevice("c");
        Device d = Device.createDevice("d");

        a.addPort(12);
        b.addPort(13);
        c.addPort(13);
        d.addPort(13);

        mb.add(a);
        mb.add(b);
        mb.add(c);
        mb.add(d);

        //broadcast to all devices
        Message m = new Message("a", 13, "hello");

        //devices need to be connected to motherboard, so they can receive the message
        mb.broadcast(a, m);

        assertTrue(b.deviceMessages().containsKey(m));
        assertTrue(c.deviceMessages().containsKey(m));
        assertTrue(d.deviceMessages().containsKey(m));

    }

    @Test
    public void testApp(){

        //motherboard and devices
        Motherboard mb = new Motherboard();
        Device d = Device.createDevice("d");
        Device e = Device.createDevice("e");
        Device f = Device.createDevice("f");

        //testing our app
        TestApp app = new TestApp(d, mb);

        //add devices to motherboard
        mb.add(d); mb.add(e); mb.add(f);

        //add ports to devices
        d.addPort(12); e.addPort(13); f.addPort(13);

        //create message
        Message m = new Message("d", 13, "hello");
        Message m2 = new Message("e", 13, "ar12");
        Message m3 = new Message("d", 12, "philly from f");
        Message m4 = new Message("d", 12, "delco");

        //broadcast message
        app.broadcast(m,d);

        //check if message was received by other devices
        assertTrue(e.deviceMessages().containsKey(m));

        //forward message
        app.forward(m2, e);

        //check if device e has m2
        assertTrue(e.deviceMessages().containsKey(m2));

        //check if device f get m3 from device e
        app.receive(m3, f);
        app.receive(m4, f);

        //check if device f has m3
        assertTrue(d.deviceMessages().containsKey(m3));

        app.showMessages();

    }

    @Test
    public void testExceptionsApps(){

        //motherboard and devices
        Motherboard mb = new Motherboard();
        Device d = Device.createDevice("d");
        Device e = Device.createDevice("e");
        Device f = Device.createDevice("f");

        //testing our app
        TestApp app = new TestApp(d, mb);

        //add devices to motherboard
        mb.add(d); mb.add(e); mb.add(f);

        //add ports to devices
        d.addPort(12); e.addPort(13); f.addPort(13);

        //create message
        Message m = null;

        assertThrows(IllegalArgumentException.class, () -> app.receive(m, f));

        assertThrows(IllegalArgumentException.class, () -> app.forward(m, f));

        assertThrows(IllegalArgumentException.class, () -> app.broadcast(m, f));
    }
}