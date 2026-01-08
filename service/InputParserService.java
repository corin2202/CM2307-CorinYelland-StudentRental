package service;

import javax.lang.model.type.PrimitiveType;
import java.util.InputMismatchException;
import java.util.Scanner;

// utility class that contains helpful parsing functions
public class InputParserService {

    // prevent instantiation
    private InputParserService(){};

    public static boolean isInputYes(Scanner sc, String prompt){

        while (true){
            System.out.printf("%s [Y/N]: \n", prompt);
            String input = sc.nextLine();

            if (input == null || input.trim().isEmpty()){
                System.out.println("Input cannot be empty.");
                continue;
            }

            if (input.trim().equalsIgnoreCase("y")){
                return true;
            }

            if (input.trim().equalsIgnoreCase("n")){
                return false;
            }
            System.out.println("Invalid input, please enter y or n");
        }

    }

    public static String obtainString(Scanner sc, String prompt){
        while(true) {
            System.out.println(prompt);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
            } else {
                return input;
            }
        }
    }

    public static double obtainDouble(Scanner sc, String prompt){
        while(true) {
            System.out.println(prompt);
            String input = sc.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }

            try {
                double choice = Double.parseDouble(input.trim());
                if (choice >= 1.0) {
                    return choice;
                } else {
                    System.out.println("Input cannot be less than 1! ");
                }


            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Invalid input");
            }
        }
    };

    public static int obtainIntInRange(Scanner sc, int start, int end, String prompt){
        while (true){
            System.out.println(prompt);
            String input = sc.nextLine();

            // empty input check
            if (input == null || input.trim().isEmpty()){
                System.out.println("Input cannot be empty.");
                continue;
            }
            try {
                int choice = Integer.parseInt(input.trim());

                // range check
                if (choice >= start && choice <= end){
                    return choice;
                } else {
                    System.out.printf("Please enter an number between %d and %d\n", start, end);
                }

                // not a number
            } catch (NumberFormatException | InputMismatchException e){
                System.out.println("Invalid input");


            }

        }
    }





    public static String obtainAccountType(Scanner sc) {
        while (true) {
            System.out.print("Would you like to create a student [S] or homeowner [H] account: ");
            String input = sc.nextLine();

            if (input == null || input.trim().isEmpty()){
                System.out.println("Input cannot be empty.");
                continue;
            }

            input = input.toUpperCase();

            if (input.equals("S") || input.equals("H")) {
                return input;
            }

            System.out.println("Invalid input");
        }
    }


    public static String obtainPassword(Scanner sc){
        String password;
        while (true) {
            System.out.println("Please enter a new password: ");
            password = sc.nextLine().trim();
            if (!password.isEmpty()){
                break;
            }
            System.out.println("Password cannot be empty.");
        }

        return password;
    }


}
