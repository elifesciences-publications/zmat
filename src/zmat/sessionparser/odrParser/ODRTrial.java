/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.odrParser;

import java.util.ArrayList;
import zmat.dnms_session.EventType;

/**
 *
 * @author casel_000
 */
public class ODRTrial extends zmat.dnms_session.Trial {

    final private int respCueOnset;
    final private int sampleOnset;
    final private int sampleValue;
    final private int respCueValue;

    public ODRTrial(EventType sample,
            int sampleValue,
            int respCueValue,
            EventType response,
            boolean laserON,
            ArrayList<Integer[]> licks,
            int sampleOnset,
            int respCueOnset
    ) {
        super(sample, EventType.unknown, response, laserON, licks, respCueOnset - sampleOnset, sampleOnset);
        this.respCueOnset = respCueOnset;
        this.sampleValue = sampleValue;
        this.respCueValue = respCueValue;
        this.sampleOnset = sampleOnset;
    }

    @Override
    public int[] getFactors() {
        return new int[]{
            this.sampleValue,
            this.respCueValue,
            this.response.ordinal(),
            this.laserON ? 1 : 0,
            this.sampleOnset
        };
    }


    public int[][] get2AFCLick() {
        int[][] all = new int[licks.size()][2];
        for (int i = 0; i < licks.size(); i++) {
            all[i] = new int[]{licks.get(i)[0] - respCueOnset,licks.get(i)[1]};
        }
        return all;
    }
}
