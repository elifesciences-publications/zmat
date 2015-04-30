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
      OdorA,OdorB,Miss,Hit,CorrectRejection,FalseAlarm,
      NewSession,Others,dnmsLaser,dnmsEarlyLaser,
      dnmsLateLaser,unknown;
}
