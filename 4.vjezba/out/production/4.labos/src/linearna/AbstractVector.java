package linearna;

public abstract class AbstractVector implements  IVector {

    public AbstractVector() {
    }

    @Override
    public abstract double get(int d);

    @Override
    public abstract IVector set(int dimension, double value);

    @Override
    public abstract int getDimension();

    @Override
    public abstract IVector copy();

    @Override
    public abstract IVector newInstance(int dimension);

    @Override
    public IVector copyPart(int part) {

        IVector vector = this.newInstance(part);

        for(int i = 0; i < part; i++) {
            if (i >= this.getDimension()){
                vector.set(i, 0);
            }else{
                vector.set(i, this.get(i));
            }
        }

        return vector;
    }

    @Override
    public IVector add(IVector other) throws IncompatibleOperandException {
        if(this.getDimension()!=other.getDimension()) {
            throw new IncompatibleOperandException();
        }

        for (int i = this.getDimension()-1; i >= 0; i--) {
            this.set(i, get(i)+ other.get(i));
        }

        return this;
    }

    @Override
    public IVector nAdd(IVector other) throws IncompatibleOperandException {
        return this.copy().add(other);
    }

    @Override
    public IVector sub(IVector other) {
        if(this.getDimension()!=other.getDimension()) {
            throw new IncompatibleOperandException();
        }

        for (int i = this.getDimension()-1; i >= 0; i--) {
            this.set(i, get(i) - other.get(i));
        }

        return this;
    }

    @Override
    public IVector nSub(IVector other) {
        return this.copy().sub(other);
    }

    @Override
    public IVector scalarMultiply(double scalar) {

        for (int i = this.getDimension()-1; i >= 0; i--) {
            this.set(i, scalar*get(i));
        }

        return this;
    }

    @Override
    public IVector nScalarMultiply(double scalar) {
        return this.copy().scalarMultiply(scalar);
    }

    @Override
    public double norm() {

        double sum = 0;

        for (int i = this.getDimension()-1; i >= 0; i--) {
            sum += this.get(i)*this.get(i);
        }

        return Math.sqrt(sum);
    }

    @Override
    public IVector normalize() {

        double norm = this.norm();

        for(int i = this.getDimension()-1; i >= 0; i--) {
            this.set(i, this.get(i)/norm);
        }

        return this;
    }

    @Override
    public IVector nNormalize() {
        return this.copy().normalize();
    }

    @Override
    public double cosine(IVector other) {
        return this.scalarProduct(other) / (this.norm() + other.norm());
    }

    @Override
    public double scalarProduct(IVector other) {

        if(this.getDimension()!=other.getDimension()) {
            throw new IncompatibleOperandException();
        }

        double sum = 0;

        for(int i = this.getDimension()-1; i >= 0; i--) {
            sum += this.get(i) * other.get(i);
        }

        return sum;
    }

    @Override

    public IVector nVectorProduct(IVector other) {
        if (this.getDimension() !=3 || other.getDimension() != 3){
            throw new IncompatibleOperandException();
        }

        IVector vector = this.newInstance(3);

        vector.set(0, this.get(1) * other.get(2) -
                      this.get(2) * other.get(1));
        vector.set(1, this.get(2) * other.get(0) -
                      this.get(0) * other.get(2));
        vector.set(2, this.get(0) * other.get(1) -
                      this.get(1) * other.get(0));

        return vector;

    }

    @Override
    public IVector nFromHomogenous() {

        double last = this.get(this.getDimension()-1);

       IVector vector =  this.newInstance(this.getDimension()-1);

       for (int i = 0; i < this.getDimension() -1 ; i++) {
           vector.set(i, this.get(i)/last);
       }

       return vector;
    }

    @Override
    public IMatrix toRowMatrix(boolean b) {
        return new MatrixVectorView(this, true);
    }

    @Override
    public IMatrix toColumnMatrix(boolean b) {
        return new MatrixVectorView(this, false);
    }

    @Override
    public double[] toArray() {

        double[] elements = new double[this.getDimension()];

        for (int i = 0; i < this.getDimension(); i++) {
            elements[i] = this.get(i);
        }
        return elements;
    }

    @Override
    public String toString(int precision) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("(%."+precision + "fi", this.get(0)));

        if(this.get(1) > 0) {
            stringBuilder.append("+");
        }

        stringBuilder.append(String.format("%."+precision + "fj", this.get(1)));

        if(this.getDimension() > 2) {
            if(this.get(2) > 0) {
                stringBuilder.append("+");
            }
            stringBuilder.append(String.format("%."+precision + "fk)", this.get(2)));
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return toString(3);
    }
}
