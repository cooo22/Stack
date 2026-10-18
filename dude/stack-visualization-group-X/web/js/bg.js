/* Description: Animated canvas background (drifting glow + rising particles). Shows when the hero video is not playing.
   Programmed by: <Your Name> <Course> <cn> <Subject>  Last Modified: 2026-10-18  Version: 1.1 */
(() => {
  const c = document.getElementById("bg"), x = c.getContext("2d");
  const reduce = matchMedia("(prefers-reduced-motion: reduce)").matches;
  let w, h, dpr;
  const orbs = [[.2, .3, 420, "120,150,255"], [.8, .25, 360, "190,210,255"], [.5, .85, 480, "90,110,200"], [.9, .8, 320, "150,170,220"]]
    .map((o, i) => ({ x: o[0], y: o[1], r: o[2], c: o[3], p: i * 1.7, s: .00018 + i * .00004 }));
  const dots = Array.from({ length: 80 }, () => ({ x: Math.random(), y: Math.random(), z: Math.random(), t: Math.random() * 6.28 }));
  function size() { dpr = Math.min(devicePixelRatio || 1, 2); w = c.width = innerWidth * dpr; h = c.height = innerHeight * dpr; }
  function frame(t) {
    x.clearRect(0, 0, w, h);
    for (const o of orbs) {
      const px = (o.x + Math.sin(t * o.s + o.p) * .08) * w, py = (o.y + Math.cos(t * o.s * 1.3 + o.p) * .08) * h, r = o.r * dpr;
      const g = x.createRadialGradient(px, py, 0, px, py, r);
      g.addColorStop(0, "rgba(" + o.c + ",.20)"); g.addColorStop(1, "rgba(" + o.c + ",0)");
      x.fillStyle = g; x.fillRect(px - r, py - r, r * 2, r * 2);
    }
    for (const d of dots) {
      d.y -= .00006 + d.z * .00018;
      if (d.y < -.02) { d.y = 1.02; d.x = Math.random(); }
      const a = (.25 + .55 * Math.abs(Math.sin(t * .0012 + d.t))) * d.z;
      x.fillStyle = "rgba(220,230,255," + a + ")";
      x.beginPath(); x.arc(d.x * w + Math.sin(t * .0003 + d.t) * 14 * dpr, d.y * h, (.6 + d.z * 1.6) * dpr, 0, 6.283); x.fill();
    }
    if (!reduce) requestAnimationFrame(frame);
  }
  size(); addEventListener("resize", size); requestAnimationFrame(frame);
})();
