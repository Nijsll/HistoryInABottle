package util;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class IO {
    public static String getString(String question) {
        Scanner scanner = Global.scanner;
        System.out.println(question);
        System.out.print("Answer: ");
        String answer = scanner.nextLine();
        return answer;
    }

    public static int getInt(String question) {
        Scanner scanner = Global.scanner;
        boolean gotAnswer = false;
        int answer = 0;
        while (!gotAnswer) {
            System.out.println(question);
            System.out.print("Answer: ");
            try {
                answer = scanner.nextInt();
                gotAnswer = true;
            } catch (InputMismatchException e) {
                System.out.println("Input could not be interpreted as a number, try again");
            }
        }
        return answer;
    }

    public static <T> T getChoice(String question, List<T> items) {
        Scanner scanner = Global.scanner;
        boolean gotAnswer = false;
        int answer = -1;
        while (!gotAnswer) {
            System.out.println(question);
            for (int i = 0; i < items.size(); i++) {
                System.out.println(" " + (i+1) + " - " + items.get(i).toString());
            }
            try {
                System.out.print("Pick one: ");
                answer = scanner.nextInt() -1;
                if (answer >= 0 && answer < items.size()) {
                    gotAnswer = true;
                } else {
                    System.out.println("That was not one of the options, try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input could not be interpreted as a number, try again");
            }
        }

        return items.get(answer);

    }

    public static void getEnter() {
        Scanner scanner = new Scanner(System.in);

        // Print the prompt
        System.out.print("Enter...");

        // Wait for user input
        String a = scanner.nextLine();

        if (a.equals("quit") || a.equals("q")) {
            Global.speed = 2;
            Global.quit = true;
        }

        // Move the cursor up and clear the line
//        System.out.print("\033[1A");  // Move up
//        System.out.print("\033[2K");  // Clear line
    }
}
