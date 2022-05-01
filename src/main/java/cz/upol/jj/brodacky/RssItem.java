package main.java.cz.upol.jj.brodacky;

public record RssItem(String title, String link, String description, String author, String pubDate) {
    @Override
    public String toString() {
        return title + ", " + description + ", " + author + ", " + pubDate + ", " + link;
    }
}