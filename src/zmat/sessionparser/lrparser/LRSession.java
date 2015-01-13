/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.lrparser;

import java.util.Queue;
import zmat.sessionparser.EventType;
import zmat.sessionparser.Trial;

/**
 *
 * @author Xiaoxing
 */
public class LRSession extends zmat.sessionparser.Session<Trial> {

    public LRSession(Queue<Trial> q) {
        super(q);
    }

    public int getBias() {
        int falseL = 0;
        int falseR = 0;
        for (Trial t : trials) {
            if (t.getResponse() == EventType.FalseAlarm) {
                if (t.getFirstOdor() == t.getSecondOdor()) {
                    falseR++;
                } else {
                    falseL++;
                }
            }
        }

        return (falseL + falseR) == 0 ? 50 : falseL * 100 / (falseL + falseR);
    }

    public int getHitRate() {
        int hit = 0;
        int fa = 0;
        for (Trial t : trials) {
            hit += t.getResponse() == EventType.Hit ? 1 : 0;
            fa += t.getResponse() == EventType.FalseAlarm ? 1 : 0;
        }
        return hit * 100 / (hit + fa);
    }
}
