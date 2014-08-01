/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat;

import java.io.File;
import java.util.ArrayList;
import zmat.sessionparser.DataProcessor;

/**
 *
 * @author Xiaoxing
 */
public class Zmat {

    private DataProcessor dp;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }

    public Zmat() {
        System.out.println("zmat ver 1.1");
    }

    public void processFile(String... s) {
        dp = new DataProcessor();
        dp.processFile(s);
    }

    public void processCyFile(String... s) {
        dp = new zmat.sessionparser.cycleparser.CyDataProcessor();
        dp.processFile(s);
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
                    boolean add = true;
                    String fileName = f.getName();
                    if (elements.length > 0) {
                        if (!fileName.startsWith(elements[0])) {
                            add = false;
                        }
                        for (int i = 1; i < elements.length; i++) {
                            if (!fileName.contains(elements[i])) {
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
//        for (String s : elements) {
//            System.out.println("[" + s + "]");
//        }
        if (rootPath.length() < 1) {
            rootPath = "I:\\Behavior\\2014\\";
        }
        ArrayList<String> fileList = listFilesList(rootPath, elements);
        return fileList.toArray(new String[fileList.size()]);
    }

}
