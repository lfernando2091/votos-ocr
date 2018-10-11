package saganet.mx.com.bingo.xdata;

/**
 * Created by LuisFernando on 03/03/2017.
 */

public class DataScalable{
    private static double[] ivalues= new double[2];
    private static double[] ovalues= new double[2];
    private static int[] ixvalue= new int[2];
    private static int[] oyvalue= new int[2];
    public DataScalable(){
        getIvalues()[0]=0.0;
        getIvalues()[1]=0.0;
        getOvalues()[0]=0.0;

        getOvalues()[1]=0.0;

        getIxvalue()[0]=0;
        getIxvalue()[1]=0;
        getOyvalue()[0]=0;

        getOyvalue()[1]=0;
    }

    public static void DoubleScale(double input1,double input2,double input3,boolean normal){
        getIvalues()[0]=input1;
        getIvalues()[1]=input2;
        getOvalues()[0]=input3;

        getOvalues()[1]=normal?
                (input2*input3)/input1:
                (input1*input3)/input2;
    }
    public static void IntegerScale(int input1,int input2,int input3,boolean normal){
        getIxvalue()[0]=input1;
        getIxvalue()[1]=input2;
        getOyvalue()[0]=input3;

        getOyvalue()[1]=normal?
                (input2*input3)/input1:
                (input1*input3)/input2;
    }

    public static double getDoubleScale(){
        return ovalues[1];
    }
    public static double getIntegerScale(){
        return oyvalue[1];
    }

    private static double[] getIvalues() {
        return ivalues;
    }
    private static double[] getOvalues() {
        return ovalues;
    }


    private static int[] getIxvalue() {
        return ixvalue;
    }

    private static int[] getOyvalue() {
        return oyvalue;
    }
}
