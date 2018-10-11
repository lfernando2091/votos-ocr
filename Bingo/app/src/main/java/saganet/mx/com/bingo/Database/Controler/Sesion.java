package saganet.mx.com.bingo.Database.Controler;

import android.content.ContentValues;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;

import saganet.mx.com.bingo.Database.Tablas.ConfigEO;
import saganet.mx.com.bingo.Database.Tablas.DatosEO;
import saganet.mx.com.bingo.Database.Tablas.HistorialEO;
import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.variables.BingoC;

/**
 * Created by LuisFernando on 22/03/2017.
 */
public class Sesion extends SQLiteOpenHelper {


    LoggerC log=new LoggerC(Sesion.class);
    private static Sesion instance;
    Context c=null;

    public static synchronized Sesion getHelper(Context context) {
        if (instance == null)
            instance = new Sesion(context);
        return instance;
    }
    public Sesion(Context context) {
        super(context, BingoC.DATA_BASE_NAME, null, BingoC.DATA_BASE_VERSION);
        c=context;
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CREATE_TABLES(sqLiteDatabase);
        SVE_DEFAULT_DATA(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_HISTORIAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_USUARIOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_SECCION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_CASILLA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_USUARIO_ASIGNACION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_CONFIG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Colums.TABLE_DATOS);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    SQLiteDatabase getReadableDatabase() {
        return(super.getReadableDatabase(BingoC.DATA_BASE_SECURITY));
    }

    SQLiteDatabase getWritableDatabase() {
        return(super.getWritableDatabase(BingoC.DATA_BASE_SECURITY));
    }

    private  void CREATE_TABLES(SQLiteDatabase db) {
        db.execSQL(Colums.CREATE_TABLE_HISTORIAL);
        db.execSQL(Colums.CREATE_INDEX_HISTORIAL_ID);
        db.execSQL(Colums.CREATE_INDEX_HISTORIAL_ID_USUARIO);
        db.execSQL(Colums.CREATE_INDEX_HISTORIAL_ID_SECCION);
        db.execSQL(Colums.CREATE_INDEX_HISTORIAL_ID_CASILLA);
        db.execSQL(Colums.CREATE_TABLE_USUARIOS);
        db.execSQL(Colums.CREATE_INDEX_USUARIOS_ID);
        db.execSQL(Colums.CREATE_TABLE_SECCION);
        db.execSQL(Colums.CREATE_INDEX_SECCION_ID);
        db.execSQL(Colums.CREATE_TABLE_CASILLA);
        db.execSQL(Colums.CREATE_INDEX_CASILLA_ID);
        db.execSQL(Colums.CREATE_TABLE_USUARIO_ASIGNACION);
        db.execSQL(Colums.CREATE_INDEX_USUARIO_ASIGNACION_ID);
        db.execSQL(Colums.CREATE_INDEX_USUARIO_ASIGNACION_ID_CASILLA);
        db.execSQL(Colums.CREATE_INDEX_USUARIO_ASIGNACION_ID_SECCION);
        db.execSQL(Colums.CREATE_INDEX_USUARIO_ASIGNACION_ID_USUARIO);
        db.execSQL(Colums.CREATE_TABLE_CONFIG);
        db.execSQL(Colums.CREATE_INDEX_CONFIG_ID);
        db.execSQL(Colums.CREATE_TABLE_DATOS);
        db.execSQL(Colums.CREATE_INDEX_DATOS_ID);
    }

    private void SVE_DEFAULT_DATA(SQLiteDatabase db){
        //Insert(db,new HistorialEO(getLastAutoId(new HistorialEO(),db),0,0,"-","-","-",0,"-","-",0,0,0));
        //Insert(db,new HistorialEO(getLastAutoId(new HistorialEO(),db),0,0,"-","-","-",0,"-","-",0,0,0));
        ConfigEO configEO1= new ConfigEO(1,BingoC.resolicionDp,BingoC.radMax, BingoC.radMin,BingoC.distMin, BingoC.thresEdge,BingoC.thresCenter,BingoC.segPlano,BingoC.impLog,BingoC.recImg);
        db.insert(configEO1.tableName(), null, configEO1.contentValues());
    }

    public long SVE(Component component){
        SQLiteDatabase sqld= getWritableDatabase();
        return sqld.insert(component.tableName(),null,component.contentValues());
    }

    public long UDE(Component component){
        SQLiteDatabase sqld= getWritableDatabase();
        return sqld.update(component.tableName(),component.contentValues(),"_id="+component.get_id(), null);
    }

    public long UDE(Component component, String[] colums, String[] values){
        SQLiteDatabase sqld= getWritableDatabase();
        ContentValues v =new ContentValues();
        for(int c=0;c<colums.length; c++){
            v.put(colums[0],values[0]);
        }
        return sqld.update(component.tableName(),v,"_id="+component.get_id(), null);
    }

    public int getLastId(Component component){
        final String MY_QUERY = "SELECT MAX(_id) FROM " + component.tableName();
        Cursor cur = this.getReadableDatabase().rawQuery(MY_QUERY, null);
        cur.moveToFirst();int ID = cur.getInt(0);cur.close();
        return ID;
    }

    public int getLastAutoId(Component component, SQLiteDatabase db) {
        int index = 0;
        Cursor cursor = component.AutoId(db);
        if(cursor.getCount() > 0){
            if (cursor.moveToFirst()) index = cursor.getInt(cursor.getColumnIndex(Colums.SEQ));
            cursor.close();return index+1;
        }else {
            return index;
        }
    }

    public int getLastAutoId(Component component) {
        return getLastAutoId(component, getReadableDatabase());
    }

    public int getResetAutoId(Component component) {
        return component.ResetAutoId(getReadableDatabase());
        //return sdb.delete(component.getAutoId(), Colums.NAME+" = ?", new String[]{component.tableName()});
    }

    private void Insert(SQLiteDatabase db,Component contact) {
        db.insert(contact.tableName(), null, contact.contentValues());
    }

    public void getHistorial() {
        String rawQuery = "SELECT "+
                Colums._ID+","+
                Colums.COLUMN_ID_USUARIO+","+
                Colums.COLUMN_ID_CASILLA+","+
                Colums.COLUMN_ID_SECCION+","+
                Colums.COLUMN_URL_IMAGE +","+
                Colums.COLUMN_TITLE+","+
                Colums.COLUMN_DESCRIPTION+","+
                Colums.COLUMN_TIME+","+
                Colums.COLUMN_DINFO+","+
                Colums.COLUMN_COUT+
                " FROM " + Colums.TABLE_HISTORIAL+
                " WHERE " + Colums.COLUMN_DELETED+"=0"+
                " AND " + Colums.COLUMN_SYNC+"=0"+
                " AND " + Colums.COLUMN_ID_USUARIO+ "="+BingoC.mUsuarioId;
        log.printf("SQL:: " + rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        BingoC.EOS= new ArrayList<HistorialEO>();
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    HistorialEO historialEO= new HistorialEO();
                    historialEO.set_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Colums._ID))));
                    historialEO.setIdUsuario(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_ID_USUARIO))));
                    historialEO.setIdCasilla(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_ID_CASILLA)));
                    historialEO.setIdSeccion(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_ID_SECCION))));
                    historialEO.setImgDescription(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_DESCRIPTION)));
                    historialEO.setImgCout(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_COUT))));
                    historialEO.setImgTitle(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_TITLE)));
                    historialEO.setImgUrl(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_URL_IMAGE)));
                    historialEO.setFechaCaptura(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_TIME)));
                    historialEO.setImgDInfo(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_DINFO)));
                    BingoC.EOS.add(historialEO);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public ConfigEO getConfiguracion(){
        ConfigEO configEO= new ConfigEO();
        String rawQuery = "SELECT "+
                Colums._ID+","+
                Colums.COLUMN_RESOLUCION_DP+","+
                Colums.COLUMN_RADIO_MAX+","+
                Colums.COLUMN_RADIO_MIN +","+
                Colums.COLUMN_DIST_MIN+","+
                Colums.COLUMN_THRES_EDGE+","+
                Colums.COLUMN_THRES_CENTER+","+
                Colums.COLUMN_SEG_PLANO+","+
                Colums.COLUMN_IMP_LOG+","+
                Colums.COLUMN_REC_IMG+
                " FROM " + Colums.TABLE_CONFIG+
                " WHERE " + Colums._ID+"=1";
        log.printf("SQL:: " + rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            BingoC.EOS= new ArrayList<HistorialEO>();
            if (cursor.moveToFirst()) {
                do {
                    configEO= new ConfigEO();
                    configEO.set_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Colums._ID))));
                    configEO.setDistMin(Double.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_DIST_MIN))));
                    configEO.setImpLog(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_IMP_LOG))));
                    configEO.setRadMax(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_RADIO_MAX))));
                    configEO.setRadMin(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_RADIO_MIN))));
                    configEO.setRecImg(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_REC_IMG))));
                    configEO.setSegPlano(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_SEG_PLANO))));
                    configEO.setThresCenter(Double.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_THRES_CENTER))));
                    configEO.setThresEdge(Double.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_THRES_EDGE))));
                    configEO.setResolicionDp(Double.valueOf(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_RESOLUCION_DP))));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return configEO;
    }

    public void getUserData(String user){
        String rawQuery = "SELECT "+
                Colums._ID+","+
                Colums.TABLE_CASILLA+"."+ Colums.COLUMN_NOMBRE + " AS "+Colums.COLUMN_NOMBRE + "_C," +
                Colums.TABLE_SECCION+"."+ Colums.COLUMN_NOMBRE + " AS "+Colums.COLUMN_NOMBRE + "_S," +
                Colums.TABLE_USUARIOS+"."+ Colums._ID + " AS "+Colums._ID+"_U"
                +" FROM " +
                Colums.TABLE_USUARIO_ASIGNACION
                +" JOIN " +
                Colums.TABLE_USUARIOS
                +" ON "+
                Colums.COLUMN_ID_USUARIO
                +" = "+
                Colums.TABLE_USUARIOS+"."+ Colums._ID
                +" JOIN " +
                Colums.TABLE_CASILLA
                +" ON "+
                Colums.COLUMN_ID_CASILLA
                +" = "+
                Colums.TABLE_CASILLA+"."+ Colums._ID
                +" JOIN " +
                Colums.TABLE_SECCION
                +" ON "+
                Colums.COLUMN_ID_SECCION
                +" = "+
                Colums.TABLE_SECCION+"."+ Colums._ID
                +" WHERE " +
                Colums.COLUMN_ID_USUARIO
                + " = " +
                user
                +" ORDER BY " +
                Colums._ID;
        log.printf("SQL getUserData:: " + rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            BingoC.mCasillas= new ArrayList<String>();
            BingoC.mSecciones= new ArrayList<String>();
            if (cursor.moveToFirst()) {
                do {
                    BingoC.mSecciones.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_NOMBRE+ "_C")));
                    BingoC.mCasillas.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_NOMBRE+ "_S")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void getUser(){
        String rawQuery = "SELECT "+
                Colums._ID + ","+
                Colums.COLUMN_NICK + ","+
                Colums.COLUMN_PASSWORD + ","+
                Colums.COLUMN_VERSION
                +" FROM " +
                Colums.TABLE_USUARIOS
                +" ORDER BY " +
                Colums._ID;
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            BingoC.mUsuariosId= new ArrayList<String>();
            BingoC.mUsuariosNick= new ArrayList<String>();
            BingoC.mUsuariosPassword= new ArrayList<String>();
            BingoC.mUsuariosVersiones= new ArrayList<String>();
            if (cursor.moveToFirst()) {
                do {
                    BingoC.mUsuariosId.add(cursor.getString(cursor.getColumnIndex(Colums._ID)));
                    BingoC.mUsuariosNick.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_NICK)));
                    BingoC.mUsuariosPassword.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_PASSWORD)));
                    BingoC.mUsuariosVersiones.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_VERSION)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void Casillas(String userId, String seccion){
       BingoC.mSeccioneId=seccion;
        String rawQuery = "SELECT DISTINCT "+
                Colums.TABLE_CASILLA+ "."+Colums._ID+ "," +
                Colums.TABLE_CASILLA+ "."+Colums.COLUMN_NOMBRE +
                " FROM "+
                Colums.TABLE_CASILLA+
                " JOIN "+
                Colums.TABLE_USUARIO_ASIGNACION+
                " ON "+
                Colums.TABLE_CASILLA+"."+Colums._ID+
                "="+
                Colums.TABLE_USUARIO_ASIGNACION+"."+Colums.COLUMN_ID_CASILLA+
                " WHERE "+ (seccion.equals("")?
                    Colums.TABLE_USUARIO_ASIGNACION+"."+Colums.COLUMN_ID_USUARIO:
                        userId.equals("")?
                                Colums.TABLE_USUARIO_ASIGNACION+"."+Colums.COLUMN_ID_SECCION:"")+
                "="+
                (seccion.equals("")?userId:seccion)+
                " ORDER BY "+
                Colums.TABLE_CASILLA+ "."+Colums._ID;
        log.printf(rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        BingoC.mCasillas= new ArrayList<String>();
        BingoC.mCasillasId= new ArrayList<String>();
        BingoC.mCasillas.add(0,"Selecciona Casilla");
        BingoC.mCasillasId.add(0,"0");
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    BingoC.mCasillas.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_NOMBRE)));
                    BingoC.mCasillasId.add(cursor.getString(cursor.getColumnIndex(Colums._ID)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void obtenerCasillas(String userId, String seccion){
        if (seccion.equals("")){
            BingoC.mCasillas= new ArrayList<String>();
            BingoC.mCasillasId= new ArrayList<String>();
            BingoC.mCasillas.add(0,"Selecciona Casilla");
            BingoC.mCasillasId.add(0,"0");
        }
        else {
            Casillas(userId,seccion);
        }
    }

    public void obtenerSeccion(){
        String rawQuery = "SELECT DISTINCT "+
                Colums.TABLE_SECCION+ "."+Colums._ID+ "," +
                Colums.TABLE_SECCION+ "."+Colums.COLUMN_NOMBRE +
                " FROM "+
                Colums.TABLE_SECCION+
                " JOIN "+
                Colums.TABLE_USUARIO_ASIGNACION+
                " ON "+
                Colums.TABLE_SECCION+"."+Colums._ID+
                "="+
                Colums.TABLE_USUARIO_ASIGNACION+"."+Colums.COLUMN_ID_SECCION+
                " WHERE "+
                Colums.TABLE_USUARIO_ASIGNACION+"."+Colums.COLUMN_ID_USUARIO+
                "="+
                BingoC.mUsuarioId+
                " ORDER BY "+
                Colums.TABLE_SECCION+ "."+Colums._ID;
        log.printf(rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        BingoC.mSecciones= new ArrayList<String>();
        BingoC.mSeccionesId= new ArrayList<String>();
        BingoC.mSecciones.add(0,"Selecciona Sección");
        BingoC.mSeccionesId.add(0,"0");
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    BingoC.mSecciones.add(cursor.getString(cursor.getColumnIndex(Colums.COLUMN_NOMBRE)));
                    BingoC.mSeccionesId.add(cursor.getString(cursor.getColumnIndex(Colums._ID)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public DatosEO obtenerEstados(String user){
        DatosEO datosEO = new DatosEO();
        String rawQuery =
                "SELECT DISTINCT  count(distinct  a."+Colums._ID+") "+Colums.TABLE_USUARIO_ASIGNACION+
                        ", count(distinct  a."+Colums.COLUMN_ID_SECCION+") "+Colums.TABLE_SECCION+
                        ", count(distinct  a."+Colums.COLUMN_ID_CASILLA+") "+Colums.TABLE_CASILLA+
                        ","+Colums.COLUMN_USUARIO_SECCION_CONTAR+
                        ","+Colums.COLUMN_SECCION_CONTAR+
                        ","+Colums.COLUMN_CASILLA_CONTAR+
                        ","+Colums.COLUMN_USUARIO_SECCION_PAQUETES+
                        " FROM "+Colums.TABLE_USUARIO_ASIGNACION+" a "+
                        ","+Colums.TABLE_DATOS+" d"+
                        " LEFT JOIN "+Colums.TABLE_SECCION+
                        " s ON a."+Colums.COLUMN_ID_SECCION+
                        "=s."+Colums._ID+
                        " LEFT JOIN "+Colums.TABLE_CASILLA+
                        " c ON a."+Colums.COLUMN_ID_CASILLA+
                        "=c."+Colums._ID+
                        " WHERE d."+Colums._ID+"="+user+
                        " AND "+Colums.COLUMN_ID_USUARIO+"="+user;
        log.printf(rawQuery);
        Cursor cursor = this.getReadableDatabase().rawQuery(rawQuery,null);
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    datosEO.setCasillaContar(BingoC.CONTAR_CASILLAS_RECIVIDOS=cursor.getInt(cursor.getColumnIndex(Colums.TABLE_CASILLA)));
                    datosEO.setSeccionContar(BingoC.CONTAR_SECCIONES_RECIVIDOS=cursor.getInt(cursor.getColumnIndex(Colums.TABLE_SECCION)));
                    datosEO.setUsuarioSeccionContar(BingoC.CONTAR_USUARIOS_ASIGNACION_RECIVIDOS=cursor.getInt(cursor.getColumnIndex(Colums.TABLE_USUARIO_ASIGNACION)));
                    datosEO.setUsuarioSeccionPaquetes(cursor.getInt(cursor.getColumnIndex(Colums.COLUMN_USUARIO_SECCION_PAQUETES)));
                    BingoC.CONTAR_CASILLAS_ESPERADOS=cursor.getInt(cursor.getColumnIndex(Colums.COLUMN_CASILLA_CONTAR));
                    BingoC.CONTAR_SECCIONES_ESPERADOS=cursor.getInt(cursor.getColumnIndex(Colums.COLUMN_SECCION_CONTAR));
                    BingoC.CONTAR_USUARIOS_ASIGNACION_ESPERADOS=cursor.getInt(cursor.getColumnIndex(Colums.COLUMN_USUARIO_SECCION_CONTAR));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return datosEO;
    }
}
