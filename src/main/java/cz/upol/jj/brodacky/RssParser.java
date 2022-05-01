package main.java.cz.upol.jj.brodacky;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class RssParser {

    private Document document;
    
    private final String[] TAGS_CHANNEL = {"title", "link", "description", "language", "copyright", "pubDate"};
    private final String[] TAGS_ITEM = {"title", "link", "description", "author", "pubDate"};
    private final String TAG_ITEM = "item";
    
    public RssParser() {
        super();
        this.document = null;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    
    public RssChannel getChannelInfo() {
        if (document == null)
            return null;

        String[] args = new String[6];

        for (int j = 0; j < args.length; j++) {
            args[j] = getFirstElementText(document, TAGS_CHANNEL[j]);
        }

        return new RssChannel(args[0], args[1], args[2], args[3], args[4], args[5]);
    }

    public ArrayList<RssItem> getItems() {
        if (document == null)
            return null;

        ArrayList<RssItem> items = new ArrayList<>();

        NodeList itemElements = document.getElementsByTagName(TAG_ITEM);
        for(int i = 0; i < itemElements.getLength(); i++){
            RssItem item = getItemAttributes((Element) itemElements.item(i));

            items.add(item);
        }

        return items;
    }

    private String getFirstElementText(Document document, String tagName) {
        Node element = document.getElementsByTagName(tagName).item(0);

        if (element != null) 
            return element.getTextContent();

        return "";
    }

    private String getFirstElementText(Element parent, String tagName) {
        Node element = parent.getElementsByTagName(tagName).item(0);

        if (element != null) 
            return element.getTextContent();

        return "";
    }

    private RssItem getItemAttributes(Element item) {
        String[] args = new String[5];

        for (int j = 0; j < args.length; j++) {
            args[j] = getFirstElementText(item, TAGS_ITEM[j]);
        }
        
        return new RssItem(args[0], args[1], args[2], args[3], args[4]);
    }
}
