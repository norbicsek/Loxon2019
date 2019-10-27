package team.executors;

public class SysOut {

    private SysOut() {
    }

    public static final void print(Object o) {
        System.out.print(o); // NOSONAR
    }

    public static final void print(String s) {
        System.out.print(s); // NOSONAR
    }

    public static final void println(Object o) {
        System.out.println(o); // NOSONAR
    }

    public static final void println(String s) {
        System.out.println(s); // NOSONAR
    }

    public static final void println() {
        System.out.println(); // NOSONAR
    }

}
