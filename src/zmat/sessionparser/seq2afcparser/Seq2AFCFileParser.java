/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.seq2afcparser;

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
 * @author casel_000
 */
public class Seq2AFCFileParser extends zmat.dnms_session.FileParser {

    @Override
    protected Queue<Session> processFile(File f) {
        int sampleStart = 0;
        int test1Start=0;
        int test2Start=0;
        ArrayList<Integer[]> licks = new ArrayList<>();
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection,
            EventType.Miss, EventType.Hit, EventType.ABORT_TRIAL};
        EventType[] odors = {EventType.OdorA, EventType.OdorB};
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            Queue<Trial> currentTrials = new LinkedList<>();
            Queue<Session> sessions = new LinkedList<>();
            EventType sample = EventType.unknown;
            EventType test1 = EventType.unknown;
            EventType test2 = EventType.unknown;
            boolean laserOn = false;
            EventType response1 = EventType.unknown;
            EventType response2 = EventType.unknown;
            int lastLick = 0;
            int type = 0;
            int val = 0;
            for (int[] evt : eventList) {
                type = evt[1];
                val = evt[2] & 0x7f;

                switch (type) {
                    case 0:
                        if (evt[0] - lastLick > 50) {
                            licks.add(new Integer[]{evt[0], evt[2]});
                            lastLick = evt[0];
                        }
                        break;
                    case 61:
                        switch (val) {
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
                    case 84:
                        int respPos = type > 7 ? 4 : type - 4;
                        if (response1 == EventType.unknown) {
                            response1 = responses[respPos];
                        } else {
                            response2 = responses[respPos];
                        }
                        break;
                    case 9:
                    case 10:
                        if (val != 0) {
                            sample = odors[type - 9];
                        }
                        break;
                    case 65:
                        laserOn = (val == 1);
                        break;
                    case 58:
                    case 59:
                        if (sample != EventType.unknown && test1 != EventType.unknown) {
                            currentTrials.offer(new Seq2AFCTrial(sample, test1, test2, response1, response2, laserOn, licks, sampleStart,test1Start,test2Start));
                        }
                        sample = EventType.unknown;
                        test1=EventType.unknown;
                        test2=EventType.unknown;
                        response1=EventType.unknown;
                        response2=EventType.unknown;
                        laserOn = false;
                        licks = new ArrayList<>();
                        break;
                    case 83:
                        EventType e = (val == 6)
                                ? EventType.OdorA
                                : EventType.OdorB;
                        if (test1 == EventType.unknown) {
                            test1 = e;
                            test1Start=evt[0];
                        } else {
                            test2 = e;
                            test2Start=evt[0];
                        }
                        break;
                }
            }
            if (currentTrials.size() > 0) {
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
