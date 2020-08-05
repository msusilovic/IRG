package hr.fer.zemris.linearna;

public class MatrixTransponseView extends AbstractMatrix {

    IMatrix original;

    public MatrixTransponseView(IMatrix original) {
        this.original = original;
    }
    @Override
    public int getRowsCount() {
        return original.getColsCount();
    }

    @Override
    public int getColsCount() {
        return original.getRowsCount();
    }

    @Override
    public double get(int row, int col) {
        return original.get(col, row);
    }

    @Override
    public IMatrix set(int row, int col, double value) {

        original.set(col, row, value);
        return original;
    }

    @Override
    public IMatrix copy() {

       return original.copy();
    }

    @Override
    public IMatrix newInstance(int row, int col) {

        return original.newInstance(row, col);

    }

    @Override
    public double[][] toArray() {

        double[][] values = new double[original.getColsCount()][original.getRowsCount()];

        for (int i = 0; i < original.getRowsCount(); i++) {
            for (int j = 0; j < original.getColsCount(); j++) {
                values[j][i] = original.get(i, j);
            }
        }

        return values;
    }
}
