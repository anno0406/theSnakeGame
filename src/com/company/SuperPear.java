package com.company;

import com.googlecode.lanterna.terminal.Terminal;

import java.util.List;
import java.util.Random;

public class SuperPear implements Draw {

    Random rand = new Random();
    Point p;
    Snake snake;
    Pear pear;

    public SuperPear(Snake snake, Pear pear) {
        this.snake = snake;
        this.pear = pear;
        p = new Point(200, 200);
    }

    public void put () {
        List<Point> snakePoints = snake.getPoints();
        boolean pearOnSnake = false;
        do {
            p = new Point(Main.XSTART + rand.nextInt(Main.WIDTH),
                    Main.YSTART + rand.nextInt(Main.HEIGHT));
            for (Point sp : snakePoints) {
                if (sp.x == p.x && sp.y == p.y) {
                    pearOnSnake = true;
                }
            }
            if (pear.p.x == p.x && pear.p.y == p.y) {
                pearOnSnake = true;
            }
        } while (pearOnSnake);
    }

    public void moveOutside() {
        p.x = 200;
        p.y = 200;
    }

    @Override
    public void draw(Terminal terminal) {
        if (!(p.x == 200 && p.y ==200)) {
            char pear = "S".toCharArray()[0];
            terminal.moveCursor(p.x, p.y);
            terminal.putCharacter(pear);
            terminal.setCursorVisible(false);
        }
        else {
            terminal.moveCursor(200,200);
            terminal.applyBackgroundColor(Terminal.Color.BLACK);
            terminal.putCharacter(' ');
            terminal.applyBackgroundColor(150, 110, 40);
        }
    }
}
