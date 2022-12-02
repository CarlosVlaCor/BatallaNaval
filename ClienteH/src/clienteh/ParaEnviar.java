package clienteh;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParaEnviar implements Runnable {

     final DataOutputStream salida;
    Scanner teclado;
    private final String nombre;
    public ParaEnviar(Socket socket) throws IOException {
        teclado = new Scanner(System.in);
        salida = new DataOutputStream(socket.getOutputStream());
        System.out.println("Escribe tu nombre");
        this.nombre = teclado.nextLine();
        salida.writeUTF(nombre);
        
    }

    @Override
    public void run() {
        
        while (true) {
            String mensaje = teclado.nextLine();
            try {
                 salida.writeUTF(mensaje);   
            } catch (IOException ex) {

            }
        }

    }
    public String getNombre(){
        return this.nombre;
    
    }
    
    public  void enviar(String mensaje){
         try {
             salida.writeUTF(mensaje);
         } catch (IOException ex) {
             Logger.getLogger(ParaEnviar.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    
}
