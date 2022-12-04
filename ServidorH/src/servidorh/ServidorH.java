
package servidorh;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServidorH {
static Map<String,UnCliente> lista = new HashMap<String,UnCliente>();
    
    public static void main(String[] args) throws IOException {
        ServerSocket socketServidor = new ServerSocket(8080);
        int contador = 0;
        while (true){
            Socket s = socketServidor.accept();
            UnCliente unCliente = new UnCliente(s);
            Thread hilo = new Thread(unCliente);
            hilo.start();
            contador++;
        }
    }
    
}
