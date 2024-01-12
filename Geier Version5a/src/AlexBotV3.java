import java.util.ArrayList;
import java.util.Random;

/**
 * @author Alexander Polyak
 * @version 3.0.0; 15.01.2024<p></p>
 *
 * Der AlexBotV3 versucht, seine Erfolgshistorie nachzuverfolgen und basierend darauf, eine Strategie auszuwählen, welche gewinnbringender ist, als die Aktuelle.
 * <p>
 * Ich habe mich für 5 Strategien entschieden, welche im separatem Package strategies liegen und alle von der abstrakten Klasse Astrategy erben.
 * <br>
 * Mein Bot nutzt einerseits simple Strategien (wie Zufalls- oder statisch-gemappte Strategien), aber hat auch dynamischere Strategien.
 * <p><p>
 * <b>Methodenübersicht für Hauptklasse AlexBotV3:</b><br>
 * <ul>
    * <li> reset(): Initialisiert die Listen der eigenen, gegnerischen Karten und Punktekarten.
    * <li> gibKarte(): Hauptmethode, gibt letztlich die zu spielende Karte aus<br>
    * <li> updateLists(int nextPointCard): Aktualisiert die Arraylisten basierend auf der aktuellen Punktekarte.
 * </ul>
 */

public class AlexBotV3 extends HolsDerGeierSpieler {

    /**
     * Hier werden die Listen für die eigenen Karten, gegnerischen Karten und verbleibende Punktekarten erstellt.
     */
    private final ArrayList<Integer> myAvailableCards = new ArrayList<>();
    private final ArrayList<Integer> enemyAvailableCards = new ArrayList<>();
    private final ArrayList<Integer> pointsLeftInGame = new ArrayList<>();

    /**
    *   Diese 3 Variablen sollen den Spielverlauf bzw. die bisherige Leistung von AlexBotV3 tracken und
    *   anhand der relativen Anzahl an Verlusten (lostGames/playedGames) sollen Strategieänderungen vorgenommen werden,
    *   um nicht bei einer verlustbringenden Strategie festzustecken.
    */
    private double playedGames;
    private double lostGames;
    private double losePercentage;
    /** Nach jeder Runde wird ein Gewinner ausgegeben, dieser wird in dieser Variable gespeichert*/
    private String winnerOfLastRound;

    private int currentStrategy;

    /**
     * Hier werden die 5 Strategien instanziiert, damit man auf die jeweiligen Strategien (und ihre playCard() Methoden) zugreifen kann.
     */
    private final AggressiveStrat aggr = new AggressiveStrat();
    private final DefensiveStrat def = new DefensiveStrat();
    private final DynamicMappedStrat dyn = new DynamicMappedStrat();
    private final StaticMappedStrat stat = new StaticMappedStrat();
    private final RandomStrat rand = new RandomStrat();

    /**
     *  Konstruktor für den AlexBotV3 - beim Instanziieren eines AlexBotV3-Objekts in der Startklasse, wird die Methode reset() aufgerufen.
     */
    public AlexBotV3() {
        currentStrategy = 1;
        lostGames = 0;
        playedGames = 0;
        losePercentage = 0;
    }

    /**
     * Mithilfe der Methode setWinnerOfLastRound() kann die Rückgabe der HolsDerGeier ganzesSpiel()-Methode gespeichert werden.
     */
    public void setWinnerOfLastRound(String winnerOfLastRound){
        this.winnerOfLastRound = winnerOfLastRound;
    }

    /**
    *  Die abstrakte Methode reset() aus der Klasse "HolsDerGeierSpieler" wird hier überschrieben.<br>
    *  Zuerst werden alle Arraylisten zurückgesetzt, danach wieder mit den Punkte-/Spielerkarten befüllt.
    */
    @Override
    public void reset() {
        //Nach jedem Rundenstart wird playedGames inkriminiert.
        playedGames++;

        //Die Arraylisten werden geleert.
        enemyAvailableCards.clear();
        myAvailableCards.clear();
        pointsLeftInGame.clear();

        //Spieler-/Gegnerkarten werden befüllt. Karten 1 bis 15
        for (int i = 1; i <= 15; i++) {
            enemyAvailableCards.add(i);
            myAvailableCards.add(i);
        }
        //Punktekarten werden befüllt. Karten -5 bis 10
        for (int i = -5; i <= 10; i++) {
            if (i != 0) {
                pointsLeftInGame.add(i);
            }
        }

        // Wenn der Gegner das letzte Spiel gewonnen hat, wird lostGames inkrementiert und die neue losePercentage berechnet.
        if(winnerOfLastRound != null && !winnerOfLastRound.equals(this.getClass().getSimpleName())){
            lostGames++;
            losePercentage = lostGames/playedGames;
        }
    }

