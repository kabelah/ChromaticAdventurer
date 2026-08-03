package org.phase1;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isUserDone = false;
        while (!isUserDone) {
            try {
                System.out.println("Which file do you want? (Type in the file path)");
                String fileName = scanner.nextLine();
                String graph = fileToString(fileName);
                ObtainChromaticNumber.start(graph);
                System.out.println("\nDo you want to check another graph? (yes/no)");
                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("yes")||answer.equalsIgnoreCase("y")) {
                    isUserDone = false;
                }
                else {
                    isUserDone = true;
                }
            } catch (NullPointerException e) {
                System.out.println("Please enter a valid path");
                isUserDone = false;
            }
        }
    }

    public static String fileToString(String fileName) {
        try {
            return new String (Files.readAllBytes(Paths.get(fileName)));
        }
        catch (Exception e) {
        return null;
        }
    }
}
