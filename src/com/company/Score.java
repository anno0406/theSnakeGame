package com.company;

import java.util.Comparator;

public class Score {
    public int score;
    public String userName;

    public Score(String userName, int score) {
        this.score = score;
        this.userName = userName;
    }


}

class ScoreComparator implements Comparator<Score> {
    @Override
    public int compare(Score o1, Score o2) {
        return o1.score - o2.score;
    }
}