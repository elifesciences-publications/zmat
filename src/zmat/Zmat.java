/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import zmat.dnms_session.DataProcessor;
import zmat.dnms_session.Day;
import zmat.dnms_session.FileParser;
import zmat.dnms_session.Session;
import zmat.dnms_session.Trial;
import zmat.sessionparser.cycleparser.CyFileParser;

/**
 *
 * @author Xiaoxing
 */
public class Zmat {

    private int minLick = 16;
    private DataProcessor dp;
    private Queue<Day> days;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }

    public Zmat() {
        System.out.println("zmat ver 1.11");
    }

    public void setMinLick(int minLick) {
        this.minLick = minLick;
    }

    public int[][] ser2mat(String s) {
        return (new zmat.dnms_session.FileParser()).getRawMat(s);
    }

    public void mat2ser(int[][] mat, String pathToFile) {
        (new zmat.dnms_session.FileParser()).mat2ser(mat, pathToFile);
    }

//    public Integer[][][] getLicks(boolean... laserOn) {
//        if (laserOn.length > 1) {
//            System.out.println("Wrong parameter (boolean laser on)");
//            return null;
//        }
//        ArrayList<Integer[][]> alldays = new ArrayList<>();
//        if (days != null) {
//            for (Day d : days) {
//                for (Session s : d.getSessions()) {
//                    for(Trial t:s.getTrails()){
//                        ((zmat.lick_session.LickTrial) t).getLicks();
//                    }    
//                    
//                }
////TODO                alldays.addAll(((zmat.sessionparser.lickParser.LickDay) d).getLicks());
//            }
//            return alldays.toArray(new Integer[days.size()][][]);
//        }
//        return null;
//    }
    public int[][] getLick() {
        ArrayList<int[]> allTrials = new ArrayList<>();
        if (days == null) {
            return null;
        }
        for (Day d : days) {
            for (Session s : d.getSessions()) {
                for (Trial t : s.getTrails()) {
                    allTrials.add(((zmat.lick_session.LickTrial) t).getDelayLick());
                }
            }
        }
        return allTrials.toArray(new int[allTrials.size()][]);
    }

    public void processFile(String... s) {
        dp = new DataProcessor();
        dp.setMinLick(this.minLick);
        dp.processFile(s);
    }

    public void processLRFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.lr_session.LRFileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
                for (Day d : days) {
                    d.removeBadSessions(20, true, minLick);
                }
            }
        };
        dp.setMinLick(this.minLick);
        dp.processFile(s);
    }

    public void processCyFile(String... s) {
        dp = new DataProcessor() {
            @Override
            public void processFile(String... s) {
                FileParser fp = new CyFileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
            }
        };
        dp.setMinLick(this.minLick);
        dp.processFile(s);
    }

    public void processLickFile(String... s) {
        dp = new DataProcessor() {

            @Override
            public void processFile(String... s) {
                FileParser fp = new zmat.lick_session.LickFileParser();
                fp.parseFiles(s);
                days = fp.getDays();
                if (days.size() < 1) {
                    System.out.println("No suitable records found.");
                }
                for (Day d : days) {
                    d.removeBadSessions(20, true, minLick);
                }
            }

        };
        dp.setMinLick(this.minLick);

        dp.processFile(s);
        days = new LinkedList<>();
        days.addAll(dp.getDays());
    }

    public int[] getHitFalseMiss(boolean lightOn) {
        return dp.getHitFalseMiss(lightOn);
    }

//    public int[][] cr() {
//        return dp.processList(DataProcessor.listType.CORRECT_RATE);
//    }
//
//    public int[][] fa() {
//        return dp.processList(DataProcessor.listType.FALSE_ALARM);
//    }
//
//    public int[][] miss() {
//        return dp.processList(DataProcessor.listType.MISS);
//    }
    private ArrayList<String> listFilesList(String rootPath, String[] elements) {
        ArrayList<String> fileList = new ArrayList<>();
        if (rootPath == null) {
            return null;
        }
        File root = new File(rootPath);
        if (!root.exists()) {
            return null;
        }
        File[] list = root.listFiles();

        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    fileList.addAll(listFilesList(f.getAbsolutePath(), elements));
                } else {
                    String fileName = f.getName();
                    boolean add = fileName.endsWith(".ser");

                    if (elements.length > 0) {
                        for (String element : elements) {

                            if (element.startsWith("-")
                                    ? fileName.contains(element.substring(1))
                                    : !fileName.contains(element)) {
                                add = false;
                            }
                        }
                    }
                    if (add) {
                        fileList.add(f.getPath());
                    }
                }
            }
        }

        return fileList;
    }

    public String[] listFiles(String rootPath, String... elements) {
        if (rootPath.length() < 1) {
            rootPath = "I:\\Behavior\\2014\\";
        }
        ArrayList<String> fileList = listFilesList(rootPath, elements);
        return fileList.toArray(new String[fileList.size()]);
    }

}
