package ruleta_de_la_suerte;

import java.util.Random;

public class Ruleta
{
	// Atributos

	private static final Ruleta instance = new Ruleta();
	private Random random = new Random();
	private final int MAX;
	private int[] casillas;
	
	//////////////////////////

	// Constructor

	public Ruleta()
	{
        // Inicialización de las casillas
        MAX = 20;
        casillas = new int[MAX];
        
        casillas[0] = 50;
	    casillas[1] = 50;
	    casillas[2] = 50;
	    casillas[3] = 50;
	    casillas[4] = -1;
	    casillas[5] = 0;
	    casillas[6] = 100;
	    casillas[7] = 100;
	    casillas[8] = 100;
	    casillas[9] = 100;
	    casillas[10] = -1;
	    casillas[11] = 0;
	    casillas[12] = 150;
	    casillas[13] = 150;
	    casillas[14] = 150;
	    casillas[15] = 150;
	    casillas[16] = 0;
	    casillas[17] = 0;
	    casillas[18] = 200;
	    casillas[19] = 200;
	    ///////////////////////////////
	}

	////////////////////////////////////////

	// Métodos

    public int girar()
    {
    	return casillas[random.nextInt(MAX)];
    }
    
    public static Ruleta getInstance() {
        return instance;
    }

    ////////////////////////////////////////
}