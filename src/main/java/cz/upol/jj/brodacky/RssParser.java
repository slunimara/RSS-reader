package main.java.cz.upol.jj.brodacky;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public ArrayList<RssItem> getItems() throws ParseException {
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

    private RssItem getItemAttributes(Element item) throws ParseException {
        String[] args = new String[5];
        for (int j = 0; j < args.length; j++) {
            args[j] = getFirstElementText(item, TAGS_ITEM[j]);
        }  

        return new RssItem(args[0], args[1], args[2], args[3], parseDate(args[4]));
    }

    private String parseDate(String s) throws ParseException {
        String dFormat = "E, dd MMM yyyy HH:mm:ss Z";
        SimpleDateFormat format = new SimpleDateFormat(dFormat, Locale.ENGLISH); 
        Date date = format.parse(s);

        SimpleDateFormat formatter = new SimpleDateFormat("E HH:mm:ss");
        String formattedDate = formatter.format(date);
        String cap = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
        return cap;
    }
}
