
public class StartGeier {

	public static void main(String[] args) {
		HolsDerGeier hdg = new HolsDerGeier();
		int games=1000;
		int e1=0;
		int e2=0;
		HolsDerGeierSpieler s1 = new AlexBotV3();
		HolsDerGeierSpieler s2 = new Random();
		hdg.neueSpieler(s1,s2);
		try{
			for(int i = 0; i < games; i++) {
			    String gewinner = hdg.ganzesSpiel();
			    System.out.println("Gewinner: " + gewinner);
			    if (gewinner == s1.getClass().getSimpleName())
			    	e1++;
			    if (gewinner == s2.getClass().getSimpleName())
			    	e2++;	
			}
		} catch (Exception e){
			System.out.println("Fehler!");
		}
		System.out.println("\n\nGesamterfolg:");
		System.out.println(s1.getClass().getSimpleName() + " hat " + e1 + " mal gewonnen.");
		System.out.println(s2.getClass().getSimpleName() + " hat " + e2 + " mal gewonnen.");
	}
}