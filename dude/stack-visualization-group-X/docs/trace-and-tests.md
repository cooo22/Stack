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

# Task 5 – Extra test cases (run `StackTests`)

| Stack | Operation | Input | Expected output |
|---|---|---|---|
| FULL (10) | POP then PUSH | 99 | 10 removed, then 99 accepted |
| Capacity 2, holds 2 | PUSH | 3 | STACK OVERFLOW |
| Holds 5 elements | Set capacity to 3 | – | CAPACITY ERROR, capacity unchanged |
| A = [7], B full | TRANSFER A to B | – | OVERFLOW message, 7 stays in A |
| Empty | UNDO | – | NOTHING TO UNDO |
