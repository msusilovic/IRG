package linearna;

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
        if (rowMatrix)
            return new VectorMatrixView(this.original.set(0, dimension, value));
        else
            return new VectorMatrixView(this.original.set(dimension, 0, value));

    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector copy() {
        double[] elements = new double[dimension];

        for (int i = 0; i < dimension; i++){
            if (rowMatrix)
                elements[i] = this.original.get(0, i);
            else
                elements[i] = this.original.get(i, 0);
        }

        return new Vector(false, true, elements);

    }

    @Override
    public IVector newInstance(int dimension) {
        return new Vector(new double[dimension]);
    }
}
