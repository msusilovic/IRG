package fraktali;

public class Complex {

    double re;
    double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex sub(Complex other) {
        return new Complex(this.re - other.re, this.im - other.im);
    }

    public double modulus() {
        return Math.sqrt(modulusSquare());
    }

    public double modulusSquare() {
        return this.re * this.re + this.im * this.im;
    }

    public Complex multiply(Complex other) {
        double re = this.re * other.re - this.im *other.im;
        double im = this.re *other.im + this.im * other.re;

        return new Complex(re, im);
    }

    public Complex square() {
        return this.multiply(this);
    }

    public Complex cube() {
        return this.square().multiply(this);
    }

    public double getRe() {
        return re;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public double getIm() {
        return im;
    }

    public void setIm(double im) {
        this.im = im;
    }
}
