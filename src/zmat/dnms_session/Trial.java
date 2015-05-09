/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

/**
 *
 * @author Libra
 */
public class Trial {

    final protected EventType firstOdor;
    final protected EventType secondOdor;
    final protected EventType response;
    final protected boolean laserON;

    public Trial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        this.firstOdor = firstOdor;
        this.secondOdor = secondOdor;
        this.response = response;
        this.laserON = laserON;
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
}
