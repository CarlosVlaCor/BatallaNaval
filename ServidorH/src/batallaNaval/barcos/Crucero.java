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
public class Crucero extends Barco{
    public Crucero(){
        super(new HashSet<String>(), false, false, "Crucero",2);
    }  
}
