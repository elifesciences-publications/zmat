/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;


/**
 *
 * @author Libra
 */
public class Trial {

    final private EventType firstOdor;
    final private EventType secondOdor;
    final protected EventType response;
    final private int laserType;
    final private boolean laserON;

    public Trial(int laserType, EventType firstOdor, EventType secondOdor, EventType response, boolean laserON) {
        this.firstOdor = firstOdor;
        this.secondOdor = secondOdor;
        this.response = response;
        this.laserType = laserType;
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

    public int getLaserType() {
        return laserType;
    }


    public boolean withLaserON() {
        return laserON;
    }
}
