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
 * @param <T>
 */
public class Day<T extends Session>{

    protected Queue<T> sessions;
    protected String fileName;

    public Day(String fileName, Queue<T> q) {
        this.fileName = fileName;
        sessions = q;
    }

    public void removeBadSessions(int trialNum, boolean fullSession, int lickCount) {
        Queue<T> q = new LinkedList<>();
        for (T session : sessions) {
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

    public List<int[]> getCorrectRates() {
        List<int[]> correctRates = new ArrayList<>();
//        System.out.println(sessions.length+"Sessions ");
        for (T session : sessions) {
            correctRates.add(session.getCorrectRate());
        }
        return correctRates;
    }

    public List<int[]> getFalseAlarmRates() {
        List<int[]> falseAlarms = new ArrayList<>();
        for (T session : sessions) {
            falseAlarms.add(session.getFalseAlarmRate());
        }
        return falseAlarms;
    }

    public List<int[]> getMissRates() {
        List<int[]> falseAlarms = new ArrayList<>();
        for (T session : sessions) {
            falseAlarms.add(session.getMissRate());
        }
        return falseAlarms;
    }

    public String getFileName() {
        return fileName;
    }

    public int[] getHitNFalse() {
        int hit = 0;
        int fa = 0;
        for (Session s : sessions) {
            int[] sCount = s.getHitNFalse();
            hit += sCount[0];
            fa += sCount[1];
        }
        return new int[]{hit, fa};
    }
}
