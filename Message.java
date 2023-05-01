package Motherboard;

/**
 * This is a Data Class containing the recipient identifier, the recipient port, and a message.
 * @param identifier the recipient device identifier.
 * @param port the recipient port.
 * @param payload the message contents.
 */
public record Message(String identifier, int port, String payload) {
}
