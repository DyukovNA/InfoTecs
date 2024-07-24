package org.students; /**
 * Test task for an internship at InfoTecs.
 * <p>Console application, that provides access to a .json file on an FTP-server.
 * Allows to alter the file and get information about the students listed in it. </p>
 */

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * org.students.Main class. Prints out menu for user to select an action. Once an action is selected calls menu method from
 * org.students.ConsoleClient. Handles exceptions that org.students.ConsoleClient throws.
 * <p>The wording of the task states, that app takes as arguments only information to connect to an FTP-server.
 * Name of the .json file is not one of the arguments provided to the app. As a result the name is hardcoded.</p>
 * @see ConsoleClient
 */
public class Main {
    /**
     * Name of the file on an FTP-server.
     */
    private static final String jsonName = "students.json";
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please, enter your username, password and IP-address of the server:");
            // TestUser 1234 0.0.0.0
            ConnectionHandler connectionHandler = new ConnectionHandler(scanner.nextLine(), jsonName);
            ConsoleClient consoleClient = new ConsoleClient(connectionHandler);
            consoleClient.menu(scanner);
        }  catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
        }
    }
}