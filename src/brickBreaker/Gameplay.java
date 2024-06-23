package brickBreaker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 15;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 500;
    private int ballposY = 500;
    private int ballDirX = 0;
    private int ballDirY = -2;
    private int level = 1;

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 5);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background with gradient
        GradientPaint bg = new GradientPaint(0, 0, Color.BLACK, 0, 600, Color.DARK_GRAY);
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, 692, 592);

        // Drawing the map
        map.draw(g2d);

        // Borders
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 3, 592);
        g2d.fillRect(0, 0, 692, 3);
        g2d.fillRect(680, 0, 3, 592);

        // Instructions
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Verdana", Font.PLAIN, 15));
        g2d.drawString("Press ESC to Restart from Level 1", 10, 30);

        // Level display
        g2d.setColor(Color.ORANGE);
        g2d.setFont(new Font("Verdana", Font.BOLD, 25));
        g2d.drawString("Level: " + level, 300, 30);

        // Score display
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Verdana", Font.BOLD, 25));
        g2d.drawString("Score: " + score, 550, 30);

        // Paddle with gradient
        GradientPaint paddleGradient = new GradientPaint(playerX, 550, Color.GREEN, playerX + 100, 550, Color.BLUE);
        g2d.setPaint(paddleGradient);
        g2d.fillRoundRect(playerX, 550, 110, 8, 10, 10);

        // Ball with gradient
        GradientPaint ballGradient = new GradientPaint(ballposX, ballposY, Color.YELLOW, ballposX + 20, ballposY + 20, Color.RED);
        g2d.setPaint(ballGradient);
        g2d.fillOval(ballposX, ballposY, 20, 20);

        if (totalBricks > 0 && !play && ballposY < 570) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Press ENTER to Start", 200, 300);
        }

        if (totalBricks <= 0) {
            if (play) {
                level++;
            }

            play = false;
            ballDirX = 0;
            ballDirY = 0;

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("You Won!", 260, 300);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Press ENTER to Continue to Level " + level, 180, 350);
        }

        if (ballposY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            score = 0;
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Game Over", 240, 300);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Press ENTER to Restart", 220, 350);
        }

        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 40, 8))) {
                ballDirY = -(1 + level);
                ballDirX = -(1 + level);
            }
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 51, 550, 30, 8))) {
                ballDirY = -(2 + level);
                ballDirX = 0;
            }
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 51 + 31, 550, 40, 8))) {
                ballDirY = -(1 + level);
                ballDirX = +(1 + level);
            }
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                            break A;
                        }
                    }
                }
            }
            ballposX += ballDirX;
            ballposY += ballDirY;
            if (ballposX < 0) {
                ballDirX = -ballDirX;
            }
            if (ballposY < 0) {
                ballDirY = -ballDirY;
            }
            if (ballposX > 660) {
                ballDirX = -ballDirX;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX <= 560) {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX > 10) {
                moveLeft();
            } 
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                map = new MapGenerator(3 + (level - 1), 5 + (level - 1));
                totalBricks = (3 + (level - 1)) * (5 + (level - 1));
                play = true;
                playerX = 310;
                ballposX = 500;
                ballposY = 500;
                ballDirY = -(1 + level);
                ballDirX = -(1 - level);
                repaint();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            level = 1;
            map = new MapGenerator(3 + (level - 1), 5 + (level - 1));
            totalBricks = (3 + (level - 1)) * (5 + (level - 1));
            playerX = 310;
            ballposX = 500;
            ballposY = 500;
            ballDirY = -(1 + level);
            ballDirX = -(1 - level);
            score = 0;
            play = false;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void moveRight() {
        playerX += 13+(level * 2);
    }

    public void moveLeft() {
        playerX -= 13 +(level *2);
    }
}