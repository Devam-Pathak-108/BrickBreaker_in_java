package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    private Timer time;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 500;
    private int ballposY = 500;
    private int ballDirX = 0;
    private int ballDirY = -2;
    private int Level = 1;

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 5);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();
        // time = new Timer(delay, this);
        // time.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        // drawing map
        map.draw((Graphics2D) g);

        // drawing map
        map.draw((Graphics2D) g);

        // border
        g.setColor(Color.blue);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(680, 0, 3, 592);

        // Escape
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 15));
        g.drawString("Press ESC to Restart form lv 1", 10, 30);
        // Level
        g.setColor(Color.yellow);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("lv : " + Level, 300, 30);
        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score : " + score, 550, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 110, 8);

        // the Ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if(totalBricks > 0 && !play && ballposY < 570){
            
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter", 190, 300);
        }

        if (totalBricks <= 0) {
            if (play) {
                Level++;
            }

            play = false;
            ballDirX = 0;
            ballDirY = 0;
            
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 190, 300);
            
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("PRESS ENTER FOR LEVEL" + Level, 230, 350);
        }

        if (ballposY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART", 230, 350);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 40, 8))) {
                ballDirY = -(1 + Level);
                ballDirX = -(1 + Level);
            }
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 51, 550, 30, 8))) {
                ballDirY = -(2 + Level);
                ballDirX = 0;
            }
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 51 + 31, 550, 40, 8))) {
                ballDirY = -(1 + Level);
                ballDirX = +(1 + Level);
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
                map = new MapGenerator(3 + (Level - 1), 5 + (Level - 1));

                totalBricks = (3 + (Level - 1)) * (5 + (Level - 1));
                play = true;
                playerX = 310;
                ballposX = 500;
                ballposY = 500;

                ballDirY = -(1 + Level);
                ballDirX = -(1 - Level);

                score = 0;
                repaint();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Level = 1;
            map = new MapGenerator(3 + (Level - 1), 5 + (Level - 1));

            totalBricks = (3 + (Level - 1)) * (5 + (Level - 1));
            playerX = 310;
            ballposX = 500;
            ballposY = 500;

            ballDirY = -(1 + Level);
            ballDirX = -(1 - Level);

            score = 0;
            play = false;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void moveRight() {
        // play = true;
        playerX += 15;
    }

    public void moveLeft() {
        // play = true;
        playerX -= 15;
    }

}
