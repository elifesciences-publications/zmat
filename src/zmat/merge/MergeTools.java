/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.merge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 *
 * @author Xiaoxing
 */
public class MergeTools {

    ArrayList<int[]> s1;
    ArrayList<int[]> s2;

    public void listSessions(String s) {
        int counter = 0;
        ArrayList<int[]> sess = file2list(s);
        for (int i = 0; i < sess.size(); i++) {
            int[] evt = sess.get(i);
            if (evt[2] == 61) {
                if (evt[3] == 1) {
                    counter++;
                    System.out.print("S" + counter + ", E[" + i + "]@" + evt[0]);
                } else if (evt[3] == 0) {
                    System.out.println(" ~ E[" + i + "]@" + evt[0]);
                }
            }
        }
    }

    private ArrayList<int[]> file2list(String f) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(f)))) {
//            @SuppressWarnings("unchecked")
            return (ArrayList<int[]>) ois.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public void mergeFile(String f1, int start1, int length1, String f2, int start2, int length2, int timeDiff, String f) {
        s1 = file2list(f1);
        s2 = file2list(f2);
        zmat.sessionparser.FileParser fp = new zmat.sessionparser.FileParser();
        fp.arrayList2ser(mergeSessions(start1, length1, start2, length2, timeDiff), f);
    }

    private ArrayList<int[]> mergeSessions(int start1, int length1, int start2, int length2, int timeDiff) {
        ArrayList<int[]> rtn = new ArrayList<>();
        for (int i = start1; i < start1 + length1; i++) {
            rtn.add(s1.get(i));
        }
        for (int i = start2; i < start2 + length2; i++) {
            int[] temp = s2.get(i);
            temp[0] -= timeDiff;
            rtn.add(temp);
        }
        rtn.add(new int[]{rtn.get(rtn.size() - 1)[0] + 10, 85, 62, 0, 170});
        return rtn;
    }
}
