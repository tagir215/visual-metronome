package com.example.artmetronome;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;

public class Notes {
    LinkedHashMap<Double,String> notes;
    Notes(){
        notes = new LinkedHashMap<Double,String>() {{
            put(41.20,"E1");
            put(43.65,"F1");
            put(46.25,"F#1");
            put(49.25,"G1");
            put(51.91,"G#1");
            put(55.00,"A1");
            put(58.27,"A#1");
            put(61.74,"B1");
            put(65.41,"C2");
            put(69.30,"C#2");
            put(73.42,"D2");
            put(77.78,"D#2");
            put(82.41,"E2");
            put(87.31,"F2");
            put(92.50,"F#2");
            put(98.00,"G2");
            put(103.83,"G#2");
            put(110.00,"A2");
            put(116.54,"A#2");
            put(123.47,"B2");
            put(130.81,"C3");
            put(138.59,"C#3");
            put(146.83,"D3");
            put(155.56,"D#3");
            put(164.81,"E3");
            put(174.61,"F3");
            put(185.00,"F#3");
            put(196.00,"G3");
            put(207.65,"G#3");
            put(220.00,"A3");
            put(233.08,"A#3");
            put(246.94,"B3");
            put(261.63,"C4");
            put(277.18,"C#4");
            put(293.66,"D4");
            put(311.13,"D#4");
            put(329.99,"E4");
            put(349.23,"F4");
            put(369.99,"F#4");
            put(392.00,"G4");
            put(415.30,"G#4");
            put(440.00,"A4");
            put(466.16,"A#4");
            put(493.88,"B4");
            put(523.25,"C5");
            put(554.37,"C#5");
            put(587.33,"D5");
            put(622.25,"D#5");
            put(659.25,"E5");
            put(698.46,"F5");
            put(739.99,"F#5");
            put(783.99,"G5");
            put(830.61,"G#5");
            put(880.00,"A5");
            put(932.33,"A#5");
            put(987.77,"B5");
            put(1046.50,"C6");
            put(1108.73,"C#6");
            put(1174.66,"D6");
            put(1244.51,"D#6");
            put(1318.51,"E6");
            put(1396.91,"F6");
            put(1479.98,"F#6");
            put(1567.98,"G6");
            put(1661.22,"G#6");
            put(1760.00,"A6");
            put(1864.66,"A#6");
            put(1975.53,"B6");
            put(2093.00,"C7");
        }};

    }

    Object[] getNote(double freq){
        double lowestDistance = 10000;
        String note = "empty";
        float position = 0;
        float i=0;
        for (Double key : notes.keySet()){
            double distance = Math.abs(key-freq);
            if (distance<lowestDistance){
                note = notes.get(key);
                lowestDistance = distance;
                position = i;
            }
            i++;

        }

        return new Object[] {note,position};
    }
}
