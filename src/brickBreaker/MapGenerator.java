package brickBreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public Color colors[][];

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        colors = new Color[row][col];
        Random rand = new Random();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
                colors[i][j] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                
            }
        }
        brickWidth = 540 / col;
        brickHeight = 100 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Use the color from the colors array
                    g.setColor(colors[i][j]);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setColor(Color.black);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}