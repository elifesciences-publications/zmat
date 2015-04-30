/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.lr_session;

import java.util.Queue;
import zmat.dnms_session.EventType;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;

/**
 * @author Xiaoxing
 */
public class LRSession extends Session {

    public LRSession(Queue<Trial> q) {
        super(q);
    }

    @Override
    protected int getTypeBaseRate(Trial trial, RateType type) {
        switch (type) {
            case performance:
                return (trial.getResponse() == EventType.FalseAlarm || trial.getResponse() == EventType.Hit) ? 1 : 0;
            case falseAlarm:
                return 1;
            case miss:
                return 1;
        }
        return 0;
    }
}
