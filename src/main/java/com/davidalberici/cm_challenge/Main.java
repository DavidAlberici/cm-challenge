package com.davidalberici.cm_challenge;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello and welcome, this is your megaverse manager. Please introduce your candidate id:");
        Scanner input = new Scanner(System.in);
        String candidateId = input.next();
        System.out.println(
                "Great, we now have the candidate id ("+candidateId+"), now, what do you want to do?\n" +
                        "Type 'help' for a list of commands, or 'exit' to finish");

        String nextCommand = null;
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
                            exit -> shuts down the program
                    """);
            case "exit" -> System.out.println("Shutting down... See you next time");
            case "reset-megaverse" -> System.out.println("Not yet implemented");
            case "build-megaverse" -> System.out.println("Not yet implemented");
            default -> System.out.println(
                    "Invalid command: '" + nextCommand + "'. Type 'help' for a list of valid commands");
        }
    }
}
