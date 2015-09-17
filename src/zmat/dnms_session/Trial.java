/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

import java.util.ArrayList;

/**
 *
 * @author Libra
 */
public class Trial {

    final protected EventType firstOdor;
    final protected EventType secondOdor;
    final protected EventType response;
    final protected boolean laserON;
    ArrayList<Integer[]> licks;
    int delayLength;
    int odor2Start;

    public Trial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, ArrayList<Integer[]> licks, int delayLength, int odor2Start) {
        this(firstOdor, secondOdor, response, laserON);
        this.licks = licks;
        this.delayLength = delayLength;
        this.odor2Start = odor2Start;
    }

    public Trial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        this.firstOdor = firstOdor;
        this.secondOdor = secondOdor;
        this.response = response;
        this.laserON = laserON;
    }

    public boolean isGoodChoice() {
        return response == EventType.Hit || response == EventType.CorrectRejection;
    }

    public boolean isMatch() {
        return firstOdor == secondOdor;
    }

    public EventType getResponse() {
        return response;
    }

    public EventType getFirstOdor() {
        return firstOdor;
    }

    public EventType getSecondOdor() {
        return secondOdor;
    }

    public boolean isDNMS() {
        if ((response == EventType.FalseAlarm || response == EventType.CorrectRejection) && isMatch()) {
            return true;
        } else {
            return (response == EventType.Hit || response == EventType.Miss) && !isMatch();
        }
    }

    public boolean withLaserON() {
        return laserON;
    }

    public Integer[][] getLicks() {
        return licks.toArray(new Integer[licks.size()][]);
    }

    public int[] getDelayLick() {
        if (null == licks) {
            return new int[]{0, 0, 0};
        }
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

    public int getResponseLick() {
        if (null == licks) {
            return 0;
        }
        int count = 0;
        for (Integer[] l : licks) {
            if (l[0] > odor2Start + 2500) {
                break;
            } else if (l[0] > odor2Start) {
                count++;
            }
        }
        return count;
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
