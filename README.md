# Stack Visualization – 

**Group Name:** <Group 9>
**Group Members:** < Terencejohn >

## Description
A 2D visualizer for an array-based, bounded LIFO stack (maximum 10 elements). The stack logic and the GUI are separate components, and the GUI only talks to the stack through its methods. Two versions are included: a Java Swing app (graded program) and a Vesper-style web demo.

## Features
- Push, Pop, Peek, Check Stack, Clear, Random (1 to capacity elements, values 1–100)
- Top element clearly marked; display updates after every operation
- Messages: `STACK UNDERFLOW: Cannot pop, stack is empty` and `STACK OVERFLOW: Cannot push, stack is full`
- Challenges: Undo, second stack with transfer A to B, operation history, capacity selector (1–10)
- Console test runner for the boundary cases

## Requirements
- Java JDK 17 or newer (Swing is built in, no extra libraries)
- Any modern browser for the web demo (internet needed for the Google Fonts and hero video)

## How to run
**Java (VS Code terminal, from the project root):**
```
javac -d out src/*.java
java -cp out Main
java -cp out StackTests
```
**Web:** open `web/index.html` in a browser (or use the VS Code Live Server extension).

## Project Structure
```
src/ArrayStack.java   stack component (array, top index, capacity)
src/StackPanel.java   draws one stack
src/StackGUI.java     Swing window, buttons, status, history
src/Main.java         entry point
src/StackTests.java   console tests (Task 5)
web/                  index.html, css/styles.css, js/stack.js, js/app.js
docs/trace-and-tests.md
screenshots/
```

## Stack Implementation
`ArrayStack` uses `int[10]` and an index `top` (-1 means empty). `push` does `data[++top] = v`, `pop` returns `data[top--]`, `peek` returns `data[top]` without moving `top`. `isFull()` is `top == capacity - 1`. Overflow and underflow throw `IllegalStateException`, which the GUI shows in the status bar. The GUI reads a copy from `toArray()` and never touches the array.

## Screenshots
<img width="1419" height="790" alt="Screenshot 2026-10-20 000526" src="https://github.com/user-attachments/assets/bd4cb2c1-178e-44ab-9d4c-2fa3b57015a5" />


## AI Disclosure
- **Tool:** Claude (Anthropic) – opus 5.5
- **Task:** code drafting for the Java and web versions, README draft, test drafting
- **Prompts used:** help me understand this group activity and ill be the one making it
- **Extent:** <e.g. substantial drafting>
- **Human verification:** terence john reviewed, ran and understood the code before committing
- **Why and how:** cuz the project is hard for me
- **Estimated AI-assisted share:** <75%>
- **Changes made to the AI output:** improved UI, add animations, fix bugs.
- **What I learned:** i learned how to push and pop, learned github, learned folder structure for me not to be confused

## Contributors
| Member | Contribution |
|---|---|
| Terence |  ArrayStack, tests, UI  |
