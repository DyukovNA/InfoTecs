package org.students;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Этот класс предоставляет методы для взаимодействия с FTP-сервером.
 */
public class ConnectionHandler {
    /**
     * Поля для FTP-ссылки и имени файла
     */
    public final String link;

    /**
     * Создает FTP-ссылку, используя ввод пользователя и имя файла, который должен храниться на сервере.
     * @param input Пользовательский ввод с информацией для входа
     * @param JSONName Имя файла
     */
    public ConnectionHandler(String input, String JSONName) throws IOException {
        String[] loginInfo = input.split(" ");
        this.link = "ftp://" + loginInfo[0] + ":" + loginInfo[1] + "@" + loginInfo[2] + "/" + JSONName;

        BufferedReader reader = getReader();
        reader.close();
    }
    /**
     * Создает BufferedReader для чтения файла из соединения..
     * @return BufferedReader для чтения файла
     * @throws IOException Если не удается получить InputStream из URLConnection
     * @see URLConnection
     */
    public BufferedReader getReader() throws IOException {
        URLConnection connection = setConnection();
        assert connection != null;
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    /**
     * Creates BufferedWriter to write to the file from connection.
     * @return BufferedWriter to write to the file
     * @throws IOException Если не удается получить OutputStream из URLConnection
     * @see URLConnection
     */
    public BufferedWriter getWriter() throws IOException {
        URLConnection connection = setConnection();
        assert connection != null;
        OutputStream outputStream = connection.getOutputStream();
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * Устанавливает соединение с FTP-сервером и файлом на нем.
     * @return Подключение к FTP-серверу
     * @see URLConnection
     */
    public URLConnection setConnection() {
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.connect();
            return connection;
        } catch (IOException e) {
            System.out.println("Невозможно подключиться к серверу");
            e.printStackTrace();
        }
        return null;
    }
}
