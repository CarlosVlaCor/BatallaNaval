package servidorh;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorH {

    static Map<String, UnCliente> lista = new HashMap<String, UnCliente>();

    public static void main(String[] args) {
        ServerSocket socketServidor = null;
        try {
            socketServidor = new ServerSocket(8080);
        } catch (IOException ex) {
            System.out.println("Error de conexion con el puerto");
            System.exit(1);
        }
        int contador = 0;
        while (true) {
            try {
                Socket s = socketServidor.accept();
                UnCliente unCliente = new UnCliente(s);
                Thread hilo = new Thread(unCliente);
                hilo.start();
                contador++;
            } catch (IOException ex) {
                System.out.println("Error de conexion");
                System.exit(1);
            }
        }
    }

}
