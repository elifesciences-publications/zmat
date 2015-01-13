/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;

import java.util.Queue;

/**
 *
 * @author Libra
 */
public class Session {

    private Trial[] trails;

    Session(Queue<Trial> q) {
        trails = new Trial[q.size()];
        for (int i = 0; i < trails.length; i++) {
            trails[i] = q.poll();
        }
    }

    public void setTrials(Queue<Trial> q) {
        trails = new Trial[q.size()];
        for (int i = 0; i < trails.length; i++) {
            trails[i] = q.poll();
        }
    }

    public int getTrialNumber() {
        return trails.length;
    }

    private int getTypeRate(Trial trial, RateType type) {
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

    private int[] getRate(RateType type) {
        int[][] count = new int[5][2];
        for (Trial trial : trails) {
            if (trial.withLaserON()) {
                count[trial.getLaserType()][1] += getTypeBaseRate(trial, type);
                count[trial.getLaserType()][0] += getTypeRate(trial, type);
            } else {
                count[0][1] += getTypeBaseRate(trial, type);;
                count[0][0] += getTypeRate(trial, type);
            }
        }
        int[] result = new int[5];
        for (int i = 0; i < 5; i++) {
            result[i] = count[i][1] > 0 ? count[i][0] / count[i][1] : -1;
        }

        return result;
    }

    private int getTypeBaseRate(Trial trial, RateType type) {
        switch (type) {
            case performance:
                return 1;
            case falseAlarm:
                return (trial.response == EventType.FalseAlarm || trial.response == EventType.CorrectRejection) ? 1 : 0;
            case miss:
                return (trial.response == EventType.Hit || trial.response == EventType.Miss) ? 1 : 0;
        }
        return 0;
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
        for (Trial trail : trails) {
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
        return trails[0].isDNMS();
    }

    private enum RateType {

        performance, falseAlarm, miss;
    }
}
