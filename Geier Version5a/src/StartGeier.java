
public class StartGeier {

	public static void main(String[] args) {
		HolsDerGeier hdg = new HolsDerGeier();
		int games=1000;
		HolsDerGeierSpieler s1 = new AlexBotV3();
		HolsDerGeierSpieler s2 = new Random();
		hdg.neueSpieler(s1,s2);
		try{
			hdg.ganzesSpiel();
		} catch (Exception e){
			System.out.println("Fehler!");
		}
		for(int i = 0; i < games; i++) {
		    String gewinner = hdg.ganzesSpiel();
		    a1.setWinnerOfLastRound(gewinner);
		}

	}
}