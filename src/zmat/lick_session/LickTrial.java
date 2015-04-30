/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.lick_session;

import java.util.ArrayList;
import zmat.dnms_session.EventType;

/**
 *
 * @author Xiaoxing
 */
public class LickTrial extends zmat.dnms_session.Trial {

    ArrayList<Integer[]> licks;
    int delayLength;

    public LickTrial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, ArrayList<Integer[]> licks, int delayLength) {
        super(firstOdor, secondOdor, response, laserON);
        this.licks = licks;
        this.delayLength = delayLength;
    }

    public Integer[][] getLicks() {
        return licks.toArray(new Integer[licks.size()][]);
    }

    public int[] getDelayLick() {
        int count = 0;
        for (Integer[] l : licks) {
            if (l[0] > 1000) {
                count++;
            } else if (l[0] > delayLength + 1000) {
                break;
            }
        }
        return new int[]{count, laserON ? 1 : 0};
    }

//    public float getDelayLickFreq() {
//        return (float) getDelayLick()[0] * 1000 / delayLength;
//    }
}
