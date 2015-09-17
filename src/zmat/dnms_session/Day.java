/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.List;
import java.util.Queue;

/**
 * @author Libra
 */
public class Day {

    protected Queue<Session> sessions;
    protected String fileName;

    public Day(String fileName, Queue<Session> q) {
        this.fileName = fileName;
        sessions = q;
    }

    public void removeBadSessions(int trialNum, boolean fullSession, int lickCount) {
        zmat.debugger.log(10, trialNum + " minimum trial/session");
        zmat.debugger.log(10, lickCount + " minimum lick attempt");
        Queue<Session> q = new LinkedList<>();
        if (null == sessions) {
            return;
        }
        for (Session session : sessions) {
//            System.out.println(session.getTrialNumber()+" trials");
            zmat.debugger.log(5, session.getTrialNumber() + " trials in a session");
            boolean sessionFull = (fullSession && session.getTrialNumber() == trialNum) || !fullSession;
            if (sessionFull && session.getLickCount() >= lickCount) {
                q.offer(session);
//                System.out.println("add Session");
            }
        }
        sessions = q;
        zmat.debugger.log(10, Integer.toString(sessions.size()) + " sessions after remove bad sessons");
    }

    public int getSessionNumber() {
        return sessions.size();
    }

    public String getFileName() {
        return fileName;
    }

    public Queue<Session> getSessions() {
        return sessions;
    }

}
