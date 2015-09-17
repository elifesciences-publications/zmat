/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

/**
 *
 * @author Libra
 */
public class debugger {
    static public int level=100;
    static public void log(int level, String log){
        if (level>debugger.level){
            System.out.println(log);
        }
    }
}
