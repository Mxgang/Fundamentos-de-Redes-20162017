import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class YodafyClienteUDP {

    public static void main(String[] args) {    
        byte[] buferEnvio;
        byte[] buferRecepcion = new byte[256];
        int bytesLeidos=0;
                
        // Nombre y puerto del host donde se ejecuta el servidor:
        String host = "localhost";
        int port = 8989;
                
        // Socket para la conexión UDP
        InetAddress direccion;
        DatagramSocket socket;
        DatagramPacket paquete;

        try {
            // Creamos un socket que se conecte a "host" y "port".
            socket = new DatagramSocket();
            direccion = InetAddress.getByName(host);

                        
            // Si queremos enviar una cadena de caracteres por un OutputStream, hay que pasarla primero
            // a un array de bytes:
            buferEnvio = "Al monte del volcán debes ir sin demora".getBytes();
                        
            // Enviamos el array por UDP
            paquete = new DatagramPacket(buferEnvio, buferEnvio.length, direccion, port);
            
            socket.send(paquete);
                        
            // Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que intentará
            // rellenar. El método "read(...)" devolverá el número de bytes leídos.
            paquete = new DatagramPacket(buferRecepcion, buferRecepcion.length);
            socket.receive(paquete);

            // Mostremos la cadena de caracteres recibidos:
            System.out.println("Recibido: " + new String(paquete.getData(), 0, paquete.getLength()));
            
            // Una vez terminado el servicio, cerramos el socket (automáticamente se cierran
            // el inputStream  y el outputStream)
            socket.close();
            
            // Excepciones:
        } catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }
    }
}
