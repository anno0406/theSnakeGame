package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final int XSTART = 5;
    public static final int YSTART = 2;
    public static final int WIDTH = 75;
    public static final int HEIGHT = 25;
    static List<Point> cornerPoints = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF16"));
        terminal.enterPrivateMode();

        Snake snake = new Snake();
        Pear pear = new Pear(snake);
        Key key;

        backGroundOneColor(terminal);
        snake.draw(terminal);
        backGround(terminal);

        do {
            Thread.sleep(1);
            key = terminal.readInput();
        } while (key == null);
        while (snake.isAlive()) {
            key = terminal.readInput();
//            terminal.clearScreen();
            snake.move(key, pear, terminal);
            snake.draw(terminal);
            pear.draw(terminal);
            Thread.sleep(100);
        }
        gameOver(terminal);
    }

    public static void gameOver(Terminal terminal) {
        terminal.clearScreen();
        printGameOverText(terminal);
    }

    public static void backGround(Terminal terminal){
        for (int i = XSTART; i < XSTART + WIDTH; i++) {
            cornerPoints.add(new Point(i, YSTART));
            cornerPoints.add(new Point(i, YSTART + HEIGHT));
        }

        for (int j = YSTART; j < YSTART + HEIGHT; j++) {
            cornerPoints.add(new Point(XSTART, j));
            cornerPoints.add(new Point(XSTART + WIDTH, j));
        }
    }

    public static void backGroundOneColor (Terminal terminal) {
        for (int i = XSTART; i < XSTART + WIDTH; i++) {
            for (int j = YSTART; j < YSTART + HEIGHT; j++) {
                terminal.moveCursor(i, j);
            terminal.putCharacter(' ');
            terminal.applyBackgroundColor(150, 110, 40);
            terminal.setCursorVisible(false);
            }
        }
    }

    private static void printGameOverText(Terminal terminal) {
        /*String path = "/Users/aramiB/Desktop/Academy/The Snake/gameover.txt";
        String string = "";
        Scanner sc = new Scanner(new File(path));
        while (sc.hasNextLine()){
            string += sc.nextLine();
            rowCounter++;
        }*/
        String[] name = {
        "      _____         __  __  ______   ______      ________ _____  _ ",
        "    / ____|   /\\   |  \\/  |  ____|  / __ \\ \\    / /  ____|  __ \\| |",
        "   | |  __   /  \\  | \\  / | |__    | |  | \\ \\  / /| |__  | |__) | |",
        "   | | |_ | / /\\ \\ | |\\/| |  __|   | |  | |\\ \\/ / |  __| |  _  /| |",
        "   | |__| |/ ____ \\| |  | | |____  | |__| | \\  /  | |____| | \\ \\|_|",
        "    \\_____/_/    \\_\\_|  |_|______|  \\____/   \\/   |______|_|  \\_(_)",
        "  __________________________________________________________________",
        "|____________________________________________________________________|",
        };

        for (int row=0; row<name.length; row++) {
            for (int col = 0; col < name[row].length(); col++) {
                terminal.moveCursor(col+10, row+10);
                terminal.applyBackgroundColor(Terminal.Color.BLACK);
                terminal.applyForegroundColor(Terminal.Color.CYAN);
                terminal.putCharacter(name[row].charAt(col));
                terminal.setCursorVisible(false);
            }
        }
        
    }
}
