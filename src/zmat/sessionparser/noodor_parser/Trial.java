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
public class Trial extends zmat.dnms_session.Trial{

    final private boolean isCatch;

    public Trial(boolean isCatch, EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        super(firstOdor,secondOdor,response,laserON);
        this.isCatch = isCatch;
    }

    public boolean isCatch() {
        return isCatch;
    }
}
