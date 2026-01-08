package ui;

import data.Booking;
import data.BookingState;
import data.HomeOwner;
import data.Room;
import observer.InboxMessage;
import service.BookRoomService;
import service.InputParserService;
import service.RoomHomeOwnerService;

import java.time.LocalDate;
import java.util.*;

public class HomeOwnerSession {

    private final HomeOwner homeOwner;
    private final Scanner sc;
    private final RoomHomeOwnerService roomHomeOwnerService;
    private final BookRoomService bookRoomService;

    public HomeOwnerSession(HomeOwner HomeOwner, Scanner sc, RoomHomeOwnerService roomHomeOwnerService, BookRoomService bookRoomService) {
        this.homeOwner = HomeOwner;
        this.sc = sc;
        this.roomHomeOwnerService = roomHomeOwnerService;
        this.bookRoomService = bookRoomService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            String choice = sc.nextLine();
            running = handleChoice(choice);
        }
    }

    private void showMenu() {
        System.out.printf("Welcome HomeOwner %s!\n", homeOwner.getUsername());
        System.out.println("---------------------------");
        System.out.println("[1] - Add a room");
        System.out.println("[2] - Show your rooms");
        System.out.println("[3] - Show request inbox");
        System.out.println("[4] - Quit");
        System.out.println("---------------------------");
    }

    private boolean handleChoice(String choice) {
        switch (choice) {
            case "1" -> promptRoomCreation(sc, roomHomeOwnerService);
            case "2" -> selectRoomMenu();
            case "3" -> showInbox();
            case "4" -> {
                System.out.printf("Goodbye %s!\n", homeOwner.getUsername());
                return false;
            }
            default -> System.out.println("Invalid input!");
        }
        return true;
    }



    // function handles editing/deletion of room
    private boolean modifyRoom(Room room){


        while (true){
            System.out.println("----------SELECTED ROOM------------");
            displayRoom(room);
            System.out.println("""
            1 - Edit bed number
            2 - Edit price per week
            3 - Edit ensuite
            4 - Edit location
            5 - Edit room name
            D - Delete room
            X - Back
            """);

            String choice = sc.nextLine().trim();

            // attribute editing
            switch (choice.toUpperCase()) {
                case "1" -> {
                    int beds = InputParserService.obtainIntInRange(sc, 1, 10, "Enter number of beds: ");
                    room.setBedNum(beds);
                }
                case "2" -> {
                    double price = InputParserService.obtainDouble(sc, "Enter price per week: ");
                    room.setPricePerWeek(price);
                }
                case "3" -> {
                    boolean ensuite = InputParserService.isInputYes(sc, "Is the room ensuite?");
                    room.setEnsuite(ensuite);
                }
                case "4" -> {
                    String location = InputParserService.obtainString(sc, "Enter new location: ");
                    room.setLocation(location);

                }
                case "5" -> {
                    String name = InputParserService.obtainString(sc, "Enter new room name: ");
                    room.setRoomName(name);

                }
                // room deletion
                case "D" -> {
                    boolean yes = InputParserService.isInputYes(sc, "Are you sure you want to delete this room?");
                    if (yes) {
                        return true;
                    }
                }
                case "X" -> {
                    return false;
                }

                default -> System.out.println("Invalid option.");
            }

        }





    }

    // obtains a valid choice between 1 and number of rooms
    // or returns an x to exit
    private String obtainRoomSelectionChoice(int maxOptions){

        while (true){
            String input = sc.nextLine();
            if (input == null || input.trim().isEmpty()){
                System.out.println("Input cannot be empty.");
                continue;
            }

            input = input.trim();


            if (input.equalsIgnoreCase("x")){
                return "x";
            } else {
                try {
                    int selection = Integer.parseInt(input);
                    if (selection > 0 && selection <= maxOptions){
                        return Integer.toString(selection);
                    } else {
                        System.out.println("Please enter a valid number between 1 and " + maxOptions);
                    }


                } catch (NumberFormatException  | InputMismatchException e) {
                    System.out.println("Please enter a valid number between 1 and " + maxOptions);

                }
            }


        }



    }

    // shows room selection menu
    // lets homeowner see their rooms and delete/edit chosen ones
    private void selectRoomMenu(){
        while(true){

            // if user deletes all rooms or starts with none, show nothing
            if (homeOwner.getMyRooms().isEmpty()){
                System.out.println("No rooms owned!");
                break;
            }

            // displays all owned rooms
            int count = 1;

            for (Room room: homeOwner.getMyRooms()){
                System.out.println("---------------------------");
                System.out.println("OWNED ROOM " + count);
                System.out.println("---------------------------");
                count++;
                displayRoom(room);

            }

            System.out.println("---------------------------");
            System.out.println("[number] Select room to view");
            System.out.println("[X] Exit");


            String choice = obtainRoomSelectionChoice(homeOwner.getMyRooms().size());

                // exit
            if (choice.equals("x")){
                return;
            } else {
                // chosen room
                int choiceNum = Integer.parseInt(choice);

                Room room = homeOwner.getMyRooms().get(choiceNum-1);
                boolean modifyChoice = modifyRoom(room); // true = delete room, false = quit
                if (modifyChoice){
                    // set bookings to canceled for this room
                    bookRoomService.cancelBookingOnRoomDeletion(room);
                    // delete room from manager and homeowner list
                    roomHomeOwnerService.deleteRoom(homeOwner, room);
                }
            }
        }
    }


    private void showInbox() {
        boolean inboxOpen = true;
        boolean empty = false;

        // show homeowner inbox messages
        while (inboxOpen) {
            System.out.println("-----------INBOX-----------");
            List<InboxMessage> messages = homeOwner.getInboxMessages();

            if (messages.isEmpty()) {
                System.out.println("No messages...");
                empty = true;

            } else {
                for (int i = 0; i < messages.size(); i++) {
                    System.out.printf("%d - %s\n", i + 1, messages.get(i).getText());
                }
            }


            System.out.println("---------------------------");
            System.out.println("[number] View request");
            System.out.println("[R] Refresh inbox");
            System.out.println("[X] Exit inbox");

            String input = sc.nextLine();

                //  exit
            if (input.equalsIgnoreCase("X")) {
                inboxOpen = false;
                // refresh
            } else if (input.equalsIgnoreCase("R")) {
                continue;
            } else {
                // handle chosen message
                try {
                    if (!empty){
                        int index = Integer.parseInt(input) - 1;
                        handleInboxSelection(messages.get(index));
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleInboxSelection(InboxMessage msg) {

        // get the booking
        Booking booking = bookRoomService.getBookingById(msg.getBookingId());

        if (booking == null) {
            System.out.println("Booking no longer exists.");
            return;
        }

        // if already processed block action
        if (booking.getState() != BookingState.PENDING) {
            System.out.println("This booking has already been processed.");
            return;
        }

        // show available actions
        System.out.println("------BOOKING REQUEST------");
        System.out.println("Room: " + booking.getRoom().getRoomName());
        System.out.println("Student: " + booking.getStudent().getUsername());
        System.out.println("---------------------------");
        System.out.println("[1] Accept booking");
        System.out.println("[2] Reject booking");
        System.out.println("[3] Back");

        String choice = sc.nextLine();

        switch (choice) {
            case "1" -> bookRoomService.acceptBooking(booking);
            case "2" -> bookRoomService.rejectBooking(booking);
            default -> {}
        }
    }




    private void promptRoomCreation(Scanner sc, RoomHomeOwnerService roomHomeOwnerService){

        // room creation prompt, asks for attributes
        System.out.println("-------ROOM CREATION-------");

        int bedNum = InputParserService.obtainIntInRange(sc, 1, 10, "Enter the number of beds: ");
        double pricePerWeek = InputParserService.obtainDouble(sc, "Enter the price per week: ");
        boolean ensuite = InputParserService.isInputYes(sc,"Does it have an ensuite?" );
        String location = InputParserService.obtainString(sc, "Enter location: ");
        String roomName = InputParserService.obtainString(sc,"What is the room name: " );

        this.roomHomeOwnerService.addRoom(homeOwner, bedNum, pricePerWeek, ensuite, location, roomName);

        System.out.println("Successfully created room! exiting...");
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
                System.out.println("Booking from " + d[0].toString() + " to " + d[1].toString());
            }
        }
    }



}
