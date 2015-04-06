/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser;

import java.util.Queue;

/**
 *
 * @author Xiaoxing
 * @param <T>
 */
public class LRSession<T extends Trial> extends Session<T> {

    
    public LRSession(Queue<T> q) {
        super(q);
    }
    
    public LRSession getInstance(Queue<T> q){
        return new LRSession(q);
    }
    @Override
    protected int getTypeBaseRate(T trial, RateType type) {
        switch (type) {
            case performance:
                return 1;
            case falseAlarm:
                return (trial.response == EventType.FalseAlarm || trial.response == EventType.Hit) ? 1 : 0;
            case miss:
                return 1;
        }
        return 0;
    }
}
