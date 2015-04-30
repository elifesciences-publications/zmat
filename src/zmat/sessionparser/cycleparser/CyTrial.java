/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import zmat.dnms_session.EventType;

/**
 *
 * @author Libra
 */
public class CyTrial extends zmat.dnms_session.Trial {

    private int delayLength;
    private int laserType;

    public enum CycleTrialType {

        D4L0, D4L1, D4L2, D6L0, D6L1, D6L2, D6L3, D8L0, D8L1, D8L2, D8L3, D8L4, OTHER
    }

    public CyTrial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        super(firstOdor, secondOdor, response, laserON);
    }

    public CyTrial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, int delayLength, int laserType) {
        super(firstOdor, secondOdor, response, laserON);
        this.delayLength = Math.round((float) delayLength / 1000) - 1;
        this.laserType = laserType;
    }

    public CycleTrialType getCycleTrialType() {
        if (!laserON) {
            switch (delayLength) {
                case 4:
                    return CycleTrialType.D4L0;
                case 6:
                    return CycleTrialType.D6L0;
                case 8:
                    return CycleTrialType.D8L0;
            }
        }
        switch (delayLength) {
            case 4:
                switch (laserType) {
                    case 80:
                        return CycleTrialType.D4L1;
                    case 90:
                        return CycleTrialType.D4L2;
                }
            case 6:
                switch (laserType) {
                    case 81:
                        return CycleTrialType.D6L1;
                    case 82:
                        return CycleTrialType.D6L2;
                    case 83:
                        return CycleTrialType.D6L3;
                }
            case 8:
                switch (laserType) {
                    case 91:
                        return CycleTrialType.D8L1;
                    case 92:
                        return CycleTrialType.D8L2;
                    case 93:
                        return CycleTrialType.D8L3;
                    case 94:
                        return CycleTrialType.D8L4;
                }
            default:
                return CycleTrialType.OTHER;
        }
    }

    public int getTypeIndex() {
        return getCycleTrialType().ordinal();
    }
}
