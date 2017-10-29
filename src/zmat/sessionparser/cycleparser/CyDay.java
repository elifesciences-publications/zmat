/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import java.util.LinkedList;
import java.util.Queue;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;

/**
 *
 * @author Xiaoxing
 */
public class CyDay extends zmat.dnms_session.Day {

    protected CyDay(String fileName, Queue<Session> q) {
        super(fileName, q);
        combineSessions();
    }

    private void combineSessions() {
        removeBadSessions(12, 3);
        LinkedList<Trial> q = new LinkedList<>();
        for (Session s : sessions) {
            for (Trial t : s.getTrails()) {
                q.add(t);
            }
        }
        sessions = new LinkedList<>();
        sessions.add(new CySession(q));
    }

}
