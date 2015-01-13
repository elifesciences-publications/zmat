/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.sessionparser.quarterparser;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Xiaoxing
 */
public class Zmat {

    private int minLick = 4;
    private DataProcessor dp;

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

    
    
    public void processFile(String... s) {
        dp = new DataProcessor();
        dp.setMinLick(this.minLick);
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
                    String fileName = f.getName();
                    boolean add = fileName.endsWith(".ser");

                    if (elements.length > 0) {
//                        if (!fileName.startsWith(elements[0])) {
//                            add = false;
//                        }
                        for (int i = 0; i < elements.length; i++) {
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
