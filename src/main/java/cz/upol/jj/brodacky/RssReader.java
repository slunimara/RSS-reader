package main.java.cz.upol.jj.brodacky;

import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class RssReader {

    private RssDownloader downloader;
    private RssParser parser;

    private RssChannel channel;
    private List<RssItem> items;
    private String url;

    public RssReader(String url) {
        this.downloader = new RssDownloader();
        this.parser = new RssParser();
        this.items = new ArrayList<RssItem>();
        this.url = url;
    }

    public RssChannel getChannel() {
        return channel;
    }

    public List<RssItem> getItems() {
        return items;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void downloadFeed() throws RssReaderExeption {
        try {
            String feed = downloader.download(url);
            Document document = getDocument(feed);
            parser.setDocument(document);

            this.channel = parser.getChannelInfo();
            this.items = parser.getItems();
        } catch (Exception e) {
            throw new RssReaderExeption("There was error while parsing document.", e);
        }
    }

    private Document getDocument(String input) throws RssReaderExeption {
        try {
            InputStream is = new ByteArrayInputStream(input.getBytes("utf-8"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(is);
        } catch (Exception e) {
            throw new RssReaderExeption("There was error while parsing document.", e);
        }
    }
}
