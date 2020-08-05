package hr.fer.zemris.linearna;

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

        rowIndexes = new int[original.getRowsCount()-1];
        colIndexes = new int[original.getColsCount()-1];
        int k = -1;
        for (int i = 0; i < original.getRowsCount(); i++) {
            if (i != row) {
                rowIndexes[++k] = i;
            }
        }
        k=-1;
        for (int i = 0; i < original.getColsCount(); i++) {
            if (i != col) {
                colIndexes[++k] = i;
            }
        }
    }

    private MatrixSubMatrixView(IMatrix original, int[] rows, int[] cols) {
        this.original = original;
        this.rowIndexes = rows;
        this.colIndexes = cols;

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
        return original.copy();
    }

    @Override
    public IMatrix newInstance(int row, int col) {
        return original.newInstance(row, col);
    }
}
