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
    protected ArrayList<Integer[]> licks;
    private int delayLength;
    private int testOnsetTS;
    private int samplePort;
    private int testPort;
    private int trialLaserType;
    private int laserOnset;
    private int laserOffset;

    public Trial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, ArrayList<Integer[]> licks, int delayLength, int testOnsetTS) {
        this(firstOdor, secondOdor, response, laserON);
        this.licks = licks;
        this.delayLength = delayLength;
        this.testOnsetTS = testOnsetTS;
    }

    public Trial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        this.firstOdor = firstOdor;
        this.secondOdor = secondOdor;
        this.response = response;
        this.laserON = laserON;
    }

    public void setLaserOnset(int laserOnset) {
        this.laserOnset = laserOnset;
    }

    public void setLaserOffset(int laserOffset) {
        this.laserOffset = laserOffset;
    }

    public void setTrialLaserType(int trialLaserType) {
        this.trialLaserType = trialLaserType;
    }

    public void setSamplePort(int sample) {
        this.samplePort = sample;
    }

    public void setTestPort(int test) {
        this.testPort = test;
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
            if (l[0] > testOnsetTS - delayLength) {
                count++;
            } else if (l[0] > testOnsetTS) {
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
            if (l[0] > testOnsetTS + 2500) {
                break;
            } else if (l[0] > testOnsetTS) {
                count++;
            }
        }
        return count;
    }

    public int[] getAllLick() {
        int[] all = new int[licks.size()];
        for (int i = 0; i < licks.size(); i++) {
            all[i] = licks.get(i)[0] - testOnsetTS;
        }
        return all;
    }

    public ArrayList<Integer[]> getLickQueue() {
        return licks;
    }

    public int getDelayLength() {
        return delayLength;
    }

    public int getTestOnset() {
        return testOnsetTS;
    }

    public int[] getFactors() {
        if ((this.samplePort == 0 && this.testPort == 0)
                || (this.samplePort == 1 && this.testPort == 1)) {
            return new int[]{
                this.firstOdor == EventType.OdorA ? 5 : 6,
                this.secondOdor == EventType.OdorA ? 5 : 6,
                this.laserON ? 1 : 0, this.response.ordinal(),
                this.getResponseLick()
            };
        } else {
            return new int[]{
                this.samplePort,
                this.testPort,
                this.laserON ? 1 : 0,
                this.response.ordinal(),
                this.getResponseLick(),
                this.testOnsetTS,
                this.trialLaserType,
                this.laserOnset,
                this.laserOffset};

        }
    }

    public int[] getELFTrialFactors() {
        return new int[]{
            this.samplePort,
            this.testPort,
            this.laserON ? 1 : 0,
            this.response.ordinal(),
            this.getResponseLick(),
            this.testOnsetTS};
    }
}
