/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.dualtaskparser;

import java.util.ArrayList;
import zmat.dnms_session.EventType;

/**
 *
 * @author Libra
 */
public class MultiTrial extends zmat.dnms_session.Trial {

    ArrayList<EventType> distractorOdor;
    ArrayList<EventType> distractorResponse;

    public MultiTrial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, ArrayList<Integer[]> licks, int delayLength, int odor2Start, ArrayList<EventType> distractorOdor, ArrayList<EventType> distractorResponse) {
        super(firstOdor, secondOdor, response, laserON, licks, delayLength, odor2Start);
        this.distractorOdor = distractorOdor;
        this.distractorResponse = distractorResponse;
    }

    public ArrayList<EventType> getDistractorResponse() {
        return distractorResponse;
    }

    @Override
    public EventType getResponse() {
        if (firstOdor == EventType.Others || secondOdor == EventType.Others) {
            return EventType.Others;
        }
        return response;
    }


}
