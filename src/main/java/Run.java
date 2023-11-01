import events.EventHandler;
import history.History;
import util.Global;
import util.IO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        EventHandler.init();
        History history = new History();
        Global.scanner = new Scanner(System.in);
        boolean running = true;

        try {
            int years = IO.getInt("How many years should be simulated?");
            while (running) {
                history.passTime(years);
                if (Global.quit) {
                    running = false;
                } else {
                    System.out.println("Continue for how many years? (q/quit to exit program)");
                    System.out.print("Answer: ");
                    try {
                        years = Global.scanner.nextInt();
                    } catch (InputMismatchException e) {
                        running = false;
                    }
                }
            }
        } finally {
            Global.scanner.close();
            System.out.println("Closing program...");
        }


    }
}
