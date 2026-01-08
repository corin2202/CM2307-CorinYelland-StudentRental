import data.*;
import manager.BookingManager;
import manager.RoomManager;
import service.BookRoomService;
import service.InputParserService;
import service.RoomHomeOwnerService;
import service.RoomSearchService;
import ui.HomeOwnerSession;
import ui.StudentSession;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StudentRentals {

    private Map<String, User> usersByUsername = new HashMap<>();

    public StudentRentals(){
        loadUsers();
    }

    public void startApp(){
        Scanner sc = new Scanner(System.in);
        loginOrRegister(sc);

        sc.close();

    }

    public void loadUsers(){
        try{
            FileInputStream readData = new FileInputStream("accountdata.ser");
            ObjectInputStream readStream = new ObjectInputStream(readData);
            Map<String,User> users = (Map<String,User>) readStream.readObject();
            readStream.close();
            usersByUsername = users;

            System.out.println("Loaded users successfully");

        }catch (IOException e) {
            if (usersByUsername.isEmpty()){
                System.out.println("No users to load");
            } else {
                System.out.println("Error loading users");
            }
        } catch (ClassNotFoundException e2){
            System.out.println("Class not found");
        }
    }

    private boolean isUsernameTaken(String inputUsername){
        return usersByUsername.containsKey(inputUsername);
    };

    private String loopWhileUsernameTaken(Scanner sc){

        while(true){

            System.out.println("Please enter a new username: ");
            String currentUsername = sc.nextLine();

            if (currentUsername == null || currentUsername.trim().isEmpty()){
                System.out.println("Input cannot be empty.");
                continue;
            }

            if (isUsernameTaken(currentUsername.trim())){
                System.out.println("Username is already taken, please try again: ");

            } else {
                return currentUsername.trim();
            }
        }
    }

    private User registerAccount(Scanner sc){
        // choose account type
        String accountType = InputParserService.obtainAccountType(sc);

        // create username that isn't taken
        String userName = loopWhileUsernameTaken(sc).trim();

        // create a non-empty password
        String password = InputParserService.obtainPassword(sc);

        boolean yes = InputParserService.isInputYes(sc,"Confirm account creation");
        if (yes){
            return UserFactory.createUser(accountType, userName, password);
        } else {
            return null;
        }

    }


    private User login(Scanner sc){

        while(true){

            System.out.println("Enter account username: ");
            String username = sc.nextLine();

            if (username == null || username.trim().isEmpty()){
                System.out.println("Input cannot be empty.");
                continue;
            }

            System.out.println("Enter account password: ");
            String password = sc.nextLine();

            // finds the account by username
            User user = usersByUsername.get(username.trim());

            // if name and password match, return the account
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }

            // option to quit if failing login too much
            boolean yes = InputParserService.isInputYes(sc, "Incorrect username or password, try again?");

            if (!yes){
                break;
            }

        }

        return null;

    }

    private void printLoginMenu(){
        System.out.println("Welcome to student rentals!");
        System.out.println("Would you like to: ");
        System.out.println("1 - Login");
        System.out.println("2 - Register an account");
        System.out.println("3 - Quit");
    }

    private void loginOrRegister(Scanner sc) {

        // debug information
        System.out.println(usersByUsername.keySet());
        System.out.println(manager.RoomManager.getInstance().getRoomList());
        System.out.println(BookingManager.getInstance().getBookingsList().toString());


        // prints menu and either logs in or registers account depending on user input
        // returns account logged into or created after
        boolean running = true;
        User account = null;
        while (running) {
            printLoginMenu();
            int choice = InputParserService.obtainIntInRange(sc, 1, 3, "Enter your choice: ");

            switch (choice) {
                case 1 -> account = login(sc);
                case 2 -> account = registerAccount(sc);
                case 3 -> running = false;

            }
            // prevents loop being entered if previously logged in to an account
            // then quitting and it passing into main loop again
            if (!running){
                break;
            }

            if (account != null) {
                // ACCOUNT EXIT
                createAndStartSession(account, sc);

                // save account used in session
                saveAccount(account);

            }
        }
        // APPLICATION EXIT
        System.out.println("Goodbye!");
    }

    // main loop func
    private void createAndStartSession(User account, Scanner sc){

        // call load function for the singletons

        // dependencies injected into accounts as needed
        RoomSearchService roomSearchService = new RoomSearchService(RoomManager.getInstance());
        RoomHomeOwnerService roomHomeOwnerService = new RoomHomeOwnerService(RoomManager.getInstance());
        BookRoomService bookRoomService = new BookRoomService(BookingManager.getInstance());

        if (account instanceof Student s) {
            StudentSession session = new StudentSession(s, roomSearchService, sc, bookRoomService);
            session.start();
        } else if (account instanceof HomeOwner h) {
            HomeOwnerSession session = new HomeOwnerSession(h, sc, roomHomeOwnerService, bookRoomService);
            session.start();
        }

    }

    public void saveAccount(User account){
        // add account to global account hashmap
        usersByUsername.put(account.getUsername(), account);

        // serialize and write hashmap to accountdata.ser file

        try{
            FileOutputStream writeData = new FileOutputStream("accountdata.ser");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(usersByUsername);
            writeStream.flush();
            writeStream.close();

            System.out.println("Saved users successfully");

        } catch (IOException e){
            System.out.println("Error writing user account to file");
        }

    }

}
