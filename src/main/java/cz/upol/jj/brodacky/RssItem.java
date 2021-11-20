package main.java.cz.upol.jj.brodacky;

public record RssItem(String title, String link, String description, String author, String pubDate) {
    @Override
    public String toString() {
        return title + System.lineSeparator() + description + System.lineSeparator() + "--------------------" + System.lineSeparator() + author + " " + pubDate + System.lineSeparator() + link + System.lineSeparator();
    }
}