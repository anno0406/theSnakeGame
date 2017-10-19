package com.company;

import java.util.Comparator;

class ScoreComparator implements Comparator<Score>
{
    @Override
    public int compare(Score o1, Score o2) {
        return o1.score - o2.score;
    }
}
