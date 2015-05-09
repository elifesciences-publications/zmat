/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.txtFile;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import zmat.dnms_session.EventType;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;
import zmat.lick_session.LickTrial;

/**
 *
 * @author Libra
 */
public class TxtFileParser extends zmat.dnms_session.FileParser {

    /*
     @Override
     protected Queue<Session> processFile(File f) {
     //        System.out.println("txt File");
     List<Event> eventList = EventParser.getEventList(f);
     int refTime = -1;
     Queue<Trial> currentTrials = new LinkedList<>();
     Queue<Session> sessions = new LinkedList<>();
     for (int i = eventList.size() - 1; i >= 0;) {
     if (eventList.get(i).isNewSession()) {
     if (currentTrials.size() > 0) {
     offerSessions(sessions, currentTrials);
     currentTrials = new LinkedList<>();
     }
     i--;
     } else if (i > 1 && eventList.get(i).isOdor() && eventList.get(i - 1).isOdor()
     && eventList.get(i - 2).isResponse()) {
     refTime = refTime > 0 ? refTime : eventList.get(i).getAbsTime();
     currentTrials.offer(new Trial(eventList.get(i).getEventType(),
     eventList.get(i - 1).getEventType(),
     eventList.get(i - 2).getEventType(), false));
     i -= 3;
     } else {
     i--;
     }
     }
     if (currentTrials.size() > 0) {
     offerSessions(sessions, currentTrials);
     }
     return sessions;
     }
     */
    private void offerSessions(Queue<Session> sessions, Queue<LickTrial> trials) {
        if (trials.size() == 20) {
            Queue<Trial> laserTrials = new LinkedList<>();
            int idx = 0;
            for (LickTrial t : trials) {
                laserTrials.add(new LickTrial(t.getFirstOdor(), t.getSecondOdor(), t.getResponse(), idx % 2 == 1, t.getLickQueue(), t.getDelayLength()));
                idx++;
            }
            sessions.offer(new Session(laserTrials));
        }
    }

    @Override
    protected Queue<Session> processFile(File f) {

        @SuppressWarnings("unchecked")
        List<Event> eventList = EventParser.getEventList(f);
        Queue<LickTrial> currentTrials = new LinkedList<>();
        Queue<Session> sessions = new LinkedList<>();
        EventType firstOdor = EventType.unknown;
        EventType secondOdor = EventType.unknown;
        EventType response;
        int trialStartTime = 0;
        int delayLength = 0;
        ArrayList<Integer[]> licks = new ArrayList<>();

        for (int idx = eventList.size(); idx > 0; idx--) {
            Event e = eventList.get(idx - 1);
            switch (e.getEventType()) {
                case Lick:
                    licks.add(new Integer[]{e.getAbsTime() - trialStartTime, 1});
                    break;
                case NewSession:
                    if (currentTrials.size() > 0) {
                        offerSessions(sessions, currentTrials);
                        currentTrials = new LinkedList<>();
                    }
                    break;
                case Hit:
                case Miss:
                case CorrectRejection:
                case FalseAlarm:
                    response = e.getEventType();
                    if (firstOdor != null && secondOdor != null) {
                        currentTrials.offer(new LickTrial(firstOdor, secondOdor, response, false, licks, delayLength));
                    }
                    firstOdor = EventType.unknown;
                    secondOdor = EventType.unknown;
                    licks = new ArrayList<>();
                    break;
                case OdorA:
                case OdorB:
                    if (firstOdor == EventType.unknown) {
                        firstOdor = e.getEventType();
                        trialStartTime = e.getAbsTime();
                        licks = new ArrayList<>();
                    } else {
                        secondOdor = e.getEventType();
                        delayLength = e.getAbsTime() - trialStartTime - 1000;
                    }
                    break;
            }
        }
        if (currentTrials.size() > 0) {
            offerSessions(sessions, currentTrials);
        }

        return sessions;

    }

}
