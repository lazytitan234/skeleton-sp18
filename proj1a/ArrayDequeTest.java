public class ArrayDequeTest {

    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");

        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.addLast("middle");
        passed = checkSize(2, lld1.size()) && passed;

        lld1.addLast("back");
        passed = checkSize(3, lld1.size()) && passed;

        lld1.addLast("back2");
        passed = checkSize(4, lld1.size()) && passed;

        lld1.addLast("t1");
        passed = checkSize(5, lld1.size()) && passed;

        lld1.addLast("t2");
        passed = checkSize(6, lld1.size()) && passed;

        lld1.addLast("t3");
        passed = checkSize(7, lld1.size()) && passed;

        lld1.addLast("t4");
        passed = checkSize(8, lld1.size()) && passed;

        lld1.addLast("t5");
        passed = checkSize(9, lld1.size()) && passed;

        System.out.println("Printing out deque: ");
        lld1.printDeque();

        printTestStatus(passed);
    }

    public static void addRemoveTest() {
        System.out.println("Running add/remove test.");

        ArrayDeque<String> lld2 = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, lld2.isEmpty());

        lld2.addLast("middle");
        passed = checkSize(1, lld2.size()) && passed;

        lld2.addLast("back");
        passed = checkSize(2, lld2.size()) && passed;

        lld2.removeFirst();
        passed = checkSize(1, lld2.size()) && passed;

        lld2.removeLast();
        passed = checkSize(0, lld2.size()) && passed;

        printTestStatus(passed);
    }

    public static void getTest() {
        System.out.println("Running add/isEmpty/Size test.");

        ArrayDeque<String> lld3 = new ArrayDeque<String>();

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.

        lld3.addLast("middle");

        lld3.addFirst("back");

        lld3.addLast("back2");

        System.out.println(lld3.get(0));

        System.out.println(lld3.get(2));

        System.out.println(lld3.get(4));

    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        getTest();
    }
}