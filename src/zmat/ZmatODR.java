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
public class ZmatODR extends Zmat {

    @Override
    public void processFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.sessionparser.odrParser.ODRFileParser();
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
        dp.setMinLick(this.minLick);
        dp.setFullSession(fullSession);
        dp.processFile(s);
        days = dp.getDays();
    }

    public int[][][] getTrialLick() {
        ArrayList<int[][]> rtn = new ArrayList<>();
        for (Day day : dp.getDays()) {
            for (Session sess : day.getSessions()) {
                for (Trial t : sess.getTrails()) {
                    rtn.add(((zmat.sessionparser.odrParser.ODRTrial)t).get2AFCLick());
                }
            }
        }
        return rtn.toArray(new int[rtn.size()][][]);
    }
}
