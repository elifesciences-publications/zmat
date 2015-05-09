/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Xiaoxing
 */
public class DataProcessor {

    protected Queue<Day> days;
    protected int minLick = 16;

    public enum listType {

        CORRECT_RATE, FALSE_ALARM, MISS;
    }

    public void setMinLick(int minLick) {
        this.minLick = minLick;
    }

    public void processFile(String... s) {
        days = new LinkedList<>();
        for (String f : s) {
            days.addAll(f.endsWith(".ser")
                    ? new FileParser().parseFiles(f).getDays()
                    : new zmat.txtFile.TxtFileParser().parseFiles(f).getDays());
        }

        if (days.size() < 1) {
            System.out.println("No suitable records found.");
        }
        for (Day d : days) {
            d.removeBadSessions(20, true, minLick);
        }

    }

    public int[] getHitFalseMiss(boolean lightOn, int trialLimit) {
        int hit = 0;
        int fa = 0;
        int miss = 0;
        int totalTrial = 0;

        for (Day d : days) {
            if (d.sessions.size() < 5) {
                continue;
            }
            int trialCount = 0;
            day:
            for (Session s : d.sessions) {

                for (Trial t : s.trials) {
                    if (trialLimit > 0 && trialCount >= trialLimit) {
                        break day;
                    }
                    if (t.withLaserON() == lightOn) {
                        totalTrial++;
                        trialCount++;

                        switch (t.response) {
                            case Hit:
                                hit++;
                                break;
                            case FalseAlarm:
                                fa++;
                                break;
                            case Miss:
                                miss++;
                                break;
                        }
                    }
                }

            }
        }
//        System.out.println(totalTrial);
        return new int[]{hit, fa, miss, totalTrial};
    }

    public int[][] getHitFalseMissSession(boolean lightOn, int trialLimit) {
        ArrayList<int[]> sessions=new ArrayList<>();
        for (Day d : days) {
            if (d.sessions.size() < 5) {
                continue;
            }
            int trialCount = 0;
            day:
            for (Session s : d.sessions) {
                int hit = 0;
                int fa = 0;
                int miss = 0;
                int totalTrial = 0;

                for (Trial t : s.trials) {
                    if (trialLimit > 0 && trialCount >= trialLimit) {
                        break day;
                    }
                    if (t.withLaserON() == lightOn) {
                        totalTrial++;
                        trialCount++;

                        switch (t.response) {
                            case Hit:
                                hit++;
                                break;
                            case FalseAlarm:
                                fa++;
                                break;
                            case Miss:
                                miss++;
                                break;
                        }
                    }
                }
                sessions.add(new int[]{hit, fa, miss, totalTrial});
            }
        }
//        System.out.println(totalTrial);
        return sessions.toArray(new int[sessions.size()][]);
    }

    public Queue<Day> getDays() {
        System.out.println(days.size() + " days");
        return days;
    }
}
