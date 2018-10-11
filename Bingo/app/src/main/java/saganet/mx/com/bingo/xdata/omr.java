package saganet.mx.com.bingo.xdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.logger.LoggerTypeC;
import saganet.mx.com.bingo.sheet.SheetAnswers;
import saganet.mx.com.bingo.sheet.SheetConfig;
import saganet.mx.com.bingo.variables.BingoC;

/**
 * Created by LuisFernando on 20/02/2017.
 */

public class omr {
    LoggerC log=new LoggerC(omr.class);
    private boolean XCorrect=false;
    private int numIntentos=3, count=0;

    public omr(){

    }
    public Mat getFindSquares(Mat mRgba,Mat hsv, omrEngine settingomr){
        return  find_squares(mRgba,hsv, settingomr);
    }
    public Bitmap getFindSquares(Bitmap bitmap,omrEngine settingomr){
        Mat mRgba = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Mat hsv = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mRgba);
        Utils.matToBitmap(find_squares2(mRgba,hsv,settingomr), bitmap);
        mRgba.release();hsv.release();
        return  bitmap;
    }
    public Bitmap getFindSquares(byte[] bitmap,omrEngine settingomr){
        Bitmap bit=null;
        for (int q=0; q<numIntentos; q++){
            if(!XCorrect){
                if(bit!=null){
                    bit.recycle();bit=null;
                }
                log.printf("Intento en getFindSquares:: " + q);
                count=q;
                bit=getFindSquares(getBitmap(bitmap),settingomr);
            } else {
                count=0;
                XCorrect=false;
                q=numIntentos;
            }
        }
        return bit;
    }

    public Bitmap getBitmap(byte[] dataImage) throws OutOfMemoryError{
        return BitmapFactory.decodeByteArray(dataImage, 0,dataImage.length);
    }


    private double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
    }

    private void drawText(Mat mRgba,Point ofs, String text) {
        Imgproc.putText(mRgba, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 3, new Scalar(0,0,0));
    }

    private Mat find_squares(Mat mRgba,Mat hsv, omrEngine settingomr) {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy;
        //Mat kernel = new Mat();
        Imgproc.Canny(mRgba, hsv, 80, 90);
        Imgproc.dilate(hsv,hsv, new Mat());
        Imgproc.findContours(hsv, contours, hierarchy= new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        Imgproc.circle(mRgba, new Point(mRgba.width()/2,mRgba.height()/2) , 15, new Scalar(255, 0, 0, 255), 2);
        try {
            MatOfPoint2f matOfPoint[] = new MatOfPoint2f[2];
            for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
                if (idx != 0) {
                    MatOfPoint contour = contours.get(idx);
                    Rect rect = Imgproc.boundingRect(contour);
                    double contourArea = Imgproc.contourArea(contour);
                    matOfPoint[0]=matOfPoint[1]= new MatOfPoint2f();
                    matOfPoint[0].fromList(contour.toList());
                    //double perimeter = Imgproc.arcLength(matOfPoint[0],true);
                    Imgproc.approxPolyDP(matOfPoint[0], matOfPoint[1], Imgproc.arcLength(matOfPoint[0], true) * 0.02, true);
                    long total = matOfPoint[1].total();
                    if (total == 3) { // is triangle
                        // do things for triangle
                    }
                    if (total >= 4 && total <= 6) {
                        List<Double> cos = new ArrayList<>();
                        Point[] points = matOfPoint[1].toArray();
                        for (int j = 2; j < total + 1; j++) cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                        Collections.sort(cos);
                        //((contourArea > minArea) && (contourArea< maxArea))
                        Double minCos = cos.get(0), maxCos = cos.get(cos.size() - 1);
                        boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                        //  && ((contourArea > settingomr.getMinArea()) && (contourArea< settingomr.getMaxArea()))
                        if (isRect && ((contourArea > settingomr.getMinArea()) && (contourArea< settingomr.getMaxArea()))) {
                            //--double ratio = Math.abs(1 - (double) rect.width / rect.height);
                            if(BingoC.rectStarted)BingoC.rect2=rect;
                            //Draw rectangle
                            Imgproc.rectangle(mRgba, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 2);
                            //--Imgproc.circle(mRgba, new Point(rect.x+(rect.width/2),rect.y+(rect.height/2)), 10, new Scalar(110,110,110,110), 5);
                            Imgproc.circle(mRgba, new Point(rect.x+(rect.width/2),rect.y+(rect.height/2)), 10, new Scalar(0,0, 0), 3);
                        }
                    }
                }
            }
        }
        catch (NullPointerException e){
        }
        contours.clear();
        hierarchy.release();
        //Imgproc.drawContours(mRgba, contours, -1, new Scalar(0, 0, 0), 1);//, 2, 8, hierarchy, 0, new Point());
        return mRgba;
    }

    private Mat find_squares2(Mat mRgba,Mat hsv, omrEngine settingomr) {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy;
        Mat cRgba = new Mat(mRgba.width(), mRgba.height(), CvType.CV_8UC4);
        //Mat kernel = new Mat();
        //-Core.inRange(mRgba, new Scalar(50, 100, 100), new Scalar(70, 255, 255), hsv);
        if(count==0) Imgproc.GaussianBlur(mRgba,cRgba,new Size(9,9),2,2);
        if(count==1) Imgproc.GaussianBlur(mRgba,cRgba,new Size(5,5),0,0);
        if(count==2) Imgproc.GaussianBlur(mRgba,cRgba,new Size(3,3),0,0);
        Imgproc.Canny(cRgba, hsv, 80, 90);
        Imgproc.dilate(hsv,hsv, new Mat());
        //if (true)return hsv;
        //Imgproc.dilate(hsv,hsv, kernel);
        Imgproc.findContours(hsv, contours, hierarchy= new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        try {
            MatOfPoint2f matOfPoint[] = new MatOfPoint2f[2];
            for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
                if (idx != 0) {
                    MatOfPoint contour = contours.get(idx);
                    Rect rect = Imgproc.boundingRect(contour);
                    double contourArea = Imgproc.contourArea(contour);
                    matOfPoint[0]=matOfPoint[1]= new MatOfPoint2f();
                    matOfPoint[0].fromList(contour.toList());
                    //double perimeter = Imgproc.arcLength(matOfPoint[0],true);
                    Imgproc.approxPolyDP(matOfPoint[0], matOfPoint[1], Imgproc.arcLength(matOfPoint[0], true) * 0.02, true);
                    long total = matOfPoint[1].total();
                    if (total == 3) { // is triangle
                        // do things for triangle
                    }
                    if (total >= 4 && total <= 6) {
                        List<Double> cos = new ArrayList<>();
                        Point[] points = matOfPoint[1].toArray();
                        for (int j = 2; j < total + 1; j++) cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                        Collections.sort(cos);
                        //((contourArea > minArea) && (contourArea< maxArea))
                        Double minCos = cos.get(0), maxCos = cos.get(cos.size() - 1);
                        boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                        //  && ((contourArea > settingomr.getMinArea()) && (contourArea< settingomr.getMaxArea()))
                        //Imgproc.rectangle(mRgba, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 2);
                        //Imgproc.circle(mRgba, new Point(rect.x+(rect.width/2),rect.y+(rect.height/2)), 10, new Scalar(0,0, 0), 3);
                        //drawText(mRgba, rect.tl(), "Area: " + randomGenerator.nextInt(100));
                        //contourArea
                        if (isRect && ((contourArea > settingomr.getMinArea()) && (contourArea< settingomr.getMaxArea()))) {
                            BingoC.rect= new Rect();
                            BingoC.rect=rect;
                            //--double ratio = Math.abs(1 - (double) rect.width / rect.height);
                            Imgproc.rectangle(mRgba, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 2);
                            XCorrect=true;
                        }
                    }
                }
            }
        }
        catch (NullPointerException e){
            log.printf(e.getMessage());
        }
        hierarchy.release();
        cRgba.release();
        contours.clear();
        //Imgproc.drawContours(mRgba, contours, -1, new Scalar(0, 0, 0), 1);//, 2, 8, hierarchy, 0, new Point());
        return mRgba;
    }
    public Bitmap getColorCircleTracking(Bitmap bitmap,omrEngine settingomr){
        Mat mRgba = new Mat(bitmap.getWidth(), bitmap.getHeight(),
                CvType.CV_8UC4);
        Mat hsv = new Mat(bitmap.getWidth(), bitmap.getHeight(),
                CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mRgba);
        //--Utils.matToBitmap(colorCircleTracking(mRgba,hsv,settingomr), bitmap);
        Utils.matToBitmap(CannyFilertCircle(mRgba,hsv,settingomr), bitmap);

        return bitmap;
    }

    private Mat CannyFilertCircle(Mat rgb, Mat hsv, omrEngine settingomr){
        Mat circles = new Mat();
        /*Suavizado de imagen*/
        Imgproc.medianBlur(rgb,rgb,3);
        /*Cambiar imagen resivida de RGB a HSV*/
        Imgproc.cvtColor(rgb, hsv, Imgproc.COLOR_BGR2HSV);
        Mat hue_image = new Mat();
        Mat kernel = new Mat();
                /*Azul*/
                //Core.inRange(hsv, new Scalar(0, 100, 100), new Scalar(10, 255, 255), lower_hue_range);
                //Core.inRange(hsv, new Scalar(160, 100, 100), new Scalar(179, 255, 125), upper_hue_range);
                /*Combinar rango de colores fuerte al mas bajo*/
                //Core.addWeighted(lower_hue_range, 1.0, upper_hue_range,1.0,1.0,hue_image);

                /* convert to grayscale */
                /*Agregar desenfoque gaussiano de 2*/
        if(BingoC.CAMERA_XX>0 && BingoC.CAMERA_XX<=BingoC.CAMERA_2X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(3.5,3.5),0,0);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),0,0);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_2X && BingoC.CAMERA_XX<=BingoC.CAMERA_3X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),0,0);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_3X && BingoC.CAMERA_XX<=BingoC.CAMERA_5X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),0,0);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_5X && BingoC.CAMERA_XX<=BingoC.CAMERA_8X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),0,0);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_8X && BingoC.CAMERA_XX<=BingoC.CAMERA_9X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(9,9),2,2);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),2,2);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),2,2);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),2,2);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_9X && BingoC.CAMERA_XX<=BingoC.CAMERA_12X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(9,9),2,2);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),2,2);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),2,2);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),2,2);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_12X && BingoC.CAMERA_XX<=BingoC.CAMERA_13X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(9,9),0,0);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),0,0);
        }
        else if(BingoC.CAMERA_XX>BingoC.CAMERA_13X){
            if(count==0) Imgproc.GaussianBlur(rgb,rgb,new Size(10,10),0,0);
            if(count==1) Imgproc.GaussianBlur(rgb,rgb,new Size(7,7),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(5,5),0,0);
            if(count==2) Imgproc.GaussianBlur(rgb,rgb,new Size(3,3),0,0);
        }
        //if (true)return rgb;
                //--Imgproc.GaussianBlur(rgb,rgb,new Size(9,9),2,2);
                //--Core.inRange(hsv, new Scalar(0, 0, 0, 0), new Scalar(110,110,110,110), hue_image);
                Imgproc.Canny(rgb, hue_image, 80, 90);
                Imgproc.dilate(hue_image,hue_image, kernel);
                //Core.inRange(hsv, new Scalar(15, 100, 100), new Scalar(30, 255, 255), lower_hue_range);

        if (settingomr.isCutSize()){
            return hue_image;
            //CannyFilterCountor(hue_image);

        }
        try {
             /* find the circle in the image */
            //(hue_image.rows()/8) + settingomr.getMinDistancia()
            Imgproc.HoughCircles(hue_image,
                    circles,
                    Imgproc.CV_HOUGH_GRADIENT,
                    settingomr.getDpResolution(),
                    settingomr.getMinDistancia(),
                    settingomr.getThresholdEdge(),
                    settingomr.getThresholdCenter(),
                    settingomr.getMinRadio(),
                    settingomr.getMaxRadio());
        }
       catch (Exception e){
           return hue_image;
       }
        /* get the number of circles detected */
        int numberOfCircles = (circles.rows() == 0) ? 0 : circles.cols();
        log.printf("numberOfCircles: " + numberOfCircles);
        //Core.circle(rgb, new Point(210,210), 10, new Scalar(100,10,10),3);
        /* draw the circles found on the image */
        BingoC.respuesta= new SheetAnswers();
        for (int i=0; i<numberOfCircles; i++) {
            /* get the circle details, circleCoordinates[0, 1, 2] = (x,y,r)
            * (x,y) are the coordinates of the circle's center
            */
            double[] circleCoordinates = circles.get(0, i);
            int x = (int) circleCoordinates[0], y = (int) circleCoordinates[1];
            Point center = new Point(x, y);
            int radius = (int) circleCoordinates[2];
            //drawText(rgb, center, "n:" + i);
            /* circle's outline */
            Imgproc.circle(rgb, center, radius, new Scalar(0, 255, 0), 4);

            SheetConfig sheetConfig = new SheetConfig();
            sheetConfig.setId(i);
            sheetConfig.setPosX(circleCoordinates[0]);
            sheetConfig.setPosY(circleCoordinates[1]);
            sheetConfig.setSumXY(circleCoordinates[0],circleCoordinates[1]);
            sheetConfig.setRadio(radius);
            sheetConfig.setAnable(true);
            sheetConfig.setColumn(0);
            sheetConfig.setRow(0);
            BingoC.respuesta.fillSheet(sheetConfig);

            /* circle's center outline */
            Imgproc.rectangle(rgb, new Point(x - 5, y - 5),
                    new Point(x + 5, y + 5),
                    new Scalar(0, 128, 255), 2);
        }
        return  rgb;
    }

    public void getCircle(Bitmap bitmap, AppCompatImageView image){
        detectionCircles(bitmap,image);
    }
    private void detectionCircles(Bitmap bitmap, AppCompatImageView image){
        /* convert bitmap to mat */
        Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(),
                CvType.CV_8UC1);
        Mat grayMat = new Mat(bitmap.getWidth(), bitmap.getHeight(),
                CvType.CV_8UC1);

        Utils.bitmapToMat(bitmap, mat);

        /* convert to grayscale */
        int colorChannels = (mat.channels() == 3) ? Imgproc.COLOR_BGR2GRAY
                : ((mat.channels() == 4) ? Imgproc.COLOR_BGRA2GRAY : 1);

        Imgproc.cvtColor(mat, grayMat, colorChannels);

        /* reduce the noise so we avoid false circle detection */
        Imgproc.GaussianBlur(grayMat, grayMat, new Size(9, 9), 2, 2);

        // accumulator value
        double dp = 1.2d;
        // minimum distance between the center coordinates of detected circles in pixels
        double minDist = 100;

        // min and max radii (set these values as you desire)
        int minRadius = 0, maxRadius = 0;

        // param1 = gradient value used to handle edge detection
        // param2 = Accumulator threshold value for the
        // cv2.CV_HOUGH_GRADIENT method.
        // The smaller the threshold is, the more circles will be
        // detected (including false circles).
        // The larger the threshold is, the more circles will
        // potentially be returned.
        double param1 = 70, param2 = 72;

        /* create a Mat object to store the circles detected */
        Mat circles = new Mat(bitmap.getWidth(),
                bitmap.getHeight(), CvType.CV_8UC1);

        /* find the circle in the image */
        Imgproc.HoughCircles(grayMat, circles,
                Imgproc.CV_HOUGH_GRADIENT, dp, minDist, param1,
                param2, minRadius, maxRadius);

        /* get the number of circles detected */
        int numberOfCircles = (circles.rows() == 0) ? 0 : circles.cols();
        log.printf("Numero de circulos detectados-------" + numberOfCircles, LoggerTypeC.eLoggerType.Warning);
        /* draw the circles found on the image */
        for (int i=0; i<numberOfCircles; i++) {


        /* get the circle details, circleCoordinates[0, 1, 2] = (x,y,r)
        * (x,y) are the coordinates of the circle's center
        */
            double[] circleCoordinates = circles.get(0, i);


            int x = (int) circleCoordinates[0], y = (int) circleCoordinates[1];

            Point center = new Point(x, y);

            int radius = (int) circleCoordinates[2];

            /* circle's outline */
            Imgproc.circle(mat, center, radius, new Scalar(0,
                    255, 0), 4);
            /* circle's center outline */
            Imgproc.rectangle(mat, new Point(x - 5, y - 5),
                    new Point(x + 5, y + 5),
                    new Scalar(0, 128, 255), -1);
        }

        /* convert back to bitmap */
        Utils.matToBitmap(mat, bitmap);
        image.setVisibility(View.VISIBLE);
        image.setImageBitmap(bitmap);
    }
}
