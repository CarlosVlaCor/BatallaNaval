
package clienteh;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClienteH {

    static List<String> bloqueados = new ArrayList<>();
    static Set<String> solicitudes = new HashSet<>();
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        
        ParaRecibir paraRecibir = new ParaRecibir(socket);
        Thread hiloRecibir = new Thread(paraRecibir);
        hiloRecibir.start();
        
        ParaEnviar paraEnviar = new ParaEnviar(socket);
        Thread hiloEnviar = new Thread(paraEnviar);
        paraRecibir.setParaEnviar(paraEnviar);
        hiloEnviar.start();
    }
    
}
