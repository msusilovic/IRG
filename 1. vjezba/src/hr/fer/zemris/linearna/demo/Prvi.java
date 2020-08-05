package hr.fer.zemris.linearna.demo;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

public class Prvi {
    public static void main(String[] args) {

        IVector prviV = Vector.parseSimple("2 3 -4");
        IVector drugiV = Vector.parseSimple("-1 4 -1");

        IVector v1 = prviV.nAdd(drugiV);
        System.out.println("v1 = " + prviV.toString(0) + " + " + drugiV.toString(0) + " = " + v1.toString(0));

        IVector treciV = Vector.parseSimple("-1 4 -1");
        double s = v1.scalarProduct(treciV);
        System.out.println("s = " + v1.toString(0) + " * " + treciV.toString(0) + " = " + s);

        IVector cetvrtiV = Vector.parseSimple("2 2 4");
        IVector v2 = v1.nVectorProduct(cetvrtiV);
        System.out.println("v2 = " + v1.toString(0) + " × " + cetvrtiV.toString(0) + " = " + v2.toString(0));

        IVector v3 = v2.nNormalize();
        System.out.println("v3 = |v2| = " + v3.toString(3));
        System.out.println(v2.toString());

        IVector v4 = v2.scalarMultiply(-1);
        System.out.println("v4 = -v2 = " + v4.toString());

        IMatrix prva = Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1");
        IMatrix druga = Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3");

        System.out.println("\nm1:");
        System.out.println(prva.nAdd(druga));

        System.out.println("\nm2:");
        System.out.println(prva.nMultiply(druga.nTranspose(false)));

        System.out.println("\nm3:");
        System.out.println(prva.nMultiply(druga.nInvert()));
    }
}
