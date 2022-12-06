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
    private boolean jugador1Listo;
    private boolean jugador2Listo;
    private boolean barcosElegidos;
    private boolean partidaTermindada = false;

    public Juego(UnCliente jugador1, UnCliente jugador2, Map<String, UnCliente> lista) {
        this.jugador1 = new Jugador(jugador1);
        this.jugador2 = new Jugador(jugador2);
        this.lista = lista;
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

    public void jugar() throws IOException {
        while (true) {

        }
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
                barcosElegidos = true;
            }
        } else {
            ColocarBarcos colocarBarcos = new ColocarBarcos();
            jugador2.getUnCliente().getSalida().writeUTF(mostrarTablero(jugador2));
            for (int i = 0; i < 4; i++) {
                colocarBarcos.colocar(jugador2, jugador2.getBarcos().get(i), this);
            }
            jugador2Listo = true;
            if (jugador1Listo == true) {
                barcosElegidos = true;
            }
        }
        while (barcosElegidos == false) {
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



    
}
