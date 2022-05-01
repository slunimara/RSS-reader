package main.java.cz.upol.jj.brodacky;

public class RssReaderExeption extends Exception {
    public RssReaderExeption(String message, Exception e) {
        super(message, e);
    }
}
