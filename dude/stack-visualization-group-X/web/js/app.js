/* Description: UI + entrance animation fallback. Talks to ArrayStack only through its methods.
   Programmed by: <Your Name> <Course> <cn> <Subject>  Last Modified: 2026-10-18  Version: 1.0 */
(() => {
  const $ = s => document.querySelector(s);
  // Entrance animation: mark finished, and force visible if animations never run.
  document.querySelectorAll(".appear").forEach(e => e.addEventListener("animationend", () => e.classList.add("is-in"), { once: true }));
  requestAnimationFrame(() => requestAnimationFrame(() => {
    const e = $(".appear");
    if (!e.getAnimations().some(a => a.playState === "running" || a.playState === "finished"))
      document.querySelectorAll(".appear").forEach(x => x.classList.add("is-in"));
  }));

  const vid = $(".hero-video");
  vid.addEventListener("playing", () => vid.classList.add("on")); // fade video in only if it really plays
  const A = new ArrayStack(), B = new ArrayStack(), undo = [], hist = [];
  const st = $("#status"), capEl = $("#cap"), valEl = $("#val");
  let flash = false, pushed = false;
  for (let i = 1; i <= 10; i++) capEl.add(new Option(i, i));
  capEl.value = 10;

  const snap = () => JSON.stringify({ a: A.toArray(), b: B.toArray(), c: A.cap });
  function restore(s) {
    s = JSON.parse(s); A.cap = B.cap = 10; A.clear(); B.clear();
    s.a.forEach(v => A.push(v)); s.b.forEach(v => B.push(v)); A.cap = B.cap = s.c;
  }
  function msg(t, err) { st.textContent = t; st.className = err ? "err" : "ok"; }

  function draw(root, S, isA) {
    root.innerHTML = "";
    for (let i = 9; i >= 0; i--) {
      const used = i <= S.top, d = document.createElement("div");
      d.className = "slot " + (used ? "filled" : i < S.cap ? "empty" : "locked");
      if (i === S.top) { d.classList.add("top"); if (isA && flash) d.classList.add("peek"); if (isA && pushed) d.classList.add("enter"); }
      d.innerHTML = "<i>" + i + "</i>" + (used ? S.data[i] : "") + (i === S.top ? "<em>TOP</em>" : "");
      root.append(d);
    }
  }
  function render() {
    draw($("#stackA"), A, true); draw($("#stackB"), B, false);
    $("#history").innerHTML = hist.slice(0, 10).map(h => "<li>" + h + "</li>").join("");
    $("#sSize").textContent = A.size() + " / " + A.cap;
    $("#sTop").textContent = A.isEmpty() ? "none" : A.peek();
    $("#sState").textContent = A.isEmpty() ? "empty" : A.isFull() ? "full" : "not empty";
    $("#fill").style.width = (A.size() / A.cap * 100) + "%";
    capEl.value = A.cap; pushed = false;
  }
  // op returns [historyLabel, message]; undo snapshot saved only if it succeeds.
  function act(op) {
    const before = snap();
    try { const r = op(); undo.push(before); hist.unshift(r[0]); msg(r[1], false); }
    catch (e) { msg(e.message, true); }
    render();
  }
  function val() {
    const t = valEl.value.trim(), n = Number(t);
    if (t === "" || !Number.isInteger(n)) throw new Error("INVALID INPUT: Enter a whole number");
    return n;
  }
  const rnd = (a, b) => a + Math.floor(Math.random() * (b - a + 1));
  // let the top block lift away first, then change the data
  const lift = (sel, fn) => { const t = $(sel + " .slot.top"); if (t) { t.classList.add("leave"); setTimeout(fn, 230); } else fn(); };
  const acts = {
    push: () => { act(() => { const v = val(); A.push(v); pushed = true; return ["Push " + v, "Pushed " + v + ". TOP is now " + v + "."]; }); valEl.value = ""; valEl.focus(); },
    pop: () => lift("#stackA", () => act(() => { const v = A.pop(); return ["Pop → " + v, "Popped " + v + " from the top."]; })),
    popB: () => lift("#stackB", () => act(() => { const v = B.pop(); return ["Pop B → " + v, "Popped " + v + " from Stack B."]; })),
    clear: () => act(() => { A.clear(); return ["Clear", "Stack A cleared. It is now empty."]; }),
    random: () => act(() => { A.clear(); const n = rnd(1, A.cap); for (let i = 0; i < n; i++) A.push(rnd(1, 100)); return ["Random (" + n + ")", "Random stack of " + n + " element(s) generated."]; }),
    transfer: () => lift("#stackA", () => act(() => { const v = A.pop(); try { B.push(v); } catch (e) { A.push(v); throw e; } return ["Move " + v + " A to B", "Moved " + v + " from Stack A to Stack B."]; })),
    peek: () => { try { const v = A.peek(); flash = true; msg("PEEK: TOP is " + v + " (not removed).", false); render(); setTimeout(() => { flash = false; render(); }, 800); } catch (e) { msg(e.message, true); } },
    check: () => msg(A.isEmpty() ? "CHECK: Stack A is EMPTY." : "CHECK: Stack A is NOT empty (size " + A.size() + ", " + (A.isFull() ? "FULL" : "not full") + ").", false),
    undo: () => { if (!undo.length) return msg("NOTHING TO UNDO: No earlier operation.", true); restore(undo.pop()); hist.unshift("Undo"); msg("Last operation undone.", false); render(); }
  };
  document.querySelectorAll("[data-act]").forEach(b => b.addEventListener("click", () => acts[b.dataset.act]()));
  valEl.addEventListener("keydown", e => { if (e.key === "Enter") acts.push(); });
  capEl.addEventListener("change", () => {
    const c = +capEl.value;
    act(() => { if (c < Math.max(A.size(), B.size())) throw new Error("CAPACITY ERROR: Cannot go below current size"); A.setCapacity(c); B.setCapacity(c); return ["Capacity " + c, "Maximum capacity set to " + c + "."]; });
  });
  render();
})();
