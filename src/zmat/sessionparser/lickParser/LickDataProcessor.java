/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.lickParser;

import zmat.sessionparser.Day;

/**
 *
 * @author Xiaoxing
 */
public class LickDataProcessor extends zmat.sessionparser.DataProcessor {

    @Override
    public void processFile(String... s) {
        LickFileParser fp = new LickFileParser();
        fp.parseFiles(s);
        days = fp.getDays();
        if (days.size() < 1) {
            System.out.println("No suitable records found.");
        }
        for (Day d : days) {
            d.removeBadSessions(20, true, minLick);
        }
    }

}
