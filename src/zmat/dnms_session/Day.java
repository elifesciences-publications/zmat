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
        Queue<Session> q = new LinkedList<>();
        for (Session session : sessions) {
//            System.out.println(session.getTrialNumber()+" trials");
            boolean sessionFull = (fullSession && session.getTrialNumber() == trialNum) || !fullSession;
            if (sessionFull && session.getLickCount() >= lickCount) {
                q.offer(session);
//                System.out.println("add Session");
            }
        }
        sessions = q;
        System.out.println(Integer.toString(sessions.size()) + " sessions.");

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
