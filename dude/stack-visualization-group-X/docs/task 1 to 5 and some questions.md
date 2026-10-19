# Task 1 – Stack trace

| Operation | Value | Stack after operation (bottom → top) | Top |
|---|---|---|---|
| Push | 10 | 10 | 10 |
| Push | 20 | 10, 20 | 20 |
| Push | 30 | 10, 20, 30 | 30 |
| Pop | (removes 30) | 10, 20 | 20 |
| Push | 40 | 10, 20, 40 | 40 |
| Peek | (reads 40) | 10, 20, 40 (unchanged) | 40 |
| Pop | (removes 40) | 10, 20 | 20 |
| Pop | (removes 20) | 10 | 10 |
| IsEmpty | – | 10 → returns false | 10 |
| Pop | (removes 10) | empty | none |

# Task 2 - GUI Design
Mockup of the 2D Stack Visualizer
<img width="1419" height="790" alt="Screenshot 2026-10-20 000526" src="https://github.com/user-attachments/assets/4a392a4a-6392-405b-afd1-857ee948155e" />

# Task 2 – GUI Design
Mockup of the 2D Stack Visualizer

| Required component | Where it is in the mockup |
|---|---|
| Area where the stack is displayed | Stack A and Stack B columns, 10 slots each, TOP marked on the newest element |
| Input field | "Value:" text field |
| Push, Pop, Peek buttons | PUSH, POP, PEEK in the first button row |
| Clear button | CLEAR in the second button row |
| Check stack button | CHECK STACK in the first button row |
| Random button | RANDOM in the second button row |
| Status message area | Line at the bottom (green for success, red for errors) |
| Challenge: undo | UNDO button |
| Challenge: second stack and transfer | Stack B column, TRANSFER A>B and POP B buttons |
| Challenge: history | History list on the right |
| Challenge: capacity selector | Capacity drop-down (1 to 10) |


Class Diagram

<img width="938" height="703" alt="image" src="https://github.com/user-attachments/assets/67e41332-5e9f-4e05-a7ec-5582893ce263" />



•	Main creates the StackGUI window.

•	StackGUI owns two ArrayStack objects (a, b) and two StackPanel objects. It only calls ArrayStack methods; it never reads or writes the internal array.

•	StackPanel only draws. It receives a copy of the stack contents from toArray() and paints it.

•	StackTests uses ArrayStack alone, with no GUI, to test the boundary cases.



Program flow

<img width="734" height="783" alt="image" src="https://github.com/user-attachments/assets/ed7fbbf2-24fc-4303-8522-f5348b5b1e22" />



How the program works
Every button in the window follows the same path. The GUI reads what the user did, asks ArrayStack to do the operation, and then redraws the screen from the stack's real contents. The stack decides whether an operation is allowed; the GUI only shows the result.

Push. The PUSH button (or Enter in the text field) calls push() in StackGUI. parse() reads the text and turns it into an int, or throws an INVALID INPUT message if it is not a whole number. act() saves a snapshot for undo and calls a.push(v). ArrayStack stores the value at data[++top]. If the stack is full it throws STACK OVERFLOW instead and nothing changes.

Pop. pop() calls a.pop(), which returns data[top] and then lowers top by one. On an empty stack it throws STACK UNDERFLOW. The popped value appears in the status message and the history list.

Peek. peek() calls a.peek(), which returns data[top] without moving top. The GUI flashes the top block and shows the value. Peek does not change the stack, so it is not saved for undo.

Check Stack. check() calls isEmpty(), isFull() and size() and reports whether Stack A is empty, how many elements it holds, and whether it is full.

Clear. clear() sets top back to -1, so the stack counts as empty. The old numbers stay in the array but are never read.

Random. random() clears Stack A, picks how many elements to create (1 up to the current capacity) and pushes that many random integers from 1 to 100.

Transfer A>B. transfer() pops the top of Stack A and pushes it onto Stack B. If Stack B is full, the value is pushed back onto Stack A so nothing is lost and the overflow message is shown.

