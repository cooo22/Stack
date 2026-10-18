/* Description: Console test runner for the Task 5 boundary cases. Run: java StackTests
Last Modified: 2026-10-17
Version: 1.0
*/
public class StackTests {
    static int fails = 0;
    static void check(String name, boolean ok) { System.out.println((ok ? "PASS  " : "FAIL  ") + name); if (!ok) fails++; }
    static boolean throwsMsg(Runnable r, String start) {
        try { r.run(); return false; } catch (IllegalStateException e) { return e.getMessage().startsWith(start); }
    }

    public static void main(String[] args) {
        ArrayStack s = new ArrayStack();
        check("EMPTY pop -> UNDERFLOW", throwsMsg(s::pop, "STACK UNDERFLOW"));
        check("EMPTY peek -> empty-stack message", throwsMsg(s::peek, "EMPTY STACK"));
        s.push(10); check("EMPTY push 10 -> [10]", s.size() == 1 && s.peek() == 10);
        s.push(20); check("[10] push 20 -> [10,20]", s.size() == 2);
        check("[10,20] peek -> 20, size unchanged", s.peek() == 20 && s.size() == 2);
        check("[10,20] pop -> 20 removed", s.pop() == 20 && s.size() == 1);
        s.clear(); check("ANY clear -> EMPTY", s.isEmpty());
        for (int i = 1; i <= 10; i++) s.push(i);
        check("FULL push 99 -> OVERFLOW", s.isFull() && throwsMsg(() -> s.push(99), "STACK OVERFLOW"));
        // extra cases
        check("Extra 1: LIFO order (10 pops 10 first)", s.pop() == 10 && s.peek() == 9);
        check("Extra 2: capacity cannot be set below current size", throwsMsg(() -> s.setCapacity(3), "CAPACITY ERROR"));
        s.clear(); s.setCapacity(2); s.push(1); s.push(2);
        check("Extra 3: capacity 2 overflows on 3rd push", throwsMsg(() -> s.push(3), "STACK OVERFLOW"));
        System.out.println(fails == 0 ? "ALL TESTS PASSED" : fails + " TEST(S) FAILED");
    }
}
