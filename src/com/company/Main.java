package com.company;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static com.googlecode.lanterna.input.Key.Kind.Enter;
import static com.googlecode.lanterna.input.Key.Kind.Escape;

public class Main {

    public static final int XSTART = 12;
    public static final int YSTART = 2;
    public static final int WIDTH = 75;
    public static final int HEIGHT = 25;
    public static int speed = 100;
    static List<Point> cornerPoints = new ArrayList<>();
    static String userName = "";

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
        String[] startName = {
                "███████╗███╗   ██╗███████╗██╗██╗  ██╗",
                "██╔════╝████╗  ██║██╔════╝██║██║ ██╔╝",
                "███████╗██╔██╗ ██║█████╗  ██║█████╔╝",
                "╚════██║██║╚██╗██║██╔══╝  ██║██╔═██╗",
                "███████║██║ ╚████║███████╗██║██║  ██╗",
                "╚══════╝╚═╝  ╚═══╝╚══════╝╚═╝╚═╝  ╚═╝"};

        for (int row = 0; row < startName.length; row++) {
            for (int col = 0; col < startName[row].length(); col++) {
                terminal.moveCursor(XSTART+(WIDTH/2)-(startName[row].length()/2)+col, row+4);
                terminal.applyForegroundColor(Terminal.Color.WHITE);
                terminal.putCharacter(startName[row].charAt(col));
                terminal.setCursorVisible(false);
            }
        }

        String topFiveScores = "TOP 5 SCORES:";
        List<String> topFive = fiveHighestScores();
        for (int i = 0; i < topFive.size(); i++) {
            for (int j = 0; j < topFive.get(i).length(); j++) {
                terminal.moveCursor(XSTART+(WIDTH/2)-(topFive.get(i).length()/2)+j, 12+i);
                terminal.putCharacter(topFive.get(i).charAt(j));
                terminal.setCursorVisible(false);
            }
        }
//        for (int i = 0; i < start.length(); i++) {
//            terminal.moveCursor(XSTART+(WIDTH/2)-(start.length()/2)+i, 8);
//            terminal.putCharacter(start.charAt(i));
//            terminal.setCursorVisible(false);
//        }

        for (int i = 0; i < topFiveScores.length(); i++) {
            terminal.moveCursor(XSTART+(WIDTH/2)-(topFiveScores.length()/2)+i, 11);
            terminal.putCharacter(topFiveScores.charAt(i));
            terminal.setCursorVisible(false);
        }

        String enterName = "ENTER YOUR USERNAME:";
        for (int i = 0; i < enterName.length(); i++) {
            terminal.moveCursor(XSTART+(WIDTH/2)-(enterName.length()/2)+i, 19);
            terminal.putCharacter(enterName.charAt(i));
            terminal.setCursorVisible(false);
        }

        Key key;
        int col = XSTART+(WIDTH/2)-(enterName.length()/2);
        while (true) {
            do {
                key = terminal.readInput();
            } while (key == null);

            if (key.getKind() == Enter){
                break;
            }
            else {
                char c = Character.toUpperCase(key.getCharacter());
                userName += c;
                terminal.moveCursor(col++, 20);
                terminal.putCharacter(c);
            }
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
        Key key;
        while (true){
            do {
                key = terminal.readInput();
            }while(key == null);
            if(key.getKind() == Escape){
                System.exit(0);
            }
        }
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
                terminal.moveCursor(col + 15, row + 10);
                terminal.applyBackgroundColor(Terminal.Color.BLACK);
                terminal.applyForegroundColor(150, 110, 40);
                terminal.putCharacter(chooseName[row].charAt(col));
                terminal.setCursorVisible(false);
            }
        }


        PrintWriter outStream = new PrintWriter(new BufferedWriter(new FileWriter("highScore.txt", true)));
        outStream.println(userName + " " + score);
        outStream.close();


    }

    private static boolean checkHighScore(int score) throws FileNotFoundException {
        Scanner scan = new Scanner(new File("highScore.txt"));
        int high = 0;
        while (scan.hasNext()){
            String s = scan.next();
            int nextScore = scan.nextInt();
            if (nextScore > high) {
                high = nextScore;
            }
        }
        return (score > high);
    }

    private static List fiveHighestScores() throws FileNotFoundException {
        List<Score> arrayList = new ArrayList<>();
        List<String> arrayListTopFive = new ArrayList<>();
        Scanner scan = new Scanner(new File("highScore.txt"));
        while (scan.hasNext()){
            String name = scan.next();
            int score = scan.nextInt();
            arrayList.add(new Score(name, score));
        }
        Collections.sort(arrayList, new ScoreComparator());
        for (int i = arrayList.size()-1; i >arrayList.size()-6; i--) {
            arrayListTopFive.add(arrayList.get(i).userName + " " + arrayList.get(i).score);
        }
        return arrayListTopFive;
    }
}
