/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval;

import batallaNaval.barcos.Barco;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import servidorh.UnCliente;

/**
 *
 * @author carlosvla
 */
public class Juego {

    private static Map<String, UnCliente> lista;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean jugador1Listo = false;
    private boolean jugador2Listo = false;
    private boolean barcosElegidos = false;
    private boolean partidaTermindada = false;
    private boolean iniciada;
    private String jugadorEnturno;
    private String inicia;
    private boolean finalizada = false;
    private boolean empate;
    private String ganador;
    private boolean ultimaOportunidad = false;

    public Juego(UnCliente jugador1, UnCliente jugador2, Map<String, UnCliente> lista) {
        this.jugador1 = new Jugador(jugador1);
        this.jugador2 = new Jugador(jugador2);
        this.lista = lista;
        iniciada = false;
    }

    public boolean isBarcosElegidos() {
        return barcosElegidos;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public boolean isPartidaTermindada() {
        return partidaTermindada;
    }

    public void jugar(String nombre) throws IOException {

        if (nombre.equals(jugador1.getUnCliente().getNombre())) {
            System.out.println("A");
            boolean salir = false;
            while (salir != true || partidaTermindada != true) {
                if (jugadorEnturno.equals(nombre)) {
                    salir = atacar(jugador1, jugador2);
                } else {
                    salir = esperar(jugador1);
                }
            }

        } else {
            System.out.println("B");
            boolean salir = false;
            while (salir != true || partidaTermindada != true) {
                if (jugadorEnturno.equals(nombre)) {
                    salir = atacar(jugador2, jugador1);
                } else {
                    salir = esperar(jugador2);
                }
            }
        }
        if (partidaTermindada == true) {
            System.out.println("Ganador: " + ganador);
        }

    }

    private void darTurnos() {
        Random random = new Random();
        int r = random.nextInt(2);
        if (r == 0) {
            jugadorEnturno = jugador1.getUnCliente().getNombre();

        } else {
            jugadorEnturno = jugador2.getUnCliente().getNombre();
        }
        inicia = jugadorEnturno;
    }

    private boolean atacar(Jugador atacante, Jugador recibe) throws IOException {
        atacante.getUnCliente().getSalida().writeUTF("Es tu turno de atacar");
        recibe.getUnCliente().getSalida().writeUTF("Es turno de " + atacante.getUnCliente().getNombre());
        boolean seguir = false;
        while (seguir != true) {
            atacante.getUnCliente().getSalida().writeUTF("Coloca la coordenada de ataque");

            String coordenada = atacante.getUnCliente().getEntrada().readUTF();
            System.out.println(coordenada);
            if (coordenadaCorrecta(coordenada, recibe.getTablero())) {
                seguir = disparar(atacante, recibe, coordenada);

            }
            if (partidaTermindada == true) {
                seguir = true;
            }
        }
        jugadorEnturno = recibe.getUnCliente().getNombre();
        recibe.getUnCliente().getSalida().writeUTF("TUTURNO");
        return partidaTermindada == true;
    }

    private boolean disparar(Jugador atacante, Jugador recibe, String coordenada) throws IOException {
        String letra = coordenada.substring(0, 1);
        String numero = coordenada.substring(1, coordenada.length());
        int valorLetra = Letra.valueOf(letra).getValor();
        int valorNumero = Integer.parseInt(numero);
        String tablero[][] = recibe.getTablero();
        if (tablero[valorLetra][valorNumero].equals("*")) {
            for (Barco barco : recibe.getBarcos()) {
                if (barco.getPosiciones().contains(coordenada + " ")) {
                    System.out.println("AAAAAAA");
                    barco.getPosiciones().remove(coordenada + " ");
                    break;
                }

            }

            tablero[valorLetra][valorNumero] = "O";
            atacante.getUnCliente().getSalida().writeUTF(mostrarTableroAtacante(atacante, recibe));
            atacante.getUnCliente().getSalida().writeUTF("A Jijuesushingadamadre le diste");
            recibe.getUnCliente().getSalida().writeUTF("A jijuesushingadamadre me diste");
            return comprobarGanador(atacante, recibe);

        } else {
            tablero[valorLetra][valorNumero] = "X";
            atacante.getUnCliente().getSalida().writeUTF("Al agua padre");
            recibe.getUnCliente().getSalida().writeUTF("Al agua padre");
            return comprobarGanador(atacante, recibe);
        }
    }

    private boolean esperar(Jugador espera) throws IOException {
        String m = "";
        while (!(m.equals("TUTURNO"))) {
            m = espera.getUnCliente().getEntrada().readUTF();
        }
        if (partidaTermindada == true) {
            return true;
        }
        return m.equals("SALIR");
    }

    private boolean coordenadaCorrecta(String coordenada, String[][] tablero) {
        String letra = coordenada.substring(0, 1);
        String numero = coordenada.substring(1, coordenada.length());
        int valorNumero;
        Letra l = Letra.valueOf(letra);
        if (l == null) {
            return false;
        }
        try {
            valorNumero = Integer.parseInt(numero);
        } catch (NumberFormatException ex) {
            return false;
        }
        if (valorNumero < 0 || valorNumero > 10) {
            return false;
        }

        if (tablero[l.getValor()][valorNumero].equals("X") || tablero[l.getValor()][valorNumero].equals("O")) {
            System.out.println("Ya se ha tirado aqui");
            return false;
        }
        return true;
    }

    public void colocarBarcos(String nombre) throws IOException {

        if (jugador1.getUnCliente().getNombre().equals(nombre)) {
            ColocarBarcos colocarBarcos = new ColocarBarcos();
            jugador1.getUnCliente().getSalida().writeUTF(mostrarTablero(jugador1));
            for (int i = 0; i < 4; i++) {
                colocarBarcos.colocar(jugador1, jugador1.getBarcos().get(i), this);
            }
            jugador1Listo = true;
            if (jugador2Listo == true) {
                darTurnos();
                barcosElegidos = true;
                jugador2.getUnCliente().getSalida().writeUTF("-BARCOELEGIDO");

            } else {
                String m;
                while (!(m = jugador1.getUnCliente().getEntrada().readUTF()).equals("-BARCOELEGIDO")) {
                    System.out.println("jugador 1" + m);
                }
            }
        } else {
            ColocarBarcos colocarBarcos = new ColocarBarcos();
            jugador2.getUnCliente().getSalida().writeUTF(mostrarTablero(jugador2));
            for (int i = 0; i < 4; i++) {
                colocarBarcos.colocar(jugador2, jugador2.getBarcos().get(i), this);
            }
            jugador2Listo = true;
            if (jugador1Listo == true) {

                darTurnos();
                barcosElegidos = true;
                jugador1.getUnCliente().getSalida().writeUTF("-BARCOELEGIDO");

            } else {
                String m = "";
                while (!(m = jugador2.getUnCliente().getEntrada().readUTF()).equals("-BARCOELEGIDO")) {
                    System.out.println("jugador 2" + m);
                }
            }
        }
    }

    public String mostrarTablero(Jugador jugador) {
        String tablero[][] = jugador.getTablero();
        String mostrar = "";
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (tablero[i][j].length() < 3) {
                    mostrar += tablero[i][j] + " ";
                } else {
                    mostrar += " " + " ";
                }
            }
            mostrar += "\n";
        }
        return mostrar;
    }

    private String mostrarTableroAtacante(Jugador atacante, Jugador recibe) {
        String tableroAtacante[][] = atacante.getTablero();
        String tableroRecibeNormal[][] = recibe.getTablero();
        String mostrar = "";
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {

                if (tableroAtacante[i][j].equals("N")) {
                    mostrar += " " + " ";
                } else if (tableroAtacante[i][j].length() < 3) {
                    mostrar += tableroAtacante[i][j] + " ";
                } else {
                    mostrar += " " + " ";
                }
            }
            mostrar += "        ";
            for (int k = 0; k < 11; k++) {
                if (tableroRecibeNormal[i][k].equals("N") || tableroRecibeNormal[i][k].equals("*")) {
                    mostrar += " " + " ";
                } else if (tableroRecibeNormal[i][k].length() < 3) {
                    mostrar += tableroRecibeNormal[i][k] + " ";
                } else {
                    mostrar += " " + " ";
                }
            }
            mostrar += "\n";
        }
        return mostrar;
    }


    private boolean comprobarGanador(Jugador atacante, Jugador recibe) {
        boolean finalizar = true;
        for (Barco barco : recibe.getBarcos()) {
            if (!barco.getPosiciones().isEmpty()) {
                finalizar = false;
            }   
        }
        if (ultimaOportunidad == true) {
            if (finalizar == true) {
                ganador = "Empate";
            }else{
                finalizar = true;
            }
            partidaTermindada = true;
        } else {
            if (finalizar == true) {
                jugadorEnturno = recibe.getUnCliente().getNombre();
                if (!jugadorEnturno.equals(inicia)) {
                    ultimaOportunidad = true;
                } else {
                    partidaTermindada = true;
                }
                ganador = atacante.getUnCliente().getNombre();
            }
        }
        return finalizar;
    }

    private void comprobarEmpate(Jugador atacante, Jugador recibe) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
