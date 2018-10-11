package saganet.mx.com.bingo.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import org.opencv.core.Rect;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import saganet.mx.com.bingo.R;
import saganet.mx.com.bingo.file.SaveMedia;
import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.photo.CameraViewBase;
import saganet.mx.com.bingo.variables.BingoC;
import saganet.mx.com.bingo.xdata.DataScalable;
import saganet.mx.com.bingo.xdata.omr;
import saganet.mx.com.bingo.xdata.omrEngine;

import android.view.View.OnTouchListener;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;


public class CaptureImage extends AppCompatActivity  implements CvCameraViewListener2, OnTouchListener {
    LoggerC log=new LoggerC(CaptureImage.class);

    public static final String EXTRA_ID = "CaptureImageInit";
    public static final int REQUEST_CAPTURE_IMAGE = 2;

    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private CameraViewBase mOpenCvCameraView;
    private static View ViewArea;
    private ProgressDialog pd = null;
    private TextView Chron;

    // Used in Camera selection from menu (when implemented)
    private boolean              mIsCalibrationCamera = false;

    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degree
    private Mat mRgba, mHSV;
    private omr engineomr=new omr();
    private omrEngine settingomr=new omrEngine();

    private omrEngine settingomr2;
    private omr engineomr2=new omr();
    private omr engineomr3=new omr();
    private static boolean ImgRecognized;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    log.printf("OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(CaptureImage.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture_image);
        mOpenCvCameraView = (CameraViewBase) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        Chron=(TextView) findViewById(R.id.chron);
        ViewArea=findViewById(R.id.activity_capture_image);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            log.printf("Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            log.printf("OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void StopC(){
        this.finish();
    }

    private static double MakeScale(double scale, int select){
        switch (select){
            case 1:
                DataScalable.DoubleScale((BingoC.MinWidth * BingoC.MinHeight),scale,(BingoC.mWidth*BingoC.mHeight),true);
                break;
            case 2:
                DataScalable.DoubleScale((BingoC.MaxWidth * BingoC.MaxHeight),scale,(BingoC.rWidth*BingoC.rHeight),true);
                break;
            case 3:
                DataScalable.DoubleScale((BingoC.mWidth * BingoC.mHeight),scale,(BingoC.MaxWidth*BingoC.MaxHeight),true);
                break;
        }
        return DataScalable.getDoubleScale();
    }

    private void setCameraTrackingValues(){
        settingomr.setDpResolution(1);
        settingomr.setMaxRadio(25);
        settingomr.setMinRadio(5);
        settingomr.setMinDistancia(22.0);
        settingomr.setThresholdEdge(100);
        settingomr.setThresholdCenter(22);
        settingomr.setMinArea(BingoC.trackMinArea);
        settingomr.setMaxArea(BingoC.trackMaxArea);
        if(BingoC.mWidth!= BingoC.MinWidth || BingoC.mHeight!=BingoC.MinHeight){
            settingomr.setMinArea(MakeScale(BingoC.trackMinArea,1));
            settingomr.setMaxArea(MakeScale(BingoC.trackMaxArea,1));
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        BingoC.mWidth = width;
        BingoC.mHeight = height;
        mOpenCvCameraView.setJpegMaxQuality();
        //mOpenCvCameraView.setEffect(Camera.Parameters.EFFECT_SEPIA);
        if(mIsCalibrationCamera){
            if (BingoC.mWidth != width || BingoC.mHeight != height) {
                BingoC.mWidth = width;
                BingoC.mHeight = height;
            }
        }
        setCameraTrackingValues();
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mHSV = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mHSV.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        // Rotate mRgba 90 degrees
        //--Core.transpose(mRgba, mRgbaT);
        //--Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
        //--Core.flip(mRgbaF, mRgba, 1 );
        //--return mRgba; // This function must return
        return engineomr.getFindSquares(mRgba,mHSV,settingomr);
    }

    private Boolean PrepareTakePicture(){
        File pictureFile = SaveMedia.getOutputMediaFile(SaveMedia.MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            log.printf("Error creating media file, check storage permissions: ");
            return false;
        }
        //String fileName = Environment.getExternalStorageDirectory().getPath() +"/sample_picture_" + currentDateandTime + ".jpg";
        mOpenCvCameraView.takePicture(pictureFile);
        log.printf("Datos salvos en: " + pictureFile.getAbsoluteFile());
        return true;
    }

    private class ATakePicture extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BingoC.Processing=true;
            pd = new ProgressDialog(CaptureImage.this);
            pd.setTitle("Analizando...");
            pd.setMessage("Obteniendo imagen capturada.");
            pd.setCancelable(false);
            pd.show();
            //image=null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result=false;
            try {
                result=PrepareTakePicture();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            BingoC.Processing=false;
            if(res){
                if(!BingoC.SegundoPlano){
                    try {
                        ProcessSync();
                    }catch (NullPointerException e){
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                }else {
                    new AProcessImage().execute(mOpenCvCameraView.getBitmap());
                }
            }
            else {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
            }
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap,Rect rect){
        try {
            Snackbar.make(ViewArea,"Imagen reconocidad, analizando",Snackbar.LENGTH_LONG).show();
            ImgRecognized=true;
            BingoC.rectStarted=true;
            return Bitmap.createBitmap(bitmap,rect.x,rect.y, rect.width, rect.height);
        }catch (NullPointerException e){
            BingoC.rectStarted=true;
            try {
                Snackbar.make(ViewArea,"Recorte por segunda opción",Snackbar.LENGTH_LONG).show();
                ImgRecognized=true;
                Log.e("2-cropBitmap","2-createBitmap");
                return  Bitmap.createBitmap(bitmap,(int)MakeScale(Double.valueOf(rect.x),3),(int)MakeScale(Double.valueOf(rect.y),3)
                        , (int)MakeScale(Double.valueOf(rect.width),3), (int)MakeScale(Double.valueOf(rect.height),3));
            }catch (Exception ex){
                ImgRecognized=false;
                Snackbar.make(ViewArea,"Imagen no reconocidad",Snackbar.LENGTH_LONG).show();
                return bitmap;
            }
        }
        //Canvas canvas=new Canvas(ret);
        //canvas.drawBitmap(bitmap, -rect.x, -rect.y, null);
    }

    public static Bitmap flip(Bitmap src) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        //m.preRotate(-180);
        try {
            Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
            dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
            return dst;
        }catch (Exception e){}
        return src;
    }

    // type definition
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    public static Bitmap flip(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if(type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }
        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private class AProcessImage extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Recortando imagen capturada actual.");
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            BingoC.rWidth=bitmaps[0].getWidth();
            BingoC.rHeight= bitmaps[0].getHeight();
            settingomr2=new omrEngine();
            settingomr2.setMinArea(BingoC.takeMinArea);
            settingomr2.setMaxArea(BingoC.takeMaxArea);
            if(BingoC.rWidth!= BingoC.MaxWidth || BingoC.rHeight!=BingoC.MaxHeight){
                settingomr2.setMinArea(MakeScale(BingoC.takeMinArea,2));
                settingomr2.setMaxArea(MakeScale(BingoC.takeMaxArea,2));
            }
            Bitmap bmp=engineomr2.getFindSquares(bitmaps[0],settingomr2);
            if(Build.VERSION.SDK_INT<19){
                BingoC.SpecialDetection=true;
                try {BingoC.bitmap.recycle();
                }catch (Exception e){}
                BingoC.bitmap=cropBitmap(bmp, BingoC.rect);
                //BingoC.bitmap2=BingoC.bitmap;
                return  BingoC.bitmap;
            }else {
                BingoC.SpecialDetection=false;
                try {if(BingoC.bitmap!=null){
                    BingoC.bitmap.recycle();
                    BingoC.bitmap=null;
                }
                }catch (Exception e){}
                BingoC.bitmap=flip(cropBitmap(bmp, BingoC.rect));
                //BingoC.bitmap2=mOpenCvCameraView.getDataImage();
                return  BingoC.bitmap;
            }
            //engineomr.getColor(bitmaps[0], image);
            //--return  cropBitmap(bmp, BingoC.rect);
        }

        @Override
        protected void onPostExecute(Bitmap res) {
            log.printf("BingoC.mHeight: " + BingoC.mHeight);
            log.printf("BingoC.mWidth: " + BingoC.mWidth);
            log.printf("BingoC.rHeight: " + BingoC.rHeight);
            log.printf("BingoC.rWidth: " + BingoC.rWidth);
            log.printf("Scale min size: " + settingomr2.getMinArea());
            log.printf("Scale max size: " + settingomr2.getMaxArea());
            if(ImgRecognized){
                new ADetectImage().execute(res);
            }else{
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    pd = null;
                }
            }
            //showPreviewScren(res);
        }
    }

    private void setCameraDetection(){
        if(!BingoC.LoadNewConfig){
            BingoC.engine.setDpResolution(1.0);
            BingoC.engine.setMaxRadio(28);
            BingoC.engine.setMinRadio(4);
            BingoC.engine.setMinDistancia(22.0);
            BingoC.engine.setThresholdEdge(100.0);
            BingoC.engine.setThresholdCenter(22.0);
            BingoC.engine.setMinArea(300000);
            BingoC.engine.setMaxArea(360000);
        }
    }

    private class ADetectImage extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setCameraDetection();
            pd.setMessage("Detectando elementos en la hoja.");
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            return  engineomr3.getColorCircleTracking(bitmaps[0],BingoC.engine);
        }

