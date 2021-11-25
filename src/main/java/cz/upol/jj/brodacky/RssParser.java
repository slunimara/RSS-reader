package main.java.cz.upol.jj.brodacky;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RssParser {
    private String xmlString;
    
    public RssParser() {
        super();
        this.xmlString = "";
    }

    public RssParser(String xmlString) {
        super();
        this.xmlString = xmlString;
    }

    public String getXmlString() {
        return xmlString;
    }

    public void setXmlString(String xmlString) {
        this.xmlString = xmlString;
    }

    private Document getDocument() throws ParserConfigurationException, SAXException, IOException {
        InputStream inStream = new ByteArrayInputStream(xmlString.getBytes());
        Reader reader = new InputStreamReader(inStream);
        InputSource inSource = new InputSource(reader);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        return docBuilder.parse(inSource);
    }
    
    private String getDocumentFirstTagText(Document document, String tagName) throws NullPointerException {
        try {
            return document.getElementsByTagName(tagName).item(0).getTextContent();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        } 
    }

    private String getElementFirstTagText(Element element, String tagName) throws NullPointerException {
        try {
            return element.getElementsByTagName(tagName).item(0).getTextContent();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        } 
    }

    public RssChannel getChannelInfo() throws Exception {
        Document document = getDocument();

        ArrayList<String> args = new ArrayList<>();
        String[] keys = {"title", "link", "description", "language", "copyright", "pubDate"};
        for (String key : keys) {
            try {
                args.add(getDocumentFirstTagText(document, key));  
            } catch (NullPointerException e) {
                args.add("");
            }
        }

        return new RssChannel(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
    }

    public ArrayList<RssItem> getItems() throws Exception {
        ArrayList<RssItem> itemList = new ArrayList<>();
        Document document = getDocument();

        NodeList items = document.getElementsByTagName("item");
        for(int i = 0; i < items.getLength(); i++){
            Element item = (Element) items.item(i);

            ArrayList<String> args = new ArrayList<>();
            String[] keys = new String[]{"title", "link", "description", "author", "pubDate"};
            for (String key : keys) {
                try {
                    args.add(getElementFirstTagText(item, key)); 
                } catch (NullPointerException e) {
                    args.add("");
                }
            }
            
            itemList.add(new RssItem(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4)));
        }

        return itemList;
    }
}
