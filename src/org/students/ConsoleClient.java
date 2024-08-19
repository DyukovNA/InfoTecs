package org.students;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLConnection;
import java.util.*;

/**
 * В данном классе описаны функции, которые предоставляет клиент, а также меню для их вызова
 */
public class ConsoleClient {
    private final ConnectionHandler connectionHandler;
    private final TextEditor textEditor;
    /**
     * Создает FTP-ссылку на основе информации, предоставленной пользователем в org.students.Main.
     * @param connectionHandler Объект для взаимодействия с FTP-сервером
     */
    public ConsoleClient(ConnectionHandler connectionHandler){
        this.connectionHandler = connectionHandler;
        this.textEditor = new TextEditor();
    }

    /**
     * Вызывает методы в соответствии с количеством действий, предоставленных пользователем.
     * @throws IOException Если не удается получить InputStream или OutputStream из URLConnection одним из методов.
     * @see ConnectionHandler
     */
    public void menu(Scanner scanner) throws IOException {
        int action;

        System.out.println("1.\tПолучить список студентов по имени\n" +
                "2.\tПолучить информацию об ученике по id\n" +
                "3.\tДобавить студента в список\n" +
                "4.\tУдалить студента по id\n" +
                "5.\tВыход\n"
        );

        do {
            System.out.println("Выберите действие:\n");
            action = scanner.nextInt();
            switch (action) {
                case 1: {
                    showSortedStudentList();
                    break;
                }
                case 2: {
                    showStudentInfo();
                    break;
                }
                case 3: {
                    showAddStudent();
                    break;
                }
                case 4: {
                    showDeleteStudent();
                    break;
                }
                case 5: {
                    System.out.println("Выход из сеанса...");
                    break;
                }
                default: {
                    System.out.println("Неизвестная команда");
                    break;
                }
            }
        } while (action != 5);
    }

    /**
     * Проверяет, пуст ли список студентов. Выводит список или сообщение об ошибке
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     */
    private void showSortedStudentList() throws IOException {
        String toReturn = getSortedListOfStudents();
        if (toReturn.equals("")) {
            System.out.println("Студенты не найдены");
            return;
        }
        System.out.println(toReturn);
    }

    /**
     * Взаимодействует с пользователем, чтобы получить идентификатор студента, информацию о котором нужно найти.
     * Вызывает метод getStudentInfo() для получения данных студента с заданным идентификатором.
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     */
    private void showStudentInfo() throws IOException {
        System.out.println("Пожалуйста, введите идентификатор студента:");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        System.out.println(getStudentInfo(id));
    }

    /**
     * Взаимодействует с пользователем, чтобы получить информацию об ученике, которую необходимо добавить в файл.
     * Вызывает метод addStudent() для добавления студента с заданными аргументами.
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     * @see TextEditor
     */
    private void showAddStudent() throws IOException {
        System.out.println("Пожалуйста, введите данные студента:\n\n" +
                "Примечание: вводите аргументы и их значения парами. " +
                "Пары разделяются пробелом и запятой (', '). \n" +
                "Аргумент и его значение разделяются пробелом.\n" +
                "Например: name Alex, age 20\n");
        Scanner scanner = new Scanner(System.in);
        String info = scanner.nextLine();
        if (!textEditor.isValidInput(info)) {
            System.out.println("Неверный ввод");
            return;
        }
        addStudent(info);
        System.out.println("Студент успешно добавлен");
    }

    /**
     * Взаимодействует с пользователем, чтобы получить идентификатор учащегося, которого необходимо удалить из файла.
     * Вызывает метод deleteStudent() для удаления студента с заданным идентификатором.
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     */
    private void showDeleteStudent() throws IOException {
        System.out.println("Please, enter student's ID");
        Scanner scanner = new Scanner(System.in);
        int idDelete = scanner.nextInt();
        deleteStudent(Integer.toString(idDelete));
        System.out.println("Student successfully removed");
    }

