package team.executors;

public class SysErr {

    private SysErr() {
    }

    public static final void print(Object o) {
        System.err.print(o); // NOSONAR
    }

    public static final void print(String s) {
        System.err.print(s); // NOSONAR
    }

    public static final void println(Object o) {
        System.err.println(o); // NOSONAR
    }

    public static final void println(String s) {
        System.err.println(s); // NOSONAR
    }

    public static final void println() {
        System.err.println(); // NOSONAR
    }

    public static final void printStackTrace(Throwable t) {
        t.printStackTrace(System.err); // NOSONAR
    }

}
