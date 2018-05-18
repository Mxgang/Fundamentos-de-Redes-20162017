import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.DatagramSocket;
import java.net.Socket;

public class YodafyServidorIterativo {

    public static void main(String[] args) {
    
        // Puerto de escucha
        int port = 8989;
        // array de bytes auxiliar para recibir o enviar datos.
        byte []buffer = new byte[256];
        // Número de bytes leídos
        int bytesLeidos = 0;
        // Socket para el servidor
        DatagramSocket socketServer;
        Socket socketServicio = null;
        
        try {
            socketServer = new DatagramSocket(port);
            do {
                
                // Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
                // argumento el nuevo socket, para que realice el procesamiento
                // Este esquema permite que se puedan usar hebras más fácilmente.
                ProcesadorYodafy procesador = new ProcesadorYodafy(socketServer);
                procesador.procesa();
                
            } while (true);
            
        } catch (IOException e) {
            System.err.println("Error al escuchar en el puerto "+port);
        }

    }

}
