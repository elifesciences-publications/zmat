/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import java.util.ArrayList;
import zmat.dnms_session.DataProcessor;
import zmat.dnms_session.Day;
import zmat.dnms_session.FileParser;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;

/**
 *
 * @author Libra
 */
public class ZmatEML extends Zmat {

    @Override
    public void processFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.sessionparser.emlparser.emlparser.FileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
            }
        };
        dp.setMinLick(this.minLick);
        dp.setFullSession(fullSession);
        dp.processFile(s);
    }

    @Override
    public int[][] getPerf(int lightOn, int trialLimit, boolean onlyWellTrained) {
        this.days = dp.getDays();
        ArrayList<int[]> sessions = new ArrayList<>();
        for (Day d : days) {
            if (d.getSessions() == null || d.getSessions().size() < 5 || (onlyWellTrained && !d.isWellTrained())) {
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

                for (Trial tt : s.getTrails()) {
                    zmat.sessionparser.emlparser.emlparser.Trial t = (zmat.sessionparser.emlparser.emlparser.Trial) tt;

                    if (trialLimit > 0 && trialCount >= trialLimit) {
                        if (totalTrial > 0) {
                            sessions.add(new int[]{hit, miss, fa, correctRejection, totalTrial, rewardedLick, unrewardedLick});
                        }
                        break day;
                    }
                    //Laser quarter and other cycles
                    if (t.getLaserType() == lightOn) {

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

}
