/* Description: Array-based bounded stack (max 10). Logic only, no DOM code.
   Programmed by: <Your Name> <Course> <cn> <Subject>  Last Modified: 2026-10-18  Version: 1.0 */
class ArrayStack {
  constructor() { this.MAX = 10; this.data = new Array(10); this.top = -1; this.cap = 10; }
  isEmpty() { return this.top === -1; }
  isFull() { return this.top === this.cap - 1; }
  size() { return this.top + 1; }
  push(v) { if (this.isFull()) throw new Error("STACK OVERFLOW: Cannot push, stack is full"); this.data[++this.top] = v; }
  pop() { if (this.isEmpty()) throw new Error("STACK UNDERFLOW: Cannot pop, stack is empty"); const v = this.data[this.top]; this.data[this.top--] = undefined; return v; }
  peek() { if (this.isEmpty()) throw new Error("EMPTY STACK: Nothing to peek, stack is empty"); return this.data[this.top]; }
  clear() { this.top = -1; this.data.fill(undefined); }
  setCapacity(c) { if (c < this.size()) throw new Error("CAPACITY ERROR: Cannot go below current size (" + this.size() + ")"); this.cap = c; }
  toArray() { return this.data.slice(0, this.top + 1); } // copy for the UI
}
