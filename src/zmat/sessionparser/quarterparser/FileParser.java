/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import zmat.dnms_session.EventType;

/**
 *
 * @author Libra
 */
public class FileParser extends zmat.dnms_session.FileParser {

    @Override
    protected Queue<zmat.dnms_session.Session> processFile(File f) {
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection, EventType.Miss, EventType.Hit};
        EventType[] odors = {EventType.OdorA, EventType.OdorB};
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            Queue<zmat.dnms_session.Trial> currentTrials = new LinkedList<>();
            Queue<zmat.dnms_session.Session> sessions = new LinkedList<>();
            int laserType = -1;
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
                        if (laserType != -1 && firstOdor != null && secondOdor != null) {
                            currentTrials.offer(new Trial(laserType, firstOdor, secondOdor, response, laserOn));
//                            System.out.println("Session+");
                        }
                        laserType = -1;
                        firstOdor = EventType.unknown;
                        secondOdor = EventType.unknown;
                        laserOn = false;
                        break;
                    case 9:
                    case 10:
                        if (evt[3] == 1) {
                            if (firstOdor == EventType.unknown) {
                                firstOdor = odors[evt[2] - 9];
                            } else {
                                secondOdor = odors[evt[2] - 9];
                            }
                        }
                        break;
                    case 65:
                        laserOn = (evt[3] == 1);
                        if (!laserOn) {
                            laserType = 0;
//                            System.out.println(laserType);
                        }
                        break;
                    case 58:
                        switch (evt[3]) {
                            case 10:
                                laserType = 0;
                                break;
                            case 91:
                            case 92:
                            case 93:
                            case 94:
                                laserType = evt[3] - 90;
                                break;
                            case 96:
                            case 97:
                            case 98:
                            case 99:
                                laserType = evt[3] - 95;
                                break;
                            case 121:
                            case 122:
                            case 123:
                            case 124:
                                laserType = evt[3] - 120;
                                break;

                        }
//                        System.out.println(laserType);
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
