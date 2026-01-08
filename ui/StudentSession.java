package ui;

import data.*;
import manager.RoomManager;
import observer.InboxMessage;
import service.BookRoomService;
import service.InputParserService;
import service.RoomSearchService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

public class StudentSession {

    private final Student student;
    private final RoomSearchService roomSearchService;
    private final Scanner sc;
    private final BookRoomService bookRoomService;

    public StudentSession(Student student, RoomSearchService roomSearchService, Scanner sc, BookRoomService bookRoomService) {
        this.student = student;
        this.roomSearchService = roomSearchService;
        this.sc = sc;
        this.bookRoomService = bookRoomService;
    }

    public void start() {
        // main loop of show menu, handle choice
        boolean running = true;
        while (running) {
            showMenu();
            String choice = sc.nextLine();
            running = handleChoice(choice);
        }
    }

    private void showMenu() {
        System.out.printf("Welcome Student %s!\n", student.getUsername());
        System.out.println("---------------------------");
        System.out.println("[1] - Show your bookings");
        System.out.println("[2] - Search for rooms");
        System.out.println("[3] - Show request inbox");
        System.out.println("[4] - Quit");
        System.out.println("---------------------------");
    }

    private void showStudentsBookings(){

        System.out.println("---------BOOKINGS----------");
        List<Booking> bookings = bookRoomService.showStudentsBookings(student);
        if (!bookings.isEmpty()){
            int count = 1;
            for (Booking b: bookings){
                Room room = b.getRoom();

                // booking information
                System.out.printf("Booking number: %d\n", count);
                System.out.println("Location: " + room.getLocation());
                System.out.println("Room number: " + room.getRoomName());

                // print availability depending on booking status
                switch(b.getState()){
                    case BookingState.CANCELLED -> System.out.println("BOOKING CANCELLED BY OWNER");
                    case BookingState.REJECTED -> System.out.println("BOOKING REQUEST REJECTED BY OWNER");
                    case BookingState.ACCEPTED -> System.out.println("Starting: " + b.getStartDate().toString() + " Ending: " + b.getEndDate().toString());
                    default -> System.out.println("BOOKING REQUEST PENDING...");
                }
                System.out.println("---------------------------");
            }

        } else {
            System.out.println("No bookings upcoming");
        }

    }

    private boolean handleChoice(String choice) {
        switch (choice) {
            case "1" -> showStudentsBookings();
            case "2" -> searchRooms();
            case "3" -> showInbox();
            case "4" -> {
                System.out.printf("Goodbye %s!\n", student.getUsername());
                return false;
            }
            default -> System.out.println("Invalid input");
        }
        return true;
    }

    private SearchCriteria getSearchCriteria(){

        System.out.println("Enter your search requirements");
        double maxPrice = InputParserService.obtainDouble(sc, "Enter the max price per week: ");
        boolean hasEnsuite  = InputParserService.isInputYes(sc, "Room has ensuite? ");
        int bedNums = InputParserService.obtainIntInRange(sc, 1, 10, "Enter the minimum number of beds: ");
        return new SearchCriteria(maxPrice, hasEnsuite, bedNums);


    }

    private void searchRooms(){

        // get input for search criteria
        SearchCriteria searchCriteria = getSearchCriteria();
        List<Room> rooms = roomSearchService.searchRooms(searchCriteria);
        
        if (!rooms.isEmpty()){

            int count = 1;
            for (Room room : rooms) {
                System.out.println("---------------------------");
                System.out.println("ROOM " + count++);
                System.out.println("---------------------------");
                displayRoom(room);
            }

            bookRoomPrompt(rooms);
            
            
        } else {
            System.out.println("---------------------------");
            System.out.println("No rooms available!");
            System.out.println("---------------------------");
        }
    }
    
    private void bookRoomPrompt(List<Room> rooms){

        // handles room booking
        boolean finishedBooking = false;
        while (!finishedBooking){
            boolean yes1 = InputParserService.isInputYes(sc, "Would you like to book a room?");
            if (yes1){
                int choice = InputParserService.obtainIntInRange(sc, 1, rooms.size(), "Enter the option number of the room to book: ");

                LocalDate[] dates = bookDatesPrompt();

                boolean yes2 = InputParserService.isInputYes(sc, format("To confirm, you would like to book option %d?", choice));
                if (yes2){
                    // book room via book room service
                    bookRoomService.bookRoom(student, rooms.get(choice-1), dates[0], dates[1]);
                    finishedBooking = true;
                }
            } else {
                finishedBooking = true;
            }
        }
    }

    // obtains 2 dates start and end to request for a booking
    private LocalDate[] bookDatesPrompt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = null;
        LocalDate endDate = null;

        while (startDate == null) {
            System.out.println("Enter the start date of your stay (YYYY-MM-DD): ");
            String startInput = sc.nextLine();
            try {
                startDate = LocalDate.parse(startInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        while (endDate == null) {
            System.out.println("Enter the end date of your stay (YYYY-MM-DD): ");
            String endInput = sc.nextLine();
            try {
                endDate = LocalDate.parse(endInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
            // checks end date is not before starting date
            if (endDate == null){
                continue;
            }

            if (endDate.isBefore(startDate)){
                System.out.println("End date cannot be before start date. Please try again.");
                endDate = null;
            }
        }




        return new LocalDate[]{startDate, endDate};
    }

    // shows student inbox messages, cant interact with them
    private void showInbox() {
        boolean inboxOpen = true;

        while (inboxOpen) {
            System.out.println("-----------INBOX-----------");

            List<InboxMessage> messages = student.getInboxMessages();

            if (messages.isEmpty()) {
                System.out.println("No messages...");
            } else {
                for (int i = 0; i < messages.size(); i++) {
                    InboxMessage msg = messages.get(i);
                    System.out.printf(
                            "%d - %s%n",
                            i + 1,
                            msg.getText()
                    );
                }
            }

            System.out.println("---------------------------");
            System.out.println("[1] Refresh inbox");
            System.out.println("[2] Exit inbox");

            String input = sc.nextLine();
            switch (input) {
                case "1" -> {} // refresh (loop re-runs)
                case "2" -> inboxOpen = false;
                default -> System.out.println("Invalid input!");
            }
        }
    }


    private void displayRoom(Room room){
        System.out.printf("Beds: %d\n", room.getBedNum());
        System.out.printf("PPW: %.2f\n", room.getPricePerWeek());
        System.out.printf("Ensuite: %b\n", room.isEnsuite());
        System.out.printf("Location: %s\n", room.getLocation());
        System.out.printf("Room name: %s\n", room.getRoomName());
        if (room.getBookedDates().isEmpty()){
            System.out.println("No dates booked");
        } else {
            for (LocalDate[] d: room.getBookedDates()){
                System.out.println("Booking from " + d[0].toString() + "to " + d[1].toString());
            }
        }
    }
}