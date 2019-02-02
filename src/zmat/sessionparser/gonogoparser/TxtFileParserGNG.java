/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.gonogoparser;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import zmat.dnms_session.EventType;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;
import zmat.txtFile.Event;
import zmat.txtFile.EventParser;
import zmat.txtFile.TxtFileParser;

/**
 *
 * @author Libra
 */
public class TxtFileParserGNG extends TxtFileParser {

    @Override
    protected void offerSessions(Queue<Session> sessions, Queue<Trial> trials) {
        if (trials.size() == 20) {
            Queue<Trial> laserTrials = new LinkedList<>();
            int idx = 0;
            for (Trial t : trials) {
                laserTrials.add(new Trial(t.getFirstOdor(), t.getSecondOdor(), t.getResponse(), idx % 2 == 1, t.getLickQueue(), t.getDelayLength(), t.getTestOnset()));
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
                        lastLick = e.getAbsTime();
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
//                    if (firstOdor == EventType.unknown) {
//                        firstOdor = e.getEventType();
//                        licks = new ArrayList<>();
//                        trialStart = e.getAbsTime();
//                    } else {
                    firstOdor = EventType.OdorB;
                    secondOdor = e.getEventType();
                    odor2Start = e.getAbsTime();
                    delayLength = e.getAbsTime() - trialStart - 1000;
//                    }
                    break;
            }
        }
        if (currentTrials.size() > 0) {
            offerSessions(sessions, currentTrials);
        }

        return sessions;

    }
}
