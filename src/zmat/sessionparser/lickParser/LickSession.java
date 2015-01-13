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
public class LickSession extends zmat.sessionparser.Session<LickTrial> {

    public LickSession(Queue<LickTrial> q) {
        super(q);
    }

    public ArrayList<Integer[][]> getLicks() {
        ArrayList<Integer[][]> licks = new ArrayList<>();
        for (LickTrial t : trials) {
            licks.add(t.getLicks());
        }
        return licks;
    }

    public ArrayList<Integer[][]> getLicks(boolean laserOn) {
        ArrayList<Integer[][]> licks = new ArrayList<>();
        for (LickTrial t : trials) {
            if (t.withLaserON() == laserOn) {
                licks.add(t.getLicks());
            }
        }
        return licks;
    }

    public ArrayList<Float> getLickFreq(boolean laserOn) {
        ArrayList<Float> freqs = new ArrayList<>();
        for (LickTrial t : trials) {
            if (t.withLaserON() == laserOn) {
                freqs.add(t.getDelayLickFreq());
            }
        }
        return freqs;
    }

}
