/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Xiaoxing
 */
public class DataProcessor {

    protected Queue<Day> days;
    protected int minLick = 4;

    public enum listType {

        CORRECT_RATE, FALSE_ALARM, MISS;
    }

    public void setMinLick(int minLick) {
        this.minLick = minLick;
    }

    public List<int[]> getList(Day d, listType type) {
        switch (type) {
            case CORRECT_RATE:
                return d.getCorrectRates();
            case FALSE_ALARM:
                return d.getFalseAlarmRates();
            case MISS:
                return d.getMissRates();
            default:
                return d.getCorrectRates();
        }
    }

    public void processFile(String... s) {
        FileParser fp = new FileParser();
        fp.parseFiles(s);
        days = fp.getDays();
        if (days.size() < 1) {
            System.out.println("No suitable records found.");
        }
        for (Day d : days) {
            d.removeBadSessions(20, true, minLick);
        }
        
    }

    public int[][] processList(DataProcessor.listType type) {
        List<int[]> values = new ArrayList<>();
        for (Day d : days) {
            values.addAll(getList(d, type));
        }
        int[][] intValues = new int[values.size()][];
        int idx = 0;
        for (int[] value : values) {
            intValues[idx++] = value;
        }
        return intValues;
    }

}
