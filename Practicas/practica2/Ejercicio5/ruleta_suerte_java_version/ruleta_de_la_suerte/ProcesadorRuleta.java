package ruleta_de_la_suerte;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class ProcesadorRuleta {

    // Atributos
    
    private Socket socketServicio;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Frases frases;
    private Frase frase_elegida;
    private Random random;
    private Jugador j1;
    private Jugador j2;
    private Jugador j3;
    private int turno;

    //////////////////////////////////////
    
    // Constructor

    public ProcesadorRuleta(Socket socketServicio){
        this.socketServicio=socketServicio;
        try {
            BufferedReader in = new BufferedReader(new FileReader("test/Registro_paneles.txt"));
            String str;
            frases = new Frases();
            while ((str = in.readLine()) != null)
                frases.Insertar(str);
            in.close();
            frase_elegida = new Frase(frases.getFraseAleatoria() );
            random = new Random();
            j1 = new Jugador();
            j2 = new Jugador();
            j3 = new Jugador();
            turno = (random.nextInt(3))+1;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo con los paneles.");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    // Métodos
    
    void procesa()
    {	
        String buferRecepcion = null;
        String nombre;
        boolean panel_resuelto = false;
        boolean login_correcto;
        ArrayList<Jugador> lista_jugadores = new ArrayList();

        try {

            do
            {
                login_correcto = false;
                inputStream = socketServicio.getInputStream();
                outputStream = socketServicio.getOutputStream();

                BufferedReader inReader = new BufferedReader(new InputStreamReader(inputStream) );
                PrintWriter outPrinter = new PrintWriter(outputStream, true);
                if( (turno == 1 && j1.getName().equals("No presentado") )  ||
                    (turno == 2 && j2.getName().equals("No presentado") )  ||
                    (turno == 3 && j3.getName().equals("No presentado") ) )
                {
                    do
                    {
                        outPrinter.println("Introduzca un usuario valido para participar: ");

                        buferRecepcion = inReader.readLine();             
                        Scanner scan = new Scanner(new File("test/Registro_usuarios.txt"));
                        while(scan.hasNextLine() && !login_correcto)
                        {
                            if(scan.nextLine().equals(buferRecepcion) )
                                login_correcto = true;
                        }
                    } while(!login_correcto);

                    if(turno == 1)
                        j1.Identificar(buferRecepcion);
                    else if(turno == 2)
                        j2.Identificar(buferRecepcion);
                    else if(turno == 3)
                        j3.Identificar(buferRecepcion);	
                    
                    outPrinter.println("Usuario validado satisfactoriamente.");
                }
                

                if(turno == 1)
                    nombre = j1.getName();
                else if(turno == 2)
                    nombre = j2.getName();
                else if(turno == 3)
                    nombre = j3.getName();
                else
                    nombre = "ERROR_DE_TURNO";

                outPrinter.println("Es el turno del jugador: " + nombre + 
                                                   "\nMENU:   {C} Comprobar panel\n\t"  +
                                                             "{R} Resolver panel\n\t"   +
                                                             "{G} Girar la ruleta\n\t"  +
                                                             "{V} Comprar vocal\n\t"    +
                                                             "{P} Ver la pista\n\t"	+
                                                             "{D] Comprobar dinero\n");


                String peticion = inReader.readLine().toUpperCase();
                String respuesta;
                switch(peticion) {
                    case "C" :
                        outPrinter.println("Estado del panel\n\n" + frase_elegida.getEstadoFrase() );
                        break;

                    case "R" :
                        outPrinter.println("Introduzca la solucion: ");
                        buferRecepcion = inReader.readLine();

                        if(buferRecepcion.equalsIgnoreCase(frase_elegida.getFrase() ) )
                        {
                            outPrinter.println("Enhorabuena, la frase es correcta, gana 500€.");
                            
                            boolean respuesta_valida = false;
                            
                            if(turno == 1)
                                j1.modificaDinero(500);
                            else if(turno == 2)
                                j2.modificaDinero(500);
                            else if(turno == 3)		
                                j3.modificaDinero(500);
                            
                            do {
                                outPrinter.println("\n¿Desea seguir jugando? (S/N)");

                                buferRecepcion = inReader.readLine();
                                
                                j1.almacenarDinero();
                                j2.almacenarDinero();
                                j3.almacenarDinero();

                                j1.Quiebra();
                                j2.Quiebra();
                                j3.Quiebra();
                                
                                if(buferRecepcion.equals("S"))
                                {
                                    panel_resuelto = false;

                                    frase_elegida = new Frase(frases.getFraseAleatoria() );

                                    turno = (random.nextInt(3))+1;
                                    respuesta_valida = true;
                                }
                                else
                                {
                                    panel_resuelto=true;
                                    respuesta_valida = true;
                                    lista_jugadores = getPuestos();
                                    outPrinter.println("Puestos:");
                                    outPrinter.println("1 --> " + lista_jugadores.get(0).getName() + " (" + lista_jugadores.get(0).getDineroTotal() +"€)");
                                    outPrinter.println("2 --> " + lista_jugadores.get(1).getName() + " (" + lista_jugadores.get(1).getDineroTotal() +"€)");
                                    outPrinter.println("3 --> " + lista_jugadores.get(2).getName() + " (" + lista_jugadores.get(2).getDineroTotal() +"€)");
                                }
                            } while(!respuesta_valida);
                        }
                        else
                        {
                            outPrinter.println("La respuesta no es correcta. Pierde turno.");
                            turno = (turno % 3) + 1;
                        }
                        break;

                    case "G" :
                        Ruleta ruleta = Ruleta.getInstance();
                        int apariciones;
                        respuesta = "Ha caido en la casilla [";
                        int casilla = ruleta.girar();
                        if(casilla>0)
                        {
                            respuesta += casilla + "]";
                            outPrinter.println(respuesta);
                            char primera_letra;
                            do
                            {
                                outPrinter.println("Introduzca la consonante que desea descubrir: ");
                                buferRecepcion = inReader.readLine();
                                primera_letra = Character.toUpperCase(buferRecepcion.charAt(0) );
                            } while(primera_letra == 'A' ||
                                    primera_letra == 'E' ||
                                    primera_letra == 'I' ||
                                    primera_letra == 'O' ||
                                    primera_letra == 'U');
                            
                            int extra = 0;

                            apariciones = frase_elegida.Check(primera_letra);

                            if(apariciones > 0)
                            {
                                String aparicion_consonantes = "La consonante ha aparecido " + apariciones + " veces. Gana " + (casilla*apariciones) + "€.";

                                if(frase_elegida.getEstadoFrase().equals(frase_elegida.getFrase() ) )
                                {
                                    extra = 500;
                                    aparicion_consonantes += "\tAdemas, el panel ha sido resuelto, gana 500€.";
                                    
                                    outPrinter.println(aparicion_consonantes);
                                    
                                    outPrinter.println("\n¿Desea seguir jugando? (S/N)");
                                    buferRecepcion = inReader.readLine();
                                    
                                    j1.almacenarDinero();
                                    j2.almacenarDinero();
                                    j3.almacenarDinero();

                                    j1.Quiebra();
                                    j2.Quiebra();
                                    j3.Quiebra();
                                    
                                    if(buferRecepcion.equals("S"))
                                    {
                                        panel_resuelto = false;

                                        frase_elegida = new Frase(frases.getFraseAleatoria() );

                                        turno = (random.nextInt(3))+1;
                                    }
                                    else
                                    {
                                        panel_resuelto=true;
                                        lista_jugadores = this.getPuestos();
                                        outPrinter.println("Puestos:");
                                        outPrinter.println("1 --> " + lista_jugadores.get(0).getName() + " (" + lista_jugadores.get(0).getDineroTotal() +"€)");
                                        outPrinter.println("2 --> " + lista_jugadores.get(1).getName() + " (" + lista_jugadores.get(1).getDineroTotal() +"€)");
                                        outPrinter.println("3 --> " + lista_jugadores.get(2).getName() + " (" + lista_jugadores.get(2).getDineroTotal() +"€)");
                                    }
                                }
                                else
                                    outPrinter.println(aparicion_consonantes);
                            }
                            else
                            {
                                    outPrinter.println("La consonante no se encuentra en el panel. Pierde turno");
                                    turno = (turno % 3) + 1;
                            }

                            if(turno == 1)
                                    j1.modificaDinero(casilla * apariciones + extra);
                            else if(turno == 2)
                                    j2.modificaDinero(casilla * apariciones + extra);
                            else if(turno == 3)		
                                    j3.modificaDinero(casilla * apariciones + extra);



                        }
                        else if(casilla==0)
                        {
                            respuesta += "Pierde Turno]";
                            outPrinter.println(respuesta);
                            turno = (turno % 3) + 1;
                        }
                        else if(casilla==-1)
                        {
                            if(turno == 1)
                                j1.Quiebra();
                            else if(turno == 2)
                                j2.Quiebra();
                            else if(turno == 3)
                                j3.Quiebra();
                            respuesta += "Quiebra]";
                            outPrinter.println(respuesta);
                            turno = (turno % 3) + 1;
                        }
                        break;

                    case "V" :
                        if( (turno == 1 && j1.getDinero() >= 75) ||
                            (turno == 2 && j2.getDinero() >= 75) ||
                            (turno == 3 && j3.getDinero() >= 75) )
                        {
                            char primera_letra;
                            do
                            {
                                outPrinter.println("Introduzca la vocal que desea comprar: ");
                                
                                buferRecepcion = inReader.readLine();
                                primera_letra = Character.toUpperCase(buferRecepcion.charAt(0) );
                            } while(primera_letra != 'A' &&
                                    primera_letra != 'E' &&
                                    primera_letra != 'I' &&
                                    primera_letra != 'O' &&
                                    primera_letra != 'U');

                            apariciones = frase_elegida.Check(primera_letra);
                            int extra = 0;

                            if(apariciones > 0)
                            {
                                String aparicion_vocales = "La vocal ha aparecido " + apariciones + " veces.";
                                
                                if(frase_elegida.getEstadoFrase().equals(frase_elegida.getFrase() ) )
                                {
                                    extra = 500;
                                    aparicion_vocales += "\tAdemas, el panel ha sido resuelto, gana 500€.";
                                    outPrinter.println(aparicion_vocales);                          
                                    
                                    outPrinter.println("\n¿Desea seguir jugando? (S/N)");
                                    buferRecepcion = inReader.readLine();
                                    
                                    j1.almacenarDinero();
                                    j2.almacenarDinero();
                                    j3.almacenarDinero();

                                    j1.Quiebra();
                                    j2.Quiebra();
                                    j3.Quiebra();                                    
            
                                    if(buferRecepcion.equals("S"))
                                    {
                                        panel_resuelto = false;
                                        
                                        frase_elegida = new Frase(frases.getFraseAleatoria() );

                                        turno = (random.nextInt(3))+1;
                                    }
                                    else
                                    {
                                        panel_resuelto=true;
                                        lista_jugadores = this.getPuestos();
                                        outPrinter.println("Puestos:");
                                        outPrinter.println("1 --> " + lista_jugadores.get(0).getName() + " (" + lista_jugadores.get(0).getDineroTotal() +"€)");
                                        outPrinter.println("2 --> " + lista_jugadores.get(1).getName() + " (" + lista_jugadores.get(1).getDineroTotal() +"€)");
                                        outPrinter.println("3 --> " + lista_jugadores.get(2).getName() + " (" + lista_jugadores.get(2).getDineroTotal() +"€)");
                                    }
                                }
                                else
                                    outPrinter.println(aparicion_vocales);
                            }

                            else
                            {
                                outPrinter.println("La vocal no se encuentra en el panel. Pierde turno");
                                turno = (turno % 3) + 1;
                            }

                            if(turno == 1)
                                j1.modificaDinero(-75 + extra);
                            else if(turno == 2)
                                j2.modificaDinero(-75 + extra);
                            else if(turno == 3)		
                                j3.modificaDinero(-75 + extra);
                        }
                        else
                        {
                            outPrinter.println("No tiene dinero suficiente para comprar una vocal (75€).");
                        }
                        break;

                    case "P" :
                        outPrinter.println(frase_elegida.getPista() );
                        break;

                    case "D" :
                        int dinero, dinero_total;
                        if      (turno == 1)
                        {
                            dinero = j1.getDinero();
                            dinero_total = j1.getDineroTotal();
                        }
                        else if (turno == 2)
                        {
                            dinero = j2.getDinero();
                            dinero_total = j2.getDineroTotal();
                        }
                        else if (turno == 3)
                        {
                            dinero = j3.getDinero();
                            dinero_total = j3.getDineroTotal();
                        }
                        else
                        {
                            dinero = -1;
                            dinero_total = -1;
                        }

                        outPrinter.println("Posee " + dinero + "€ en esta ronda.\nSu dinero total es de " +
                                                dinero_total + "€.");
                        break;

                    default :
                        outPrinter.println("Entrada no valida");
                        break;
                }
            } while(!panel_resuelto);
        } catch (IOException e) {
            System.err.println("Error al obtener los flujos de entrada/salida.");
        }
    }
    
    public ArrayList<Jugador> getPuestos()
    {
        ArrayList<Jugador> jugadores = new ArrayList();
        int dinero1 = j1.getDineroTotal();
        int dinero2 = j2.getDineroTotal();
        int dinero3 = j3.getDineroTotal();

        Jugador ganador;
        Jugador segundo;
        Jugador tercero;

        if(dinero1 > dinero2)
        {
            if(dinero1 > dinero3)
            {
                ganador = new Jugador(j1);

                if(dinero2 > dinero3)
                {
                    segundo = new Jugador(j2);
                    tercero = new Jugador(j3);
                }
                else
                {
                    segundo = new Jugador(j3);
                    tercero = new Jugador(j2);
                }
            }
            else
            {
                ganador = new Jugador(j3);
                segundo = new Jugador(j1);
                tercero = new Jugador(j2);
            }
        }
        else
        {
            if(dinero2 > dinero3)
            {
                ganador = new Jugador(j2);
                if(dinero1 > dinero3)
                {
                    segundo = new Jugador(j1);
                    tercero = new Jugador(j3);
                }
                else
                {
                    segundo = new Jugador(j3);
                    tercero = new Jugador(j1);
                }
            }
            else
            {
                ganador = new Jugador(j3);
                segundo = new Jugador(j2);
                tercero = new Jugador(j1);
            }
        }
        
        jugadores.add(ganador);
        jugadores.add(segundo);
        jugadores.add(tercero);
        return jugadores;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////
}