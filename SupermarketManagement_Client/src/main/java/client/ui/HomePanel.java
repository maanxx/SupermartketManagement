package client.ui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalGlue());

        try {
            ImageIcon homeIcon = new ImageIcon(("img/ff.jpg"));
            // Get the original image dimensions
            int originalWidth = homeIcon.getIconWidth();
            int originalHeight = homeIcon.getIconHeight();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int sidebarWidth = (int) (screenSize.width * 0.22); // From AdminDashboard
            int maxWidth = screenSize.width - sidebarWidth - 20; // viewControlPanel width
            int maxHeight = screenSize.height - 20; // viewControlPanel height

            double aspectRatio = (double) originalWidth / originalHeight;
            int scaledWidth, scaledHeight;

            // Scale to fit within maxWidth and maxHeight
            if (aspectRatio > (double) maxWidth / maxHeight) {
                // Image is wider relative to panel; scale by width
                scaledWidth = maxWidth;
                scaledHeight = (int) (maxWidth / aspectRatio);
            } else {
                // Image is taller relative to panel; scale by height
                scaledHeight = maxHeight;
                scaledWidth = (int) (maxHeight * aspectRatio);
            }

            // Scale the image
            Image scaledImage = homeIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            contentPanel.add(imageLabel);
        } catch (Exception e) {
            System.err.println("Error loading image /img/ff.jpg: " + e.getMessage());
            JLabel errorLabel = new JLabel("Image not found");
            errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
            errorLabel.setForeground(AdminDashboard.BUTTON_BG_COLOR);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(errorLabel);
        }

        // Add vertical glue to center content vertically
        contentPanel.add(Box.createVerticalGlue());

        // Add the content panel to the center of the HomePanel
        add(contentPanel, BorderLayout.CENTER);
    }
}