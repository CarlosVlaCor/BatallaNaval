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
import java.util.LinkedHashMap;
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
    
    private static Map<String,Integer> tablaDePuntuaciones = new HashMap<>();
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
            }else if(mensaje.equals("TABLA PUNTUACIONES")){
                tablaDePuntuaciones();
            }else if(mensaje.equals("PEOR ENEMIGO")){
                peorEnemigo();
                
            }else if(mensaje.equals("TORTA")){
                torta();
            }
        }
        salida.writeUTF("Saliste de la Batalla Naval");
        unCliente.setEstado("chat");
    }
    private void tablaDePuntuaciones() throws IOException{
        String mensaje="";
        for(Map.Entry entry : tablaDePuntuaciones.entrySet()){
            mensaje+= entry.getKey() +" "+  entry.getValue()+"pts"+"\n";
        }
        unCliente.getSalida().writeUTF(mensaje);
    }
    private void peorEnemigo() throws IOException{
        String mensaje = "";      
        Map<String,Integer> resultado = unCliente.getPerdidos().entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap::new));
        for(Map.Entry entry : resultado.entrySet()){
            mensaje+= "Tu peor enemigo "+ entry.getKey();
            break;
        }
        unCliente.getSalida().writeUTF(mensaje);
    }
    private void torta() throws IOException{
        String mensaje = "";      
        Map<String,Integer> resultado = unCliente.getGanados().entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap::new));
        for(Map.Entry entry : resultado.entrySet()){
            mensaje+= "Tu torta "+ entry.getKey();
            break;
        }
       unCliente.getSalida().writeUTF(mensaje);
        
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
                unCliente.getSalida().writeUTF("No se encontro el usuario");
            }
        } else {
            unCliente.getSalida().writeUTF("Es necesario utilizar el @");
        }
    }

    private void aceptarSolicitud(String mensaje) throws IOException {
        System.out.println("Entró");
        String mensajesDivididos[] = mensaje.split("-");
        String destino = mensajesDivididos[1];
        UnCliente clienteObtenido = lista.get(destino);
        if (clienteObtenido != null) {
            if (clienteObtenido.isEnEntorno() == true) {
                juegoActual = new Juego(unCliente, clienteObtenido, lista,tablaDePuntuaciones);
                tableros.add(juegoActual);
                clienteObtenido.getSalida().writeUTF("ACEPTADA-" + unCliente.getNombre());

                jugar();
            } else {
                unCliente.getSalida().writeUTF("El jugador no se encuentra disponible");
            }
        } else {
            unCliente.getSalida().writeUTF("No se encontró el usuario");
        }

    }
    private void mostrarTableros() throws IOException{
        String t="";
        List<Juego> comoJugador1 = tableros.stream()
                .filter((x) -> x.getJugador1().getUnCliente()
                        .getNombre().equals(unCliente.getNombre())).toList();
        List<Juego> comoJugador2 = tableros.stream().filter((x) -> x.getJugador2().getUnCliente().getNombre()
                                .equals(unCliente.getNombre())).toList();
      if(!comoJugador1.isEmpty()){
          for(Juego juego : comoJugador1){
              t+= juego.getJugador2().getUnCliente().getNombre()+"\n";
          }
      }else if(!comoJugador2.isEmpty()){
          for(Juego juego : comoJugador2){
             t+= juego.getJugador1().getUnCliente().getNombre()+"\n"; 
          }
      }
      unCliente.getSalida().writeUTF(t);
       
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
        if(juegoActual.isPartidaTermindada()){
            tableros.remove(juegoActual);
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
