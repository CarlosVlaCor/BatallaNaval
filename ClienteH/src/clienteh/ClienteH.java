
package clienteh;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteH {

    static List<String> bloqueados = new ArrayList<>();
    static Set<String> solicitudes = new HashSet<>();
    public static void main(String[] args)  {
        Socket socket= null;
        try {
            socket = new Socket("localhost", 8080);
        } catch (IOException ex) {
            System.out.println("Error en la conexion");
            System.exit(1);
        }
        
        ParaRecibir paraRecibir = null;
        try {
            paraRecibir = new ParaRecibir(socket);
        } catch (IOException ex) {
            System.out.println("Error en la conexion");
            System.exit(1);
        }
        Thread hiloRecibir = new Thread(paraRecibir);
        hiloRecibir.start();
        
        ParaEnviar paraEnviar = null;
        try {
            paraEnviar = new ParaEnviar(socket);
        } catch (IOException ex) {
            System.out.println("Error en la conexion");
            System.exit(1);
        }
        Thread hiloEnviar = new Thread(paraEnviar);
        paraRecibir.setParaEnviar(paraEnviar);
        hiloEnviar.start();
    }
    
}
