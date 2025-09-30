package core;

public class TextMessage extends Message {
    public TextMessage(String content) { super(content); }
    @Override public String format() { return "[TEXT] " + content; }
}

