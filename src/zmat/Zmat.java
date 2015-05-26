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

    private int minLick = 0;
    private DataProcessor dp;
    private Queue<Day> days;
    private ArrayList<String> rootFiles;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }

    public Zmat() {
        System.out.println("zmat ver 1.61");
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

    public int[][] getLick(int trialNum) {
        ArrayList<int[]> allTrials = new ArrayList<>();

        if (days == null) {
            return null;
        }

        for (Day d : days) {
            int trialCount = 0;
            day:
            for (Session s : d.getSessions()) {

                for (Trial t : s.getTrails()) {
                    if (trialNum > 0 && trialCount >= trialNum) {
                        break day;
                    }
                    allTrials.add(((zmat.lick_session.LickTrial) t).getDelayLick());
                    trialCount++;
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

    private void processCyFile(String... s) {
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
        days = new LinkedList<>();
        for (String f : s) {
            dp = new DataProcessor() {
                @Override
                public void processFile(String... s) {
                    FileParser fp = s[0].endsWith(".ser")
                            ? new zmat.lick_session.LickFileParser()
                            : new zmat.txtFile.TxtFileParser();
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
            dp.processFile(new String[]{f});
            days.addAll(dp.getDays());
        }
    }


    public int[][] getPerf(boolean lightOn, int trialLimit) {
        return dp.getPerf(lightOn, trialLimit);
    }

    public ArrayList<String> updateFilesList(String[] rootPath) {
        if (rootPath == null || rootPath.length == 0) {
            return null;
        }
        ArrayList<String> fileList = new ArrayList<>();
        for (String onePath : rootPath) {
            File root = new File(onePath);
            if (!root.exists()) {
                continue;
            }
            File[] list = root.listFiles();

            if (list != null) {
                for (File f : list) {
                    if (f.isDirectory()) {
                        fileList.addAll(updateFilesList(new String[]{f.getAbsolutePath()}));
                    } else if (f.getName().endsWith(".ser") || f.getName().endsWith("Process.txt")) {
                        fileList.add(f.getPath());
                    }
                }
            }

            this.rootFiles = fileList;
        }
        return fileList;
    }

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
                    boolean add = fileName.endsWith(".ser") || fileName.endsWith("Process.txt");

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

    public String[] listFiles(String rootPath, String[] elements) {
        if (rootPath.length() < 1) {
            rootPath = "I:\\Behavior\\2014\\";
        }
        ArrayList<String> fileList = listFilesList(rootPath, elements);
        return fileList.toArray(new String[fileList.size()]);
    }

    public String[] listFiles(String[] elements) {
        Queue<String> files = new LinkedList<>();
        file:
        for (String s : this.rootFiles) {
            for (String e : elements) {
                if ((!e.startsWith("-")) && !s.contains(e)) {
                    continue file;
                }
                if (e.startsWith("-") && s.contains(e.substring(1))) {
                    continue file;
                }
            }
            files.add(s);
        }
        return files.toArray(new String[files.size()]);
    }

}
