package main.java.cz.upol.jj.brodacky;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private Scanner scanner = new Scanner(System.in);
    private RssDownloader downloader = new RssDownloader("https://servis.idnes.cz/rss.aspx?c=zpravodaj");
    private RssParser parser = new RssParser();
    private int PRINT_COUNT = 3;
    
    public void start() {
        printLine();
        System.out.println("---        Welcome in RSS reader        ---");
        app();
    }

    public int options() {
        printLine();
        System.out.println("---               Options               ---");
        System.out.println("(0) Quit (1) Read feeds");
        System.out.print("Enter number: ");
        return scanner.nextInt();
    }

    public void app(){
        while(true){
            try {
                int option = this.options();
                System.out.println();
                if (option == 0) {
                    quit();
                    break;
                }
                switch (option) {
                    case 1:
                        feeds();
                        break;
                    default:
                        invalidOption();
                        break;
                } 
            } catch (InputMismatchException e) {
                invalidOption();
            }
        }
    }

    public void feeds() {
        printLine();
        System.out.println("---                Feeds                ---");
        System.out.println("Default RSS feed is: " + downloader.getURL());
        System.out.println("Do you want to change it?");
        System.out.println("(0) No (1) Yes");
        System.out.print("Enter number: ");
        try {
            if(scanner.nextInt() == 1) {
                setFeedURL();
            }
        } catch (InputMismatchException e) {
            invalidOption();
        }

        try {
            parser.setXmlString(downloader.download());
        } catch (Exception e) {
            e.getStackTrace();
        }

        printChannel();
        printItems();
    }

    public void printChannel() {
        try {
            System.out.println(parser.getChannelInfo().toString());
        } catch (Exception e) {
            System.out.println("Error has eccurred! #01");
        }
    }

    public void printItems() {
        int indexStart = 0;
        int indexEnd = PRINT_COUNT;
        try {
            ArrayList<RssItem> itemList = parser.getItems();
            while(true){
                for (int i = indexStart; i < indexEnd && i < itemList.size(); i++) {
                    System.out.println(itemList.get(i));
                }

                indexStart = indexEnd;
                indexEnd += PRINT_COUNT;

                if(indexEnd >= itemList.size()){
                    break;
                }

                System.out.println("Do you want to print more?");
                System.out.println("(0) No (1) Yes");
                System.out.print("Enter number: ");

                if((scanner.nextInt() == 0)) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error has eccurred! #02");
        }
    }

    public void invalidOption() {
        System.out.println("Error! This option doesn't exist.");
    }

    public void printLine() {
        System.out.println("-------------------------------------------");
    }

    public void quit() {
        printLine();
        System.out.println("--- This app was made by Marek Brodacký ---");
        System.out.println("---    Thank you for using this app.    ---");
        printLine();
    }

    public void setFeedURL() {
        System.out.print("Enter valid XML feed URL (eg. https://servis.idnes.cz/rss.aspx?c=zpravodaj) or type number (0) for default choise: ");
        String input = scanner.next();
        if(!input.equals("0")){
            downloader.setURL(input);
        } 
    }
}
