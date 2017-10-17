package com.company;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;

public class Snake implements Draw {

    List<Point> points = new ArrayList<>();
    Direction direction;

    public Snake() {
        this.direction = Direction.RIGHT;
        for (int i = 0; i < 5; i++) {
            points.add(new Point(Main.WIDTH/2 + i, Main.HEIGHT/2));
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void draw(Terminal terminal) {
        for (Point point : points) {
            terminal.moveCursor(point.x, point.y);
            terminal.putCharacter('O');
            terminal.setCursorVisible(false);
        }
    }

    public boolean pearSpot(Pear pear){
        int xPos = points.get(points.size()-1).x;
        int yPos = points.get(points.size()-1).y;
        if (pear.p.x == xPos && pear.p.y == yPos){
            return true;
        }
        return false;
    }

    public void move(Key key, Pear pear, Terminal terminal){
        int posXForLastElem = points.get(points.size()-1).x;
        int posYForLastElem = points.get(points.size()-1).y;
        if (!pearSpot(pear)){
            terminal.moveCursor(points.get(0).x, points.get(0).y);
            terminal.putCharacter(' ');
            points.remove(0);
        }
        else {
            pear.put();
        }

        if (key == null){
            moveNoKeyPressed(posXForLastElem, posYForLastElem);
        }
        else {
            switch (key.getKind()) {

                case ArrowUp:
                    moveUp(posXForLastElem, posYForLastElem);
                    direction = Direction.UP;
                    break;

                case ArrowDown:
                    moveDown(posXForLastElem, posYForLastElem);
                    direction = Direction.DOWN;
                    break;

                case ArrowLeft:
                    moveLeft(posXForLastElem, posYForLastElem);
                    direction = Direction.LEFT;
                    break;

                case ArrowRight:
                    moveRight(posXForLastElem, posYForLastElem);
                    direction = Direction.RIGHT;
                    break;

                default:
                    break;
            }
        }

    }

    private void moveNoKeyPressed(int posX, int posY) {

        switch (direction) {
            case UP:
                moveUp(posX, posY);
                break;

            case DOWN:
                moveDown(posX, posY);
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
        points.add(new Point(posX, posY-1));
    }

    private void moveDown(int posX, int posY) {
        points.add(new Point(posX, posY+1));
    }

    private void moveLeft(int posX, int posY) {
        points.add(new Point(posX-1, posY));
    }

    private void moveRight(int posX, int posY) {
        points.add(new Point(posX+1, posY));
    }

    public boolean isAlive(){
        for (int i = 0; i < points.size() - 3; i++) {
            if (points.get(i).x == points.get(points.size()-1).x &&
                    points.get(i).y == points.get(points.size()-1).y ){
                return false;
            }
        }

        for (Point elem : Main.cornerPoints) {
            if (points.get(points.size()-1).x == elem.x && points.get(points.size()-1).y == elem.y){
                return false;
            }
        }
        return true;
    }
}
