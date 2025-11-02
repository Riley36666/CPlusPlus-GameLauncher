package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Player settings
    private int playerX = 100, playerY = 630;
    private final int playerWidth = 50, playerHeight = 50;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Color playerColor = Color.RED, floorColor = Color.PINK, cactusColor = Color.GREEN;
    private int time = 16;

    // Physics
    private double velocityY = 0;
    private final double gravity = 0.6, jumpStrength = -12;
    private boolean onGround = false;
    private int cameraX = 0;
    private int cameraY = 0;
    // Level data
    private final int[][] platforms, wincon, cactus, trampoline;
    private Timer timer;
    private JFrame frame;
    private boolean gameOver = false, lost = false;
    private int currentlevel, UnlockedLevels;

    // Images
    private Image backgroundImage, floorImage;
    private int bgWidth = 1920, bgHeight = 1080;

    // Constructors
    public GamePanel(int[][] platforms, int[][] wincon, int[][] cactus, int[][] trampoline) {
        this.platforms = platforms;
        this.wincon = wincon;
        this.cactus = cactus;
        this.trampoline = trampoline;
        init();
    }

    public GamePanel(int level) {
        this.platforms = LevelData.getLevel(level);
        this.wincon = LevelData.wincon(level);
        this.cactus = LevelData.cactus(level);
        this.trampoline = LevelData.trampoline(level);
        this.currentlevel = level;
        init();
    }

    private void init() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = Toolkit.getDefaultToolkit().getImage(GamePanel.class.getResource("/background.png"));
        if (backgroundImage == null)
            backgroundImage = Toolkit.getDefaultToolkit().getImage("images/background.png");

        floorImage = Toolkit.getDefaultToolkit().getImage(GamePanel.class.getResource("/floor.png"));
        if (floorImage == null)
            floorImage = Toolkit.getDefaultToolkit().getImage("images/floor.png");

        // Colors
        switch (GameSettings.getPlayerColor().toUpperCase()) {
            case "GREEN": playerColor = Color.GREEN; break;
            case "YELLOW": playerColor = Color.YELLOW; break;
            case "BLUE": playerColor = Color.BLUE; break;
            case "PINK": playerColor = Color.PINK; break;
            default: playerColor = Color.RED; break;
        }

        switch (GameSettings.getFloorColor().toUpperCase()) {
            case "RED": floorColor = Color.RED; break;
            case "YELLOW": floorColor = Color.YELLOW; break;
            case "BLUE": floorColor = Color.BLUE; break;
            case "PINK": floorColor = Color.PINK; break;
            default: floorColor = Color.PINK; break;
        }

        switch (GameSettings.getCactusColor().toUpperCase()) {
            case "RED": cactusColor = Color.RED; break;
            case "YELLOW": cactusColor = Color.YELLOW; break;
            case "BLUE": cactusColor = Color.BLUE; break;
            case "PINK": cactusColor = Color.PINK; break;
            default: cactusColor = Color.GREEN; break;
        }

        UnlockedLevels = GameSettings.getUnlockedFloors();

        // FPS timing
        switch (GameSettings.getFPSValue()) {
            case 30: time = 33; break;
            case 60: time = 16; break;
            case 120: time = 8; break;
            case 240: time = 4; break;
        }

        timer = new Timer(time, this);
        timer.start();
    }

    public static void start(int level) {
        GamePanel panel = new GamePanel(level);
        JFrame frame = new JFrame("Level " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setResizable(false);
        frame.add(panel);
        panel.setFrame(frame);
        frame.setVisible(true);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png")));
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        if (backgroundImage != null) {
            int bgX = (int) (-cameraX * 0.5) % bgWidth;
            if (bgX > 0) bgX -= bgWidth;
            for (int x = bgX; x < getWidth(); x += bgWidth)
                g.drawImage(backgroundImage, x, 680 - (bgHeight - 400), bgWidth, bgHeight, this);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

// --- FIXED FLOOR DRAWING: Texture exactly matches platform bounds ---
        for (int[] p : platforms) {
            if (floorImage != null && floorImage.getWidth(this) > 0 && floorImage.getHeight(this) > 0) {
                int imgW = floorImage.getWidth(this);
                int imgH = floorImage.getHeight(this);
                int startX = p[0] - cameraX;
                int endX = startX + p[2];
                int startY = p[1];
                int endY = startY + p[3];

                // Fill only the platform area
                for (int y = startY; y < endY; y += imgH) {
                    for (int x = startX; x < endX; x += imgW) {
                        int drawW = Math.min(imgW, endX - x);
                        int drawH = Math.min(imgH, endY - y);
                        g.drawImage(floorImage, x, y, x + drawW, y + drawH,
                                0, 0, drawW, drawH, this);
                    }
                }
            } else {
                g.setColor(floorColor);
                g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);
            }
        }




        // Cactus
        g.setColor(cactusColor);
        for (int[] p : cactus)
            g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);

        // Trampolines
        g.setColor(Color.BLACK);
        for (int[] p : trampoline)
            g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);

        // Win zones
        g.setColor(Color.YELLOW);
        for (int[] p : wincon)
            g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);

        // Player
        g.setColor(playerColor);
        g.fillRect(playerX - cameraX, playerY, playerWidth, playerHeight);

        // Game over / lost messages
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("YOU WIN!", getWidth() / 2 - 150, getHeight() / 2);
            timer.stop();
            gameOver = false;
            if (currentlevel == UnlockedLevels) {
                GameSettings.setUnlockedFloors(UnlockedLevels + 1);
                GameSettings.save();
            }
            new javax.swing.Timer(1000, e2 -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                LevelSelector.open(new JFrame());
            }) {{
                setRepeats(false);
                start();
            }};
            return;
        }

        if (lost) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("LOST!", getWidth() / 2 - 150, getHeight() / 2);
            timer.stop();
            lost = false;
            new javax.swing.Timer(1000, e2 -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                GamePanel.start(currentlevel);
            }) {{
                setRepeats(false);
                start();
            }};
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) playerX -= 8;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) playerX += 8;
        if ((pressedKeys.contains(KeyEvent.VK_SPACE) || pressedKeys.contains(KeyEvent.VK_W) ||
                pressedKeys.contains(KeyEvent.VK_UP)) && onGround) {
            velocityY = jumpStrength;
            onGround = false;
        }

        velocityY += gravity;
        playerY += velocityY;

        // --- Collision detection ---
        onGround = false;
        Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);

        // Trampoline first
        for (int[] p : trampoline) {
            Rectangle trampRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(trampRect)) {
                int playerBottom = playerRect.y + playerRect.height;
                int trampTop = trampRect.y;
                boolean fromAbove = (playerBottom - velocityY) <= trampTop;
                if (fromAbove && velocityY > 0) {
                    playerY = trampTop - playerHeight;
                    velocityY = -18;
                    onGround = false;
                }
            }
        }

        // Platforms
        playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
        for (int[] p : platforms) {
            Rectangle platRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(platRect)) {
                if (playerY + playerHeight - velocityY <= p[1]) {
                    playerY = p[1] - playerHeight;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }

        // Cactus
        for (int[] p : cactus) {
            Rectangle cRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (cRect.intersects(playerRect)) lost = true;
        }

        // Win
        for (int[] p : wincon) {
            Rectangle wRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(wRect)) gameOver = true;
        }

        if (playerY > 680) lost = true;
        if (playerX > 1080) cameraX = playerX - 1080;
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            if (frame != null) {
                if (!gameOver) {
                    if (!lost){
                    frame.dispose();
                    LevelSelector.open(frame);
        }}}}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
