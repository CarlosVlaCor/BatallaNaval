/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval;

import batallaNaval.barcos.Barco;
import java.io.IOException;

/**
 *
 * @author carlosvla
 */
public class ColocarBarcos {
    
    public void colocar(Jugador jugador, Barco barco,Juego juego) throws IOException {
        
        while (!barco.isColocado()) {
            jugador.getUnCliente().getSalida().writeUTF("Coloca las coordenadas de tu " + barco.getNombre() + ", tamaño: " + barco.getLongitud() + " casillas");
            String coordenadas = jugador.getUnCliente().getEntrada().readUTF();
            if (verificarCoordenadas(coordenadas, barco, jugador.getTablero())) {
                jugador.getUnCliente().getSalida().writeUTF(juego.mostrarTablero(jugador));
            }

        }
    }
    private boolean verificarCoordenadas(String coordenadas, Barco barco, String tablero[][]) {
        boolean colocado = false;
        if(!coordenadas.contains(",")){
            
            return false;
        }
        String[] coordenadasDivididas = coordenadas.split(",");
        String letra1 = coordenadasDivididas[0].substring(0, 1);
        String numero1 = coordenadasDivididas[0].substring(1, coordenadasDivididas[0].length());
        String letra2 = coordenadasDivididas[1].substring(0, 1);
        String numero2 = coordenadasDivididas[1].substring(1, coordenadasDivididas[0].length());
        try {
            int num1 = Integer.parseInt(numero1);
            int num2 = Integer.parseInt(numero2);
            if (coordenadasValidas(letra1, letra2, num1, num2)) {
                if (letra1.equals(letra2)) {
                    int diferencia;
                    if (num1 > num2) {
                        diferencia = num1 - (num2 - 1);
                    } else {
                        diferencia = (num1 - 1) - num2;
                        if (diferencia < 0) {
                            diferencia *= -1;

                        }
                    }
                    if (diferencia == barco.getLongitud()) {
                        return colocarHorizontal(Letra.valueOf(letra1).getValor(), num1, num2, barco, tablero);
                    } else {
                    }

                } else if (numero1.equals(numero2)) {
                    Letra coor = Letra.valueOf(letra1);
                    Letra coor2 = Letra.valueOf(letra2);
                    int diferencia;
                    if (coor.getValor() > coor2.getValor()) {
                        diferencia = coor.getValor() - ((coor2.getValor()) - 1);
                    } else {
                        diferencia = (coor.getValor() - 1) - coor2.getValor();
                        if (diferencia < 0) {
                            diferencia *= -1;

                        }
                    }
                    if (diferencia == barco.getLongitud()) {
                        return colocarVertical(Integer.parseInt(numero1), coor.getValor(), coor2.getValor(), barco, tablero);
                    } else {

                    }
                } else {
                    colocado = false;
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Formato de coordenadas incorrecto");
            colocado = false;
        }
        return colocado;
    }
    
    private boolean coordenadasValidas(String letra1, String letra2, int num1, int num2) {
        if (num1 < 1 || num1 > 10) {
            return false;
        }
        if (num2 < 1 || num2 > 10) {
            return false;
        }
        if (Letra.valueOf(letra1) == null) {
            return false;
        }
        return Letra.valueOf(letra2) != null;
    }
    private boolean colocarHorizontal(int valorLetra, int primerValor, int segundoValor, Barco barco, String tablero[][]) {
        if (primerValor < segundoValor) {
            int primerValorProv = primerValor;

            if (verificarColocacionHorizontal(valorLetra, primerValor, segundoValor, tablero) == false) {
                return false;
            }
            for (; primerValor <= segundoValor; primerValor++) {
                if (!((valorLetra - 1) < 1)) {
                    tablero[valorLetra - 1][primerValor] = "N";
                }
                if (!((valorLetra + 1) > 10)) {
                    tablero[valorLetra + 1][primerValor] = "N";
                }

                barco.getPosiciones().add(tablero[valorLetra][primerValor]);
                tablero[valorLetra][primerValor] = "*";
            }
            if (!((primerValorProv - 1) < 1)) {

                tablero[valorLetra][primerValorProv - 1] = "N";
                if (!((valorLetra - 1) < 1)) {
                    tablero[valorLetra - 1][primerValorProv - 1] = "N";
                }
                if (!((valorLetra + 1) > 10)) {
                    tablero[valorLetra + 1][primerValorProv - 1] = "N";
                }
            }
            if (!((segundoValor + 1) > 10)) {
                tablero[valorLetra][segundoValor + 1] = "N";
                if (!((valorLetra - 1) < 1)) {
                    tablero[valorLetra - 1][segundoValor + 1] = "N";
                }
                if (!((valorLetra + 1) > 10)) {
                    tablero[valorLetra + 1][segundoValor + 1] = "N";
                }
            }

        } else {
            int segundoValorProv = segundoValor;
            if (verificarColocacionHorizontal(valorLetra, segundoValor, primerValor, tablero) == false) {
                return false;
            }
            for (; segundoValor <= primerValor; segundoValor++) {
                if (!((valorLetra - 1) < 1)) {
                    tablero[valorLetra - 1][segundoValor] = "N";
                }
                if (!((valorLetra + 1) > 10)) {
                    tablero[valorLetra + 1][segundoValor] = "N";
                }
                barco.getPosiciones().add(tablero[valorLetra][segundoValor]);
                tablero[valorLetra][segundoValor] = "*";
            }
            if (!((segundoValorProv - 1) < 1)) {

                tablero[valorLetra][segundoValorProv - 1] = "N";
                if (!((valorLetra - 1) < 1)) {
                    tablero[valorLetra - 1][segundoValorProv - 1] = "N";
                }
                if (!((valorLetra + 1) > 10)) {
                    tablero[valorLetra + 1][segundoValorProv - 1] = "N";
                }
            }
            if (!((primerValor + 1) > 10)) {
                tablero[valorLetra][primerValor + 1] = "N";
                if (!((valorLetra - 1) < 1)) {
                    tablero[valorLetra - 1][primerValor + 1] = "N";
                }
                if (!((valorLetra + 1) > 10)) {
                    tablero[valorLetra + 1][primerValor + 1] = "N";
                }
            }

        }
        if (barco.getPosiciones().size() == barco.getLongitud()) {
            barco.setColocado(true);
            return true;
        }
        return false;
    }

    private boolean colocarVertical(int numero, int letra, int letra2, Barco barco, String tablero[][]) {
        if (letra < letra2) {
            if(verificarColocacionVertical(numero, letra, letra2, tablero)==false){return false;}
            int letraProv = letra;
            for (; letra <= letra2; letra++) {
                if (!((numero - 1) < 1)) {
                    tablero[letra][numero - 1] = "N";
                }
                if (!((numero + 1) > 10)) {
                    tablero[letra][numero + 1] = "N";
                }

                barco.getPosiciones().add(tablero[letra][numero]);
                tablero[letra][numero] = "*";

            }
            if (!((letraProv - 1) < 1)) {

                tablero[letraProv - 1][numero] = "N";
                if (!((numero - 1) < 1)) {
                    tablero[letraProv - 1][numero - 1] = "N";
                }
                if (!((numero + 1) > 10)) {
                    tablero[letraProv - 1][numero + 1] = "N";
                }
            }
            if (!((letra2 + 1) > 10)) {
                tablero[letra2 + 1][numero] = "N";
                if (!((numero - 1) < 1)) {
                    tablero[letra2 + 1][numero - 1] = "N";
                }
                if (!((numero + 1) > 10)) {
                    tablero[letra2 + 1][numero + 1] = "N";
                }
            }
        } else {
            int letra2Prov = letra2;
            if(verificarColocacionVertical(numero, letra2, letra, tablero)==false){return false;}
            for (; letra2 <= letra; letra2++) {
                if (!((numero - 1) < 1)) {
                    tablero[letra2][numero - 1] = "N";
                }
                if (!((numero + 1) > 10)) {
                    tablero[letra2][numero + 1] = "N";
                }
                barco.getPosiciones().add(tablero[letra2][numero]);
                tablero[letra2][numero] = "*";
            }
            if (!((letra2Prov - 1) < 1)) {

                tablero[letra2Prov - 1][numero] = "N";
                if (!((numero - 1) < 1)) {
                    tablero[letra2Prov - 1][numero - 1] = "N";
                }
                if (!((numero + 1) > 10)) {
                    tablero[letra2Prov - 1][numero + 1] = "N";
                }
            }
            if (!((letra + 1) > 10)) {
                tablero[letra + 1][numero] = "N";
                if (!((numero - 1) < 1)) {
                    tablero[letra + 1][numero - 1] = "N";
                }
                if (!((numero + 1) > 10)) {
                    tablero[letra + 1][numero + 1] = "N";
                }
            }
        }
        if (barco.getPosiciones().size() == barco.getLongitud()) {
            barco.setColocado(true);
            return true;
        }
        return false;
    }
       private boolean verificarColocacionHorizontal(int valorLetra, int primerValor, int segundoValor, String[][] tablero) {
        for (; primerValor <= segundoValor; primerValor++) {
            if (tablero[valorLetra][primerValor].equals("*")) {
                System.out.println("No se puede colocar sobre otro barco");
                return false;
            } else if (tablero[valorLetra][primerValor].equals("N")) {
                System.out.println("No se puede colocar tan cerca los barcos");
                return false;
            }
        }
        return true;
    }

    private boolean verificarColocacionVertical(int numero, int letra, int letra2, String tablero[][]) {
        for (; letra <= letra2; letra++) {
            if (tablero[letra][numero].equals("*")) {

                System.out.println("No se puede colocar sobre otro barco");
                return false;
            }else if (tablero[letra][numero].equals("N")) {
                System.out.println("No se puede colocar tan cerca los barcos");
                return false;
            }
        }
        return true;
    }

}
