/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author casel_000
 */
public class ZPID {

    public double[][] ser2mat(String s) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(s)))) {
            @SuppressWarnings("unchecked")
            LinkedList<LinkedList<Double>> eventList = (LinkedList<LinkedList<Double>>) ois.readObject();
            double[][] rtn = new double[eventList.get(0).size()][3];
            for (int i = 0; i < eventList.get(0).size(); i++) {
                rtn[i] = new double[]{
                    eventList.get(0).get(i),
                    eventList.get(1).get(i),
                    eventList.get(2).get(i)};
            }
            return rtn;
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(ex.toString());
        }
        return new double[0][0];
    }
}
