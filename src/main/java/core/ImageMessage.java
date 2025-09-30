package core;

public class ImageMessage extends Message {
    public ImageMessage(String content) { super(content); }
    @Override public String format() { return "[IMAGE] " + content; }
}

