package com.example.artmetronome;

public class ComplexNumber {
    float real;
    float img;
    ComplexNumber(float real, float img){
        this.real = real;
        this.img = img;
    }

    ComplexNumber times(ComplexNumber c){
        return new ComplexNumber(real*c.real - img*c.img, real*c.img + img*c.real);
    }
    ComplexNumber times(float multiplier){
        return new ComplexNumber(multiplier*real,multiplier*img);
    }
    ComplexNumber plus(ComplexNumber c){
        return new ComplexNumber(real + c.real, img + c.img);
    }

    ComplexNumber minus(ComplexNumber c){
        return new ComplexNumber(real - c.real, img - c.img);
    }




}
