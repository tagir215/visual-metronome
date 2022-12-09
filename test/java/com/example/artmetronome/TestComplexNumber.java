package com.example.artmetronome;

import org.junit.Test;
import static org.junit.Assert.*;


public class TestComplexNumber {
    @Test
    public void testMultiply(){
        ComplexNumber c1 = new ComplexNumber(2,4);
        ComplexNumber c2 = new ComplexNumber(2,2);
        ComplexNumber result = c1.times(c2);
        assertEquals(-4,result.real,1);
        assertEquals(12,result.img,1);

        ComplexNumber result2 = c1.times(5);
        assertEquals(10,result2.real,1);
        assertEquals(20,result2.img,1);
    }
    @Test
    public void plusMinus(){
        ComplexNumber c1 = new ComplexNumber(2,4);
        ComplexNumber c2 = new ComplexNumber(2,2);
        ComplexNumber result = c1.plus(c2);
        assertEquals(4,result.real,6);
        assertEquals(6,result.img,2);

        ComplexNumber result2 = c1.minus(c2);
        assertEquals(0,result2.real,6);
        assertEquals(2,result2.img,2);

    }
}
