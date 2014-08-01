/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import java.util.Queue;
import zmat.sessionparser.Session;
import zmat.sessionparser.Trial;

/**
 *
 * @author Xiaoxing
 */
public class CySession extends zmat.sessionparser.Session {

    protected CySession(Queue<CyTrial> q) {
        super(q);
    }

    @Override
    protected int[] getRate(Session.RateType type) {
        int[] match = new int[13];
        int[] sum = new int[13];
        for (Trial trial : trials) {
            int typeIndex = ((CyTrial) trial).getTypeIndex();
            sum[typeIndex]++;
            match[typeIndex] += getTypeRate(trial, type);
        }
        System.out.println(Integer.toString(sum[0]) + " trials");
        int[] ratio = new int[12];
        for (int i = 0; i < 12; i++) {
            ratio[i] = match[i] / sum[i];
        }
        return ratio;
    }
}
