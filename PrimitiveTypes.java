// GitHub: https://github.com/asavich-code/PrimitiveTypes
import java.util.Scanner;
import java.util.function.Function;

public class PrimitiveTypes {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.printf("%-8s %-6s %-6s %-28s %-28s%n", "Тип", "Бит", "Байт", "Минимум", "Максимум");
        printRow("byte", Byte.SIZE, Byte.BYTES, Byte.MIN_VALUE, Byte.MAX_VALUE);
        printRow("short", Short.SIZE, Short.BYTES, Short.MIN_VALUE, Short.MAX_VALUE);
        printRow("int", Integer.SIZE, Integer.BYTES, Integer.MIN_VALUE, Integer.MAX_VALUE);
        printRow("long", Long.SIZE, Long.BYTES, Long.MIN_VALUE, Long.MAX_VALUE);
        printRow("float", Float.SIZE, Float.BYTES, Float.MIN_VALUE, Float.MAX_VALUE);
        printRow("double", Double.SIZE, Double.BYTES, Double.MIN_VALUE, Double.MAX_VALUE);
        printRow("char", Character.SIZE, Character.BYTES, (int) Character.MIN_VALUE, (int) Character.MAX_VALUE);
        System.out.println("boolean: " + Boolean.FALSE + ", " + Boolean.TRUE);
        System.out.println();

        byte b = readValue("byte", Byte::parseByte);
        short s = readValue("short", Short::parseShort);
        int i = readValue("int", Integer::parseInt);
        long l = readValue("long", Long::parseLong);
        float f = readValue("float", Float::parseFloat);
        double d = readValue("double", Double::parseDouble);
        char c = readValue("char", PrimitiveTypes::parseChar);
        boolean bool = readValue("boolean", PrimitiveTypes::parseStrictBoolean);

        System.out.println(b + " " + s + " " + i + " " + l + " " + f + " " + d + " " + c + " " + bool);
        SCANNER.close();
    }

    private static void printRow(String name, int bits, int bytes, Object min, Object max) {
        System.out.printf("%-8s %-6d %-6d %-28s %-28s%n", name, bits, bytes, min, max);
    }

    private static <T> T readValue(String typeName, Function<String, T> parser) {
        while (true) {
            System.out.print("Введите " + typeName + ": ");
            String line = SCANNER.nextLine().trim();
            try {
                return parser.apply(line);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: \"" + line + "\" не подходит для типа " + typeName + ". Попробуйте ещё раз.");
            }
        }
    }

    private static Character parseChar(String text) {
        if (text.length() != 1) {
            throw new NumberFormatException("нужен ровно один символ");
        }
        return text.charAt(0);
    }

    private static Boolean parseStrictBoolean(String text) {
        if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
            throw new NumberFormatException("нужно true или false");
        }
        return Boolean.parseBoolean(text);
    }
}
