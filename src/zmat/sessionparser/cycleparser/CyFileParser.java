/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import zmat.sessionparser.EventType;
import zmat.sessionparser.Session;

/**
 *
 * @author Libra
 */
public class CyFileParser extends zmat.sessionparser.FileParser {

    @Override
    public void parseFiles(String... s) {
        days = new LinkedList<>();
        for (String path : s) {
            days.add(new CyDay(path, processFile(new File(path))));
        }
    }

    public CyFileParser(CySession s) {
        super(s);
    }
    
    

    @Override
    protected Queue<CySession> processFile(File f) {
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection, EventType.Miss, EventType.Hit};
        EventType[] odors = {EventType.OdorA, EventType.OdorB};
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            Queue<CyTrial> currentTrials = new LinkedList<>();
            Queue<CySession> sessions = new LinkedList<>();
            EventType firstOdor = EventType.unknown;
            EventType secondOdor = EventType.unknown;
            boolean laserOn = false;
            EventType response;
            int laserType = 0;
            int delayLength = 0;
            int firstOdorTime = 0;

            for (int[] evt : eventList) {
                switch (evt[2]) {
                    case 61:
                        switch (evt[3]) {
//                            case 1:
//                                currentTrials = new LinkedList<>();
//                                break;
                            case 0:
                                if (currentTrials.size() > 0) {
                                    sessions.offer(new CySession(currentTrials));
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
                            currentTrials.offer(new CyTrial(firstOdor, secondOdor, response, laserOn, delayLength, laserType));
                        }
                        firstOdor = EventType.unknown;
                        secondOdor = EventType.unknown;
                        laserOn = false;
                        delayLength = 0;
                        laserType = 0;
                        break;
                    case 9:
                    case 10:
                        if (evt[3] == 1) {
                            if (firstOdor == EventType.unknown) {
                                firstOdor = odors[evt[2] - 9];
                                firstOdorTime = evt[0];
                            } else if (evt[0] < firstOdorTime + 12500) {
                                delayLength = evt[0] - firstOdorTime;
                                secondOdor = odors[evt[2] - 9];
                            }
                        }
                        break;
                    case 65:
                        laserOn = (evt[3] == 1);
                        break;
                    case 58:
                        laserType = evt[3];
                        break;

                }
            }
            if (currentTrials.size() > 0) {
                sessions.offer(new CySession(currentTrials));
            }
            return sessions;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
