package linearna;

/**
 * A matrix based on a vector.
 */
public class MatrixVectorView extends AbstractMatrix {

    IVector original;
    boolean asRowMatrix;

    /**
     *
     * @param original
     * @param asRowMatrix if <code>true</code> matrix has one row
     */
    public MatrixVectorView (IVector original , boolean asRowMatrix) {
        this.asRowMatrix = asRowMatrix;
        this.original = original;
    }
    @Override
    public int getRowsCount() {
        return asRowMatrix ? 1 : original.getDimension();
    }

    @Override
    public int getColsCount() {
        return asRowMatrix ? original.getDimension() : 1;
    }

    @Override
    public double get(int row, int col) {
        if(asRowMatrix){
            return original.get(col);
        }else{
            return original.get(row);
        }
    }

    @Override
    public IMatrix set(int row, int col, double value) {
        if (asRowMatrix)
            return new MatrixVectorView(this.original.set(col, value), true);
        else
            return new MatrixVectorView(this.original.set(row, value), false);

    }

    @Override
    public IMatrix copy() {
        IMatrix m = newInstance(this.getRowsCount(), this.getColsCount());

        if(asRowMatrix) {
            for (int i = 0; i < original.getDimension(); i++) {
                m.set(0, i, original.get(i));
            }
        }else{
            for (int i = 0; i < original.getDimension(); i++) {
                m.set(i, 0, original.get(i));
            }
        }

        return m;
}

    @Override
    public IMatrix newInstance(int row, int col) {
       return new MatrixVectorView(original, true);
    }
}
