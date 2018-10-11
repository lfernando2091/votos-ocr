package saganet.mx.com.bingo.xdata;

/**
 * Created by LuisFernando on 22/02/2017.
 */
public class omrEngine {
    // minimum distance between the center coordinates of detected circles in pixels
    private double minDistancia=22.0;
    // min and max radii (set these values as you desire)
    private int minRadio=4;
    private int maxRadio=28;
    // accumulator value
    private double dpResolution=1.0;
    /*param1 = gradient value used to handle edge detection
     param2 = Accumulator threshold value for the
     cv2.CV_HOUGH_GRADIENT method.
     The smaller the threshold is, the more circles will be
     detected (including false circles).
     The larger the threshold is, the more circles will
     potentially be returned.*/
    private double thresholdEdge=100.0;
    private double thresholdCenter=22.0;

    public double getMinDistancia() {
        return minDistancia;
    }

    public void setMinDistancia(double minDistancia) {
        this.minDistancia = minDistancia;
    }

    public int getMinRadio() {
        return minRadio;
    }

    public void setMinRadio(int minRadio) {
        this.minRadio = minRadio;
    }

    public int getMaxRadio() {
        return maxRadio;
    }

    public void setMaxRadio(int maxRadio) {
        this.maxRadio = maxRadio;
    }

    public double getDpResolution() {
        return dpResolution;
    }

    public void setDpResolution(double dpResolution) {
        this.dpResolution = dpResolution;
    }

    public double getThresholdEdge() {
        return thresholdEdge;
    }

    public void setThresholdEdge(double thresholdEdge) {
        this.thresholdEdge = thresholdEdge;
    }

    public double getThresholdCenter() {
        return thresholdCenter;
    }

    public void setThresholdCenter(double thresholdCenter) {
        this.thresholdCenter = thresholdCenter;
    }


    private double maxArea=0;
    private double minArea=0;
    private boolean cutSize=false;

    public double getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(double maxArea) {
        this.maxArea = maxArea;
    }

    public double getMinArea() {
        return minArea;
    }

    public void setMinArea(double minArea) {
        this.minArea = minArea;
    }

    public boolean isCutSize() {
        return cutSize;
    }

    public void setCutSize(boolean cutSize) {
        this.cutSize = cutSize;
    }
}
