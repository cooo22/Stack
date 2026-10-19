/* Description: Entry point. Opens the Swing stack visualizer.
Programmed by: Terence john Martin BSIT CC104 DATA STRUCTURES AND ALGORITHMS
Last Modified: 2026-10-17
Version: 1.0
*/
import java.awt.*;
import javax.swing.*;

public class StackPanel extends JPanel {
    private final String title;
    private int[] values = new int[0];
    private int cap = ArrayStack.MAX_CAPACITY;
    private boolean flash;

    public StackPanel(String title) {
        this.title = title;
        setPreferredSize(new Dimension(280, 560));
        setBackground(Color.BLACK);
    }

    public void update(int[] v, int capacity) { values = v; cap = capacity; repaint(); }

    /** Highlights the top element for a moment (used by PEEK). */
    public void flashTop() {
        flash = true; repaint();
        Timer t = new Timer(800, e -> { flash = false; repaint(); });
        t.setRepeats(false); t.start();
    }

    private void centered(Graphics2D g, String s, int cx, int baseY) {
        g.drawString(s, cx - g.getFontMetrics().stringWidth(s) / 2, baseY);
    }

    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cellW = 150, cellH = 38, gap = 6, x = (getWidth() - cellW) / 2, bottom = getHeight() - 30;
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        centered(g, title + "  (" + values.length + "/" + cap + ")", getWidth() / 2, 24);
        for (int i = 0; i < ArrayStack.MAX_CAPACITY; i++) {
            int y = bottom - (i + 1) * (cellH + gap);
            boolean used = i < values.length, isTop = i == values.length - 1;
            if (used) {
                g.setPaint(isTop && flash ? new Color(186, 208, 255)
                        : new GradientPaint(0, y, Color.WHITE, 0, y + cellH, new Color(200, 200, 200)));
                g.fillRoundRect(x, y, cellW, cellH, 10, 10);
                g.setColor(new Color(17, 17, 17));
                g.setFont(new Font("SansSerif", Font.BOLD, 18));
                centered(g, String.valueOf(values[i]), getWidth() / 2, y + 26);
            } else {
                g.setColor(i < cap ? new Color(120, 120, 120) : new Color(50, 50, 50));
                g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{4f, 4f}, 0f));
                g.drawRoundRect(x, y, cellW, cellH, 10, 10);
            }
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g.setColor(new Color(150, 150, 150));
            g.drawString("[" + i + "]", x + cellW + 8, y + 24);
            if (isTop) {
                g.setColor(new Color(186, 208, 255));
                g.setFont(new Font("SansSerif", Font.BOLD, 13));
                g.drawString("TOP ->", x - 56, y + 24);
            }
        }
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        centered(g, "BOTTOM", getWidth() / 2, bottom + 16);
        g.dispose();
    }
}
