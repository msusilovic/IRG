package linearna;

import java.awt.geom.Point2D;

public class IRG {

    public static Matrix translate3D(float dx, float dy, float dz) {
        double elements[][] = new double[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {dx, dy, dz, 1}
        };

        return new Matrix(4, 4, elements, true);
    }

    public static IMatrix scale3D(float sx, float sy, float sz ) {
        double elements[][] = new double[][] {
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        };

        return new Matrix(4, 4, elements, true);
    }

    public static IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) {

        IVector h  = center.sub(eye).normalize();

        IVector u = h.nVectorProduct(viewUp).normalize();
        IVector v = u.nVectorProduct(h).normalize();

        double elements[][] = new double[][] {
                {u.get(0), v.get(0), -h.get(0), 0},
                {u.get(1), v.get(1), -h.get(1), 0},
                {u.get(2), v.get(2), -h.get(2), 0},
                {0, 0, 0, 1}
        };


        IMatrix first = IRG.translate3D((float)-eye.get(0), (float)-eye.get(1), (float)-eye.get(2));
        IMatrix second = new  Matrix(4, 4, elements, true);

        IMatrix result =  first.nMultiply(second);

        return result;
    }

    public static IMatrix buildFrustumMatrix(double l, double r, double b, double t, int n, int f) {
        double elements[][] = new double[][] {
                {2 * n / (r - l), 0, 0, 0},
                {0, 2 * n / (t - b), 0, 0},
                {(r + l) / (r - l), (t + b) / (t - b), -(f + n) /(double)(f - n), -1.0},
                {0, 0, - 2 * f * n / (double)(f - n), 0}
        };

        IMatrix result = new Matrix(4, 4, elements, true);

        return result;
    }

    public static boolean isAntiClockwise(IVector v1, IVector v2, IVector v3) {

        double[] x= new double[] {v1.get(0), v2.get(0), v3.get(0), v1.get(0)};
        double[] y = new double[] {v1.get(1), v2.get(1), v3.get(1), v1.get(1)};
        double[] a = new double[3];
        double[][] b = new double[3][3];
        double[] c = new double[3];

        for(int i = 0; i < 3; i++){
            b[i][0] = y[i] - y [i + 1];
            b[i][1] = -x[i] + x[i + 1];
            b[i][2] = x[i] * y[i + 1] - x[i + 1] * y[i];
        }

        boolean ccw = true;
        for(int i = 0; i < 3; i++){

            int j;
            if (i == 0) j = 2;
            else if ( i == 1) j = 0;
            else j = 1;

            if (x[j] * b[i][0] + y[j] * b[i][1] + b[i][2] <= 0) {
                return false;
            }
        }
        return true;
    }

}
