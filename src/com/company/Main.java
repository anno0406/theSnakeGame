package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int WIDTH = 75;
    public static final int HEIGHT = 25;
    static List<Point> cornerPoints = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF16"));
        terminal.enterPrivateMode();

        Snake snake = new Snake();
        Pear pear = new Pear(snake);
        Key key;


        do {
            Thread.sleep(1);
            key = terminal.readInput();
        } while (key == null);
        while (snake.isAlive()) {
            key = terminal.readInput();
            terminal.clearScreen();
            //backGround(terminal);
            snake.draw(terminal);
            pear.draw(terminal);
            snake.move(key, pear);
            Thread.sleep(100);
        }
        gameOver(terminal);
    }

    public static void gameOver(Terminal terminal){
        System.out.println("God jul!");
        //System.exit(0);
    }

    public static void backGround(Terminal terminal){
        for (int i = 0; i < WIDTH; i++) {
            cornerPoints.add(new Point(i, 0));
            cornerPoints.add(new Point(i, HEIGHT));
        }

        for (int j = 0; j < HEIGHT; j++) {
            cornerPoints.add(new Point(0, j));
            cornerPoints.add(new Point(WIDTH, j));
        }

        for (Point elem :cornerPoints ) {
            terminal.moveCursor(elem.x, elem.y);
            terminal.putCharacter('*');
            terminal.setCursorVisible(false);
        }
    }
}
