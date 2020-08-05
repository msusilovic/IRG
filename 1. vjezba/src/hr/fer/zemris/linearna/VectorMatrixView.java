package hr.fer.zemris.linearna;

public class VectorMatrixView extends AbstractVector {

    IMatrix original;
    int dimension;
    boolean rowMatrix;

    public VectorMatrixView(IMatrix original) {

        this.original = original;

        if(original.getRowsCount() == 1) {
            this.rowMatrix = true;
            this.dimension = original.getColsCount();
        }else{
            this.rowMatrix = false;
            this.dimension = original.getRowsCount();
        }
    }

    @Override
    public double get(int d) {
        if (rowMatrix){
            return original.get(0, d);
        }else{
            return original.get(d, 0);
        }
    }

    @Override
    public IVector set(int dimension, double value) {
        if (rowMatrix) {
            original.set(0, dimension, value);
        }else{
            original.set(dimension, 0, value);
        }

        return this;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector copy() {
        return newInstance(this.getDimension());

    }

    @Override
    public IVector newInstance(int dimension) {
        return new VectorMatrixView(original);
    }
}
