/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Libra
 */
public class Day {

    private Session[] sessions;
    final String fileName;

    Day(String fileName, Queue<Session> q) {
        this.fileName = fileName;
        sessions = new Session[q.size()];
        for (int i = 0; i < sessions.length; i++) {
            sessions[i] = q.poll();
        }
    }

    public void removeBadSessions(boolean fullSession, int lickCount) {
        Queue<Session> q = new LinkedList<>();
        for (int i = 0; i < sessions.length; i++) {
            boolean sessionFull = (fullSession && sessions[i].getTrialNumber() ==20 ) || !fullSession;
            if (sessionFull && sessions[i].getLickCount() >= lickCount) {
                q.offer(sessions[i]);
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
        for (int i = 0; i < sessions.length; i++) {
            correctRates.add(sessions[i].getCorrectRate());
        }
        return correctRates;
    }
    public List<int[]> getFalseAlarmRates() {
        List<int[]> falseAlarms = new ArrayList<>();
        for (int i = 0; i < sessions.length; i++) {
            falseAlarms.add(sessions[i].getFalseAlarmRate());
        }
        return falseAlarms;
    }
    public List<int[]> getMissRates() {
        List<int[]> falseAlarms = new ArrayList<>();
        for (int i = 0; i < sessions.length; i++) {
            falseAlarms.add(sessions[i].getMissRate());
        }
        return falseAlarms;
    }

    public String getFileName() {
        return fileName;
    }
    
    
        public double[] getCorrectRateByDay() {
        List<int[]> list = getCorrectRates();
        int exp = 0;
        int ctl = 0;
        for (int[] pair : list) {
            exp += pair[0];
            ctl += pair[1];
        }
        double[] avg={(double) exp/list.size(), (double)ctl/list.size()};
        return avg;
    }
    public double[] getFalseAlarmRateByDay() {
        List<int[]> list = getFalseAlarmRates();
        int exp = 0;
        int ctl = 0;
        for (int[] pair : list) {
            exp += pair[0];
            ctl += pair[1];
        }
        double[] avg={(double) exp/list.size(), (double)ctl/list.size()};
        return avg;
    }
    public double[] getMissRateByDay() {
        List<int[]> list = getMissRates();
        int exp = 0;
        int ctl = 0;
        for (int[] pair : list) {
            exp += pair[0];
            ctl += pair[1];
        }
        double[] avg={(double) exp/list.size(), (double)ctl/list.size()};
        return avg;
    }
}
