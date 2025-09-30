package core;

public class FileMessage extends Message {
    public FileMessage(String content) { super(content); }
    @Override public String format() { return "[FILE] " + content; }
}

