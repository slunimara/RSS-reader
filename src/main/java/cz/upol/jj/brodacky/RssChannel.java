package main.java.cz.upol.jj.brodacky;

public record RssChannel(String title, String link, String description, String language, String copyright, String pubDate) {
    @Override
    public String toString() {
        return title + System.lineSeparator() + "Description:\t\t" + description + System.lineSeparator() + "Link:\t\t\t" + link + System.lineSeparator() + "Language:\t\t" + language + System.lineSeparator() + "Copyright:\t\t" + copyright + System.lineSeparator() + "Publication date:\t" + pubDate + System.lineSeparator() + System.lineSeparator();
    }
}
