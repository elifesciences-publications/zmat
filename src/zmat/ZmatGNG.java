/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import zmat.dnms_session.DataProcessor;
import zmat.dnms_session.FileParser;

/**
 *
 * @author Libra
 */
public class ZmatGNG extends Zmat {

    @Override
    public void processFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.sessionparser.gonogoparser.FileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
            }
        };
        dp.setMinLick(this.minLick);
        dp.setFullSession(fullSession);
        dp.processFile(s);
    }
}