        @Override
        protected void onPostExecute(Bitmap res) {
            if (pd != null) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                pd = null;
            }
            showPreviewScren(res);
        }
    }

    public void TomarFoto(){
        BingoC.rectStarted=false;
        new CountDownTimer(3000, 250) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                Chron.setVisibility(View.VISIBLE);
                Chron.setText(""+TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }

            public void onFinish() {
                Chron.setVisibility(View.GONE);
                if(!BingoC.Processing){
                    try{
                        new ATakePicture().execute();
                    }catch (Exception e){

                    }
                }
            }
        }.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TomarFoto();
        return false;
    }

    private void showPreviewScren(Bitmap res) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment editNameDialogFragment = PreviewImage.newInstance("","",res,this);
            editNameDialogFragment.show(fm, "fragment");
            BingoC.bitmap2=getByteFromBitmap(res);
        }
        catch (Exception e){}
    }

    private byte[] getByteFromBitmap(Bitmap b){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String sn = "";
        if (!data.getStringExtra(PreviewImage.RESULT_TYPE).equals("")) {
            sn = data.getStringExtra(PreviewImage.RESULT_TYPE);
        }
        switch (sn) {
            case PreviewImage.RESULT_OK:
                BingoC.TakePictureProcessing=true;
                break;
            case PreviewImage.RESULT_CANCELED:
                break;
        }
    }

    private  void ProcessSync() throws OutOfMemoryError {
                    Bitmap bitmap= mOpenCvCameraView.getBitmap();
                    BingoC.rWidth=bitmap.getWidth();
                    BingoC.rHeight= bitmap.getHeight();
                    settingomr2=new omrEngine();
                    settingomr2.setMinArea(BingoC.takeMinArea);
                    settingomr2.setMaxArea(BingoC.takeMaxArea);
                    if(BingoC.rWidth!= BingoC.MaxWidth || BingoC.rHeight!=BingoC.MaxHeight){
                        settingomr2.setMinArea(MakeScale(BingoC.takeMinArea,2));
                        settingomr2.setMaxArea(MakeScale(BingoC.takeMaxArea,2));
                    }
                    bitmap=engineomr2.getFindSquares(mOpenCvCameraView.getDataImage(),settingomr2);
        //if(true){
            //showPreviewScren(ThumbnailUtils.extractThumbnail(bitmap, 800, 600));
            //if (pd.isShowing()) pd.dismiss();
            //return;
        //}
                    BingoC.SpecialDetection=true;
                    try {
                        try {
                            if(BingoC.bitmap!=null){
                                BingoC.bitmap.recycle();
                                BingoC.bitmap=null;
                            }
                        }catch (Exception e){}
                        BingoC.bitmap=cropBitmap(bitmap, BingoC.rect);
                        BingoC.rect=null;
                        BingoC.bitmap2=mOpenCvCameraView.getDataImage();
                        if(!BingoC.ApplyCutSize)ImgRecognized=true;
                        if(ImgRecognized){
                            setCameraDetection();
                            showPreviewScren(engineomr3.getColorCircleTracking(BingoC.bitmap,BingoC.engine));
                        }
                        else {
                            if (pd.isShowing()) pd.dismiss();
                            Snackbar.make(ViewArea,"Imagen no reconocida :(",Snackbar.LENGTH_LONG).show();
                        }
                    }catch (OutOfMemoryError outOfMemoryError){
                        Snackbar.make(ViewArea,"Memoria RAM sobrecargada :(",Snackbar.LENGTH_LONG).show();
                        BingoC.Processing=false;
                        if (pd.isShowing()) pd.dismiss();

                    }
        if (pd.isShowing()) pd.dismiss();
            BingoC.Processing=false;
    }
}
