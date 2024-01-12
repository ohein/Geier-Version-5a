
public class StartGeier {

	public static void main(String[] args) {
		HolsDerGeier hdg = new HolsDerGeier();
		HolsDerGeierSpieler s1 = new AlexBotV3();
		HolsDerGeierSpieler s2 = new Random();
		hdg.neueSpieler(s1,s2);
		try{
			hdg.ganzesSpiel();
		} catch (Exception e){
			System.out.println("Fehler!");
		}
	}
}