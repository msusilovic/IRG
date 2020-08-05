package hr.fer.zemris.linearna;

public interface IVector {

    public double get(int d);

    public IVector set(int position, double value);

    public int getDimension();

    public IVector copy();

    public IVector copyPart(int part);

    public IVector newInstance(int dimension);

    public IVector add(IVector other);

    public IVector nAdd(IVector other);

    public IVector sub(IVector other);

    public IVector nSub(IVector other);

    public IVector scalarMultiply(double scalar);

    public IVector nScalarMultiply(double scalar);

    public double norm();

    public IVector normalize();

    public IVector nNormalize();

    public double cosine(IVector other);

    public double scalarProduct(IVector other);

    public IVector nVectorProduct(IVector other);

    public IVector nFromHomogenous();

    public IMatrix toRowMatrix(boolean b);

    public IMatrix toColumnMatrix(boolean b);

    public double[] toArray();

    public String toString(int precision);
}
