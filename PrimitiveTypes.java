import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Практикум: примитивные типы Java и классы-обёртки.
 * 1) Выводит характеристики всех восьми примитивных типов.
 * 2) Запрашивает у пользователя значения каждого типа и преобразует
 *    введённую строку в нужный тип методами классов-обёрток.
 */
public class PrimitiveTypes {

    // Общий Scanner для чтения строк из терминала
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        printTypesInfo();
        readAndConvertValues();
        SCANNER.close();
    }

    /**
     * Часть 1. Таблица характеристик примитивных типов.
     * Все значения берутся из констант классов-обёрток.
     */
    private static void printTypesInfo() {
        System.out.println("=== Примитивные типы Java ===");
        System.out.printf("%-8s %-6s %-6s %-28s %-28s%n",
                "Тип", "Бит", "Байт", "Минимум", "Максимум");

        // Целочисленные типы
        printRow("byte", Byte.SIZE, Byte.BYTES, Byte.MIN_VALUE, Byte.MAX_VALUE);
        printRow("short", Short.SIZE, Short.BYTES, Short.MIN_VALUE, Short.MAX_VALUE);
        printRow("int", Integer.SIZE, Integer.BYTES, Integer.MIN_VALUE, Integer.MAX_VALUE);
        printRow("long", Long.SIZE, Long.BYTES, Long.MIN_VALUE, Long.MAX_VALUE);

        // Типы с плавающей точкой: MIN_VALUE — наименьшее ПОЛОЖИТЕЛЬНОЕ число,
        // поэтому отдельно показываем и самое отрицательное значение (-MAX_VALUE)
        printRow("float", Float.SIZE, Float.BYTES, Float.MIN_VALUE, Float.MAX_VALUE);
        printRow("double", Double.SIZE, Double.BYTES, Double.MIN_VALUE, Double.MAX_VALUE);
        System.out.println("   (для float/double MIN_VALUE — наименьшее положительное число;");
        System.out.println("    самое отрицательное: float = " + (-Float.MAX_VALUE)
                + ", double = " + (-Double.MAX_VALUE) + ")");

        // char — беззнаковый 16-битный символ Unicode; выводим коды символов
        printRow("char", Character.SIZE, Character.BYTES,
                "'\\u0000' (" + (int) Character.MIN_VALUE + ")",
                "'\\uffff' (" + (int) Character.MAX_VALUE + ")");

        // boolean: размер в спецификации JVM не определён, у Boolean нет SIZE/MIN/MAX,
        // поэтому выводим допустимые значения из констант Boolean.FALSE и Boolean.TRUE
        System.out.printf("%-8s %-6s %-6s %-28s %-28s%n",
                "boolean", "—", "—", Boolean.FALSE, Boolean.TRUE);
        System.out.println("   (размер boolean не зафиксирован спецификацией; на практике обычно 1 байт)");
        System.out.println();
    }

    // Вывод одной строки таблицы
    private static void printRow(String name, int bits, int bytes, Object min, Object max) {
        System.out.printf("%-8s %-6d %-6d %-28s %-28s%n", name, bits, bytes, min, max);
    }

    /**
     * Часть 2. По очереди запрашиваем значение каждого типа
     * и преобразуем строку методами классов-обёрток.
     */
    private static void readAndConvertValues() {
        System.out.println("=== Преобразование строк в примитивные типы ===");
        try {
            byte b = readValue("byte", Byte::parseByte);
            short s = readValue("short", Short::parseShort);
            int i = readValue("int", Integer::parseInt);
            long l = readValue("long", Long::parseLong);
            float f = readValue("float", Float::parseFloat);
            double d = readValue("double", Double::parseDouble);
            char c = readValue("char", PrimitiveTypes::parseChar);
            boolean bool = readValue("boolean", PrimitiveTypes::parseStrictBoolean);

            // Итоговая сводка всех полученных значений
            System.out.println();
            System.out.println("=== Итог ===");
            System.out.println("byte    = " + b);
            System.out.println("short   = " + s);
            System.out.println("int     = " + i);
            System.out.println("long    = " + l);
            System.out.println("float   = " + f);
            System.out.println("double  = " + d);
            System.out.println("char    = '" + c + "' (код " + (int) c + ")");
            System.out.println("boolean = " + bool);
        } catch (NoSuchElementException e) {
            // Ввод закончился (например, Ctrl+D / Ctrl+Z или конец pipe)
            System.out.println();
            System.out.println("Ввод завершён раньше времени — программа остановлена.");
        }
    }

    /**
     * Запрашивает строку и преобразует её указанной функцией.
     * При ошибке формата выводит понятное сообщение и просит ввести значение снова.
     */
    private static <T> T readValue(String typeName, Function<String, T> parser) {
        while (true) {
            System.out.print("Введите значение типа " + typeName + ": ");
            String line = SCANNER.nextLine().trim();
            try {
                T value = parser.apply(line);
                System.out.println("  -> преобразовано в " + typeName + ": " + value);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  Ошибка: строка \"" + line + "\" не является корректным значением типа "
                        + typeName + ". " + hint(typeName) + " Попробуйте ещё раз.");
            }
        }
    }

    // Подсказка о допустимом диапазоне/формате для каждого типа
    private static String hint(String typeName) {
        switch (typeName) {
            case "byte":    return "Допустимо целое от " + Byte.MIN_VALUE + " до " + Byte.MAX_VALUE + ".";
            case "short":   return "Допустимо целое от " + Short.MIN_VALUE + " до " + Short.MAX_VALUE + ".";
            case "int":     return "Допустимо целое от " + Integer.MIN_VALUE + " до " + Integer.MAX_VALUE + ".";
            case "long":    return "Допустимо целое от " + Long.MIN_VALUE + " до " + Long.MAX_VALUE + ".";
            case "float":
            case "double":  return "Ожидается число, например 3.14 или -2.5e3 (разделитель — точка).";
            case "char":    return "Ожидается ровно один символ.";
            case "boolean": return "Ожидается true или false.";
            default:        return "";
        }
    }

    /**
     * Преобразование строки в char. Отдельного Character.parseChar нет,
     * поэтому проверяем длину и берём символ через charAt; для наглядности
     * упаковываем результат в Character.valueOf.
     */
    private static Character parseChar(String text) {
        if (text.length() != 1) {
            throw new NumberFormatException("Нужен ровно один символ");
        }
        return Character.valueOf(text.charAt(0));
    }

    /**
     * Boolean.parseBoolean никогда не бросает исключение: всё, кроме "true"
     * (без учёта регистра), он превращает в false. Чтобы опечатка вроде "ture"
     * не стала незаметно false, сначала проверяем, что введено true или false.
     */
    private static Boolean parseStrictBoolean(String text) {
        if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
            throw new NumberFormatException("Нужно true или false");
        }
        return Boolean.parseBoolean(text);
    }
}
