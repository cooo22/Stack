/* Description: Entry point. Opens the Swing stack visualizer.
Programmed by: Terence john Martin BSIT CC104 DATA STRUCTURES AND ALGORITHMS
Last Modified: 2026-10-17
Version: 1.0
*/
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StackGUI().setVisible(true));
    }
}
