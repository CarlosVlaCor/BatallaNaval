/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package batallaNaval;

/**
 *
 * @author carlosvla
 */
public enum Letra {
     A(1),B(2),C(3),D(4),E(5),F(6),G(7),H(8),I(9),J(10);
   private int valor;
   
   Letra(int valor){this.valor = valor;}
   
   public int getValor(){return valor;}
}
