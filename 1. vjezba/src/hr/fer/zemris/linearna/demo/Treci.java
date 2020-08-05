package hr.fer.zemris.linearna.demo;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

import java.util.Scanner;

public class Treci {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Unesite podatke o prvoj tocki:");
        IVector a = Vector.parseSimple(sc.nextLine().strip());

        System.out.println("Unesite podatke o drugoj tocki:");
        IVector b = Vector.parseSimple(sc.nextLine().strip());

        System.out.println("Unesite podatke o trecoj tocki:");
        IVector c = Vector.parseSimple(sc.nextLine().strip());

        System.out.println("Unesite podatke o tocki T:");
        IVector t = Vector.parseSimple(sc.nextLine().strip());

        double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm()/2.0;
        double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm()/2.0;
        double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm()/2.0;
        double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm()/2.0;

        double t1 = povA/pov;
        double t2 = povB/pov;
        double t3 = povC/pov;

        System.out.println(String.format("Baricentricne koordinate su: (%.2f, %.2f, %.2f)", t1, t2, t3));
    }
}
