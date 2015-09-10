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
    int odor2Start;

    public LickTrial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, ArrayList<Integer[]> licks, int delayLength, int odor2Start) {
        super(firstOdor, secondOdor, response, laserON);
        this.licks = licks;
        this.delayLength = delayLength;
        this.odor2Start = odor2Start;
    }

    public Integer[][] getLicks() {
        return licks.toArray(new Integer[licks.size()][]);
    }

    public int[] getDelayLick() {
        int count = 0;
        for (Integer[] l : licks) {
            if (l[0] > odor2Start - delayLength) {
                count++;
            } else if (l[0] > odor2Start) {
                break;
            }
        }
        return new int[]{count, firstOdor == EventType.OdorA ? 1 : 0, laserON ? 1 : 0};
    }

    public int[] getAllLick() {
        int[] all = new int[licks.size()];
        for (int i = 0; i < licks.size(); i++) {
            all[i] = licks.get(i)[0] - odor2Start;
        }
        return all;
    }

    public ArrayList<Integer[]> getLickQueue() {
        return licks;
    }

    public int getDelayLength() {
        return delayLength;
    }

    public int getOdor2Start() {
        return odor2Start;
    }

}
