/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.lrparser;

import java.util.Queue;

/**
 *
 * @author Xiaoxing
 */
public class LRDay extends zmat.sessionparser.Day<LRSession> {

    public LRDay(String fileName, Queue<LRSession> q) {
        super(fileName, q);
    }

    public int[] getHitRate() {
        int[] rtn = new int[sessions.size()];
        int idx = 0;
        for (LRSession s : sessions) {
            rtn[idx++] = s.getHitRate();
        }
        return rtn;
    }

    public int[] getBias() {
        int[] rtn = new int[sessions.size()];
        int idx = 0;
        for (LRSession s : sessions) {
            rtn[idx++] = s.getBias();
        }
        return rtn;
    }

}
