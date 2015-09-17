/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import zmat.debugger;

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
            FileParser fp = f.endsWith(".ser")
                    ? new FileParser()
                    : new zmat.txtFile.TxtFileParser();
            fp.parseFiles(f);

            days.addAll(fp.getDays());
        }
        if (days.isEmpty()) {
            System.out.println("No suitable records found.");
        }
        for (Day d : days) {
            d.removeBadSessions(20, true, minLick);
        }
    }

    public int[][] getPerf(int lightOn, int trialLimit) {
        ArrayList<int[]> sessions = new ArrayList<>();
        for (Day d : days) {
            if (d.sessions == null || d.sessions.size() < 5) {
                continue;
            }
            int trialCount = 0;
            day:
            for (Session s : d.sessions) {
                int hit = 0;
                int fa = 0;
                int miss = 0;
                int totalTrial = 0;
                int correctRejection = 0;
                int rewardedLick = 0;
                int unrewardedLick = 0;

                for (Trial t : s.trials) {
                    if (trialLimit > 0 && trialCount >= trialLimit) {
                        if (totalTrial > 0) {
                            sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial, rewardedLick, unrewardedLick});
                        }
                        break day;
                    }
                    //Laser quarter and other cycles
                    if ((t instanceof zmat.sessionparser.quarterparser.Trial
                            && ((zmat.sessionparser.quarterparser.Trial) t).getLaserType() == lightOn)
                            //Laser On and Off
                            || (!(t instanceof zmat.sessionparser.quarterparser.Trial)
                            && (lightOn == 2 || t.withLaserON() == (lightOn == 1)))) {

                        totalTrial++;
                        trialCount++;

                        switch (t.response) {
                            case Hit:
                                hit++;
                                rewardedLick += t.getResponseLick();
                                break;
                            case FalseAlarm:
                                fa++;
                                unrewardedLick += t.getResponseLick();
                                break;
                            case Miss:
                                miss++;
                                rewardedLick += t.getResponseLick();
                                break;
                            case CorrectRejection:
                                correctRejection++;
                                unrewardedLick += t.getResponseLick();
                                break;
                        }
                    }
                }
                if (totalTrial > 0) {
                    sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial, rewardedLick, unrewardedLick});
                }
            }
        }
//        System.out.println(totalTrial);
        return sessions.toArray(new int[sessions.size()][]);
    }

    public int[][] getCatchPerf(int catchTrial, int trialLimit) {
        ArrayList<int[]> sessions = new ArrayList<>();
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
                int correctRejection = 0;

                for (Trial t : s.trials) {
                    if (trialLimit > 0 && trialCount >= trialLimit) {
                        if (totalTrial > 0) {
                            sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial});
                        }
                        break day;
                    }
                    //Laser quarter and other cycles
                    if (((zmat.sessionparser.noodor_parser.Trial) t).isCatch() == (catchTrial == 1)) {

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
                            case CorrectRejection:
                                correctRejection++;
                                break;
                        }
                    }
                }
                if (totalTrial > 0) {
                    sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial});
                }
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
