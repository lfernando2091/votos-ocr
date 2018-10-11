package saganet.mx.com.bingo.Database.Tablas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;

/**
 * Created by LuisFernando on 11/04/2017.
 */

public class ConfigEO extends Component {
    private int _id;
    private double resolicionDp;
    private int radMax;
    private int radMin;
    private double distMin;
    private double thresEdge;
    private double thresCenter;
    private boolean segPlano;
    private boolean impLog;
    private boolean recImg;
    private ContentValues v;

    public ConfigEO(int _id, double resolicionDp, int radMax, int radMin, double distMin, double thresEdge, double thresCenter, boolean segPlano, boolean impLog,boolean recImg) {
        this.recImg = recImg;
        this._id = _id;
        this.resolicionDp = resolicionDp;
        this.radMax = radMax;
        this.radMin = radMin;
        this.distMin = distMin;
        this.thresEdge = thresEdge;
        this.thresCenter = thresCenter;
        this.segPlano = segPlano;
        this.impLog = impLog;
    }
    public ConfigEO(){

    }

    @Override
    public String toString() {
        return "ConfigEO{" +
                "_id=" + _id +
                ", resolicionDp=" + resolicionDp +
                ", radMax=" + radMax +
                ", radMin=" + radMin +
                ", distMin=" + distMin +
                ", thresEdge=" + thresEdge +
                ", thresCenter=" + thresCenter +
                ", segPlano=" + segPlano +
                ", impLog=" + impLog +
                ", recImg=" + recImg +
                '}';
    }

    @Override
    public ContentValues contentValues() {
        v=new ContentValues();
        v.put(Colums._ID,get_id());
        v.put(Colums.COLUMN_RESOLUCION_DP,getResolicionDp());
        v.put(Colums.COLUMN_RADIO_MAX,getRadMax());
        v.put(Colums.COLUMN_RADIO_MIN,getRadMin());
        v.put(Colums.COLUMN_DIST_MIN,getDistMin());
        v.put(Colums.COLUMN_THRES_EDGE,getThresEdge());
        v.put(Colums.COLUMN_THRES_CENTER,getThresCenter());
        v.put(Colums.COLUMN_SEG_PLANO,isSegPlano());
        v.put(Colums.COLUMN_IMP_LOG,isImpLog());
        v.put(Colums.COLUMN_REC_IMG,isRecImg());
        return v;
    }

    @Override
    public Component JSONValues(JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    public String tableName() {
        return Colums.TABLE_CONFIG;
    }


    @Override
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double getResolicionDp() {
        return resolicionDp;
    }

    public void setResolicionDp(double resolicionDp) {
        this.resolicionDp = resolicionDp;
    }

    public int getRadMax() {
        return radMax;
    }

    public void setRadMax(int radMax) {
        this.radMax = radMax;
    }

    public int getRadMin() {
        return radMin;
    }

    public void setRadMin(int radMin) {
        this.radMin = radMin;
    }

    public double getDistMin() {
        return distMin;
    }

    public void setDistMin(double distMin) {
        this.distMin = distMin;
    }

    public double getThresEdge() {
        return thresEdge;
    }

    public void setThresEdge(double thresEdge) {
        this.thresEdge = thresEdge;
    }

    public double getThresCenter() {
        return thresCenter;
    }

    public void setThresCenter(double thresCenter) {
        this.thresCenter = thresCenter;
    }

    public boolean isSegPlano() {
        return segPlano;
    }

    public void setSegPlano(boolean segPlano) {
        this.segPlano = segPlano;
    }

    public boolean isImpLog() {
        return impLog;
    }

    public void setImpLog(boolean impLog) {
        this.impLog = impLog;
    }

    public boolean isRecImg() {
        return recImg;
    }

    public void setRecImg(boolean recImg) {
        this.recImg = recImg;
    }
}
