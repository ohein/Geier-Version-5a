
	import java.util.ArrayList;
	/**
	 * 
	 * @author Klara Schramm
	 * @version 10.01.2024 
	 */
	public class GeiervonKlaraSchramm extends HolsDerGeierSpieler {

	    
		//*Verteilung von Karten - Arrays werden aufgefüllt*//
	    private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
	    
	    
	            
	    public void reset() {
	    	
	        for (int i=15;i>0;i--)            
	            nochNichtGespielt.add(i);                                 
	    }
	    //* Strategie nochzuGewinnen: 3 Zahlenbereiche: 1. Zahlen von -5 (14) bis -1, 2. Zahlen von 1-5 und Zahlen von 6-10*//
	    //*Strategie nochnichtGespielt: 3 Zahlenbereiche: 1. Zahlen von 1-5 2. 5-9 3.10-13 bei 10 get last 9=15
	    public int gibKarte(int naechsteKarte) {
	       int ret=-99;
	     
	    //* Spezielle Karte 10 - kleinste vorhandene Handkarte wird gespielt*//
	    	  if(naechsteKarte==10) {
	    		  ret=nochNichtGespielt.get(nochNichtGespielt.size()-1);
	    	  }
	    	  //*Spezielle Karte 9 - 15 wird ausgespielt*//
	    	  else if(naechsteKarte==9) {
	    		  ret =15;
	    	  }
	    	 
	    	  else if(naechsteKarte>=-4) {
		    	  int a,b;
		    	  
		    	  //*Zahlenbereich von 6-8 - Karten werden zufällig von 11-13 ausgespielt*//
		    	  if(naechsteKarte>=6) {
		    		  a=3;
		    		  b=11;
		    	  }
		    	  //*Zahlenbereich von 1-5 - Karten werden zufällig im Bereich von 1-6 ausgespielt*//
		    	  else if(naechsteKarte>=1) {
		    		  a=6;
		    		  b=1;
		    	  }
		    	  //* Zahlenbereich von (-4)-(-1) - Karten werden zufällig im Bereich von 7-10 ausgespielt*//
		    	  else {
		    		  a=4;
		    		  b=7;
		    	  }		 
		    	  do {
	    			 //*Umwandlung Fließkommzahl in ganze Zahlen*//
	    		  ret=(int)(Math.random()*100)%a+b;
	    		
	    		  //*Kontrolle ob Karte existiert*//
	    		 }while(!nochNichtGespielt.contains(ret));
		    	 
	    	  }  
	    	 //* Sonderkarte -5 = Karte 14 wird ausgespielt*// 
	    	  else if(naechsteKarte==-5) {
	    		  ret=14;
	    	  }
	    	  //*Löscht die gespielten Karten aus dem Array der noch nicht gespielten Karteb*//
	    	  nochNichtGespielt.remove(nochNichtGespielt.indexOf(ret));
		   
	    	  //*Ausgewählte Karte wird ausgespielt*//
	    	  return ret;
		   
	    }
	}
	    
	    
	
