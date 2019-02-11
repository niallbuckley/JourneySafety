package com.gmail.buckleyniall100.myapplication;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class LinearRegression {
    private RealMatrix w, estimate;
    public LinearRegression(double[][] xArray, double[][] yArray) throws Exception {
        applyNormalEquation(MatrixUtils.createRealMatrix(xArray), MatrixUtils.createRealMatrix(yArray));
    }
    private void applyNormalEquation(RealMatrix x, RealMatrix y) throws Exception {
        LUDecomposition Ludecomposition = new LUDecomposition(x.transpose().multiply(x));
        if(Ludecomposition.getDeterminant()==0.0) throw new Exception("no inverse " + x);
        else{w = Ludecomposition.getSolver().getInverse().multiply((x.transpose().multiply(y)));}
        //estimate = x.multiply(w);
        System.out.println("Estimate == " + w);
    }
    public double estimateRent(){
        return MatrixUtils.createColumnRealMatrix(new double[]{1, Double.valueOf("500"), Double.valueOf("4")}).transpose().multiply(w).getData()[0][0];
    }

    public RealMatrix getW(){return w;}
    public RealMatrix getEstimate(){return estimate;}

}