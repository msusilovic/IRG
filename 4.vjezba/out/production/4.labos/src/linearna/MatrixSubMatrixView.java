package linearna;

public class MatrixSubMatrixView extends AbstractMatrix {

    IMatrix original;
    int[] rowIndexes;
    int[] colIndexes;

    /**
     *
     * @param original
     * @param row   row to leave out
     * @param col   column to leave out
     */
    public MatrixSubMatrixView(IMatrix original, int row, int col) {

        this.original = original;
        int dimR = original.getRowsCount();
        int dimC = original.getColsCount();

        this.rowIndexes = new int[dimR - 1];
        this.colIndexes = new int[dimC - 1];

        for (int i = 0, k = 0; i < dimR; i++){
            if (i != row)
                this.rowIndexes[k++] = i;
        }
        for (int j = 0, k = 0; j < dimC; j++){
            if (j != col)
                this.colIndexes[k++] = j;
        }


    }
    private MatrixSubMatrixView(IMatrix original, int[] rowIndexes, int[] colIndexes){
        this.original = original;
        this.rowIndexes = rowIndexes;
        this.colIndexes = colIndexes;
    }


    @Override
    public int getRowsCount() {
        return rowIndexes.length;
    }

    @Override
    public int getColsCount() {
        return colIndexes.length;
    }

    @Override
    public double get(int row, int col) {
        if (row >= rowIndexes.length ||col >= colIndexes.length) {
            throw new IncompatibleOperandException();
        }

        int originalRow = rowIndexes[row];
        int originalCol = colIndexes[col];

        return original.get(originalRow, originalCol);
    }

    @Override
    public IMatrix set(int row, int col, double value) {
        if (row >= rowIndexes.length ||col >= colIndexes.length) {
            throw new IncompatibleOperandException();
        }

        int originalRow = rowIndexes[row];
        int originalCol = colIndexes[col];

        original.set(originalRow, originalCol, value);

        return this;
    }

    @Override
    public IMatrix copy() {
        int rows = rowIndexes.length;
        int cols = colIndexes.length;
        double[][] values = new double[rows][cols];

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                values[i][j] = original.get(rowIndexes[i], colIndexes[j]);
            }
        }

        return new Matrix(rows, cols, values, true);
    }

    @Override
    public IMatrix newInstance(int row, int col) {
        return new Matrix(row, col);
    }


}


