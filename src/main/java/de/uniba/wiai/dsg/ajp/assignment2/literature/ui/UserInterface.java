package de.uniba.wiai.dsg.ajp.assignment2.literature.ui;

import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.DatabaseService;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.LiteratureDatabaseException;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.MainService;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.impl.MainServiceImpl;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model.PublicationType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInterface {
    private final ConsoleHelper consoleHelper = ConsoleHelper.build();
    private final MainService mainService = new MainServiceImpl();
    private DatabaseService databaseService;

    private static final String mainMenuString =
            "(1) Load and Validate Literature Database\n" +
            "(2) Create New Literature Database\n" +
            "(0) Exit System\n";

    private static final String subMenuString =
            "(1) Add Author\n" +
            "(2) Remove Author\n" +
            "(3) Add Publication\n" +
            "(4) Remove Publication\n" +
            "(5) List Authors\n" +
            "(6) List Publications\n" +
            "(7) Print XML on Console\n" +
            "(8) Save XML to File\n" +
            "(0) Back to main menu / close without saving\n";

    /**
     * main menu
     */
    public void mainMenu(){
        boolean exit = false;
        while(!exit){
            System.out.print(mainMenuString);
            try{
                int chosen = consoleHelper.askIntegerInRange("Please enter the number of a menu item:",0, 2);
                switch (chosen){
                    case 0:
                        exit = true;
                        break;
                    case 1:
                        loadAndValidate();
                        break;
                    case 2:
                        createDatabase();
                        break;
                    default:
                }
            }catch (IOException e){
                handleIOException(e);
            }
        }
    }

    /**
     * submenu
     */
    private void subMenu(){
        boolean exit = false;
        while (!exit){
            System.out.print(subMenuString);
            try {
                int chosen = consoleHelper.askIntegerInRange("Please enter the number of a menu item:",0, 8);
                switch (chosen){
                    case 0:
                        System.out.println("Going back to main menu...");
                        exit = true;
                        break;
                    case 1:
                        addAuthor();
                        break;
                    case 2:
                        removeAuthor();
                        break;
                    case 3:
                        addPublication();
                        break;
                    case 4:
                        removePublication();
                        break;
                    case 5:
                        listAuthors();
                        break;
                    case 6:
                        listPublications();
                        break;
                    case 7:
                        printXMLOnConsole();
                        break;
                    case 8:
                        saveXMLToFile();
                        break;
                    default:
                }
            }catch (IOException i){
                handleIOException(i);
            }
        }
    }

    /**
     * loads and validates a file given from input
     */
    private void loadAndValidate(){
        try {
            String path = consoleHelper.askNonEmptyString("Please enter the filename of the database to load:");
            databaseService = mainService.load(path);
            subMenu();
        }catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()){
                loadAndValidate();
            }
        }catch (IOException e){
            handleIOException(e);
        }
    }

    /**
     * creates an empty database
     */
    private void createDatabase(){
        try {
            databaseService = mainService.create();
            subMenu();
        }catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()){
                createDatabase();
            }
        }
    }

    /**
     * adds an author
     */
    private void addAuthor() {
        System.out.println("Please enter the details of the author to be added:");
        try {
            String name = consoleHelper.askNonEmptyString("Name:");
            String email = consoleHelper.askNonEmptyString("Email:");
            String id = consoleHelper.askNonEmptyString("id:");
            databaseService.addAuthor(name, email, id);
            System.out.println("Author added successfully.");
        }catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()) {
                addAuthor();
            }
        }
        catch (IOException i){
            handleIOException(i);
        }
    }

    /**
     * removes an author by its id
     */
    private void removeAuthor(){
        try{
            String authorID = consoleHelper.askNonEmptyString("Please enter the id of the author to be removed:");
            databaseService.removeAuthorByID(authorID);
            System.out.println("Author removed successfully.");
        }catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()){
                removeAuthor();
            }
        }
        catch (IOException i){
            handleIOException(i);
        }
    }

    /**
     * adds a publication
     */
    private void addPublication(){
        System.out.println("Please enter the details of the publication to be added:");
        try {
            String title = consoleHelper.askNonEmptyString("Title:");
            int yearPublished = consoleHelper.askInteger("Year of publication:");
            PublicationType type = askPublicationType();
            List<String> authorIDs = askMultiple("Author id(s), one at a time:");
            String id = consoleHelper.askNonEmptyString("id:");
            databaseService.addPublication(title, yearPublished, type, authorIDs, id);
            System.out.println("Publication added successfully.");
        }
        catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()){
                addPublication();
            }
        }
        catch (IOException i){
            handleIOException(i);
        }
    }

    /**
     * removes a publication by its id
     */
    private void removePublication(){
        try{
            String publicationID = consoleHelper.askNonEmptyString("Please enter the id of the publication to be removed:");
            databaseService.removePublicationByID(publicationID);
            System.out.println("Publication removed successfully.");
        }catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()){
                removePublication();
            }
        }
        catch (IOException i){
            handleIOException(i);
        }
    }

    /**
     * lists all author's details and their publications
     */
    private void listAuthors(){
        System.out.println("Author list:");
        databaseService.getAuthors().forEach(System.out::println);
    }

    /**
     * lists all publication's details and their authors
     */
    private void listPublications(){
        System.out.println("Publication list:");
        databaseService.getPublications().forEach(System.out::println);
    }

    /**
     * prints the generated XML of the database on the console
     */
    private void printXMLOnConsole(){
        System.out.println("XML of database:");
        try {
            databaseService.printXMLToConsole();
        } catch (LiteratureDatabaseException l) {
            handleLitDBException(l);
        }
    }

    /**
     * saves the generated XML of the database to a file
     */
    private void saveXMLToFile(){
        try {
            String path = consoleHelper.askNonEmptyString("Please enter the filename where to save the database:");
            databaseService.saveXMLToFile(path);
            System.out.println("Saved successfully.");
        }catch (LiteratureDatabaseException l){
            handleLitDBException(l);
            if(retry()){
                saveXMLToFile();
            }
        }catch (IOException e){
            handleIOException(e);
        }
    }

    /**
     * used to print IOExceptions
     * @param e the exception to print
     */
    private void handleIOException(IOException e){
        System.out.println("Something went wrong...please try and restart the program.");
    }

    /**
     * used to print LiteratureDatabaseExceptions
     * @param l the exception to print
     */
    private void handleLitDBException(LiteratureDatabaseException l){
        System.out.println(l.getMessage());
    }

    /**
     * @return answer to retry?
     */
    private boolean retry(){
        try{
            String result = consoleHelper.askString("Retry? (y/n)").toLowerCase();
            return result.equals("y") || result.equals("yes");
        }catch (IOException i){
            handleIOException(i);
        }
        return false;
    }

    /**
     * asks the user multiple times for input until he inputs nothing
     * @param message the prompt for input
     * @return List of Strings the user put in
     */
    private List<String> askMultiple(String message){
        List<String> list = new ArrayList<>();
        message += ("\nEnter nothing to abort/finish.");
        try{
            String content = consoleHelper.askString(message);
            while(!content.equals("")){
                list.add(content);
                content = consoleHelper.askString(message);
            }
        }catch (IOException i){
            handleIOException(i);
        }
        return list;
    }

    /**
     * Asks the user to input a publication type. Helper method for addPublication()
     * @return the publication type
     */
    private PublicationType askPublicationType(){
        String message = "Publication type (article, techreport, book, mastersthesis, phdthesis, inproceedings):";
        List<String> validTypes = List.of("article", "techreport", "book", "mastersthesis", "phdthesis", "inproceedings");
        try {
            String type = consoleHelper.askNonEmptyString(message);
            while(!validTypes.contains(type.toLowerCase())){
                type = consoleHelper.askNonEmptyString(message);
            }
            switch (type){
                case "article":
                    return PublicationType.ARTICLE;
                case "techreport":
                    return PublicationType.TECHREPORT;
                case "book":
                    return PublicationType.BOOK;
                case "mastersthesis":
                    return PublicationType.MASTERSTHESIS;
                case "phdthesis":
                    return PublicationType.PHDTHESIS;
                case "inproceedings":
                    return PublicationType.INPROCEEDINGS;
                default:
            }
        }catch (IOException i){
            handleIOException(i);
        }
        return null;
    }

}
