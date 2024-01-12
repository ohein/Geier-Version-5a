

import java.util.ArrayList;

public abstract class Astrategy {
    abstract int playCard(ArrayList<Integer> myAvailableCards, int nextPointCard);
    abstract int playCard(ArrayList<Integer> pointsLeftInGame, ArrayList<Integer> myAvailableCards, ArrayList<Integer> enemyAvailableCards, int nextPointCard);
}
