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

    final private int respCueStart;
    final private int sampleValue;
    final private int respCueValue;


    public ODRTrial(EventType sample,
            int sampleValue,
            int respCueValue,
            EventType response,
            boolean laserON,
            ArrayList<Integer[]> licks,
            int sampleStart,
            int respCueStart
    ) {
        super(sample, EventType.unknown, response, laserON, licks, respCueStart-sampleStart,respCueStart);
        this.respCueStart = respCueStart;
        this.sampleValue = sampleValue;
        this.respCueValue=respCueValue;
    }


    @Override
    public int[] getFactors() {
        return new int[]{
            this.sampleValue,
            this.respCueValue,
            this.response.ordinal(),
            this.laserON ? 1 : 0,
        };
    }
}
