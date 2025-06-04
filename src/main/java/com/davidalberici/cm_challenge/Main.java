package com.davidalberici.cm_challenge;

import com.davidalberici.cm_challenge.hexagon.Megaverse;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello and welcome, this is your megaverse manager. Please introduce your candidate id:");
        String candidateId = "b2f8b8be-5953-4d4a-a43d-6752db2b7088";
        System.out.println("Assumed id: b2f8b8be-5953-4d4a-a43d-6752db2b7088");
//        Scanner input = new Scanner(System.in);
//        String candidateId = input.next();
        System.out.println(
                "Great, we now have the candidate id ("+candidateId+"), now, what do you want to do?\n" +
                        "Type 'help' for a list of commands, or 'exit' to finish");

        String nextCommand = null;
        Scanner input = new Scanner(System.in);
        do {
            nextCommand = input.next();
            handleNextCommand(nextCommand);
        } while (!"exit".equals(nextCommand));

    }

    private static void handleNextCommand(String nextCommand) {
        switch (nextCommand) {
            case "help" -> System.out.println("""
                        Commands:
                            help -> shows this list, a summary of all commands
                            reset-megaverse -> deletes everything from the current megaverse
                            build-megaverse -> looks at your current goal, and builds the megaverse accordingly
                            print-current-megaverse -> prints the current megaverse in the console
                            print-goal-megaverse -> prints the goal megaverse in the console
                            check-current-megaverse-validity -> tells you if the current megaverse is valid or not
                            exit -> shuts down the program
                    """);
            case "exit" -> System.out.println("Shutting down... See you next time");
            case "reset-megaverse" -> System.out.println("Not yet implemented");
            case "build-megaverse" -> System.out.println("Not yet implemented");
            case "check-current-megaverse-validity" -> printCurrentMegaverseValidity();
            case "print-current-megaverse" -> printCurrentMegaverse();
            case "print-goal-megaverse" -> printGoalMegaverse();
            default -> System.out.println(
                    "Invalid command: '" + nextCommand + "'. Type 'help' for a list of valid commands");
        }
    }

    private static void printCurrentMegaverseValidity() {
        try {
            SimpleDependencyInjection.hexagonApi.getCurrentMegaverse().checkIsValid();
        } catch (Exception e) {
            System.out.println("Current megaverse is invalid: " + e.getMessage());
            return;
        }
        System.out.println("Current megaverse is valid.");
    }

    private static void printCurrentMegaverse() {
        Megaverse m = SimpleDependencyInjection.hexagonApi.getCurrentMegaverse();
        MegaversePrinter.printMegaverse(m);
    }

    private static void printGoalMegaverse() {
        Megaverse m = SimpleDependencyInjection.hexagonApi.getGoalMegaverse();
        MegaversePrinter.printMegaverse(m);
    }
}
