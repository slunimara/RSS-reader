package main.java.cz.upol.jj.brodacky;

public record RssChannel(String title, String link, String description, String language, String copyright, String pubDate) {
    @Override
    public String toString() {
        return title + ", Description:" + description + ", Link:" + link + ", Language:" + language + ", Copyright:" + copyright + ", Publication date:" + pubDate + System.lineSeparator();
    }
}
