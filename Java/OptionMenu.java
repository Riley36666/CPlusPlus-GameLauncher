package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OptionMenu {

    public static void open(JFrame previousFrame) {
        // Hide the previous screen
        previousFrame.setVisible(false);

        // Create frame
        JFrame frame = new JFrame("Options");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        // Layout setup
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(25, 25, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // --- Title ---
        JLabel title = new JLabel("Game Options", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        // --- Player color option ---
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel playerLabel = new JLabel("Player Color:");
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(playerLabel, gbc);

        gbc.gridx = 1;
        String[] colors = {"RED", "GREEN", "YELLOW", "BLUE", "PINK"};
        JComboBox<String> playerColorBox = new JComboBox<>(colors);
        playerColorBox.setSelectedItem(GameSettings.getPlayerColor());
        panel.add(playerColorBox, gbc);

        // --- Floor color option ---
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel floorLabel = new JLabel("Floor Color:");
        floorLabel.setForeground(Color.WHITE);
        floorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(floorLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> floorColorBox = new JComboBox<>(colors);
        floorColorBox.setSelectedItem(GameSettings.getFloorColor());
        panel.add(floorColorBox, gbc);

        // --- Cactus color option ---
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel cactusLabel = new JLabel("Cactus Color:");
        cactusLabel.setForeground(Color.WHITE);
        cactusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(cactusLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> cactusColorBox = new JComboBox<>(colors);
        cactusColorBox.setSelectedItem(GameSettings.getCactusColor());
        panel.add(cactusColorBox, gbc);



        // --- FPS Limit ---
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel FPS = new JLabel("FPS:");
        FPS.setForeground(Color.WHITE);
        FPS.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(FPS, gbc);

        gbc.gridx = 1;
        String[] limits = {"30", "60", "120", "240"};
        JComboBox<String> FPSlists = new JComboBox<>(limits);
        FPSlists.setSelectedItem(GameSettings.getFPS());
        panel.add(FPSlists, gbc);

        // --- Fullscreen checkbox ---
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JCheckBox fullscreenBox = new JCheckBox("Fullscreen Mode");
        fullscreenBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        fullscreenBox.setForeground(Color.WHITE);
        fullscreenBox.setOpaque(false);
        fullscreenBox.setSelected(GameSettings.isFullscreen());
        panel.add(fullscreenBox, gbc);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png"))
        );

        // --- Buttons ---
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JButton saveButton = new JButton("Save");
        styleButton(saveButton, new Color(60, 120, 220));
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(80, 80, 80));
        panel.add(backButton, gbc);

        // --- Save Button Action ---
        saveButton.addActionListener((ActionEvent e) -> {
            GameSettings.setPlayerColor((String) playerColorBox.getSelectedItem());
            GameSettings.setFloorColor((String) floorColorBox.getSelectedItem());
            GameSettings.setCactusColor((String) cactusColorBox.getSelectedItem());
            GameSettings.setFullscreen(fullscreenBox.isSelected());
            GameSettings.setFPS(FPSlists.getSelectedItem().toString());
            GameSettings.save();

            JOptionPane.showMessageDialog(frame, "Settings saved successfully!");
        });

        // --- Back Button Action ---
        backButton.addActionListener((ActionEvent e) -> {
            frame.dispose();
            StartScreen.startscreen();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
    }
}