Undo. Before every successful change, act() stores a copy of both stacks and the capacity. undoLast() restores the most recent copy. Failed operations and Peek do not create a snapshot.

Capacity selector. setCapacity() changes how many slots are usable (1 to 10). It refuses a value lower than the number of elements already stored.

Keeping the screen in sync. After every action, refresh() calls panelA.update(a.toArray(), a.getCapacity()) and the same for Stack B. StackPanel draws only from that copy, so the picture always matches the real stack.


# Classes

| Class | Responsibility |
|---|---|
| ArrayStack | The stack data structure. Stores up to 10 ints in an array and implements push, pop, peek, isEmpty, isFull, size, clear, capacity and toArray. Contains no GUI code. |
| StackGUI | The Swing window (extends JFrame). Builds the buttons, text field, status line, history list and capacity selector, and handles every click by calling ArrayStack methods. |
| StackPanel | A custom JPanel that draws one stack vertically with the newest element on top, the TOP label, empty slots and the peek highlight. |
| Main | Entry point. Opens StackGUI using SwingUtilities.invokeLater. |
| StackTests | Console program that checks the boundary cases (underflow, overflow, peek, LIFO order, capacity) without a GUI. |

# Identifiers (important variables)

| Where | Name and type | Purpose |
|---|---|---|
| ArrayStack | data : int[10] | Array that stores the elements |
| ArrayStack | top : int | Index of the top element; -1 means the stack is empty |
| ArrayStack | capacity : int | Current limit chosen by the user (1 to 10) |
| ArrayStack | MAX_CAPACITY : int | Constant 10, the size of the array |
| StackGUI | a, b : ArrayStack | Stack A and Stack B |
| StackGUI | panelA, panelB : StackPanel | The two drawing areas |
| StackGUI | input : JTextField | Where the user types the value to push |
| StackGUI | status : JLabel | Shows the result or error message |
| StackGUI | history : DefaultListModel<String> | Operation history shown in the list |
| StackGUI | undo : Deque<int[][]> | Snapshots used by UNDO |


| Where | Name and type | Purpose |
|---|---|---|
| StackGUI | capBox : JComboBox<Integer> | Capacity selector |
| StackPanel | values : int[], cap : int, flash : boolean | Copy of the stack to draw, its capacity, and the peek highlight |

# Methods

| Method | What it does |
|---|---|
| ArrayStack.push(value) | Throws STACK OVERFLOW if full; otherwise data[++top] = value |
| ArrayStack.pop() | Throws STACK UNDERFLOW if empty; otherwise returns data[top--] |
| ArrayStack.peek() | Throws an empty-stack message if empty; otherwise returns data[top] without removing it |
| ArrayStack.isEmpty(), isFull(), size() | top == -1; top == capacity - 1; top + 1 |
| ArrayStack.clear() | Sets top to -1 |
| ArrayStack.setCapacity(c) | Sets the limit if c is 1 to 10 and not below the current size |
| ArrayStack.toArray() | Returns a copy of the stored elements, bottom first, for the GUI to draw |
| StackGUI.act(op) | Runs a change; saves a snapshot, performs it, logs history and shows the message; catches errors and shows them in red |
| StackGUI.parse() | Converts the text field to an int or reports an invalid input |
| StackGUI.snapshot() / restore(s) | Copy both stacks and capacity; put them back for UNDO |
| StackGUI.refresh() | Updates both panels and the capacity selector from the stacks |
| StackPanel.update(values, capacity) | Stores the new copy and repaints |
| StackPanel.paintComponent(g) | Draws slots, values, the TOP label and the BOTTOM label |
| StackPanel.flashTop() | Highlights the top block for 0.8 seconds using a Swing Timer |

Libraries used
•	javax.swing: JFrame, JPanel, JButton, JTextField, JLabel, JComboBox, JList, JScrollPane, DefaultListModel, Timer and SwingUtilities for the window and controls.

