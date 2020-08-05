package linearna;

public interface IMatrix {

    public int getRowsCount();

    public int getColsCount();

    public double get(int row, int col);

    public IMatrix set(int row, int col, double value);

    public IMatrix copy();

    public IMatrix newInstance(int row, int col);

    public IMatrix nTranspose(boolean b);

    public IMatrix add(IMatrix other);

    public IMatrix nAdd(IMatrix other);

    public IMatrix sub(IMatrix other);

    public IMatrix nSub(IMatrix other);

    public IMatrix nMultiply(IMatrix other);

    public double determinant();

    public IMatrix subMatrix(int row, int col, boolean b);

    public IMatrix nInvert();

    public double[][] toArray();

    public IVector toVector(boolean b);

    public IMatrix scalarMultiply(double scalar);
}