    /**
     * Сортирует список студентов в алфавитном порядке и форматирует его в строку.
     * @return Строку с нумерованным списком
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     */
    public String getSortedListOfStudents() throws IOException {
        List<String> students = getListOfStudents();
        Collections.sort(students);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < students.size(); i++) {
            sb.append(i + 1).append(". ").append(students.get(i));
            if (i != students.size() - 1) sb.append(",\n");
        }
        return sb.toString();
    }

    /**
     * Находит студента с указанным идентификатором в файле и возвращает всю информацию, записанную в файле.
     * @param id ID студента
     * @return Строка со всей информацией об ученике в файле
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     */
    public String getStudentInfo(String id) throws IOException {
        Map<String, String> data = new LinkedHashMap<>();

        findStudentGetInfo(id, data);
        if (data.isEmpty()) {
            return "Информация не найдена";
        }

        return data.toString().replaceAll("[{}]", "").replace("=", ": ");
    }

    /**
     * Записывает информацию об ученике в файл. Генерирует идентификатор.
     * @param info Строка заданного формата с информацией о студенте
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     * @see TextEditor
     */
    public void addStudent(String info) throws IOException {
        writeHeaderIfEmpty();

        StringBuilder text = new StringBuilder();
        writeStudentToSB(text, info);
        BufferedWriter writer = connectionHandler.getWriter();

        writer.write(text.toString());
        writer.close();
    }

    /**
     * Удаляет запись студента с заданным идентификатором, переписывая весь файл и пропуская ненужную часть.
     * При необходимости меняет идентификаторы.
     * @param idToRemove ID студента
     * @throws IOException Если невозможно получить InputStream или OutputStream из URLConnection
     * @see ConnectionHandler
     * @see TextEditor
     */
    public void deleteStudent(String idToRemove) throws IOException {
        StringBuilder text = new StringBuilder();
        writeWithoutStudent(text, idToRemove);

        BufferedWriter writer = connectionHandler.getWriter();

        writer.write(text.toString());
        writer.close();
    }

    /**
     * Заполняет пустой список именами, найденными в файле
     * @throws IOException Если не удается получить InputStream из URLConnection
     * @see URLConnection
     * @see TextEditor
     */
    public List<String> getListOfStudents() throws IOException {
        List<String> students = new ArrayList<>();
        BufferedReader reader = connectionHandler.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(("\"name\":"))) {
                students.add(
                        textEditor.clean(line).replace("name:", "")
                );
            }
        }
        reader.close();
        return students;
    }

    /**
     * Находит студента в файле по указанному идентификатору и читает все его поля.
     * @param idToFind Предоставленный идентификатор
     * @param data Словарь аргументов и их значений
     * @throws IOException Если не удается получить InputStream из URLConnection
     * @see URLConnection
     * @see TextEditor
     */
    private void findStudentGetInfo(String idToFind, Map<String, String> data) throws IOException {
        BufferedReader reader = connectionHandler.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            if (textEditor.isId(line)) {
                String s = String.valueOf(textEditor.parseId(line));
                if (s.equals(idToFind)) {
                    while (!line.matches("\\s+},?")) {
                        String[] info = textEditor.clean(line).split(":");
                        data.putIfAbsent(info[0], info[1]);
                        line = reader.readLine();
                    }
                }
            }
        }
        reader.close();
    }

    /**
     * Записывает форматированную информацию об ученике в StringBuilder.
     * @param text StringBuilder в который записывается информация
     * @param info Информация о студенте
     * @throws IOException Если невозможно работать с BufferedReader
     */
    private void writeStudentToSB(StringBuilder text, String info) throws IOException {
        BufferedReader reader = connectionHandler.getReader();

        int maxId = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            if (textEditor.isId(line)) {
                // Max value of id read to this moment
                maxId = textEditor.parseId(line);
                textEditor.writeNewLine(text, line);
            } else if (textEditor.isEndOfLastEntry(line)) {
                // If it is last record insert new record with ID += 1
                maxId += 1;
                textEditor.writeNewLine(text, line + ",");
                textEditor.writeNewStudent(maxId, text, info);
                break;
            } else if (line.matches("\\s+]")) {
                maxId += 1;
                textEditor.writeNewStudent(maxId, text, info);
                break;
            }
            else textEditor.writeNewLine(text, line);
        }

        reader.close();
    }

    /**
     * Перезаписывает файл в StringBuilder, игнорируя студента с помощью idToRemove.
     * @param text StringBuilder в который записывается информация
     * @param idToRemove Идентификатор студента, которого необходимо удалить
     * @throws IOException Если невозможно работать с BufferedReader
     */
    private void writeWithoutStudent(StringBuilder text, String idToRemove) throws IOException {
        BufferedReader reader = connectionHandler.getReader();

        String line;
        StringBuilder studentBlock = new StringBuilder();
        boolean isRemoved = false;

        while ((line = reader.readLine()) != null) {
            if (textEditor.isBeginning(line)) {
                if (!textEditor.isBeginning(studentBlock.toString().replaceAll("\n", ""))) {
                    text.append(studentBlock);
                }
                studentBlock = new StringBuilder();
                textEditor.writeNewLine(studentBlock, line);
            } else if (textEditor.isEndOfLastEntry(line)) {
                textEditor.writeNewLine(studentBlock, line);
                text.append(studentBlock);
            } else if (textEditor.isId(line)) {
                isRemoved = analyseId(idToRemove, line, isRemoved, text, studentBlock, reader);
            } else if (textEditor.isField(line) || textEditor.isEndOfEntry(line)) {
                textEditor.writeNewLine(studentBlock, line);
            } else
                textEditor.writeNewLine(text, line);
        }

        reader.close();
    }

    /**
     * Вызывается в каждой строке, содержащей идентификатор. Если запись с idToRemove уже удалена, строка записывается в
     * текст с идентификатором, уменьшенным на 1. Если идентификатор в строке соответствует idToRemove, вызывает
     * skipStudent(), чтобы студент не был записан в StudentBlock, который содержит информацию об одном конкретном
     * студенте. В остальных случаях записывает строку в StudentBlock.
     * @param idToRemove ID of student
     * @param line Last line read by BufferedReader
     * @param isRemoved Indicator of if the student was removed
     * @param text StringBuilder with altered file
     * @param studentBlock StringBuilder with .json list entry
     * @param reader BufferedReader of file
     * @return Is student entry skipped
     * @throws IOException If error occurred in skipStudent
     */
    private boolean analyseId(
            String idToRemove, String line, boolean isRemoved, StringBuilder text,
            StringBuilder studentBlock, BufferedReader reader
    ) throws IOException {
        int currId;
        currId = textEditor.parseId(line);
        if (Integer.parseInt(idToRemove) == currId && !isRemoved) {
            skipStudent(line, reader, text);
            isRemoved = true;
        } else if (isRemoved) {
            currId -= 1;
            textEditor.writeNewLine(studentBlock, "\t\t\t\"id\": " + currId + ",");
        } else {
            textEditor.writeNewLine(studentBlock, line);
        }
        return isRemoved;
    }

    /**
     * Пропускает всю информацию записанную о студените
     * @param line Last line read by BufferedReader
     * @param reader BufferedReader of file
     * @param text StringBuilder with altered file
     */
    private void skipStudent(String line, BufferedReader reader, StringBuilder text) throws IOException {
        while (textEditor.isField(line)) {
            line = reader.readLine();
        }
        if (line.matches(TextEditor.endOfLastEntryRegex)) {
            if (text.indexOf("},") != -1) {
                text.replace(text.length()-5, text.length()-1, "\t\t}");
            }
        }
    }

    /**
     * Записывает в пустой файл пустой список "students".
     * @throws IOException Если невозможно прочитать следующую строку
     */
    private void writeHeaderIfEmpty() throws IOException {
        String header = getHeaderIfEmpty();
        if (!header.equals("")) {
            BufferedWriter writerHeader = connectionHandler.getWriter();
            writerHeader.write(header);
            writerHeader.flush();
            writerHeader.close();
        }
    }

    /**
     * Проверяет, пуст ли файл. Если да, возвращает пустой список json "students".
     * @throws IOException Если невозможно прочитать следующую строку
     */
    private String getHeaderIfEmpty() throws IOException {
        BufferedReader reader = connectionHandler.getReader();
        if (reader.readLine() == null) {
            return "{\n\t\"students\": [\n\t]\n}";
        }
        reader.close();
        return "";
    }
}
