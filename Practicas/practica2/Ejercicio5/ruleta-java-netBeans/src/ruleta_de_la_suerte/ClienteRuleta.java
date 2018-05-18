package ruleta_de_la_suerte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteRuleta
{
    public static void main(String[] args) {
        
        boolean salir = false;
        String buferRecepcion;
        String host = "localhost";
        int port = 8989;
        boolean cambia_turno;
        
        PrintWriter outPrinter = null;
        BufferedReader inReader = null; 
        Scanner scan = null;
        Socket socketServicio = null;
        
        try {
            socketServicio = new Socket(host, port);
            outPrinter = new PrintWriter(socketServicio.getOutputStream(),true);
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream())); 
            scan= new Scanner(System.in);
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }
        
        do
        {
            cambia_turno = false;
            try {
                buferRecepcion = inReader.readLine();   
                System.out.println(buferRecepcion);
                
                String recibido = "";
                if(buferRecepcion.contains("Introduzca un usuario valido") ) 
                {
                    while(!recibido.equals("Usuario validado satisfactoriamente."))
                    {
                        String login = scan.nextLine();
                        outPrinter.println(login);
                        outPrinter.flush(); 

                        recibido = inReader.readLine();
                        System.out.println(recibido);
                    }
                    buferRecepcion = inReader.readLine();
                    System.out.println(buferRecepcion);
                }
                
                if(buferRecepcion.contains("Es el turno del jugador"))
                {
                    for(int i = 0; i < 6; ++i)
                    {
                        buferRecepcion = inReader.readLine();
                        System.out.println(buferRecepcion);
                    }
                }
                
                buferRecepcion = inReader.readLine();
                System.out.println(buferRecepcion);
                
                String respuesta = scan.nextLine();
                outPrinter.println(respuesta);
                outPrinter.flush();
                
                buferRecepcion = inReader.readLine();
                System.out.println(buferRecepcion);
                
                if( (buferRecepcion.contains("Estado del panel") ) )
                {
                    for(int i = 0; i < 2; ++i)
                    {
                        buferRecepcion = inReader.readLine();
                        System.out.println(buferRecepcion);
                    }
                }
                else if (buferRecepcion.contains("Introduzca la solucion"))
                {
                    cambia_turno = true;
                    String solucion = scan.nextLine();
                    outPrinter.println(solucion);
                    outPrinter.flush();
                    String comprueba_solucion = inReader.readLine();
                    System.out.println(comprueba_solucion);
                    if(comprueba_solucion.contains("Enhorabuena"))
                    {
                        boolean repetir = true;
                        do
                        {
                            String pregunta_seguir;
                            pregunta_seguir = inReader.readLine();
                            System.out.println(pregunta_seguir);
                            pregunta_seguir = inReader.readLine();
                            System.out.println(pregunta_seguir);
                            String seguir_jugando;
                            seguir_jugando = scan.nextLine();
                            outPrinter.println(seguir_jugando);
                            outPrinter.flush();
                            if(seguir_jugando.equals("N") )
                            {
                                salir = true;
                                repetir = false;
                                for(int i = 0; i < 4; ++i)
                                {// Podio
                                    buferRecepcion = inReader.readLine();
                                    System.out.println(buferRecepcion);
                                }
                                socketServicio.close();
                            }
                            else if(seguir_jugando.equals("S") )
                            {
                                salir = false;
                                repetir = false;
                            }
                        } while(repetir);
                    }
                }
                else if (buferRecepcion.contains("Ha caido en la casilla") )
                {
                    boolean repetir = true;
                    if(!buferRecepcion.contains("Pierde Turno") && !buferRecepcion.contains("Quiebra"))
                    {
                        do
                        {
                            String introducir_consonante = inReader.readLine();
                            System.out.println(introducir_consonante);
                            String consonante;
                            consonante = scan.nextLine();
                            outPrinter.println(consonante);
                            outPrinter.flush();
                            if(!consonante.equals("A") && !consonante.equals("E") && !consonante.equals("I") && !consonante.equals("O") && !consonante.equals("U"))
                                repetir = false;
                        } while(repetir);             
                        
                        String aparece;
                        aparece = inReader.readLine();
                        System.out.println(aparece);
                        if(aparece.contains("La consonante ha aparecido"))
                        {
                            if(aparece.contains("el panel ha sido resuelto") )
                            {
                                String pregunta_seguir;
                                pregunta_seguir = inReader.readLine();
                                System.out.println(pregunta_seguir);
                                pregunta_seguir = inReader.readLine();
                                System.out.println(pregunta_seguir);
                                String seguir_jugando;
                                seguir_jugando = scan.nextLine();
                                outPrinter.println(seguir_jugando);
                                outPrinter.flush();
                                if(seguir_jugando.equals("N"))
                                {
                                    salir = true;
                                    for(int i = 0; i < 4; ++i)
                                    {// Podio
                                        buferRecepcion = inReader.readLine();
                                        System.out.println(buferRecepcion);
                                    }
                                    socketServicio.close();
                                }
                            }
                        }
                    }
                    else
                        cambia_turno = true;
                }
                else if (buferRecepcion.contains("Introduzca la vocal") )
                {
                    boolean repetir = true;
                    
                    
                    do
                    {
                        String vocal;
                        vocal = scan.nextLine();
                        outPrinter.println(vocal);
                        outPrinter.flush();
                        if(vocal.equalsIgnoreCase("A") || vocal.equalsIgnoreCase("E") || vocal.equalsIgnoreCase("I") || vocal.equalsIgnoreCase("O") || vocal.equalsIgnoreCase("U"))
                        {
                            repetir = false;
                        }
                        else
                        {
                            buferRecepcion = inReader.readLine();
                            System.out.println(buferRecepcion);
                        }
                    } while(repetir);                    

                    String aparece;
                    aparece = inReader.readLine();
                    System.out.println(aparece);
                    if(aparece.contains("La vocal ha aparecido") )
                    {
                        if(aparece.contains("el panel ha sido resuelto") )
                        {
                            String pregunta_seguir;
                            pregunta_seguir = inReader.readLine();
                            System.out.println(pregunta_seguir);
                            pregunta_seguir = inReader.readLine();
                            System.out.println(pregunta_seguir);
                            String seguir_jugando;
                            seguir_jugando = scan.nextLine();
                            outPrinter.println(seguir_jugando);
                            outPrinter.flush();

                            if(seguir_jugando.equals("N"))
                            {
                                salir = true;
                                for(int i = 0; i < 4; ++i)
                                {// Podio
                                    buferRecepcion = inReader.readLine();
                                    System.out.println(buferRecepcion);
                                }
                                socketServicio.close();
                            }
                        }
                    }
                    else
                        cambia_turno = true;
                }
                else if (buferRecepcion.contains("Posee") )
                {
                    buferRecepcion = inReader.readLine();
                    System.out.println(buferRecepcion);
                }
                
            } catch (IOException e) {
                System.err.println("Error de entrada/salida al abrir el socket.");
            }
        } while(!salir);
    }
}