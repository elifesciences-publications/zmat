/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser;

/**
 *
 * @author Libra
 */
public class Trial {

    protected EventType firstOdor;
    protected EventType secondOdor;
    protected EventType response;
    protected boolean laserON;

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
