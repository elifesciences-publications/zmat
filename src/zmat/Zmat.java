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
import zmat.sessionparser.DataProcessor;
import zmat.sessionparser.Day;
import zmat.sessionparser.Session;

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
        System.out.println("zmat ver 1.1");
    }

    public void setMinLick(int minLick) {
        this.minLick = minLick;
    }

    public int[][] ser2mat(String s) {
        return (new zmat.sessionparser.FileParser()).getRawMat(s);
    }

    public void mat2ser(int[][] mat, String pathToFile) {
        (new zmat.sessionparser.FileParser()).mat2ser(mat, pathToFile);
    }

    public int[][] hitRate() {
        ArrayList<int[]> alldays = new ArrayList<>();
        if (days != null) {
            for (Day d : days) {
                alldays.add(((zmat.sessionparser.lrparser.LRDay) d).getHitRate());
            }
            return alldays.toArray(new int[days.size()][]);
        }
        return null;
    }

    public int[][] bias() {
        ArrayList<int[]> alldays = new ArrayList<>();
        if (days != null) {
            for (Day d : days) {
                alldays.add(((zmat.sessionparser.lrparser.LRDay) d).getBias());
            }
            return alldays.toArray(new int[days.size()][]);
        }
        return null;
    }

    public Integer[][][] getLicks() {
        ArrayList<Integer[][]> alldays = new ArrayList<>();
        if (days != null) {
            for (Day d : days) {
                alldays.addAll(((zmat.sessionparser.lickParser.LickDay) d).getLicks());
            }
            return alldays.toArray(new Integer[days.size()][][]);
        }
        return null;
    }

    public Integer[][][] getLicks(boolean laserOn) {
        ArrayList<Integer[][]> alldays = new ArrayList<>();
        if (days != null) {
            for (Day d : days) {
                alldays.addAll(((zmat.sessionparser.lickParser.LickDay) d).getLicks(laserOn));
            }
            return alldays.toArray(new Integer[days.size()][][]);
        }
        return null;
    }

    public Float[] getLickFreq(boolean laserOn) {
        ArrayList<Float> alldays = new ArrayList<>();
        if (days != null) {
            for (Day d : days) {
                alldays.addAll(((zmat.sessionparser.lickParser.LickDay) d).getLickFreq(laserOn));
            }
            return alldays.toArray(new Float[alldays.size()]);
        }
        return null;
    }

    public void processFile(String... s) {
        dp = new DataProcessor(new Session(null));
        dp.setMinLick(this.minLick);
        dp.processFile(s);
    }

    public void processCyFile(String... s) {
        dp = new zmat.sessionparser.cycleparser.CyDataProcessor();
        dp.setMinLick(this.minLick);
        dp.processFile(s);
    }

    public void processLRFile(String... s) {
        dp = new zmat.sessionparser.lrparser.LRDataProcessor();
        dp.setMinLick(this.minLick);
        dp.processFile(s);
        days = new LinkedList<>();
        days.addAll(dp.getDays());
    }

    public void processLickFile(String... s) {
        dp = new zmat.sessionparser.lickParser.LickDataProcessor();
        dp.setMinLick(this.minLick);
        dp.processFile(s);
        days = new LinkedList<>();
        days.addAll(dp.getDays());
    }

    public int[] getHitNFalse() {
        return dp.getHitNFalse();
    }

    public int[][] cr() {
        return dp.processList(DataProcessor.listType.CORRECT_RATE);
    }

    public int[][] fa() {
        return dp.processList(DataProcessor.listType.FALSE_ALARM);
    }

    public int[][] miss() {
        return dp.processList(DataProcessor.listType.MISS);
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
                    boolean add = fileName.endsWith(".ser");

                    if (elements.length > 0) {
                        for (String element : elements) {
                            if (!fileName.contains(element)) {
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
