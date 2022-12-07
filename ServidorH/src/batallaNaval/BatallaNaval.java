/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    private static List<Juego> tableros = new ArrayList<>();
    private Juego juegoActual;

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
            }else if(mensaje.contains("ENTRAR")){
                entrar(mensaje);
            }else if(mensaje.equalsIgnoreCase("MOSTRAR TABLEROS")){
                mostrarTableros();
            }
        }
        salida.writeUTF("Saliste de la Batalla Naval");
        unCliente.setEstado("chat");
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
                juegoActual = new Juego(unCliente, clienteObtenido, lista);
                tableros.add(juegoActual);
                clienteObtenido.getSalida().writeUTF("ACEPTADA-" + unCliente.getNombre());

                jugar();
            } else {
            }
        } else {

        }

    }
    private void mostrarTableros(){
        List<Juego> comoJugador1 = tableros.stream()
                .filter((x) -> x.getJugador1().getUnCliente()
                        .getNombre().equals(unCliente.getNombre())).toList();
        List<Juego> comoJugador2 = tableros.stream().filter((x) -> x.getJugador2().getUnCliente().getNombre()
                                .equals(unCliente.getNombre())).toList();
      if(!comoJugador1.isEmpty()){
           comoJugador1.stream().forEach(x -> System.out.println(x.getJugador2().getUnCliente().getNombre()));
      }else if(!comoJugador2.isEmpty()){
          comoJugador2.stream().forEach(x -> System.out.println(x.getJugador1().getUnCliente().getNombre()));
      }
       
    }

    private void solicitudAceptada(String mensaje) throws IOException {
        String mensajesDivididos[] = mensaje.split("-");

        List<Juego> juego = tableros.stream().filter((x)
                -> x.getJugador1().getUnCliente().getNombre().equals(mensajesDivididos[1])
                && x.getJugador2().getUnCliente().getNombre().equals(unCliente.getNombre()))
                .collect(Collectors.toList());

        juegoActual = juego.get(0);
        jugar();
    }

    private void jugar() throws IOException {
        while (juegoActual.isPartidaTermindada() == false) {
            
            if (juegoActual.isBarcosElegidos() == true) {
                juegoActual.jugar(unCliente.getNombre());
            } else {
                System.out.println("A Jugar");
                unCliente.setEstado("jugando");
                juegoActual.colocarBarcos(unCliente.getNombre());
            }
            if(unCliente.getEstado().equals("entorno")){
                break;
            }
        }
        unCliente.getSalida().writeUTF("Saliste de la partida");
    }

    private void entrar(String mensaje) throws IOException {
        String[] mensajesDivididos = mensaje.split("@");
        List<Juego> juego = tableros.stream().filter((x)
                -> x.getJugador1().getUnCliente().getNombre().equals(mensajesDivididos[1])
                && x.getJugador2().getUnCliente().getNombre().equals(unCliente.getNombre()))
                .collect(Collectors.toList());
        List<Juego>juego2 = tableros.stream().filter((x)
                -> x.getJugador2().getUnCliente().getNombre().equals(mensajesDivididos[1])
                && x.getJugador1().getUnCliente().getNombre().equals(unCliente.getNombre()))
                .collect(Collectors.toList());
        if(!juego.isEmpty()){
            juegoActual = juego.get(0);
            unCliente.setEstado("jugando");
            jugar();
        }else if(!juego2.isEmpty()){
             juegoActual = juego2.get(0);
             unCliente.setEstado("jugando");
            jugar();
        }
        
        
    }
}
