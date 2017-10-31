/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.seq2afcparser;

import java.util.ArrayList;
import zmat.dnms_session.EventType;

/**
 *
 * @author casel_000
 */
public class Seq2AFCTrial extends zmat.dnms_session.Trial {

    final private EventType response2;
    final private EventType test1;
    final private EventType test2;
    final private int test1Start;
    final private int test2Start;

    public Seq2AFCTrial(EventType sample,
            EventType test1,
            EventType test2,
            EventType response1,
            EventType response2,
            boolean laserON,
            ArrayList<Integer[]> licks,
            int sampleStart,
            int test1Start,
            int test2Start) {
        super(sample, EventType.unknown, response1, laserON, licks, 0, sampleStart);
        this.test1 = test1;
        this.test2 = test2;
        this.response2 = response2;
        this.test1Start = test1Start;
        this.test2Start = test2Start;
    }

    public int getTest1() {
        return test1.ordinal();
    }
    
    @Override
    public int[] getFactors() {
        return new int[]{this.firstOdor.ordinal(),
            this.test1.ordinal(),
            this.test2.ordinal(),
            this.response.ordinal(),
            this.response2.ordinal()
        };
    }
}
