package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelector {

    private static int currentPage = 1;
    private static final int amountofpages = 4;
    private static int getUnlockedLevels() {
        return GameSettings.getUnlockedFloors();
    }


    public static void open(JFrame startFrame) {
        startFrame.setVisible(false);
        showPage(1);
    }

    private static void showPage(int page) {
        JFrame frame = new JFrame("Level Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png"))
        );

        JPanel backgroundPanel = new JPanel() {
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
        backgroundPanel.setLayout(new BorderLayout(0, 40));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        // --- Title & unlocked label ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Select a Level", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));

        JLabel unlockedLabel = new JLabel("Unlocked Levels: " + getUnlockedLevels());
        unlockedLabel.setForeground(Color.WHITE);
        unlockedLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        unlockedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        unlockedLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(unlockedLabel, BorderLayout.EAST);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        // --- Center content ---
        if (page < amountofpages) {
            JPanel gridPanel = new JPanel(new GridLayout(2, 5, 20, 20));
            gridPanel.setOpaque(false);

            int startLevel = (page - 1) * 10 + 1;
            int endLevel = startLevel + 9;

            for (int i = startLevel; i <= endLevel; i++) {
                JButton button = createLevelButton("Level " + i);
                int level = i;
                button.addActionListener(e -> {
                            if (level <= getUnlockedLevels()) {
                        frame.dispose();
                        load(level);
                    } else {
                        JOptionPane.showMessageDialog(
                                frame,
                                "You don’t have this level unlocked yet!",
                                "Locked Level",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                gridPanel.add(button);
            }

            backgroundPanel.add(gridPanel, BorderLayout.CENTER);
        } else {
            JLabel wipLabel = new JLabel("WIP", SwingConstants.CENTER);
            wipLabel.setForeground(Color.LIGHT_GRAY);
            wipLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
            backgroundPanel.add(wipLabel, BorderLayout.CENTER);
        }

        // --- Bottom navigation panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            frame.dispose();
            StartScreen.startscreen();
        });
        bottomPanel.add(backButton, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);

        if (page > 1) {
            JButton prevPage = new JButton("<");
            styleSecondaryButton(prevPage);
            prevPage.addActionListener(e -> {
                frame.dispose();
                showPage(page - 1);
            });
            navPanel.add(prevPage);
        }

        if (page < amountofpages) {
            JButton nextPage = new JButton(">");
            styleSecondaryButton(nextPage);
            nextPage.addActionListener(e -> {
                frame.dispose();
                showPage(page + 1);
            });
            navPanel.add(nextPage);
        }

        bottomPanel.add(navPanel, BorderLayout.EAST);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    private static JButton createLevelButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

    private static void styleSecondaryButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
            }
        });
    }

    private static void load(int level) {
        System.out.println("Loading level " + level);
        GamePanel.start(level);
    }
}
