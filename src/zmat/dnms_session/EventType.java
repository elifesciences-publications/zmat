/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.dnms_session;

/**
 *
 * @author Libra
 */
public enum EventType {
//      LICK,WATER,
      Lick,OdorA,OdorB,Hit,Miss,FalseAlarm,CorrectRejection,
      NewSession,Others,dnmsLaser,dnmsEarlyLaser,
      dnmsLateLaser,unknown,ABORT_TRIAL;
}
