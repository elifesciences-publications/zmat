/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.noodor_parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import zmat.dnms_session.EventType;
import zmat.dnms_session.Session;

/**
 *
 * @author Libra
 */
public class FileParser extends zmat.dnms_session.FileParser {

    @Override
    protected Queue<Session> processFile(File f) {
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection, EventType.Miss, EventType.Hit};
        EventType[] odors = {EventType.OdorA, EventType.OdorB};
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            Queue<zmat.dnms_session.Trial> currentTrials = new LinkedList<>();
            Queue<Session> sessions = new LinkedList<>();
            boolean isCatch = false;
            EventType firstOdor = EventType.unknown;
            EventType secondOdor = EventType.unknown;
            boolean laserOn = false;
            EventType response;

            for (int[] evt : eventList) {
                switch (evt[2]) {
                    case 61:
                        switch (evt[3]) {
                            case 1:
                                currentTrials = new LinkedList<>();
                                break;
                            case 0:
                                if (currentTrials.size() > 0) {
                                    sessions.offer(new Session(currentTrials));
                                }
                                break;
                        }
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
//                        System.out.println("Response");
                        response = responses[evt[2] - 4];
                        if (firstOdor != EventType.unknown && secondOdor != EventType.unknown) {
                            currentTrials.offer(new Trial(isCatch, firstOdor, secondOdor, response, laserOn));
//                            System.out.println("Session+");
                        }
                        isCatch = false;
                        firstOdor = EventType.unknown;
                        secondOdor = EventType.unknown;
                        laserOn = false;
                        break;
                    case 9:
                    case 10:
                        if (evt[3] != 0) {
                            if (firstOdor == EventType.unknown) {
                                firstOdor = odors[evt[2] - 9];
                            } else {
                                secondOdor = odors[evt[2] - 9];
                            }
                        }
                        if (evt[3] == 2 || evt[3] == 3) {
                            isCatch = true;
                        }
                        break;
                    case 65:
                        laserOn = (evt[3] == 1);
                        break;
                    case 79:
                        if ((firstOdor == EventType.OdorA && evt[3] == 2)
                                || (firstOdor == EventType.OdorB && evt[3] == 1)) {
                            isCatch = true;
                        }
                        break;
                }
            }
            if (currentTrials.size() > 0) {
                sessions.offer(new Session(currentTrials));
//                System.out.println("Session+");
            }
            return sessions;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
