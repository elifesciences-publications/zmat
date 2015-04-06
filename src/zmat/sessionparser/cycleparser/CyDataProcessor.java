/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.cycleparser;

import zmat.sessionparser.*;

/**
 *
 * @author Xiaoxing
 */
public class CyDataProcessor extends zmat.sessionparser.DataProcessor{

    
    
    @Override
    public void processFile(String... s) {
        FileParser fp = new CyFileParser(new CySession(null));
        fp.parseFiles(s);
        days = fp.getDays();
        if (days.size() < 1) {
            System.out.println("No suitable records found.");
        }
    }

}
