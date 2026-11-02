package atm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main - Application Entry Point for the Classroom ATM Simulation.
 *
 * This class performs initial setup:
 * 1. Sets the native operating system Look and Feel for an authentic UI experience.
 * 2. Launches the Swing GUI safely on the Event Dispatch Thread (EDT) using SwingUtilities.invokeLater.
 */
public class Main {

    public static void main(String[] args) {
        // Step 1: Set System Look and Feel to match the host OS (Windows/macOS/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[INFO] Could not apply system look and feel. Falling back to default Swing look.");
        }

        // Step 2: Initialize and display the GUI on the Event Dispatch Thread (EDT)
        // In Swing, all UI component creation and mutation MUST happen on the EDT
        // to prevent multithreading race conditions and graphical artifacts.
        SwingUtilities.invokeLater(() -> {
            try {
                ATMGUI app = new ATMGUI();
                app.setVisible(true);
                System.out.println("=================================================");
                System.out.println(" Classroom ATM Simulation Launched Successfully! ");
                System.out.println(" Default Demo Account: 1001001 | Demo PIN: 1234   ");
                System.out.println("=================================================");
            } catch (Exception e) {
                System.err.println("[FATAL] Failed to initialize ATM GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
