/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import zmat.dnms_session.DataProcessor;
import zmat.dnms_session.Day;
import zmat.dnms_session.FileParser;

/**
 *
 * @author Libra
 */
public class ZmatSeq2AFC extends Zmat {

    @Override
    public void processFile(String... s) {
        dp = new DataProcessor(){
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.sessionparser.seq2afcparser.Seq2AFCFileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
                for (Day d : days) {
                    d.testForWellTrainState();
                    d.removeBadSessions(fullSession, minLick);
                }
            }
        };
        dp.setMinLick(this.minLick);
        dp.setFullSession(fullSession);
        dp.processFile(s);
        days = dp.getDays();
    }

}
