package ruleta_de_la_suerte;

import java.util.ArrayList;
import java.util.Random;

public class Frases
{
    // Atributos

    private Random random;
    private int n_frases_utilizadas;
    private ArrayList<Frase> frases_utilizadas;
    
    ////////////////////////////////////////////////////////////////////////////////////////

    // Métodos

    public Frases()
    {
        random = new Random();
        frases_utilizadas = new ArrayList();
        n_frases_utilizadas = 0;
    }
    
    public void Insertar(String frase_y_pista)
    {
        char DELIMITADOR = ';';
        String str = frase_y_pista.substring(0,frase_y_pista.indexOf(DELIMITADOR));
        String str2 = frase_y_pista.substring(frase_y_pista.indexOf(DELIMITADOR) + 1);

        n_frases_utilizadas++;

        str = str.toUpperCase();

        Frase frase_panel = new Frase(str,str2);
        frases_utilizadas.add(frase_panel);
    }

    public Frase getFraseAleatoria()
    {
        int n = random.nextInt(n_frases_utilizadas);
        Frase frase_devuelta = new Frase(frases_utilizadas.get(n) );
        frases_utilizadas.remove(n);
        n_frases_utilizadas--;
        return frase_devuelta;
    }
    
    public ArrayList<Frase> getFrases()
    {
        return frases_utilizadas;
    }

    ///////////////////////////////////////////////////////////////////////
}