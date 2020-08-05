package linearna;

public class Matrix extends AbstractMatrix {

    double[][] elements;
    int rows;
    int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        this.elements = new double[rows][cols];
    }

    public Matrix(int rows, int cols, double[][] elements, boolean direct) {

        this.rows = rows;
        this.cols = cols;

        if(direct) {
            this.elements = elements;
        }else{
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++)  {
                    this.set(i, j, elements[i][j]);
                }
            }
        }
    }

    @Override
    public int getRowsCount() {
        return this.rows;
    }

    @Override
    public int getColsCount() {
        return this.cols;
    }

    @Override
    public double get(int row, int col) {
        return elements[row][col];
    }

    @Override
    public IMatrix set(int row, int col, double value) {
        elements[row][col] = value;

        return this;
    }

    @Override
    public IMatrix copy() {

        return new Matrix(rows, cols, elements, false);
    }

    @Override
    public IMatrix newInstance(int row, int col) {
        return new Matrix(row, col);
    }

    public static Matrix parseSimple(String stringValues) {
        String[] rowsString = stringValues.split("\\|");

        int rows = rowsString.length;
        int cols =  rowsString[0].strip().split("\\s+").length;

        double[][] values = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            String[] oneRowParts = rowsString[i].strip().split("\\s+");

            for (int j = 0; j < oneRowParts.length; j++) {

                values[i][j] = Double.parseDouble(oneRowParts[j]);
            }
        }

        return new Matrix(rows, cols, values, true);
    }
}
