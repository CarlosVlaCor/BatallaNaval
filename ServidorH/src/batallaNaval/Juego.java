/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval;

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
    private UnCliente unCliente;
     private static Map<String, UnCliente> lista;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private String[][] oceanoContrario;
    private String[][] miOceano;
    public Juego(UnCliente unCliente, Map<String, UnCliente> lista){
        this.unCliente = unCliente;
        entrada = unCliente.getEntrada();
        salida = unCliente.getSalida();
        this.lista = lista;
    }
    public void jugar() throws IOException{
        while(true){
            System.out.println(entrada.readUTF());
        }
    }
}
