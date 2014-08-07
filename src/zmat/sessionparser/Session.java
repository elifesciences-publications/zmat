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

    protected int[] getRate(RateType type) {
        int correctLaserOn = 0;
        int countLaserOn = 0;
        int correctLaserOff = 0;
        int countLaserOff = 0;
        for (T trial : trials) {
            if (trial.withLaserON()) {
                countLaserOn++;
                correctLaserOn += getTypeRate(trial, type);
            } else {
                countLaserOff++;
                correctLaserOff += getTypeRate(trial, type);
            }
        }

        return (new int[]{correctLaserOn / countLaserOn, correctLaserOff / countLaserOff});
    }

    public int[] getCorrectRate() {
        return getRate(RateType.performance);
    }

    public int[] getFalseAlarmRate() {
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

}
