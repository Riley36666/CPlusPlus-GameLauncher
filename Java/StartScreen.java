package Java;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StartScreen {
    public static void startscreen() {
        // --- Frame setup ---
        JFrame frame = new JFrame("My Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png"))
        );
        // --- Background panel with gradient ---
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(10, 10, 30),
                        0, getHeight(), new Color(30, 30, 80));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(150, 200, 150, 200));

        // --- Title label ---
        JLabel title = new JLabel("My Game");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Button styling helper ---
        JButton startButton = createButton("Start Game");
        JButton optionsButton = createButton("Options");
        JButton exitButton = createButton("Exit Game");

        // --- Button actions ---
        startButton.addActionListener(e -> {
            System.out.println("Start button pressed!");
            LevelSelector.open(frame);
            frame.dispose();
        });

        optionsButton.addActionListener(e -> {
            System.out.println("Options button pressed!");
            OptionMenu.open(frame);
        });

        exitButton.addActionListener(e -> {
            System.out.println("Exit button pressed!");
            frame.dispose();
        });

        // --- Add components ---
        panel.add(title);
        panel.add(Box.createVerticalStrut(60));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(optionsButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(exitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBackground(new Color(60, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 160, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 120, 220));
            }
        });

        return button;
    }
}