•	java.awt: Graphics2D, Color, Font, GradientPaint, BasicStroke, RenderingHints, Dimension and the layout managers (BorderLayout, GridLayout, FlowLayout) for drawing and layout.

•	java.util: Arrays (copying the array), Deque and ArrayDeque (undo snapshots) and Random (Random button).

•	java.util.function.Supplier: lets act() receive an operation to run.

•	The web version (web folder) uses plain HTML, CSS and JavaScript with no libraries. It mirrors the same stack logic in stack.js.

Task 3 – Program Documentation
GitHub Repository
[]


#Task 4 – Screenshot 1 to 11 and
1. <img width="888" height="1028" alt="Screenshot 2026-10-20 010827" src="https://github.com/user-attachments/assets/2acfbc93-455f-4366-ab36-f8c24e7762f5" />

2. <img width="838" height="1019" alt="Screenshot 2026-10-20 010845" src="https://github.com/user-attachments/assets/5537c61b-e0a4-461b-8090-2952cc92a972" />

3. <img width="830" height="1028" alt="Screenshot 2026-10-20 010903" src="https://github.com/user-attachments/assets/e6aaca8c-4cdb-4bb5-b806-3e40b248ee1f" />

4. <img width="849" height="1018" alt="Screenshot 2026-10-20 010917" src="https://github.com/user-attachments/assets/4f4a065c-6a84-40d0-80b9-f7473d92740d" />

5. <img width="834" height="1013" alt="Screenshot 2026-10-20 010935" src="https://github.com/user-attachments/assets/4a249895-91b5-4c63-9a34-1578861f6983" />

6.<img width="808" height="1030" alt="Screenshot 2026-10-20 010954" src="https://github.com/user-attachments/assets/759e4613-0459-48c6-a273-5016baa4dc75" />

7. <img width="331" height="406" alt="Screenshot 2026-10-20 011012" src="https://github.com/user-attachments/assets/caeec912-6810-4efc-b199-af71ae69aa20" />

8. <img width="451" height="393" alt="Screenshot 2026-10-20 011024" src="https://github.com/user-attachments/assets/dbbbedea-fc5d-4e0b-8357-a0851bf2c6c3" />

9. <img width="318" height="396" alt="Screenshot 2026-10-20 011047" src="https://github.com/user-attachments/assets/1eaf0d45-95b1-4a4e-bbc5-316c6a826727" />

10. <img width="423" height="744" alt="Screenshot 2026-10-20 011125" src="https://github.com/user-attachments/assets/2f7e78a0-4305-4e74-b2d9-85ce4a3910ee" />

11. <img width="350" height="301" alt="Screenshot 2026-10-20 011136" src="https://github.com/user-attachments/assets/75c8597d-9aa2-46d9-84c5-a4a92a8fb2c7" />


# Task 5 – Test Boundary Conditions



# Test Executions

<img width="640" height="340" alt="Screenshot 2026-10-20 011247" src="https://github.com/user-attachments/assets/eb55e782-0764-42ad-9f99-fff52fb07311" />

# Overflow

<img width="465" height="846" alt="Screenshot 2026-10-20 011449" src="https://github.com/user-attachments/assets/c3bf703f-09cb-4d8d-91e1-0834bd869bb0" />

# Underflow

<img width="503" height="266" alt="Screenshot 2026-10-20 012946" src="https://github.com/user-attachments/assets/c97af51a-0eee-4635-9a12-d762d69a557b" />

# Empty Peek

<img width="430" height="149" alt="Screenshot 2026-10-20 011543" src="https://github.com/user-attachments/assets/4c19f952-9832-4dea-ae75-940b5565d716" />

# Error & Fix

<img width="434" height="150" alt="Screenshot 2026-10-20 011612" src="https://github.com/user-attachments/assets/cf48d35f-c477-4559-8523-ca0db936b4fd" />



