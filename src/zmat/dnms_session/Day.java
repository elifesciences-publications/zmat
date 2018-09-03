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

    protected WellTrainState isWellTrained = WellTrainState.UNKNOWN;

    private enum WellTrainState {
        TRUE, FALSE, UNKNOWN;
    }

    public Day(String fileName, Queue<Session> q) {
        this.fileName = fileName;
        sessions = q;
    }

    public void removeBadSessions(int trialNum, int lickCount) {
        zmat.debugger.log(10, trialNum + " minimum trial/session");
        zmat.debugger.log(10, lickCount + " minimum lick attempt");
        Queue<Session> q = new LinkedList<>();
        if (null == sessions || sessions.isEmpty()) {
            return;
        }
        zmat.debugger.log(10, Integer.toString(sessions.size()) + " sessions before remove bad sessons");
        for (Session session : sessions) {
//            System.out.println(session.getTrialNumber()+" trials");
//            zmat.debugger.log(5, session.getTrialNumber() + " trials in a session");
            boolean sessionFull = trialNum < 1 || (session.getTrialNumber() == trialNum);
            if (sessionFull && session.getLickCount() >= lickCount) {
                q.offer(session);
//                System.out.println("add Session");
            } else {
//                System.out.println("Removed bad session.");
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

    public boolean isWellTrained() {
        if (this.isWellTrained == WellTrainState.UNKNOWN) {
            throw new IllegalStateException("Welltrain state not tested.");
        }
        return this.isWellTrained == WellTrainState.TRUE;
    }

    public void testForWellTrainState() {
        LinkedList<Trial> consecutive = new LinkedList<>();
        outer:
        for (Session s : this.sessions) {
            for (Trial t : s.trials) {
//                if (t.withLaserON()) {
//                    continue;
//                }
                consecutive.add(t);
                if (consecutive.size() >= 40) {
                    int sumCorrect = 0;
                    for (Trial tt : consecutive) {
                        if (tt.isGoodChoice()) {
                            sumCorrect++;
                        }
                    }
                    if (sumCorrect >= 32) {
                        isWellTrained = WellTrainState.TRUE;
                        break outer;
                    } else {
                        consecutive.pop();
                    }
                }
            }
        }
        if (this.isWellTrained == WellTrainState.UNKNOWN) {
            this.isWellTrained = WellTrainState.FALSE;
        }
    }
}
