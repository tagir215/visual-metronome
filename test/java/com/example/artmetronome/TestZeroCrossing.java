package com.example.artmetronome;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestZeroCrossing {
    @Test
    public void testGetZeroCrossings(){
        List<Float> zeroCrossings = ZeroCrossing.getZeroCrossings(createSample(),1);
        System.out.println(zeroCrossings.size()+" is size");
        for(int i=0; i<zeroCrossings.size(); i++){
            System.out.println(zeroCrossings.get(i));
        }
    }
    private List<Float> createSample(){
        return Arrays.asList(
            1f,2f,3f,2f,4f,5f,3f,2f,1f,2f,-2f,-3f,-2f,-1f,-3f,-4f,-3f,1f,3f,1f,4f,4f,5f,4f,3f,2f
        );
    }
    @Test
    public void testCountHills(){
        int hills = ZeroCrossing.countHills(createSample());
        assertEquals(6,hills);
    }
    @Test
    public void testGetMedian(){
        List<Integer>list = Arrays.asList(1,4,4,4,5,3,3,2,5,5,5,5,5,5,5,5,5,3,2,1,4,4,4);
        int median = ZeroCrossing.getMedian(list);
        assertEquals(5,median);
    }
}
