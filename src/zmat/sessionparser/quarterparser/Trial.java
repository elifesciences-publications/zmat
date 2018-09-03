/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;

import zmat.dnms_session.EventType;

/**
 *
 * @author Libra
 */
public class Trial extends zmat.dnms_session.Trial {

    final private int laserType;

    public Trial(int laserType, EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        super(firstOdor, secondOdor, response, laserON);
        this.laserType = laserType;
    }

    public int getLaserType() {
        return laserType;
    }

    @Override
    public int[] getFactors() {

        return new int[]{
            this.firstOdor == EventType.OdorA ? 5 : 6,
            this.secondOdor == EventType.OdorA ? 5 : 6,
            this.laserON ? 1 : 0,
            this.response.ordinal(),
            this.laserType,
            this.getResponseLick()
        };
    }

}
