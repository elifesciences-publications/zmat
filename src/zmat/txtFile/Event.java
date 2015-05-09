/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.txtFile;

import zmat.dnms_session.EventType;


/**
 *
 * @author Libra
 */
public class Event {

    final private int AbsTime;
    final private EventType event;

    public Event(int AbsTime, EventType event) {
        this.AbsTime = AbsTime;
        this.event = event;
    }

    public EventType getEventType() {
        return event;
    }

    public int getAbsTime() {
        return AbsTime;
    }

    public boolean isOdor() {
        return event == EventType.OdorA || event == EventType.OdorB;
    }

    public boolean isNewSession() {
        return event == EventType.NewSession;
    }

    public boolean isResponse() {
        return event == EventType.Hit || event == EventType.CorrectRejection
                || event == EventType.Miss || event == EventType.FalseAlarm;
    }
}
