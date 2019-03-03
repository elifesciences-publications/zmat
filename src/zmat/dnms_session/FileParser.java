/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Libra
 */
public class FileParser {

    protected Queue<Day> days;

    public FileParser parseFiles(String... s) {
        days = new LinkedList<>();
        for (String path : s) {
//            System.out.println("add day");
            days.add(new Day(path, processFile(new File(path))));
        }
        return this;
    }

    protected Queue<Session> processFile(File f) {
        int odor2Start = 0;
        int trialStart = 0;
        int delayLength = 0;
        ArrayList<Integer[]> licks = new ArrayList<>();
        EventType[] responses = {EventType.FalseAlarm, EventType.CorrectRejection,
            EventType.Miss, EventType.Hit, EventType.ABORT_TRIAL};
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
            int lastLick = 0;
            int type = 0;
            int val = 0;
            int samplePort = 0;
            int testPort = 0;
            int laserTType=-1;
            int laserOnset=Integer.MAX_VALUE;
            int laserOffset=Integer.MAX_VALUE;
            for (int[] evt : eventList) {
                if (evt.length == 5) {
                    type = evt[2];
                    val = evt[3];
                } else if (evt.length == 3) {
                    type = evt[1];
                    val = evt[2] & 0x7f;
                }

                switch (type) {
                    case 0:
                        if (evt[0] - lastLick > 50) {
                            licks.add(new Integer[]{evt[0], evt[2]});
                            lastLick = evt[0];
                        }
                        break;
                    case 1:
                        if (val == 1) {
                            break;
                        }
                        type = 61;
                        val = 0;
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
                        response = responses[respPos];
                        if (firstOdor != EventType.unknown && secondOdor != EventType.unknown) {
                            Trial newTrial = new Trial(firstOdor, secondOdor, response, laserOn, licks, delayLength, odor2Start);
                            newTrial.setSamplePort(samplePort);
                            newTrial.setTestPort(testPort);
                            newTrial.setTrialLaserType(laserTType);
                            newTrial.setLaserOnset(laserOnset);
                            newTrial.setLaserOffset(laserOffset);
                            currentTrials.offer(newTrial);
                        }
                        firstOdor = EventType.unknown;
                        secondOdor = EventType.unknown;
//                        licks = new ArrayList<>();
                        laserOn = false;
                        laserTType=-1;
                        laserOnset=Integer.MAX_VALUE;
                        laserOffset=Integer.MAX_VALUE;
                        break;
                    case 9:
                    case 10:
                        if (val != 0) {
                            if (firstOdor == EventType.unknown) {
                                firstOdor = odors[type - 9];
//                                licks = new ArrayList<>();
                                trialStart = evt[0];
                                samplePort = val;
                            } else {
                                secondOdor = odors[type - 9];
                                odor2Start = evt[0];
                                delayLength = evt[0] - trialStart - 1000;
                                testPort = val;
                            }
                        }
                        break;
                    case 65:
                        laserOn = (val == 1);
                        if (!licks.isEmpty()) {
                            licks = new ArrayList<>();
                        }
                        break;
                    case 58:
                        laserTType=val;
                        if (!licks.isEmpty()) {
                            licks = new ArrayList<>();
                        }
                        break;
                    case 79:
                        if(val!=0){
                            laserOnset=evt[0];
                        }else{
                            laserOffset=evt[0];
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

    public Queue<Day> getDays() {
//        System.out.println(days.size()+" days");
        return days;
    }

    public int[][] getRawMat(String s) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(s)))) {
            @SuppressWarnings("unchecked")
            ArrayList<int[]> eventList = (ArrayList<int[]>) ois.readObject();
            return eventList.toArray(new int[eventList.size()][]);
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(ex.toString());
        }
        return new int[0][0];
    }

    public void mat2ser(int[][] mat, String pathToFile) {
        ArrayList<int[]> l = new ArrayList<>();
        l.addAll(Arrays.asList(mat));
        arrayList2ser(l, pathToFile);
    }

    public void arrayList2ser(ArrayList<int[]> l, String pathToFile) {

        File targetFile = new File(pathToFile);
        File parent = targetFile.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(targetFile))) {
            out.writeObject(l);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
