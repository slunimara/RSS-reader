package main.java.cz.upol.jj.brodacky;

public record RssChannel(String title, String link, String description, String language, String copyright, String pubDate) {
    @Override
    public String toString() {
        
        return "Channel: " + title + System.lineSeparator() + 
            "Description: " + description + System.lineSeparator() + 
            "Link: " + link + System.lineSeparator() + 
            "Language: " + language + System.lineSeparator() + 
            "Publication date: " + pubDate + System.lineSeparator() + 
            "Copyright: " + copyright + System.lineSeparator();
    }
}
