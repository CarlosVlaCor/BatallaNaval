package servidorh;

import batallaNaval.BatallaNaval;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnCliente implements Runnable {

    final DataInputStream entrada;
    private String estado = "chat";
    final DataOutputStream salida;
    private String nombre;
    private static Set<String> usuariosEnEntorno = new HashSet<>();
    private boolean enEntorno = false;
    private Map<String, String> tableros = new HashMap<>();

    public UnCliente(Socket s) throws IOException {
        entrada = new DataInputStream(s.getInputStream());
        salida = new DataOutputStream(s.getOutputStream());

    }

    public String getNombre() {
        return nombre;
    }

    public DataInputStream getEntrada() {
        return entrada;
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    public boolean isEnEntorno() {
        return enEntorno;
    }
    public void setEstado(String estado){
        this.estado = estado;
    }
    public String getEstado(){
        return estado;
    }

    @Override
    public void run() {
        String mensaje;
        recibirNombre();
        while (true) {
            try {
                mensaje = entrada.readUTF();
                if (esSolicitud(mensaje)) {
                    enviarSolicitud(mensaje);
                } else if (arrobado(mensaje)) {
                    envioPersonal(mensaje);
                } else if (esBloquear(mensaje)) {
                    accionBloquear(mensaje);
                } else if (esDesbloquear(mensaje)) {
                    accionDesbloquear(mensaje);
                } else if (mensaje.contains("SOLICITUDACEPTADA")) {
                    solicitudAceptada(mensaje);
                } else if (mensaje.equals("BATALLA NAVAL")) {
                    iniciarEntorno();
                } else {
                    envioGeneral(mensaje);
                }
            } catch (IOException ex) {

            }
        }
    }

    private void recibirNombre() {
        try {
            String nombreRecibido = entrada.readUTF();
            UnCliente unCliente = ServidorH.lista.get(nombreRecibido);
            if (unCliente == null) {
                nombre = nombreRecibido;
                ServidorH.lista.put(nombre, this);
            } else {
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
            if (!cliente.nombre.equals(nombre) && cliente.isEnEntorno() == false) {
                cliente.salida.writeUTF(this.nombre + ": " + mensaje);
            }
        }
    }

    private boolean esBloquear(String mensaje) {
        return mensaje.contains("BLOQUEAR");
    }

    private void accionBloquear(String mensaje) throws IOException {
        String nombreABloquear[] = mensaje.split(" ");
        if (nombreABloquear[1].equalsIgnoreCase(nombre)) {
            salida.writeUTF("BLOQUEAR MismoUsuario");
        } else {
            UnCliente unCliente = ServidorH.lista.get(nombreABloquear[1]);
            if (unCliente != null) {
                salida.writeUTF(mensaje);
            } else {
                salida.writeUTF("BLOQUEAR NoExiste");
            }
        }
    }

    private boolean esDesbloquear(String mensaje) {
        return mensaje.contains("DESBLOQUEO");
    }

    private void accionDesbloquear(String mensaje) throws IOException {
        String nombreABloquear[] = mensaje.split(" ");
        if (nombreABloquear[1].equalsIgnoreCase(nombre)) {
            salida.writeUTF("DESBLOQUEO MismoUsuario");
        } else {
            UnCliente unCliente = ServidorH.lista.get(nombreABloquear[1]);
            if (unCliente != null) {
                salida.writeUTF(mensaje);
            } else {
                salida.writeUTF("DESBLOQUEO NoExiste");
            }
        }
    }

    private boolean esSolicitud(String mensaje) {
        return mensaje.contains("SOLICITUD");
    }

    private void enviarSolicitud(String mensaje) throws IOException {
        if (arrobado(mensaje)) {
            String[] mensajesDividido = mensaje.split("@");
            UnCliente unCliente = ServidorH.lista.get(mensajesDividido[1]);
            if (unCliente != null) {
                unCliente.salida.writeUTF("SOLICITUD " + nombre);
                salida.writeUTF("Solicitud enviada");
            } else {
                salida.writeUTF("No existe ese usuario");
            }
        } else {
            salida.writeUTF("Es necesario especificar el destinor");
        }
    }

    private void solicitudAceptada(String mensaje) {
        String[] mensajesDivididos = mensaje.split("-");
        tableros.put(mensajesDivididos[1], nombre);
        UnCliente unCliente = ServidorH.lista.get(mensajesDivididos[1]);
        unCliente.tableros.put(nombre, mensajesDivididos[1]);

    }

    private void iniciarEntorno() throws IOException {
        enEntorno = true;
        estado = "entorno";
        BatallaNaval batallaNaval = new BatallaNaval(this, ServidorH.lista);
        batallaNaval.entornoDelJuego();
        enEntorno = false;
    }

}
