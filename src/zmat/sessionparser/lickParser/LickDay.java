/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.lickParser;

import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author Xiaoxing
 */
public class LickDay extends zmat.sessionparser.Day<LickSession> {

    public LickDay(String fileName, Queue<LickSession> q) {
        super(fileName, q);
    }

    public ArrayList<Integer[][]> getLicks() {
        ArrayList<Integer[][]> allSessions = new ArrayList<>();
        for (LickSession s : sessions) {
            allSessions.addAll(s.getLicks());
        }
        return allSessions;
    }

    public ArrayList<Integer[][]> getLicks(boolean laserOn) {
        ArrayList<Integer[][]> allSessions = new ArrayList<>();
        for (LickSession s : sessions) {
            allSessions.addAll(s.getLicks(laserOn));
        }
        return allSessions;
    }

    public ArrayList<Float> getLickFreq(boolean laserOn) {
        ArrayList<Float> freqs = new ArrayList<>();
        for (LickSession s : sessions) {
            freqs.addAll(s.getLickFreq(laserOn));
        }
        return freqs;
    }
}
