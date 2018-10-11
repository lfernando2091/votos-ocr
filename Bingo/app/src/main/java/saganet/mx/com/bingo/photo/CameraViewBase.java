package saganet.mx.com.bingo.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.variables.BingoC;
import saganet.mx.com.bingo.xdata.omr;


/**
 * Created by LuisFernando on 27/02/2017.
 */

public class CameraViewBase extends JavaCameraView implements PictureCallback {
    LoggerC log=new LoggerC(CameraViewBase.class);
    private File mPictureFileName;
    private Bitmap bitmap=null;
    private byte[] dataImage;

    public CameraViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }
    public void setJpegMaxQuality() {
        Camera.Parameters params = mCamera.getParameters();
        log.printf("camara--- "+mCamera.getParameters().flatten());
        // Check what resolutions are supported by your camera
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        // Iterate through all available resolutions and choose one.
        // The chosen resolution will be stored in mSize.
        Camera.Size mSize = null;
        Collections.sort(sizes, new Comparator<Camera.Size>(){
            public int compare(Camera.Size obj1, Camera.Size obj2) {
                // ## Ascending order
                return obj2.height - obj1.height; // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values
                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });
        //for (Size size : sizes)log.printf("Chosen resolution: "+size.width+" "+size.height);
        mSize = sizes.get(0);
        log.printf( "Chosen resolution: "+mSize.width+" "+mSize.height);
        BingoC.CAMERA_XX=(mSize.width*mSize.height);
        params.setPictureSize(mSize.width, mSize.height);
        //params.set("iso-speed", 400);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

    public void takePicture(final File fileName) {
        log.printf("Taking picture");
        this.mPictureFileName = fileName;
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);

        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
    }

    public byte[] getDataImage() {
        return dataImage;
    }

    public Bitmap getBitmap() throws OutOfMemoryError{
        return BitmapFactory.decodeByteArray(this.dataImage, 0, this.dataImage.length);
    }
    public Bitmap getURLBitmap(){
        urlToBitmap(mPictureFileName);
        return bitmap;
    }
    private void convertToBitmap(byte[] photoPath){
        try {bitmap.recycle();
        }catch (Exception e){}
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap= BitmapFactory.decodeByteArray(photoPath, 0, photoPath.length);
        }catch (OutOfMemoryError e){
            try {bitmap.recycle();
            }catch (Exception ex){}
        }
    }


    private void bytesArrayImage(byte[] photoPath){
        this.dataImage=photoPath;
    }

    private void urlToBitmap(File f){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {bitmap.recycle();
        }catch (Exception e){        }
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

        // Write the image in a file (in jpeg format)
            // The camera preview was automatically stopped. Start it again.
            //convertToBitmap(bytes);
            bytesArrayImage(bytes);
            //--FileOutputStream fos = new FileOutputStream(mPictureFileName);
            //--fos.write(bytes);
            //--fos.close();
            BingoC.PHOTO_PATH=this.mPictureFileName.getPath();
            BingoC.PHOTO_TITLE=mPictureFileName.getName();
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            //Log.e("PictureDemo", "Exception in photoCallback", e);


    }
}
