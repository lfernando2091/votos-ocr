package saganet.mx.com.bingo.file;

import android.net.Uri;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import saganet.mx.com.bingo.logger.LoggerC;

/**
 * Created by LuisFernando on 16/02/2017.
 */

public abstract class SaveMedia {
    LoggerC log = new LoggerC(SaveMedia.class);
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String LOCATION_IMAGE="/sdcard/Bingo/img/";
    public static final String LOCATION_SYNC="/sdcard/Bingo/sync/";

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(LOCATION_IMAGE);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            if(mediaFile.exists())mediaFile.delete();
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
            if(mediaFile.exists())mediaFile.delete();
        } else {
            return null;
        }

        return mediaFile;
    }
    /** Create a File for saving something */
    public static File getOutputFile(String nameFile){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(LOCATION_SYNC);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SAGANET_"+ nameFile + ".json");
            if(mediaFile.exists())mediaFile.delete();
        return mediaFile;
    }
}