    /**
     *  Die abstrakte Klasse gibKarte() aus der Klasse "HolsDerGeierSpieler" wird hier überschrieben.<br><br>
     *  Zunächst werden mit dem Methodenaufruf updateLists() mit den Eingabeparametern pointsLeftInGame (Arrayliste; verbleibende Punktekarten), enemyAvailableCards (Arraylist; verbleibende Gegnerkarten),
     *  letzterZug() (int; die letzte gegnerische Karte) und nextPointCard (int; aufgedeckte Karte) die Listen aktualisiert, welche den Spielverlauf tracken.<br><br>
     *  Danach wird der Strategy Handler aufgerufen, der basierend auf der aktuellen Strategie eine Karte zurückgibt.
     */
    @Override
    public int gibKarte(int nextPointCard) {
        CardManager.updateLists(pointsLeftInGame,enemyAvailableCards,letzterZug(),nextPointCard);
        return strategyHandler(nextPointCard);
    }

    /**
     * Der StrategyHandler ist das Herz vom AlexBotV3 - hier wird entschieden welche Taktik gespielt wird, um den Gegner bestmöglich zu kontern.  <br>
     * Einziger Nachteil ist, dass der Bot, bei wenigen Spielen, die zu passende Taktik sehr ungenau auswählt (zu wenige Daten für Entscheidung).
     */
    private int strategyHandler(int nextPointCard){
        //Festgelegter Wert (kann an Bedürfnisse angepasst werden) - überschreitet losePercentage diesen Wert, ändert der StrategyHandler die Strategie von AlexBotV3
        final double STRATEGY_THRESHOLD = 0.5;

        /*
         * Wenn die Verlustwahrscheinlichkeit die ertragbare Verlustwahrscheinlichkeit übersteigt und dies im Rahmen von mehreren Spielen passiert (gemäß dem Gesetz der großen Zahlen),
         * ist die aktuelle Strategie wohl nicht gewinnbringend und muss geändert werden.
         */
        if(losePercentage > STRATEGY_THRESHOLD && playedGames > 50){
            //Strategie wird inkrementiert; durch den Modulo Operator bleibt der Counter im Bereich von 1-5.
            currentStrategy = (currentStrategy % 5) +1;
            lostGames = 0;
            playedGames = 0;
            losePercentage = 0;
        }

        /*
         * Die folgenden 3 switch-cases sollen darstellen, wie das gleiche Problem auf drei verschiedene Arten gelöst werden kann, wobei der Output bei allen drei gleich bleibt.
         */
        /* V1:
        int retCard = 0;
        switch (currentStrategy){
            case 1: //Dynamic Mapped
                //Strategieaufruf von Klasse DynamicMapped
                retCard = dyn.playCard(pointsLeftInGame,myAvailableCards,enemyAvailableCards,nextPointCard);
                break;
            case 2: //Static Mapped
                //Strategieaufruf von Klasse StaticMapped
                retCard = stat.playCard();
                break;
            case 3: //Aggressive
                //Strategieaufruf von Klasse Aggressive
                retCard = aggr.playCard();
                break;
            case 4: //Defensive
                //Strategieaufruf von Klasse Defensive
                retCard = def.playCard();
                break;
            case 5: //Random
                //Strategieaufruf von Klasse Random
                retCard = rand.playCard();
                break;
        }
        return retCard;

        V2:
        int retCard = switch (currentStrategy) {
            case 1 -> //Dynamic Mapped
                    dyn.playCard(pointsLeftInGame, myAvailableCards, enemyAvailableCards, nextPointCard);
            case 2 -> //Static Mapped
                    stat.playCard();
            case 3 -> //Aggressive
                    aggr.playCard();
            case 4 -> //Defensive
                    def.playCard();
            case 5 -> //Random
                    rand.playCard();
            default -> 1;
        };
        return retCard;
        */

        /* V3:
         * Hier wird auf Basis des aktuellen Wertes der Variable currentStrategy die playCard()-Methode der jeweiligen Strategie aufgerufen und der int zurückgegeben.
         */
        return switch (currentStrategy) {
            case 1 -> //Dynamic Mapped
                    dyn.playCard(pointsLeftInGame, myAvailableCards, enemyAvailableCards, nextPointCard);
            case 2 -> //Static Mapped
                   stat.playCard(myAvailableCards, nextPointCard);
            case 3 -> //Aggressive
                    aggr.playCard(myAvailableCards, nextPointCard);
            case 4 -> //Defensive
                    def.playCard(myAvailableCards, nextPointCard);
            case 5 -> //Random
                    rand.playCard(myAvailableCards, nextPointCard);
            default -> dyn.playCard(pointsLeftInGame, myAvailableCards, enemyAvailableCards, nextPointCard);
        };
    }
}
