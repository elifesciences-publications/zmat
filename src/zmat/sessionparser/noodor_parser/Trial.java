/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.noodor_parser;

import zmat.dnms_session.EventType;

/**
 *
 * @author Libra
 */
public class Trial extends zmat.dnms_session.Trial {

    final private boolean isCatch;

    public Trial(boolean isCatch, EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        super(firstOdor, secondOdor, response, laserON);
        this.isCatch = isCatch;
    }

    public boolean isCatch() {
        return isCatch;
    }

    @Override
    public int[] getFactors() {
//        System.out.println(".");
        return new int[]{
            this.firstOdor == EventType.OdorA ? 5 : 6,
            this.secondOdor == EventType.OdorA ? 5 : 6,
            this.isCatch ? 1 : 0, this.response.ordinal(),
            this.laserON ? 1 : 0
        };

    }
}
