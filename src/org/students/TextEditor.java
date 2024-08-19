package org.students;


/**
 * Этот класс предоставляет методы для работы с данными из файла.
 */
public class TextEditor {
    /**
     * Регулярные выражения, используемые в классе для парсинга файла .json.
     */
    public static final String idRegex = "\\s+\"id\": \\d+,?";
    public static final String fieldRegex = "\\s+\"[\\w\\d]+\":\\s+\"?[\\w\\d\\s]+\"?,?\n?";
    public static final String symbolsRegex = "[\\s\"{},]";
    public static final String endOfEntryRegex = "\\s+},";
    public static final String endOfLastEntryRegex = "\\s+}";
    public static final String beginOfNewEntryRegex = "\\s+\\{";
    public static final String inputRegex = "\\w+ [\\w\\d]+((, \\w+ [\\w\\d,.]+)+)?";

    public TextEditor() {}

    /**
     * Записывает новую запись об ученике в файл
     * @param id ID нового студента
     * @param text StringBuilder с измененным файлом
     * @param info Строка заданного формата с информацией о студенте
     */
    public void writeNewStudent(int id, StringBuilder text, String info) {
        String[] infoPairs = info.split(", ");
        int i = infoPairs.length;

        writeNewLine(text, "\t\t{");
        writeNewLine(text, "\t\t\t\"id\": " + id + ",");

        for (String infoPair: infoPairs) {
            i-=1;
            writeNewField(i, infoPair, text);
        }

        text.append("\t\t}\n\t]\n}");
    }
    /**
     * Записывает в текст новое поле с данными из infoPair.
     * @param i Счетчик того, сколько полей осталось записать
     * @param infoPair Содержит ключ и значение, разделенные пробелом.
     * @param text StringBuilder с измененным файлом
     */
    public void writeNewField(int i, String infoPair, StringBuilder text) {
        String argumentDgt = "\t\t\t\"%s\": %s";
        String argumentStr = "\t\t\t\"%s\": \"%s\"";

        String[] entry = infoPair.split(" ");

        if (entry[1].matches("\\d+")) {
            text.append(String.format(argumentDgt, entry[0].toLowerCase(), entry[1]));
        } else text.append(String.format(argumentStr, entry[0].toLowerCase(), entry[1]));
        if (i == 0) {
            text.append("\n");
        } else text.append(",\n");
    }

    /**
     * Записывает строку с переходом на новую строку строкой в StringBuilder.
     * @param sb StringBuilder
     * @param line Строка, которую необходимо записать
     */
    public void writeNewLine(StringBuilder sb, String line) {
        sb.append(line).append("\n");
    }

    /**
     * Находит и возвращает ID из строки определённого формата
     * @param line Строка определённого формата
     * @return ID
     */
    public int parseId(String line) {
        return Integer.parseInt(
                clean(line).replace("id:", "")
        );
    }

    /**
     * Удаляет из строки все специальные символы, которые могут быть в файле JSON.
     * @param line Строка для изменения
     * @return Имя поля и его значение
     */
    public String clean(String line) {
        return line.replaceAll(symbolsRegex,"");
    }

    /**
     * Проверяет, содержит ли строка допустимый ввод
     * @param line Строка для проверки
     * @return Содержит ли строка действительные данные
     */
    public boolean isValidInput(String line) {
        return line.matches(inputRegex);
    }

    /**
     * Проверяет, содержит ли строка идентификатор
     * @param line Строка для проверки
     * @return Содержит ли строка ID
     */
    public boolean isId(String line) {
        return line.matches(idRegex);
    }

    /**
     * Проверяет, содержит ли строка символ '}', указывающий последнюю запись в списке json.
     * @param line Строка для проверки
     * @return Содержит ли строка '}'
     */
    public boolean isEndOfLastEntry(String line) {
        return line.matches(endOfLastEntryRegex);
    }
    /**
     * Проверяет, содержит ли строка символ '{', указывающий на начало новой записи в списке json
     * @param line Строка для проверки
     * @return Содержит ли строка '{'
     */
    public boolean isBeginning(String line) {
        return line.matches(beginOfNewEntryRegex);
    }
    /**
     * Проверяет, содержит ли строка поле списка json.
     * @param line Строка для проверки
     * @return Содержит ли строка поле
     */
    public boolean isField(String line){
        return line.matches(fieldRegex);
    }
    /**
     * Проверяет, содержит ли строка '},' указывающие на начало новой записи в списке json.
     * @param line Строка для проверки
     * @return Содержит ли строка '},'
     */
    public boolean isEndOfEntry(String line) {
       return line.matches(endOfEntryRegex);
    }
}
