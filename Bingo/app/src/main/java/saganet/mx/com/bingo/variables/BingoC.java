package saganet.mx.com.bingo.variables;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;

import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import saganet.mx.com.bingo.Database.Tablas.HistorialEO;
import saganet.mx.com.bingo.sheet.SheetAnswers;
import saganet.mx.com.bingo.xdata.omrEngine;

/**
 * Created by LuisFernando on 24/02/2017.
 */

public class BingoC {
    public static Rect rect;
    public static Rect rect2;
    public static boolean ApplyCutSize=true;
    public static boolean rectStarted=true;
    public static boolean Processing;
    public static boolean TakePictureProcessing;
    public static Bitmap bitmap;
    public static byte[] bitmap2;
    public static int mWidth;
    public static int mHeight;
    public static int rWidth;
    public static int rHeight;
    public static final int MinWidth=960;
    public static final int MinHeight=540;
    public static final int MaxWidth=2064;
    public static final int MaxHeight=1552;
    public static final double trackMinArea=280000;
    public static final double trackMaxArea=370000;
    public static final double takeMinArea=7000;
    public static final double takeMaxArea=2500000;
    public static final double takeHeight=1500;
    public static final double takeWidth=900;
    public static SheetAnswers respuesta;
    public static omrEngine engine=new omrEngine();
    public static boolean LoadNewConfig;
    public static boolean SpecialDetection;
    public static final String DATA_BASE_EXTENTION=".db";
    public static final String DATA_BASE_NAME="com.saganet" + DATA_BASE_EXTENTION;
    public static final int DATA_BASE_VERSION = 1;
    public static final String DATA_BASE_SECURITY="!ZtaSwrwPYiDUwyKNSDFoop35+V7q%Ay9MhFGkDhit¿vLQ?)abFYhHq";
    public static List<HistorialEO> EOS=new ArrayList<HistorialEO>();
    public static List<String> mCasillas=new ArrayList<String>();
    public static String mCasilla;
    public static List<String> mCasillasId=new ArrayList<String>();
    public static String mCasillaId;
    public static List<String> mSecciones=new ArrayList<String>();
    public static String mSeccione;
    public static List<String> mSeccionesId=new ArrayList<String>();
    public static String mSeccioneId="";
    public static List<String> mUsuariosId=new ArrayList<String>();
    public static String mUsuarioId;
    public static List<String> mUsuariosNick=new ArrayList<String>();
    public static String mUsuarioNick;
    public static List<String> mUsuariosPassword=new ArrayList<String>();
    public static String mUsuarioPassword;
    public static List<String> mUsuariosVersiones=new ArrayList<String>();
    public static String mUsuarioVersion;
    public static boolean SegundoPlano;
    public static String PHOTO_PATH;
    public static String PHOTO_TITLE;
    public static String DEVICE_IMEI="0";
    public static String DEVICE_LATITUDE="0";
    public static String DEVICE_LONGITUDE="0";
    public static int CONTAR_CASILLAS_RECIVIDOS=0;
    public static int CONTAR_SECCIONES_RECIVIDOS=0;
    public static int CONTAR_USUARIOS_ASIGNACION_RECIVIDOS=0;
    public static int CONTAR_CASILLAS_ESPERADOS=0;
    public static int CONTAR_SECCIONES_ESPERADOS=0;
    public static int CONTAR_USUARIOS_ASIGNACION_ESPERADOS=0;

    public static final double resolicionDp=1.0;
    public static final int radMax=28;
    public static final int radMin=4;
    public static final double distMin=22.0;
    public static final double thresEdge=100.0;
    public static final double thresCenter=22.0;
    public static final boolean segPlano=false;
    public static final boolean impLog=true;
    public static final boolean recImg=true;

    public static final int CAMERA_13X=13000000;
    public static final int CAMERA_12X=12000000;
    public static final int CAMERA_9X=9000000;
    public static final int CAMERA_8X=8000000;
    public static final int CAMERA_5X=5000000;
    public static final int CAMERA_3X=3000000;
    public static final int CAMERA_2X=2000000;
    public static int CAMERA_XX=0;

    public static void Mensage(String s, Context context){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Aviso");
        builder1.setMessage(s);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /*
    URL DE SINCRONIZACIÓN
     */
    //http://192.168.1.69:8443/bingo/app/avisoEnvioArchivo
    //https://cgf.mx/Bingo/app/
    //http://cgf.mx/bingo/app/usuarioUpdate
    public static final String URL_USUARIOS="https://cgf.mx/Bingo/app/usuarioUpdate";
    public static final String URL_USUARIOS_PAQUETE="https://cgf.mx/Bingo/app/usuarioPaqueteUpdate";
    public static final String URL_SINCRONIZACION="https://cgf.mx/Bingo/app/resultadosUpdate";
    public static final String ExeptionResultSeverPass="error_clave_incorrecta";
    public static final String ExeptionResultSeverNick="error_usuario_no_existe";
    public static final String UsuarioUpdate="usuario_update";
    public static final String UsuarioSeccionUpdate="usuario_seccion_update";
    public static final String SeccionUpdate="seccion_update";
    public static final String CasillaUpdate="casilla_update";
    public static final String DatosSync="datos_sync";
}
