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
    final private int sampleValue;
    final private int laserTypeValue;

    final private EventType dualDRResponse;
    final private int dualDRSample;
    final private int dualDRTest;

    public Seq2AFCTrial(EventType sample,
            int sampleValue,
            EventType test1,
            EventType test2,
            EventType response1,
            EventType response2,
            boolean laserON,
            int laserTypeValue,
            ArrayList<Integer[]> licks,
            int sampleStart,
            int test1Start,
            int test2Start,
            int dualSample,
            int dualTest,
            EventType dualResponse
    ) {
        super(sample, EventType.unknown, response1, laserON, licks, 0, sampleStart);
        this.test1 = test1;
        this.test2 = test2;
        this.response2 = response2;
        this.test1Start = test1Start;
        this.test2Start = test2Start;
        this.sampleValue = sampleValue;
        this.laserTypeValue=laserTypeValue;
        this.dualDRSample = dualSample;
        this.dualDRTest = dualTest;
        this.dualDRResponse = dualResponse;

    }

    public int getTest1() {
        return test1.ordinal();
    }

    @Override
    public int[] getFactors() {
        return new int[]{
            this.sampleValue, //mat 1
            this.getOdor2Start(), //mat 2
            this.test1.ordinal(), //mat 3
            this.test1Start, //mat 4
            this.test2.ordinal(), //mat 5
            this.test2Start, //mat 6
            this.response.ordinal(), //mat 7
            this.response2.ordinal(), //mat 8
            this.laserON ? 1 : 0, //mat 9
            this.laserTypeValue, //mat 10
            this.dualDRSample, //mat 11
            this.dualDRTest, //mat 12
            this.dualDRResponse.ordinal() //mat 13

        };
    }
}
