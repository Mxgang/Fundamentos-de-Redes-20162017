package ruleta_de_la_suerte;

public class Jugador
{
    private String nombre;
    private int dinero_ronda;
    private int dinero_total;

    public Jugador()
    {
        nombre = "No presentado";
        dinero_ronda = 0;
        dinero_total = 0;
    }

    public Jugador(Jugador j)
    {
        this.nombre = j.getName();
        this.dinero_ronda = j.getDinero();
        this.dinero_total = j.getDineroTotal();
    }
    
    public int getDinero()
    {
            return this.dinero_ronda;
    }

    public int getDineroTotal()
    {
            return this.dinero_total;
    }

    public String getName()
    {
            return this.nombre;
    }

    public void Quiebra()
    {
            this.dinero_ronda = 0;
    }

    public void modificaDinero(int cantidad)
    {
            this.dinero_ronda += cantidad;
    }

    public void Identificar(String id)
    {
        nombre = id;
    }

    public void almacenarDinero()
    {
        dinero_total += dinero_ronda;
    }
}