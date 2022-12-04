/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import servidorh.UnCliente;

/**
 *
 * @author carlosvla
 */
public class BatallaNaval {

    private UnCliente unCliente;
    private static Map<String, UnCliente> lista;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Set<String> solicitudesDeJuego = new HashSet<>();

    public BatallaNaval(UnCliente unCliente, Map<String, UnCliente> lista) {
        this.unCliente = unCliente;
        entrada = unCliente.getEntrada();
        salida = unCliente.getSalida();
        this.lista = lista;

    }

    public void entornoDelJuego() throws IOException {
        salida.writeUTF("Entraste a la Batalla Naval");
        String mensaje;
        while (!(mensaje = entrada.readUTF()).equalsIgnoreCase("SALIR")) {
            if (mensaje.contains("ENVIAR SOLICITUD")) {
                enviarSolicitud(mensaje);
            } else if (mensaje.contains("SOLICITUDACEPTADA")) {
                aceptarSolicitud(mensaje);
            } else if (mensaje.contains("ACEPTADA")) {
                solicitudAceptada(mensaje);
            }
        }
    }

    private void enviarSolicitud(String mensaje) throws IOException {
        if (mensaje.contains("@")) {
            String mensajesDivididos[] = mensaje.split("@");
            String destino = mensajesDivididos[1];
            UnCliente clienteObtenido = lista.get(destino);
            if (clienteObtenido != null) {
                if (clienteObtenido.isEnEntorno() == true) {
                    clienteObtenido.getSalida().writeUTF("SOLICITUD-" + unCliente.getNombre());
                    unCliente.getSalida().writeUTF("Solicitud enviada");
                } else {
                    unCliente.getSalida().writeUTF("No se encuentra disponible para jugar");
                }
            } else {

            }
        } else {

        }
    }

    private void aceptarSolicitud(String mensaje) throws IOException {
        System.out.println("Entró");
        String mensajesDivididos[] = mensaje.split("-");
        String destino = mensajesDivididos[1];
        UnCliente clienteObtenido = lista.get(destino);
        if (clienteObtenido != null) {
            if (clienteObtenido.isEnEntorno() == true) {
                System.out.println("Entró");
                clienteObtenido.getSalida().writeUTF("ACEPTADA-" + unCliente.getNombre());
                Juego juego = new Juego(unCliente, lista);
                juego.jugar();
            } else {
                unCliente.getSalida().writeUTF("No se encuentra disponible para jugar");
            }
        } else {

        }

    }

    private void solicitudAceptada(String mensaje) throws IOException {
        String mensajesDivididos[] = mensaje.split("-");
        Juego juego = new Juego(unCliente, lista);
        juego.jugar();

    }
}
