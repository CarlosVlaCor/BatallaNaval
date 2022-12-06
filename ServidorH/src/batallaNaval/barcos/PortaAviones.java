/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval.barcos;

import java.util.HashSet;

/**
 *
 * @author carlosvla
 */
public class PortaAviones extends Barco{
     public PortaAviones(){
        super(new HashSet<String>(), false, false, "Portaaviones", 5);
    }
     
}
