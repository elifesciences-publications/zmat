/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Xiaoxing
 */
public class CyDay extends zmat.sessionparser.Day<CySession> {

    protected CyDay(String fileName, Queue<CySession> q) {
        super(fileName, q);
        combineSessions();
    }

    private void combineSessions() {
        removeBadSessions(12, true, 3);
        LinkedList<CyTrial> q = new LinkedList<>();
        for (CySession s : sessions) {
            for (CyTrial t : s.getTrails()) {
                q.add(t);
            }
        }
        sessions = new LinkedList<>();
        sessions.add(new CySession(q));
    }

}
