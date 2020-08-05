package hr.fer.zemris.linearna;

public abstract class AbstractMatrix implements IMatrix {

    @Override
    public abstract int getRowsCount();

    @Override
    public abstract int getColsCount();

    @Override
    public abstract double get(int row, int col);

    @Override
    public abstract IMatrix set(int row, int col, double value);

    @Override
    public abstract IMatrix copy();

    @Override
    public abstract IMatrix newInstance(int row, int col);

    @Override
    public IMatrix nTranspose(boolean liveView) {

            return new MatrixTransponseView(liveView ? this : this.copy());

    }

    @Override
    public IMatrix nAdd(IMatrix other) {

        return this.copy().add(other);
    }

    @Override
    public IMatrix add(IMatrix other) {

        checkDimensions(other);

        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                this.set(i, j, this.get(i, j) + other.get(i, j));
            }
        }

        return this;
    }

    private void checkDimensions(IMatrix other) {

        if (this.getColsCount() != other.getColsCount() || this.getRowsCount() != other.getRowsCount()) {
            throw new IncompatibleOperandException();
        }
    }

    @Override
    public IMatrix sub(IMatrix other) {

        checkDimensions(other);

        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                this.set(i, j, this.get(i, j) - other.get(i, j));
            }
        }

        return this;
    }

    @Override
    public IMatrix nSub(IMatrix other) {

        return this.copy().sub(other);
    }

    @Override
    public IMatrix nMultiply(IMatrix other) {

        if (this.getColsCount() != other.getRowsCount()) {
            throw new IncompatibleOperandException();
        }

        IMatrix product =  this.newInstance(this.getRowsCount(), other.getColsCount());

        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < other.getColsCount(); j++) {
                for (int k = 0; k < this.getColsCount(); k ++) {
                    product.set(i, j, product.get(i, j) + this.get(i, k) * other.get(k, j));
                }
            }
        }

        return product;
    }

    @Override
    public double determinant() {

        return this.determinant(this);

    }

    /**
     * Calculates determinant recursively.
     *
     * @param matrix
     * @return
     */
    private double determinant(IMatrix matrix) {

        if (matrix.getRowsCount() != matrix.getColsCount()) {
            throw new IncompatibleOperandException();
        }


        double determinant = 0;

        if (matrix.getRowsCount() == 1) {
            return (matrix.get(0,0));
        }

        if (matrix.getRowsCount() == 2) {
            return matrix.get(0,0) * matrix.get(1,1) - matrix.get(0,1) * matrix.get(1,0);
        }

        for (int i = 0; i < matrix.getColsCount(); i++) {

            determinant += sign(i) * matrix.get(0, i) * determinant(matrix.subMatrix(0, i, false));
        }

        return determinant;
    }

    private double sign(int i) {
        if (i%2 == 0) return 1;
        return -1;
    }

    @Override
    public IMatrix subMatrix(int row, int col, boolean liveView) {

        return new MatrixSubMatrixView(liveView ? this : this.copy(), row, col);

    }

    @Override
    public IMatrix nInvert() {

        if (this.getRowsCount() != this.getColsCount()) {
            throw new IncompatibleOperandException();
        }

        double determinant = this.determinant();

        if(determinant == 0) {
            throw new IncompatibleOperandException();
        }

        IMatrix cofactors = this.newInstance(this.getRowsCount(), this.getColsCount());

        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                cofactors.set(i, j, determinant(subMatrix(i, j, false)) * sign(i, j));
            }
        }
        return cofactors.nTranspose(true).scalarMultiply(1/this.determinant());

    }

    private int sign(int i, int j) {
        if ((i%2 == 0 && j%2 ==0) || (i%2 ==1 && j%2 == 1)) {
            return 1;
        }else{
            return -1;
        }
    }

    @Override
    public double[][] toArray() {
        double[][] values = new double[this.getRowsCount()][this.getColsCount()];

        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                values[i][j] = this.get(i, j);
            }
        }

        return values;
    }

    @Override
    public IVector toVector(boolean b) {
        return null;
    }

    @Override
    public String toString() {
        return toString(3);
    }

    public String toString(int precision) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.getRowsCount(); i ++) {
            sb.append("[");
            for (int j = 0; j < this.getColsCount(); j++) {
                sb.append(String.format("%."+precision + "f", this.get(i, j)));
                if (j != this.getColsCount() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("] \n");
        }
        return sb.toString();
    }

    @Override
    public IMatrix scalarMultiply(double scalar) {
        for (int i = 0; i < this.getRowsCount(); i++) {
            for (int j = 0; j < this.getColsCount(); j++) {
                this.set(i, j, scalar*this.get(i, j));
            }
        }

        return this;
    }
}
