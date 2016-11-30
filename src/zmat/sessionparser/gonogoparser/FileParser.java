/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.gonogoparser;

import java.io.File;
import java.util.Queue;
import zmat.dnms_session.Session;

/**
 *
 * @author Libra
 */
public class FileParser extends zmat.dnms_session.FileParser {
    @Override
    protected Queue<Session> processFile(File f) {
        if(f.getAbsolutePath().trim().endsWith(".txt")){
            zmat.sessionparser.gonogoparser.TxtFileParserGNG fp=new zmat.sessionparser.gonogoparser.TxtFileParserGNG();
            return fp.processFile(f);
        }else{
            FileParserSer fp=new FileParserSer();
            return fp.processFile(f);
        }
    }
}
