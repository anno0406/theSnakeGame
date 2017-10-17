package com.company;

import com.googlecode.lanterna.terminal.Terminal;

import java.util.List;
import java.util.Random;

public class Pear implements Draw {

    Random rand = new Random();
    Point p;
    Snake snake;

    public Pear(Snake snake) {
        p = new Point(5,5);
        this.snake = snake;
    }

    public void put () {
        List<Point> snakePoints = snake.getPoints();
        boolean pearOnSnake = false;
        do {
            p = new Point(rand.nextInt(Main.WIDTH), rand.nextInt(Main.HEIGHT));
            for (Point sp : snakePoints) {
                if (sp.x == p.x && sp.y == p.y)
                    pearOnSnake = true;
            }
        } while (pearOnSnake);
    }

    @Override
    public void draw(Terminal terminal) {
        char pear = "\u1F40D".toCharArray()[0];
        terminal.moveCursor(p.x, p.y);
        terminal.putCharacter(pear);
        terminal.setCursorVisible(false);
    }
}