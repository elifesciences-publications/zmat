/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Libra
 * @param <T>
 */
public class Session<T extends Trial> {

    protected Queue<T> trials;

    public Session(Queue<T> q) {
        trials = new LinkedList<>();
        trials.addAll(q);
    }

    public int getTrialNumber() {
        return trials.size();
    }

    protected int getTypeRate(T trial, RateType type) {
        switch (type) {
            case performance:
                return trial.isGoodChoice() ? 100 : 0;
            case falseAlarm:
                return (trial.getResponse() == EventType.FalseAlarm) ? 100 : 0;
            case miss:
                return (trial.getResponse() == EventType.Miss) ? 100 : 0;
        }
        return 0;
    }

    protected int getTypeBaseRate(T trial, RateType type) {
        switch (type) {
            case performance:
                return 1;
            case falseAlarm:
                return (trial.response==EventType.FalseAlarm || trial.response==EventType.CorrectRejection) ? 1 : 0;
            case miss:
                return (trial.response==EventType.Hit || trial.response==EventType.Miss) ? 1 : 0;
        }
        return 0;
    }

    protected int[] getRate(RateType type) {
        int correctLaserOn = 0;
        int countLaserOn = 0;
        int correctLaserOff = 0;
        int countLaserOff = 0;
        for (T trial : trials) {
            if (trial.withLaserON()) {
                countLaserOn+=getTypeBaseRate(trial,type);
                correctLaserOn += getTypeRate(trial, type);
            } else {
                countLaserOff+=getTypeBaseRate(trial,type);
                correctLaserOff += getTypeRate(trial, type);
            }
        }
        if (countLaserOff == 0) {
            return (new int[]{correctLaserOn / countLaserOn, -1});
        } else if (countLaserOn == 0) {
            return (new int[]{-1, correctLaserOff / countLaserOff});
        }
        return (new int[]{correctLaserOn / countLaserOn, correctLaserOff / countLaserOff});
    }

    public int[] getCorrectRate() {
        return getRate(RateType.performance);
    }

    public int[] getFalseAlarmRate() {
//        System.out.println(Arrays.toString(getRate(RateType.falseAlarm)));
        return getRate(RateType.falseAlarm);
    }

    public int[] getMissRate() {
        return getRate(RateType.miss);
    }

    public int getLickCount() {
        int lickCount = 0;
        for (T trail : trials) {
            switch (trail.getResponse()) {
                case Hit:
                    lickCount++;
                    break;
                case FalseAlarm:
                    lickCount++;
                    break;
                default:
            }
        }

        return lickCount;
    }

    public boolean isDNMS() {
        return trials.peek().isDNMS();
    }

    protected enum RateType {

        performance, falseAlarm, miss;
    }

    public Queue<T> getTrails() {
        return trials;
    }

    public int[] getHitNFalse() {
        int hit = 0;
        int fa = 0;
        for (Trial t : trials) {
            switch (t.response) {
                case Hit:
                    hit++;
                    break;
                case FalseAlarm:
                    fa++;
                    break;
            }
        }
        return new int[]{hit, fa};
    }

}
