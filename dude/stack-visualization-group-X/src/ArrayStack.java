/* Description: Entry point. Opens the Swing stack visualizer.
Programmed by: Terence john Martin BSIT CC104 DATA STRUCTURES AND ALGORITHMS
Last Modified: 2026-10-17
Version: 1.0
*/
import java.util.Arrays;

public class ArrayStack {
    public static final int MAX_CAPACITY = 10;
    private final int[] data = new int[MAX_CAPACITY]; // the array that stores the elements
    private int top = -1;                              // index of the top element (-1 = empty)
    private int capacity = MAX_CAPACITY;               // usable capacity chosen by the user (1..10)

    public void push(int value) {
        if (isFull()) throw new IllegalStateException("STACK OVERFLOW: Cannot push, stack is full");
        data[++top] = value;
    }

    public int pop() {
        if (isEmpty()) throw new IllegalStateException("STACK UNDERFLOW: Cannot pop, stack is empty");
        return data[top--];
    }

    public int peek() {
        if (isEmpty()) throw new IllegalStateException("EMPTY STACK: Nothing to peek, stack is empty");
        return data[top]; // read only: top does not move
    }

    public boolean isEmpty() { return top == -1; }
    public boolean isFull()  { return top == capacity - 1; }
    public int size()        { return top + 1; }
    public void clear()      { top = -1; }
    public int getCapacity() { return capacity; }

    public void setCapacity(int c) {
        if (c < 1 || c > MAX_CAPACITY) throw new IllegalStateException("CAPACITY ERROR: Choose 1 to " + MAX_CAPACITY);
        if (c < size()) throw new IllegalStateException("CAPACITY ERROR: Cannot go below current size (" + size() + ")");
        capacity = c;
    }

    /** Copy of the elements, bottom first. The GUI reads this copy, never the real array. */
    public int[] toArray() { return Arrays.copyOf(data, size()); }
}
