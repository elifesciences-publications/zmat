/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import java.util.ArrayList;
import zmat.dnms_session.DataProcessor;
import zmat.dnms_session.Day;
import zmat.dnms_session.EventType;
import zmat.dnms_session.FileParser;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;

/**
 *
 * @author Xiaoxing
 */
public class ZmatDual extends Zmat {

    @Override
    public void processFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.sessionparser.dualtaskparser.DualParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
                for (Day d : days) {
                    d.testForWellTrainDay();
                    d.removeBadSessions(fullSession, minLick);
                }
            }
        };
        dp.setFullSession(fullSession);
        dp.setMinLick(this.minLick);
        dp.processFile(s);
    }

    public int[][] getPerf(int lightOn, int distracted, int trialLimit) {
        this.days = dp.getDays();
        ArrayList<int[]> sessions = new ArrayList<>();
        for (Day d : days) {
            if (d.getSessions() == null || d.getSessions().size() < 1) {
                continue;
            }
            int trialCount = 0;

            day:
            for (Session s : d.getSessions()) {
                int hit = 0;
                int fa = 0;
                int miss = 0;
                int totalTrial = 0;
                int correctRejection = 0;
                int rewardedLick = 0;
                int unrewardedLick = 0;
                int distractroHit = 0;
                int distractroMiss = 0;
                int distractroFA = 0;
                int distractroCR = 0;

                for (Trial tt : s.getTrails()) {
                    zmat.sessionparser.dualtaskparser.DualTrial t = (zmat.sessionparser.dualtaskparser.DualTrial) tt;
                    if (trialLimit > 0 && trialCount >= trialLimit) {
                        if (totalTrial > 0) {
                            sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial, rewardedLick, unrewardedLick,
                                distractroHit, distractroMiss, distractroFA, distractroCR});
                        }
                        break day;
                    }
                    //Laser quarter and other cycles
                    boolean trialDistracted = (distracted == 2) || ((distracted == 0) == (t.getDistractorResponse() == EventType.unknown));
                    boolean trialLightOn = lightOn == 2 || t.withLaserON() == (lightOn == 1);
                    if (trialLightOn & trialDistracted) {

                        totalTrial++;
                        trialCount++;

                        switch (t.getResponse()) {
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

                        switch (t.getDistractorResponse()) {
                            case Hit:
                                distractroHit++;
                                break;
                            case FalseAlarm:
                                distractroFA++;
                                break;
                            case Miss:
                                distractroMiss++;
                                break;
                            case CorrectRejection:
                                distractroCR++;
                                break;
                        }

                    }
                }
                if (totalTrial > 0) {
                    sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial, rewardedLick, unrewardedLick,
                        distractroHit, distractroMiss, distractroFA, distractroCR});
                }
            }
        }
//        System.out.println(totalTrial);
        return sessions.toArray(new int[sessions.size()][]);
    }

    @Override
    public int[][] getTrialLick(int trialNum) {
        this.days = dp.getDays();
        ArrayList<int[]> allTrials = new ArrayList<>();
        if (days == null) {
            return null;
        }

        for (Day d : days) {
            int trialCount = 0;
            int matchCount = 0;
            int nmCount = 0;
            day:
            for (Session s : d.getSessions()) {
                for (Trial tt : s.getTrails()) {
                    zmat.sessionparser.dualtaskparser.DualTrial t = (zmat.sessionparser.dualtaskparser.DualTrial) tt;
                    int currentCount;
                    if (trialNum > 0 && trialCount >= trialNum) {
                        break day;
                    }

                    if (t.getResponse() == EventType.Hit || t.getResponse() == EventType.Miss) {
                        matchCount++;
                        currentCount = matchCount;
                    } else {
                        nmCount++;
                        currentCount = nmCount;
                    }

                    if (t.getAllLick().length == 0) {
//                        allTrials.add(new int[]{currentCount, 65535, t.getResponse().ordinal() - 3, t.withLaserON() ? 1 : 0, t.getDistractorResponse().ordinal() - 3});
                        allTrials.add(new int[]{currentCount, 65535, t.getResponse().ordinal() - 3, t.withLaserON() ? 1 : 0, t.getDistractorOdor().ordinal()});
                    } else {
                        for (int i : t.getAllLick()) {
//                            allTrials.add(new int[]{currentCount, i, t.getResponse().ordinal() - 3, t.withLaserON() ? 1 : 0, t.getDistractorResponse().ordinal() - 3});
                            allTrials.add(new int[]{currentCount, i, t.getResponse().ordinal() - 3, t.withLaserON() ? 1 : 0, t.getDistractorOdor().ordinal()});
                        }
                    }
                    trialCount++;
                }
            }
        }
        return allTrials.toArray(new int[allTrials.size()][]);
    }

}
