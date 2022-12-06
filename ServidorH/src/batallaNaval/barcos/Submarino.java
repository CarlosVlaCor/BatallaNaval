/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package batallaNaval.barcos;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author carlosvla
 */
public class Submarino extends Barco{
    
    public Submarino(){
        super(new HashSet<String>(), false, false, "Submarino", 3);
    }
}
