package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Main {

    public static final int XSTART = 8;
    public static final int YSTART = 2;
    public static final int WIDTH = 75;
    public static final int HEIGHT = 25;
    public static int speed = 100;
    static List<Point> cornerPoints = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException {
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF16"));
        terminal.enterPrivateMode();
        MusicPlayer musicPlayer = new MusicPlayer();
        Snake snake = new Snake();
        Pear pear = new Pear(snake);
        SuperPear superPear = new SuperPear(snake, pear);
        Key key;

        backGroundOneColor(terminal);
        startPage(terminal);
        gameArea(terminal);

        do {
            Thread.sleep(1);
            key = terminal.readInput();
        } while (key == null);
        terminal.clearScreen();
        backGroundOneColor(terminal);
        musicPlayer.play("Snake Game (online-audio-converter.com).mp3", true);
        while (snake.isAlive()) {
            key = terminal.readInput();
            snake.move(key, pear, terminal, superPear);
            printScore(terminal, snake.scoreCounter);
            snake.draw(terminal);
            pear.draw(terminal);
            superPear.draw(terminal);
            Thread.sleep(speed);
        }
        gameOver(terminal, snake.scoreCounter);
        musicPlayer.stop("Snake Game (online-audio-converter.com).mp3");
    }

    public static void startPage(Terminal terminal) throws FileNotFoundException {
        String start = "+++++++SNAKE+++++++";
        String topFiveScores = "TOP 5 SCORES:";
        List<String> topFive = fiveHighestScores();
        for (int i = 0; i < topFive.size(); i++) {
            for (int j = 0; j < topFive.get(i).length(); j++) {
                terminal.moveCursor(XSTART+(WIDTH/2)-(topFive.get(i).length()/2)+j, 12+i);
                terminal.putCharacter(topFive.get(i).charAt(j));
                terminal.setCursorVisible(false);
            }
        }
        for (int i = 0; i < start.length(); i++) {
            terminal.moveCursor(XSTART+(WIDTH/2)-(start.length()/2)+i, 8);
            terminal.putCharacter(start.charAt(i));
            terminal.setCursorVisible(false);
        }

        for (int i = 0; i < topFiveScores.length(); i++) {
            terminal.moveCursor(XSTART+(WIDTH/2)-(topFiveScores.length()/2)+i, 11);
            terminal.putCharacter(topFiveScores.charAt(i));
            terminal.setCursorVisible(false);
        }
    }

    public static void printScore(Terminal terminal, int score) {
        String s = "SCORE " + score;
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        for (int i = s.length()-1; i >=0; i--) {
            terminal.moveCursor(XSTART + WIDTH -(s.length()-i), YSTART-1);
            terminal.putCharacter(s.charAt(i));
        }
        terminal.applyBackgroundColor(150, 110, 40);
    }

    public static void speedUp(){
        speed -= 5;
    }

    public static void gameOver(Terminal terminal, int score) throws IOException {
        terminal.clearScreen();
        printGameOverText(terminal, score);
    }

    public static void gameArea(Terminal terminal) {
        for (int i = XSTART; i < XSTART + WIDTH; i++) {
            cornerPoints.add(new Point(i, YSTART-1));
            cornerPoints.add(new Point(i, YSTART + HEIGHT));
        }

        for (int j = YSTART; j < YSTART + HEIGHT; j++) {
            cornerPoints.add(new Point(XSTART-1, j));
            cornerPoints.add(new Point(XSTART + WIDTH, j));
        }
    }

    public static void backGroundOneColor(Terminal terminal) {
        terminal.applyBackgroundColor(150, 110, 40);
        for (int i = XSTART; i < XSTART + WIDTH; i++) {
            for (int j = YSTART; j < YSTART + HEIGHT; j++) {
                terminal.moveCursor(i, j);
                terminal.putCharacter(' ');
            }
        }
        terminal.setCursorVisible(false);
    }

    private static void printGameOverText(Terminal terminal, int score) throws IOException {
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
                "                                                                      ",
                "                              SCORE: " + score
        };

        String [] name2 = {
                "      _____         __  __  ______   ______      ________ _____  _ ",
                "    / ____|   /\\   |  \\/  |  ____|  / __ \\ \\    / /  ____|  __ \\| |",
                "   | |  __   /  \\  | \\  / | |__    | |  | \\ \\  / /| |__  | |__) | |",
                "   | | |_ | / /\\ \\ | |\\/| |  __|   | |  | |\\ \\/ / |  __| |  _  /| |",
                "   | |__| |/ ____ \\| |  | | |____  | |__| | \\  /  | |____| | \\ \\|_|",
                "    \\_____/_/    \\_\\_|  |_|______|  \\____/   \\/   |______|_|  \\_(_)",
                "  __________________________________________________________________",
                "|____________________________________________________________________|",
                "                                                                      ",
                "                              SCORE: " + score                         ,
                "                            NEW HIGH SCORE!"
        };

        String [] chooseName = name;
        if (checkHighScore(score)) {
            chooseName = name2;
        }

        for (int row = 0; row < chooseName.length; row++) {
            for (int col = 0; col < chooseName[row].length(); col++) {
                terminal.moveCursor(col + 10, row + 10);
                terminal.applyBackgroundColor(Terminal.Color.BLACK);
                terminal.applyForegroundColor(Terminal.Color.CYAN);
                terminal.putCharacter(chooseName[row].charAt(col));
                terminal.setCursorVisible(false);
            }
        }


        PrintWriter outStream = new PrintWriter(new BufferedWriter(new FileWriter("highScore.txt", true)));
        outStream.println(score);
        outStream.close();


    }

    private static boolean checkHighScore(int score) throws FileNotFoundException {
        Scanner scan = new Scanner(new File("highScore.txt"));
        int high = 0;
        while (scan.hasNextInt()){
            int nextScore = scan.nextInt();
            if (nextScore > high) {
                high = nextScore;
            }
        }
        return (high < score);
    }

    private static List fiveHighestScores() throws FileNotFoundException {
        List<Integer> arrayList = new ArrayList<>();
        List<String> arrayListTopFive = new ArrayList<>();
        Scanner scan = new Scanner(new File("highScore.txt"));
        while (scan.hasNextInt()){
            arrayList.add(scan.nextInt());
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size()-1; i >arrayList.size()-6; i--) {
            arrayListTopFive.add(arrayList.get(i).toString());
        }
        return arrayListTopFive;
    }
}
