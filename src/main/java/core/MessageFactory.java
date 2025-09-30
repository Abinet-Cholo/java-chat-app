package core;

public class MessageFactory {
    public Message create(String input) {
        if (input.startsWith("IMG:")) {
            return new ImageMessage(input.substring(4));
        } else if (input.startsWith("FILE:")) {
            return new FileMessage(input.substring(5));
        } else {
            return new TextMessage(input);
        }
    }
}

