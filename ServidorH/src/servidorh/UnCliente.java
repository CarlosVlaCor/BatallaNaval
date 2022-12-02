package servidorh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class UnCliente implements Runnable {

     final DataInputStream entrada;
    final DataOutputStream salida;
    private String nombre;

    public UnCliente(Socket s) throws IOException {
        entrada = new DataInputStream(s.getInputStream());
        salida = new DataOutputStream(s.getOutputStream());

    }


    @Override
    public void run() {
        String mensaje;
        recibirNombre();
        while (true) {
            try {
                mensaje = entrada.readUTF();
                if (arrobado(mensaje)) {
                    envioPersonal(mensaje);
                } else if(esBloquear(mensaje)){
                    accionBloquear(mensaje);
                } else if(esDesbloquear(mensaje)){
                    accionDesbloquear(mensaje);
                }else {
                    envioGeneral(mensaje);
                }
            } catch (IOException ex) {

            }
        }
    }
    
    private void recibirNombre(){
        try {
            String nombreRecibido = entrada.readUTF();
            UnCliente unCliente = ServidorH.lista.get(nombreRecibido);
            if(unCliente == null){
                nombre = nombreRecibido;
               ServidorH.lista.put(nombre, this);
            }else{
               salida.writeUTF("--nombre igual--");
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(UnCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean arrobado(String mensaje) {
        return mensaje.contains("@");
    }

    private void envioPersonal(String mensaje) throws IOException {
        String[] mensajesDividido = mensaje.split("@");
        String destino = mensajesDividido[1];
        if (!destino.equals(nombre)) {
            try {
                UnCliente cliente = ServidorH.lista.get(destino);
                cliente.salida.writeUTF(this.nombre + ": " + mensajesDividido[0]);
            } catch (NullPointerException ex) {
                salida.writeUTF("No se encontro un cliente con ese nombre");
            }
        } else {
            salida.writeUTF("No puedes mandarte mensaje a ti mismo");
        }

    }

    private void envioGeneral(String mensaje) throws IOException {
        for (UnCliente cliente : ServidorH.lista.values()) {
            if (!cliente.nombre.equals(nombre)) {
                cliente.salida.writeUTF(this.nombre + ": " + mensaje);
            }
        }
    }

    private boolean esBloquear(String mensaje) {
        return mensaje.contains("BLOQUEAR");
    }

    private void accionBloquear(String mensaje) throws IOException {
        String nombreABloquear[] = mensaje.split(" ");
        if(nombreABloquear[1].equalsIgnoreCase(nombre)){
            salida.writeUTF("BLOQUEAR MismoUsuario");
        }else{
            UnCliente unCliente = ServidorH.lista.get(nombreABloquear[1]);
            if(unCliente != null){
                salida.writeUTF(mensaje);
            }else{
                salida.writeUTF("BLOQUEAR NoExiste");
            }
        }
    }

    private boolean esDesbloquear(String mensaje) {
        return mensaje.contains("DESBLOQUEO");
    }

    private void accionDesbloquear(String mensaje) throws IOException {
        String nombreABloquear[] = mensaje.split(" ");
        if(nombreABloquear[1].equalsIgnoreCase(nombre)){
            salida.writeUTF("DESBLOQUEO MismoUsuario");
        }else{
            UnCliente unCliente = ServidorH.lista.get(nombreABloquear[1]);
            if(unCliente != null){
                salida.writeUTF(mensaje);
            }else{
                salida.writeUTF("DESBLOQUEO NoExiste");
            }
        }
    }


}
