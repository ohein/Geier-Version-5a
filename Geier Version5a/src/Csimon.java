
package player;

import java.util.ArrayList;

public class Csimon extends HolsDerGeierSpieler {

    // Listen zur Speicherung von Kartendaten
    private ArrayList<Integer> handkarten = new ArrayList<Integer>();
    private ArrayList<Integer> gegnerischeKarten = new ArrayList<Integer>();
    private ArrayList<Integer> restlicheKarten = new ArrayList<Integer>();
    private ArrayList<Integer> kartengespielt = new ArrayList<Integer>();
    private ArrayList<Integer> kartengespieltich = new ArrayList<Integer>();
    private ArrayList<Integer> kartengeier = new ArrayList<Integer>();

    // Variablen zur Spielverfolgung und Punktzählung
    private int spielRunde;
    private int punkte = 0;
    private int punkteZumRechnen = 0;

    // Methode zur Initialisierung des Spiels
    @Override
    public void reset() {
        punkte = 0;
        punkteZumRechnen = 0;
        spielRunde = 0;

        // Initialisieren der Kartenlisten
        restlicheKarten.clear();
        for (int i = -5; i < 16; i++)
            if (i != 0)
                restlicheKarten.add(i);

        gegnerischeKarten.clear();
        for (int i = 1; i < 16; i++)
            gegnerischeKarten.add(i);

        handkarten.clear();
        for (int i = 1; i < 16; i++)
            handkarten.add(i);

        kartengespieltich.clear();
        kartengespielt.clear();
        kartengeier.clear();
    }

    // Methode für die Auswahl und Rückgabe einer Karte in jeder Runde
    @Override
    public int gibKarte(int naechsteKarte) {
        // Hinzufügen der vom Geier gespielten Karte zur Liste
        kartengeier.add(naechsteKarte);

        // Überprüfen, ob der Gegner in der letzten Runde eine Karte gespielt hat
        if (spielRunde > 0) {
            // Falls ja, überprüfen, ob die Karte des Gegners noch im Spiel ist
            if (gegnerischeKarten.contains(letzterZug())) {
                // Falls ja, entfernen sie aus der Liste der verbleibenden Karten des Gegners
                gegnerischeKarten.remove(gegnerischeKarten.indexOf(letzterZug()));
                // Hinzufügen der vom Gegner gespielten Karte zur Liste
                kartengespielt.add(letzterZug());
            }
        }

        // Falls es nicht die erste Runde ist, Punkte zählen
        if (spielRunde > 0)
            zaehlePunkte();

        // Auswählen und spielen einer Karte basierend auf der Strategie
        int ret = handkarten.remove(handkarten.indexOf(auswahlKarte(naechsteKarte)));
        // Hinzufügen der vom Spieler gespielten Karte zur Liste
        kartengespieltich.add(ret);

        // Inkrementieren der Spielrunde
        spielRunde++;
        // Rückgabe der gespielten Karte
        return ret;
    }

    // Methode zur Punktezählung nach jeder Runde
    private void zaehlePunkte() {
        // Überprüfen, ob die letzte Runde unentschieden war
        if (kartengespieltich.get(kartengespieltich.size() - 1) == kartengespielt.get(kartengespielt.size() - 1)) {
            // Falls ja, Punkte hinzufügen und den Wert für die Berechnung setzen
            punkte += kartengeier.get(kartengeier.size() - 2);
            punkteZumRechnen = punkte + kartengeier.get(kartengeier.size() - 1);
        } else {
            // Falls nicht, Punkte und Berechnungswert zurücksetzen
            punkte = 0;
            punkteZumRechnen = 0;
        }
    }

    // Methode zur Auswahl der zu spielenden Karte basierend auf der Spielsituation
    private int auswahlKarte(int naechsteKarte) {
        int karte = 0;

        // Überprüfen, ob der Betrag der nächsten Karte kleiner als 6 ist
        if (betrag(naechsteKarte) < 6) {
            // Falls ja, die doppelte Anzahl des Betrags und einen Zufallswert hinzufügen
            karte = betrag(naechsteKarte) * 2 + zufallsWert();
        } else if (naechsteKarte < 10) {
            // Falls nicht, aber die nächste Karte kleiner als 10 ist, die Karte plus 5 und einen Zufallswert hinzufügen
            karte = naechsteKarte + 5 + zufallsWert();
        } else {
            // Falls nicht, die höchste Karte (15) spielen
            karte = 15;
        }

        // Rückgabe der gefundenen nächsten Karte
        return findeNaechsteKarte(karte);
    }

    // Methode zur Bestimmung des Betrags einer Zahl
    private int betrag(int geierKarte) {
        return Math.abs(geierKarte);
    }

    // Methode zur Umsetzung der Spielstrategie und Suche der nächsten zu spielenden Karte
    private int findeNaechsteKarte(int wunschKarte) {
        // Initialisieren von Variablen für die Suche
        boolean fertig = false;
        int i = 0;
        int ergebnis = 0;

        // Schleife für die Suche
        while (!fertig && ((wunschKarte - i) > 0 || (wunschKarte + i) < 16)) {
            // Überprüfen, ob die Karte in den Handkarten ist
            if (handkarten.contains(wunschKarte + i)) {
                fertig = true;
                ergebnis = wunschKarte + i;
                break;
            }
            // Überprüfen, ob die Karte in den Handkarten ist
            if (handkarten.contains(wunschKarte - i)) {
                fertig = true;
                ergebnis = wunschKarte - i;
                break;
            }
            // Inkrementieren des Suchindex
            i++;
        }

        // Rückgabe des gefundenen Ergebnisses
        return ergebnis;
    }

    // Methode zur Generierung eines Zufallswerts
    private int zufallsWert() {
        // Überprüfen, ob der Zufallswert 0 oder 1 sein soll
        if (0 != (int) (Math.random() * 100) % 3) {
            return 0;
        } else {
            return 1;
        }
    }
}