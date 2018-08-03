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
public class DualTrial extends zmat.dnms_session.Trial {

    EventType distractorOdor;
    EventType distractorResponse;

    public DualTrial(EventType firstOdor, EventType secondOdor, EventType response, boolean laserON, ArrayList<Integer[]> licks, int delayLength, int odor2Start, EventType distractorOdor, EventType distractorResponse) {
        super(firstOdor, secondOdor, response, laserON, licks, delayLength, odor2Start);
        this.distractorOdor = distractorOdor;
        this.distractorResponse = distractorResponse;
    }

    public EventType getDistractorResponse() {
        return distractorResponse;
    }

    @Override
    public EventType getResponse() {
        if (firstOdor == EventType.Others || secondOdor == EventType.Others) {
            return EventType.Others;
        }
        return response;
    }

    public EventType getDistractorOdor() {
        return distractorOdor;
    }

    @Override
    public int[] getFactors() {
//        System.out.println("DBG DTR TYPE"+distractorOdor);
        return new int[]{
            this.firstOdor == EventType.OdorA ? 2 : 3,
            this.secondOdor == EventType.OdorA ? 5 : 6,
            this.laserON ? 1 : 0,
            this.response.ordinal(),
            this.distractorOdor.ordinal(),
            this.distractorResponse.ordinal(),
            this.getResponseLick()
        };

    }
}
