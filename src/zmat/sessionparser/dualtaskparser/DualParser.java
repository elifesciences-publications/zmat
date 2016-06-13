/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.dualtaskparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import zmat.dnms_session.EventType;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;

/**
 *
 * @author Libra
 */
public class DualParser extends zmat.dnms_session.FileParser {

    @Override
    protected Queue<Session> processFile(File f) {
        int odor2Start = 0;
        int trialStart = 0;
        int delayLength = 0;
        ArrayList<Integer[]> licks = new ArrayList<>();
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection, EventType.Miss, EventType.Hit};
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            Queue<Trial> currentTrials = new LinkedList<>();
            Queue<Session> sessions = new LinkedList<>();
            EventType firstOdor = EventType.unknown;
            EventType secondOdor = EventType.unknown;
            EventType distractorOdor = EventType.unknown;
            boolean laserOn = false;
            EventType response;
            EventType distractorResponse = EventType.unknown;
            int lastLick = 0;
            for (int[] evt : eventList) {
                switch (evt[2]) {
                    case 0:
                        if (evt[0] - lastLick > 50) {
                            licks.add(new Integer[]{evt[0], evt[3]});
                            lastLick = evt[0];
                        }
                        break;
                    case 1:
                        if (evt[3] == 1) {
                            break;
                        }
                        evt[2] = 61;
                        evt[3] = 0;
                    case 61:
                        switch (evt[3]) {
                            case 0:
                                if (currentTrials.size() > 0) {
                                    sessions.offer(new Session(currentTrials));
                                    currentTrials = new LinkedList<>();
                                }
                                break;
                        }
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
//                            System.out.println(""+evt[2]+", "+evt[3]);
                        if (evt[3] == 2 || evt[3] == 1) {

                            response = responses[evt[2] - 4];
                            if (firstOdor != EventType.unknown && secondOdor != EventType.unknown) {
                                currentTrials.offer(new DualTrial(firstOdor, secondOdor, response, laserOn, licks, delayLength, odor2Start, distractorOdor, distractorResponse));
                            }
                            firstOdor = EventType.unknown;
                            secondOdor = EventType.unknown;
                            distractorOdor = EventType.unknown;
                            distractorResponse = EventType.unknown;
//                        licks = new ArrayList<>();
                            laserOn = false;
                        } else if (evt[3] == 3) {
                            distractorResponse = responses[evt[2] - 4];
//                            System.out.println(""+evt[2]+", "+evt[3]);//TODO: DEBUG
                        }
                        break;

                    case 9:
                    case 10:
                        if (evt[3] != 0) {
                            if (firstOdor == EventType.unknown) {
                                firstOdor = evt[3] == 20 ? EventType.Others : ((evt[2] == 9) ? EventType.OdorA : EventType.OdorB);
//                                licks = new ArrayList<>();
                                trialStart = evt[0];
                            } else {
                                secondOdor = evt[3] == 20 ? EventType.Others : (evt[2] == 9) ? EventType.OdorA : EventType.OdorB;
                                odor2Start = evt[0];
                                delayLength = evt[0] - trialStart - 1000;
                            }
                        }
                        break;
                    case 64:
                    case 66:
                        distractorOdor = (evt[2] == 66) ? EventType.OdorA : EventType.OdorB;
                        break;
                    case 65:
                        laserOn = (evt[3] == 1);
                        break;
                    case 58:
                    case 59:
                        licks = new ArrayList<>();
                        break;
                }
            }
            if (currentTrials.size() > 0) {
                System.out.println(currentTrials.size());
                sessions.offer(new Session(currentTrials));
            }
            zmat.debugger.log(10, Integer.toString(sessions.size()) + " sessions");
            return sessions;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
