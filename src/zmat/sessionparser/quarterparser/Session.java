/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;

import java.util.Queue;
import zmat.dnms_session.Trial;

/**
 *
 * @author Libra
 */
public class Session extends zmat.dnms_session.Session{

    public Session(Queue<Trial> q) {
        super(q);
    }

}
