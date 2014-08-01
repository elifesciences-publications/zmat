/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import java.util.LinkedList;
import java.util.Queue;
import zmat.sessionparser.Session;
import zmat.sessionparser.Trial;

/**
 *
 * @author Xiaoxing
 */
public class CyDay extends zmat.sessionparser.Day {

    protected CyDay(String fileName, Queue<CySession> q) {
        super(fileName, q);
        combineSessions();
    }

    private void combineSessions() {
        removeBadSessions(12, true, 3);
        LinkedList<CyTrial> q = new LinkedList<>();
        for (Session s : sessions) {
            for (Trial t : s.getTrails()) {
                q.add((CyTrial) t);
            }
        }
        sessions = new Session[]{new CySession(q)};
    }

}
