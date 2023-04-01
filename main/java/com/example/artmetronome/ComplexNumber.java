package com.example.artmetronome;

public class ComplexNumber {
    private float real;

    public float getReal() {
        return real;
    }

    public void setReal(float real) {
        this.real = real;
    }

    public float getImg() {
        return img;
    }

    public void setImg(float img) {
        this.img = img;
    }

    private float img;
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
