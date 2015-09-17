/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zmat.txtFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import zmat.dnms_session.EventType;

/**
 *
 * @author Libra
 */
public class EventParser {

    private ArrayList<Integer[]> licks = new ArrayList<>();

    protected static EventType parseEvent(String line) {
        if (line.isEmpty()) {
            return EventType.NewSession;
        }
        String[] splitLine;
        splitLine = line.split(":|\\s+");

        EventType type;
        switch (splitLine[splitLine.length - 1]) {
            case "Lick":
                type = EventType.Lick;
                break;
            case "OdorA":
                type = EventType.OdorA;
                break;
            case "OdorB":
                type = EventType.OdorB;
                break;
            case "Miss":
                type = EventType.Miss;
                break;
            case "Correct":
                type = EventType.CorrectRejection;
                break;
            case "Wrong":
                type = EventType.FalseAlarm;
                break;
            case "Right":
                type = EventType.Hit;
                break;
            case "1":
//                        System.out.println(splitLine[splitLine.length-2]);
                switch (splitLine[splitLine.length - 2]) {
                    case "Miss":
                        type = EventType.Miss;
                        break;
                    case "CR":
                        type = EventType.CorrectRejection;
                        break;
                    case "FA":
                        type = EventType.FalseAlarm;
                        break;
                    case "Right":
                        type = EventType.Hit;
                        break;
                    default:
                        type = EventType.Others;
                }
                break;
            default:
                type = EventType.Others;
        }
        return type;
    }

    protected static int parseTime(String line) {
        if (line.isEmpty()) {
            return 0;
        }
        String[] splitLine;
        splitLine = line.split(":|\\s+");

        int[] timeNumbers = new int[4];
        for (int i = 4; i > 0; i--) {
            if (splitLine.length > i && splitLine[i].matches("\\d+")) {
                timeNumbers[4 - i] = Integer.parseInt(splitLine[i]);
            }
        }
        int absTime = timeNumbers[0]
                + timeNumbers[1] * 1000
                + timeNumbers[2] * 60 * 1000
                + timeNumbers[3] * 3600 * 1000;
        return absTime;
    }

    public static List<Event> getEventList(File f) {
        List<Event> eventList = new ArrayList<>(500);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {

                int absTime = EventParser.parseTime(line);
                EventType type = EventParser.parseEvent(line);

                if (type != EventType.Others) {
                    eventList.add(new Event(absTime, type));
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return eventList;
    }
}
