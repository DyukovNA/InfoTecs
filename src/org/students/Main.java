package org.students;

/**
 * <p>Консольное приложение, обеспечивающее доступ к файлу .json на FTP-сервере.
 * Позволяет изменить файл и получить информацию о перечисленных в нем студентах. </p>
 */

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * org.students.Main class. Выводит меню, в котором пользователь может выбрать действие.
 * После выбора действия вызывается метод меню из org.students.ConsoleClient.
 * Обрабатывает исключения, которые выбрасывает org.students.ConsoleClient.
 * @see ConsoleClient
 */
public class Main {
    /**
     * Имя файла на FTP-сервере.
     */
    private static final String jsonName = "students.json";
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Пожалуйста, введите логин, пароль и IP-адрес сервера:");
            // TestUser 1234 0.0.0.0
            ConnectionHandler connectionHandler = new ConnectionHandler(scanner.nextLine(), jsonName);
            ConsoleClient consoleClient = new ConsoleClient(connectionHandler);
            consoleClient.menu(scanner);
        }  catch (IOException e) {
            System.out.println("\nОшибка подключения. Проверьте свои данные для входа и повторите попытку.\n");
        } catch (InputMismatchException e) {
            System.out.println("Неверные данные");
        }
    }
}