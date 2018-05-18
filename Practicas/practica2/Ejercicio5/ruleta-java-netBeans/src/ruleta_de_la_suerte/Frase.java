package ruleta_de_la_suerte;

public class Frase
{
	// Atributos

    private String frase;
    private String estado_frase;
    private String pista;
 
    /////////////////////////////

    // Constructores

    public Frase(String s, String p)
    {
	    frase = s;
	    pista = p;
            estado_frase = "";
	    for(int i = 0; i < s.length(); ++i)
	    {
	        if(s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
	            estado_frase += '_';
	        else
	            estado_frase += s.charAt(i);
	    }
    }
    
    public Frase(Frase f)
    {
        this.frase = f.getFrase();
        this.estado_frase = f.getEstadoFrase();
        this.pista = f.getPista();
    }
    
    ///////////////////////////////////////

	// Métodos

    public int Check(char c)
    {
        String estado_anterior = estado_frase;
        int apariciones = 0;
        for(int i =0; i<frase.length(); ++i)
        {
            if(c == frase.charAt(i))
            {
                String nuevo_estado = estado_frase.substring(0,i) + c + estado_frase.substring(i+1);
                estado_frase = nuevo_estado;
                apariciones++;
            }
        }
        if(estado_frase.equals(estado_anterior) )
            apariciones = 0;
        return apariciones;
    }
    
    public String getFrase() {
    	return this.frase;
    }

    public String getEstadoFrase()
    {
    	return this.estado_frase;
    }

    public String getPista()
    {
    	return this.pista;
    }
    public String toString()
    {
    	return frase;
    }

    //////////////////////////////////////////
}