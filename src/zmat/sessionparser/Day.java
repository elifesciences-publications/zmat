/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Libra
 */
public class Day {

    protected Session[] sessions;
    protected String fileName;

    protected <T extends Session> Day(String fileName, Queue<T> q) {
        this.fileName = fileName;
        sessions = new Session[q.size()];
        for (int i = 0; i < sessions.length; i++) {
            sessions[i] = q.poll();
        }
    }

    public void removeBadSessions(int trialNum, boolean fullSession, int lickCount) {
        Queue<Session> q = new LinkedList<>();
        for (Session session : sessions) {
            boolean sessionFull = (fullSession && session.getTrialNumber() == trialNum) || !fullSession;
            if (sessionFull && session.getLickCount() >= lickCount) {
                q.offer(session);
            }
        }
        sessions = new Session[q.size()];
        for (int i = 0; i < sessions.length; i++) {
            sessions[i] = q.poll();
        }
    }

    public int getSessionNumber() {
        return sessions.length;
    }

    public List<int[]> getCorrectRates() {
        List<int[]> correctRates = new ArrayList<>();
//        System.out.println(sessions.length+"Sessions ");
        for (Session session : sessions) {
            correctRates.add(session.getCorrectRate());
        }
        return correctRates;
    }

    public List<int[]> getFalseAlarmRates() {
        List<int[]> falseAlarms = new ArrayList<>();
        for (Session session : sessions) {
            falseAlarms.add(session.getFalseAlarmRate());
        }
        return falseAlarms;
    }

    public List<int[]> getMissRates() {
        List<int[]> falseAlarms = new ArrayList<>();
        for (Session session : sessions) {
            falseAlarms.add(session.getMissRate());
        }
        return falseAlarms;
    }

    public String getFileName() {
        return fileName;
    }

}
