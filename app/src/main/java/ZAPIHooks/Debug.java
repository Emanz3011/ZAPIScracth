package ZAPIHooks;

/**
 * Allows for more clear debugging and error handling.
 */
final class Debug {
    public static void Error(String s) {
        System.err.println("ZAPI Error-" + s);
    }

    public static void Status(String s) {
        System.out.println("ZAPI Status-" + s);
    }
}
