import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void Test0() {
        ArrayDequeSolution<Integer> goodDeque = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> badDeque = new StudentArrayDeque<>();

        for (int i = 0; i < 100; i++) {
            int numberBetweenZeroAndHundred = StdRandom.uniform(100);
            if (numberBetweenZeroAndHundred < 50) {
                goodDeque.addFirst(numberBetweenZeroAndHundred);
                badDeque.addFirst(numberBetweenZeroAndHundred);
                goodDeque.addLast(numberBetweenZeroAndHundred + 2);
                badDeque.addLast(numberBetweenZeroAndHundred + 2);
                goodDeque.addLast(numberBetweenZeroAndHundred + 3);
                badDeque.addLast(numberBetweenZeroAndHundred + 3);
                Integer goodInt = goodDeque.removeFirst();
                Integer badInt = badDeque.removeFirst();
                assertEquals(goodInt, badInt);
                goodInt = goodDeque.removeFirst();
                badInt = badDeque.removeFirst();
                assertEquals(goodInt, badInt);
            } else {
                goodDeque.addLast(numberBetweenZeroAndHundred);
                badDeque.addLast(numberBetweenZeroAndHundred);
                goodDeque.addFirst(numberBetweenZeroAndHundred - 1);
                badDeque.addFirst(numberBetweenZeroAndHundred - 1);
                goodDeque.addLast(numberBetweenZeroAndHundred - 20);
                badDeque.addLast(numberBetweenZeroAndHundred - 20);
                Integer goodInt = goodDeque.removeLast();
                Integer badInt = badDeque.removeLast();
                assertEquals(goodInt, badInt);
                goodInt = goodDeque.removeLast();
                badInt = badDeque.removeLast();
                assertEquals(goodInt, badInt);
            }
        }
    }
}

