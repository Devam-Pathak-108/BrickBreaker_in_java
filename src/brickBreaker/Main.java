package brickBreaker;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay();
        obj.setBounds(0, 0, 700, 600);
        obj.setTitle("Brick Breaks!!");
        obj.setResizable(false);

        obj.setLocationRelativeTo(null);

        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
}