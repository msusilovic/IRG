package linearna;

public class Vector extends AbstractVector {

    private double[] elements;

    private int dimension;

    private boolean readOnly;

    public Vector(double[] elements) {
        this.elements = elements;
        this.dimension = elements.length;
        this.readOnly = false;
    }

    public Vector(boolean readOnly, boolean direct, double[] values) {

        this.readOnly = readOnly;

        if (direct) {
            this.elements = values;
        }else{
            for(int i = 0; i < values.length; i++) {
                this.set(i, values[i]);
            }
        }
    }

    @Override
    public double get(int d) {
        return elements[d];
    }

    @Override
    public IVector set(int position, double value) {
        elements[position] = value;

        return this;
    }

    @Override
    public int getDimension() {
        return this.dimension;
    }

    @Override
    public IVector copy() {
        IVector v = this.newInstance(this.getDimension());

        for (int i = 0; i < this.dimension; i++) {
            v.set(i, this.get(i));
        }

        return v;
    }

    @Override
    public IVector newInstance(int dimension) {
        return new Vector(new double[dimension]);
    }

    public static Vector parseSimple(String elements) {
        String[] strings = elements.split("\\s+");

        double [] values = new double[strings.length];

        for (int i = 0; i < strings.length; i++) {
            values[i] = Double.parseDouble(strings[i]);
        }

        return new Vector(values);
    }
}
