package com.company;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;

public class Snake implements Draw {

    List<Point> points = new ArrayList<>();
    Direction direction;
    int scoreCounter = 0;

    public Snake() {
        this.direction = Direction.RIGHT;
        for (int i = 0; i < 5; i++) {
            points.add(new Point(Main.XSTART + Main.WIDTH / 2 + i, Main.YSTART + Main.HEIGHT / 2));
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void draw(Terminal terminal) {
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        for (Point point : points) {
            terminal.moveCursor(point.x, point.y);
            terminal.putCharacter('\u2b24');
            terminal.setCursorVisible(false);
        }
    }

    public boolean pearSpot(Pear pear) {
        int xPos = points.get(points.size() - 1).x;
        int yPos = points.get(points.size() - 1).y;
        if (pear.p.x == xPos && pear.p.y == yPos) {
            return true;
        }
        return false;
    }

    public boolean superPearSpot(SuperPear pear) {
        int xPos = points.get(points.size() - 1).x;
        int yPos = points.get(points.size() - 1).y;
        if (pear.p.x == xPos && pear.p.y == yPos) {
            return true;
        }
        return false;
    }

    public void move(Key key, Pear pear, Terminal terminal, SuperPear superPear) throws InterruptedException {
        int posXForLastElem = points.get(points.size() - 1).x;
        int posYForLastElem = points.get(points.size() - 1).y;
        if (!pearSpot(pear) && !superPearSpot(superPear)) {
            terminal.moveCursor(points.get(0).x, points.get(0).y);
            terminal.putCharacter(' ');
            points.remove(0);
        }
        else if (pearSpot(pear)) {
            pear.put();
            scoreCounter += 1000;
            if (scoreCounter % 5000 == 0) {
                superPear.put();
            }
            Main.speedUp();
        }
        else {
            scoreCounter += 5000;
            superPear.moveOutside();
            Main.speedUp();
        }

        if (key == null) {
            moveNoKeyPressed(posXForLastElem, posYForLastElem);
        } else {
            Key.Kind kbd = key.getKind();
            if (direction == Direction.UP || direction == Direction.DOWN) {
                switch (kbd) {
                    case ArrowLeft:
                        moveLeft(posXForLastElem, posYForLastElem);
                        direction = Direction.LEFT;
                        break;

                    case ArrowRight:
                        moveRight(posXForLastElem, posYForLastElem);
                        direction = Direction.RIGHT;
                        break;

                    default:
                        moveNoKeyPressed(posXForLastElem, posYForLastElem);
                        break;
                }
            } else {
                switch (kbd) {
                    case ArrowUp:
                        moveUp(posXForLastElem, posYForLastElem);
                        direction = Direction.UP;
                        break;

                    case ArrowDown:
                        moveDown(posXForLastElem, posYForLastElem);
                        direction = Direction.DOWN;
                        break;
                    default:
                        moveNoKeyPressed(posXForLastElem, posYForLastElem);
                        break;
                }
            }
        }
    }

    private void moveNoKeyPressed(int posX, int posY) throws InterruptedException {

        switch (direction) {
            case UP:
                moveUp(posX, posY);
                Thread.sleep(10);
                break;

            case DOWN:
                moveDown(posX, posY);
                Thread.sleep(10);
                break;

            case LEFT:
                moveLeft(posX, posY);
                break;

            case RIGHT:
                moveRight(posX, posY);
                break;
        }
    }


    private void moveUp(int posX, int posY) {
        points.add(new Point(posX, posY - 1));
    }

    private void moveDown(int posX, int posY) {
        points.add(new Point(posX, posY + 1));
    }

    private void moveLeft(int posX, int posY) {
        points.add(new Point(posX - 1, posY));
    }

    private void moveRight(int posX, int posY) {
        points.add(new Point(posX + 1, posY));
    }

    public boolean isAlive() {
        for (int i = 0; i < points.size() - 3; i++) {
            if (points.get(i).x == points.get(points.size() - 1).x &&
                    points.get(i).y == points.get(points.size() - 1).y) {
                return false;
            }
        }

        for (Point elem : Main.cornerPoints) {
            if (points.get(points.size() - 1).x == elem.x && points.get(points.size() - 1).y == elem.y) {
                return false;
            }
        }
        return true;
    }
}
