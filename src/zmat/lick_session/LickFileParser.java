/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.lick_session;

import zmat.dnms_session.Trial;
import zmat.dnms_session.EventType;
import zmat.dnms_session.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Libra
 */
public class LickFileParser extends zmat.dnms_session.FileParser {


    @Override
    protected Queue<Session> processFile(File f) {
        int trialStartTime = 0;
        int delayLength = 0;
        ArrayList<Integer[]> licks = new ArrayList<>();
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection, EventType.Miss, EventType.Hit};
        EventType[] odors = {EventType.OdorA, EventType.OdorB};
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            Queue<Trial> currentTrials = new LinkedList<>();
            Queue<Session> sessions = new LinkedList<>();
            EventType firstOdor = EventType.unknown;
            EventType secondOdor = EventType.unknown;
            boolean laserOn = false;
            EventType response;

            for (int[] evt : eventList) {
                switch (evt[2]) {
                    case 0:
                        licks.add(new Integer[]{evt[0] - trialStartTime, evt[3]});
                        break;
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
                        response = responses[evt[2] - 4];
                        if (firstOdor != null && secondOdor != null) {
                            currentTrials.offer(new LickTrial(firstOdor, secondOdor, response, laserOn, licks, delayLength));
                        }
                        firstOdor = EventType.unknown;
                        secondOdor = EventType.unknown;
                        licks = new ArrayList<>();
                        laserOn = false;
                        break;
                    case 9:
                    case 10:
                        if (evt[3] == 1) {
                            if (firstOdor == EventType.unknown) {
                                firstOdor = odors[evt[2] - 9];
                                trialStartTime = evt[0];
                                licks = new ArrayList<>();
                            } else {
                                secondOdor = odors[evt[2] - 9];
                                delayLength = evt[0] - trialStartTime - 1000;
                            }
                        }
                        break;
                    case 65:
                        laserOn = (evt[3] == 1);
                        break;
                }
            }
            if (currentTrials.size() > 0) {
                sessions.offer(new Session(currentTrials));
            }
//            System.out.println(Integer.toString(sessions.size())+" sessions");
            return sessions;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
