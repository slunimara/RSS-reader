package main.java.cz.upol.jj.brodacky;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private final String exampleUrl = "https://www.irozhlas.cz/rss/irozhlas";
    private RssReader reader = new RssReader(exampleUrl);
    private List<RssItem> items;
    private int printCount = 3;
    private Scanner scanner;
    
    public Client() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printLine();
        System.out.println("---        Welcome in RSS reader        ---");
        app();
    }

    public void app() {
        while(true){
            int option = this.options();
            System.out.println(System.lineSeparator());
            if (option == 0) {
                quit();
                break;
            }
            switch (option) {
                case 1:
                    try {
                        feeds();
                    } catch (Exception e) {}
                    break;
                case 2:
                    settings();
                    break;
                default:
                    invalidOption();
                    break;
            } 
        }
    }

    public void feeds() throws Exception {
        printLine();
        System.out.println("---                Feeds                ---");
        System.out.println("Current RSS feed is: " + reader.getUrl());

        try {
            reader.downloadFeed();
        } catch (Exception e) {
            System.out.println(System.lineSeparator() + e + System.lineSeparator());
            throw new Exception();
        }

        items = reader.getItems();

        if(items.isEmpty()){
            System.out.println(System.lineSeparator() + "This feed does not contains any item." + System.lineSeparator());
            throw new Exception();
        }

        while(true){
            int option = this.feedOptions();
            System.out.println(System.lineSeparator());
            if (option == 0) {
                break;
            }
            switch (option) {
                case 1:
                    printChannel();
                    break;
                case 2:
                    printItems();
                    break;
                case 3:
                    printTitles();
                    break;
                case 4:
                    choseItem();
                    break;
                case 5:
                    findInItems();
                    break;
                default:
                    invalidOption();
                    break;
            } 
        }
    }

    public void settings() {
        printLine();
        System.out.println("---               Settings              ---");

        while(true){ 
            int option = settingsOptions();
            System.out.println(System.lineSeparator());
            if (option == 0) {
                break;
            }
            switch (option) {
                case 1:
                    printSettings();
                    break;
                case 2:
                    setFeedURL();
                    break;
                case 3:
                    setPRINT_COUNT();
                    break;
                default:
                    invalidOption();
                    break;
            } 
        }
    }

    public int options() {
        try {
            printLine();
            System.out.println("---               Options               ---");
            System.out.println("(0) Quit (1) Read feeds (2) Settings");
            System.out.print("Enter number: ");
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            invalidOption();
            return 0;
        }
    }

    public int feedOptions() {
        try {
            printLine();
            System.out.println("---             Feed Options            ---");
            System.out.println("(0) Quit (1) Print channel info");
            System.out.println("(2) Print items (3) Print titles");
            System.out.println("(4) Chose item (5) Find with keyword");
            System.out.print("Enter number: ");
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            invalidOption();
            return 0;
        }
    }

    public int settingsOptions() {
        try {
            printLine();
            System.out.println("---           Settings Options          ---");
            System.out.println("(0) Quit (1) Print settings");
            System.out.println("(2) Set Feed URL (3) Set PRINT_COUNT");
            System.out.print("Enter number: ");
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            invalidOption();
            return 0;
        }
    }

    public void printChannel() {
        System.out.println(reader.getChannel().toString());
    }

    public void printItems() {
        int indexStart = 0;
        int indexEnd = printCount;

        while(true){
            for (int i = indexStart; i < indexEnd && i < items.size(); i++) {
                System.out.println(items.get(i));
            }

            indexStart = indexEnd;
            indexEnd += printCount;

            if(indexEnd >= items.size()){
                break;
            }

            System.out.println("Do you want to print more?");
            System.out.println("(0) No (1) Yes");
            System.out.print("Enter number: ");

            if((scanner.nextInt() == 0)) {
                System.out.println(System.lineSeparator());
                break;
            } else {
                System.out.println();
            }
        }
    }

    public void printTitles() {
        int i = 1;
        for (RssItem item : items) {
            System.out.println(i++ + ". " + item.title());
        }
        System.out.println(System.lineSeparator());
    }

    public void choseItem() {
        while(true){
            System.out.print("Enter index of the message:");
            try{
                int index = scanner.nextInt();
                System.out.println(items.get(index - 1).toString());
                break;
            } catch (Exception e) {
                invalidOption();
            } 
        }
        System.out.println();
    }

    public void printSettings() {
        System.out.println("Current RSS feed is: " + reader.getUrl());
        System.out.println("PRINT_COUNT is set to: " + printCount);
        System.out.println(System.lineSeparator());
    }

    public void invalidOption() {
        System.out.println("Error! This option doesn't exist." + System.lineSeparator());
    }

    public void printLine() {
        System.out.println("-------------------------------------------");
    }

    public void findInItems() {
        ArrayList<Integer> indexList = new ArrayList<>();
        System.out.println("Enter character sequence which you are looking for:");
        System.out.print("Character sequence: ");
        String sequence = scanner.next();

        System.out.println();

        try{
            System.out.println("Where do you want to search?");
            System.out.println("(0) Titles (1) Description (2) Both");
            System.out.print("Enter number: ");
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    indexList = keyInItem(sequence, option);
                    break;
                case 1:
                    indexList = keyInItem(sequence, option);
                    break;
                case 2:
                    indexList = keyInItem(sequence, 0);
                    indexList.addAll(keyInItem(sequence, 1));
                    break;
                default:
                    invalidOption();
                    break;
            }
            System.out.println(System.lineSeparator());
            if(indexList.size() != 0){
                Stream<Integer> indexes = indexList.stream();
                indexes.distinct().forEach(i -> System.out.println(items.get(i).toString()));
            } else {
                System.out.println("There is no item with this character sequence." + System.lineSeparator());
            }
        } catch (Exception e) {
            invalidOption();
        } 
    }

    private ArrayList<Integer> keyInItem(String key, int option) {
        ArrayList<Integer> indexList = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            boolean contains = false;
            
            if(option == 0){
                contains = items.get(i).title().contains(key);    
            } else {
                contains = items.get(i).description().contains(key);  
            }

            if(contains){
                indexList.add(i);
            }
        }
        return indexList;
    }

    public void quit() {
        printLine();
        System.out.println("--- This app was made by Marek Brodacký ---");
        System.out.println("---    Thank you for using this app.    ---");
        printLine();

        scanner.close();
    }

    public void setPRINT_COUNT() {
        System.out.print("Enter number bigger than 1 or type number (0) for default choise: ");
        try {
            int input = scanner.nextInt();
            if(0 < input){
                printCount = input;
            }
            System.out.println(System.lineSeparator());
        } catch (InputMismatchException e) {
            invalidOption();
        }
    }

    public void setFeedURL() {
        System.out.print("Enter valid XML feed URL (eg. https://servis.idnes.cz/rss.aspx?c=zpravodaj) or type number (0) for default choise: ");
        String input = scanner.next();
        if(!input.equals("0")){
            reader.setUrl(input);
        } 
        System.out.println(System.lineSeparator());
    }
}
