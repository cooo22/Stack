/* Description: Console test runner for the Task 5 boundary cases. Run: java StackGUI
Last Modified: 2026-10-17
Version: 1.0
*/
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class StackGUI extends JFrame {
    private final ArrayStack a = new ArrayStack(), b = new ArrayStack();
    private final StackPanel panelA = new StackPanel("STACK A"), panelB = new StackPanel("STACK B");
    private final JTextField input = new JTextField(6);
    private final JLabel status = new JLabel("Ready. Enter a value and press PUSH.");
    private final DefaultListModel<String> history = new DefaultListModel<>();
    private final JComboBox<Integer> capBox = new JComboBox<>();
    private final Deque<int[][]> undo = new ArrayDeque<>(); // snapshots taken before each change
    private final Random rnd = new Random();
    private boolean syncing;

    public StackGUI() {
        super("Stack Visualizer - LIFO (array-based, max 10)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(8, 8));

        JPanel stacks = new JPanel(new GridLayout(1, 2, 8, 0));
        stacks.setOpaque(false); stacks.add(panelA); stacks.add(panelB);

        JList<String> list = new JList<>(history);
        list.setBackground(new Color(15, 15, 15)); list.setForeground(Color.LIGHT_GRAY);
        JScrollPane hist = new JScrollPane(list);
        hist.setPreferredSize(new Dimension(190, 0));
        TitledBorder tb = BorderFactory.createTitledBorder("History");
        tb.setTitleColor(Color.WHITE); hist.setBorder(tb);

        for (int i = 1; i <= ArrayStack.MAX_CAPACITY; i++) capBox.addItem(i);
        capBox.setSelectedItem(ArrayStack.MAX_CAPACITY);
        capBox.addActionListener(e -> { if (!syncing) setCapacity((Integer) capBox.getSelectedItem()); });

        JLabel valueLbl = new JLabel("Value:"); valueLbl.setForeground(Color.WHITE);
        JLabel capLbl = new JLabel("Capacity:"); capLbl.setForeground(Color.WHITE);
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4)); row1.setOpaque(false);
        row1.add(valueLbl); row1.add(input);
        row1.add(btn("PUSH", this::push)); row1.add(btn("POP", this::pop));
        row1.add(btn("PEEK", this::peek)); row1.add(btn("CHECK STACK", this::check));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4)); row2.setOpaque(false);
        row2.add(btn("RANDOM", this::random)); row2.add(btn("CLEAR", this::clear));
        row2.add(btn("UNDO", this::undoLast)); row2.add(btn("TRANSFER A>B", this::transfer));
        row2.add(btn("POP B", this::popB)); row2.add(capLbl); row2.add(capBox);
        status.setFont(new Font("SansSerif", Font.BOLD, 14));
        status.setBorder(BorderFactory.createEmptyBorder(4, 12, 8, 12));
        JPanel south = new JPanel(new GridLayout(3, 1)); south.setOpaque(false);
        south.add(row1); south.add(row2); south.add(status);

        add(stacks, BorderLayout.CENTER); add(hist, BorderLayout.EAST); add(south, BorderLayout.SOUTH);
        input.addActionListener(e -> push()); // Enter key pushes
        refresh();
        pack(); setLocationRelativeTo(null);
    }

    private JButton btn(String text, Runnable r) {
        JButton x = new JButton(text); x.addActionListener(e -> r.run()); return x;
    }

    private int parse() {
        try { return Integer.parseInt(input.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalStateException("INVALID INPUT: Enter a whole number"); }
    }

    /** Runs a change on the stacks. Saves an undo snapshot only if it succeeds. op returns {historyLabel, statusMessage}. */
    private void act(Supplier<String[]> op) {
        int[][] snap = snapshot();
        try {
            String[] r = op.get();
            undo.push(snap); history.add(0, r[0]); say(r[1], false);
        } catch (IllegalStateException ex) { say(ex.getMessage(), true); }
        refresh();
    }

    private void push() { act(() -> { int v = parse(); a.push(v); return new String[]{"Push " + v, "Pushed " + v + ". TOP is now " + v + "."}; }); input.setText(""); input.requestFocus(); }
    private void pop() { act(() -> { int v = a.pop(); return new String[]{"Pop -> " + v, "Popped " + v + " from the top."}; }); }
    private void popB() { act(() -> { int v = b.pop(); return new String[]{"Pop B -> " + v, "Popped " + v + " from Stack B."}; }); }
    private void clear() { act(() -> { a.clear(); return new String[]{"Clear", "Stack A cleared. It is now empty."}; }); }

    private void random() {
        act(() -> {
            a.clear();
            int n = 1 + rnd.nextInt(a.getCapacity());
            for (int i = 0; i < n; i++) a.push(1 + rnd.nextInt(100));
            return new String[]{"Random (" + n + ")", "Random stack of " + n + " element(s) generated."};
        });
    }

    private void transfer() {
        act(() -> {
            int v = a.pop();
            try { b.push(v); } catch (IllegalStateException e) { a.push(v); throw e; } // put it back if B is full
            return new String[]{"Transfer " + v + " A>B", "Moved " + v + " from Stack A to Stack B."};
        });
    }

    private void setCapacity(int c) {
        act(() -> {
            if (c < Math.max(a.size(), b.size())) throw new IllegalStateException("CAPACITY ERROR: Cannot go below current size");
            a.setCapacity(c); b.setCapacity(c);
            return new String[]{"Capacity " + c, "Maximum capacity set to " + c + "."};
        });
    }

    private void peek() { // read only: no undo snapshot, no change
        try { int v = a.peek(); panelA.flashTop(); say("PEEK: TOP is " + v + " (not removed).", false); }
        catch (IllegalStateException ex) { say(ex.getMessage(), true); }
    }

    private void check() {
        say(a.isEmpty() ? "CHECK: Stack A is EMPTY."
                : "CHECK: Stack A is NOT empty (size " + a.size() + ", " + (a.isFull() ? "FULL" : "not full") + ").", false);
    }

    private void undoLast() {
        if (undo.isEmpty()) { say("NOTHING TO UNDO: No earlier operation.", true); return; }
        restore(undo.pop()); history.add(0, "Undo"); say("Last operation undone.", false); refresh();
    }

    private int[][] snapshot() { return new int[][]{a.toArray(), b.toArray(), {a.getCapacity()}}; }

    private void restore(int[][] s) {
        a.clear(); b.clear(); a.setCapacity(ArrayStack.MAX_CAPACITY); b.setCapacity(ArrayStack.MAX_CAPACITY);
        for (int v : s[0]) a.push(v);
        for (int v : s[1]) b.push(v);
        a.setCapacity(s[2][0]); b.setCapacity(s[2][0]);
    }

    private void refresh() {
        panelA.update(a.toArray(), a.getCapacity());
        panelB.update(b.toArray(), b.getCapacity());
        syncing = true; capBox.setSelectedItem(a.getCapacity()); syncing = false;
    }

    private void say(String msg, boolean error) {
        status.setText(msg);
        status.setForeground(error ? new Color(255, 120, 120) : new Color(140, 230, 160));
    }
}
