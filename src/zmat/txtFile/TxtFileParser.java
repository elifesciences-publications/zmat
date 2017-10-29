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
    protected void offerSessions(Queue<Session> sessions, Queue<Trial> trials) {
        if (trials.size() == 20) {
            Queue<Trial> laserTrials = new LinkedList<>();
            int idx = 0;
            for (Trial t : trials) {
                laserTrials.add(new Trial(t.getFirstOdor(), t.getSecondOdor(), t.getResponse(), idx % 2 == 1, t.getLickQueue(), t.getDelayLength(), t.getOdor2Start()));
                idx++;
            }
            sessions.offer(new Session(laserTrials));
        }
    }

    @Override
    protected Queue<Session> processFile(File f) {
        @SuppressWarnings("unchecked")
        List<Event> eventList = EventParser.getEventList(f);
        Queue<Trial> currentTrials = new LinkedList<>();
        Queue<Session> sessions = new LinkedList<>();
        EventType firstOdor = EventType.unknown;
        EventType secondOdor = EventType.unknown;
        EventType response;
        int odor2Start = 0;
        int delayLength = 0;
        int trialStart = 0;
        ArrayList<Integer[]> licks = new ArrayList<>();

        int lastLick = 0;
        for (int idx = eventList.size(); idx > 0; idx--) {
            Event e = eventList.get(idx - 1);
            switch (e.getEventType()) {
                case Lick:
                    if (e.getAbsTime() - lastLick > 50) {
                        licks.add(new Integer[]{e.getAbsTime(), 1});
                        lastLick=e.getAbsTime();
                    }
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
                    if (firstOdor != EventType.unknown && secondOdor != EventType.unknown) {
                        currentTrials.offer(new Trial(firstOdor, secondOdor, response, false, licks, delayLength, odor2Start));
                    }
                    firstOdor = EventType.unknown;
                    secondOdor = EventType.unknown;
                    licks = new ArrayList<>();
                    break;
                case OdorA:
                case OdorB:
                    if (firstOdor == EventType.unknown) {
                        firstOdor = e.getEventType();
                        licks = new ArrayList<>();
                        trialStart = e.getAbsTime();
                    } else {
                        secondOdor = e.getEventType();
                        odor2Start = e.getAbsTime();
                        delayLength = e.getAbsTime() - trialStart - 1000;
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
