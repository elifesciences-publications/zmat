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
public class Trial extends zmat.dnms_session.Trial{

    final private int laserType;

    public Trial(int laserType, EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        super(firstOdor,secondOdor,response,laserON);
        this.laserType = laserType;
    }

    public int getLaserType() {
        return laserType;
    }
}
