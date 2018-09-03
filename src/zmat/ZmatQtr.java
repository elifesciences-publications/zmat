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
public class ZmatQtr extends Zmat {

    @Override
    public void processFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.sessionparser.quarterparser.FileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.isEmpty()) {
                    System.out.println("No suitable records found.");
                }
//                for (Day d : days) {
//                    d.testForWellTrainState();
//                    d.removeBadSessions(fullSession, minLick);
//
//                }
            }
        };
        dp.processFile(s);
    }

//            days = new LinkedList<>();
//        for (String f : s) {
//            FileParser fp = f.endsWith(".ser")
//                    ? new FileParser()
//                    : new zmat.txtFile.TxtFileParser();
//            fp.parseFiles(f);
//
//            days.addAll(fp.getDays());
//        }
//        if (days.isEmpty()) {
//            System.out.println("No suitable records found.");
//        }
//
//        for (Day d : days) {
//            d.testForWellTrainState();
//            d.removeBadSessions(fullSession, minLick);
//
//        }
}
