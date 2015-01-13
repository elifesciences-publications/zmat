/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.lrparser;

import java.util.Queue;
import zmat.sessionparser.Day;

/**
 *
 * @author Xiaoxing
 */
public class LRDataProcessor extends zmat.sessionparser.DataProcessor {

    Queue<LRDay> days;

    @Override
    public void processFile(String... s) {
        LRFileParser fp = new LRFileParser();
        fp.parseFiles(s);
        days = fp.getDays();
        if (days.size() < 1) {
            System.out.println("No suitable records found.");
        }

        for (Day d : days) {
            d.removeBadSessions(20, true, minLick);
        }
    }

    @Override
    public Queue<LRDay> getDays() {
        return days;
    }
}
