package linearna;

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

        if (this.getColsCount() != other.getRowsCount()){
            throw new IncompatibleOperandException("Can't multiply");
        }


        int dimMutual = this.getColsCount();
        int dimR = this.getRowsCount();
        int dimC = other.getColsCount();

        IMatrix retVal = new Matrix(dimR, dimC);
        for (int i = 0; i < dimR; i++){
            for (int j = 0; j < dimC; j++){
                double val = 0;
                for (int k = 0; k < dimMutual; k++){
                    val += this.get(i, k) * other.get(k, j);
                }
                retVal.set(i, j, val);
            }
        }
        return retVal;

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

        int dimR = matrix.getRowsCount();
        double retVal = 0;
        for (int i = 0; i < dimR; i++){
            retVal += (i % 2 == 0 ? 1 : -1) * matrix.get(i, 0) * determinant(matrix.subMatrix(i, 0, true));
        }
        return retVal;

    }

    @Override
    public IMatrix subMatrix(int row, int col, boolean liveView) {

        MatrixSubMatrixView matrix = new MatrixSubMatrixView(this, row, col);
        if (liveView) return matrix;

        return matrix.copy();

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

        return new VectorMatrixView(this);
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
