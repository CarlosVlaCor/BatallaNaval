/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval.barcos;

import java.util.Set;

/**
 *
 * @author carlosvla
 */
public abstract class Barco {
    protected Set<String> posiciones;
    protected boolean hundido;
    protected boolean colocado;
    protected String nombre;
    protected int longitud;
    
    public Barco(Set<String> posiciones, boolean hundido, boolean colocado, String nombre, int longitud){
        this.posiciones = posiciones;
        this.hundido = hundido;
        this.colocado = colocado;
        this.nombre = nombre;
        this.longitud = longitud;
    }
    public void setColocado(boolean colocado){
        this.colocado = colocado;
    }
    public Set<String> getPosiciones() {
        return posiciones;
    }

    public boolean isHundido() {
        return hundido;
    }

    public boolean isColocado() {
        return colocado;
    }

    public String getNombre() {
        return nombre;
    }

    public int getLongitud() {
        return longitud;
    }
    
    
    
    
}
