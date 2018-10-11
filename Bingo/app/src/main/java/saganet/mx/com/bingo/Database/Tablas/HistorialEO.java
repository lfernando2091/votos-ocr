package saganet.mx.com.bingo.Database.Tablas;

import android.content.ContentValues;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;

/**
 * Created by LuisFernando on 22/03/2017.
 */

public class HistorialEO extends Component {
    private int _id;
    private int idUsuario;
    private int idSeccion;
    private String idCasilla;
    //private UsuarioAsignacionEO idUsuarioAsignacion;
    private String imgUrl;
    private String imgTitle;
    private int imgCout;
    private String imgDescription;
    private String imgDInfo;
    private String fechaCaptura;
    private int imgDelete;
    private int sync;
    private int version;

    private ContentValues v;

    public HistorialEO() {

    }

    public HistorialEO(int _id) {
        this._id = _id;
    }

    public HistorialEO(int _id, int idUsuario, int idSeccion, String idCasilla, String imgUrl, String imgTitle, int imgCout, String imgDescription, String imgDInfo, int imgDelete, int sync, int version) {
        this._id = _id;
        this.idUsuario = idUsuario;
        this.idSeccion = idSeccion;
        this.idCasilla = idCasilla;
        this.imgUrl = imgUrl;
        this.imgTitle = imgTitle;
        this.imgCout = imgCout;
        this.imgDescription = imgDescription;
        this.imgDInfo = imgDInfo;
        this.imgDelete = imgDelete;
        this.sync = sync;
        this.version = version;
    }

    @Override
    public int get_id() {
        return _id;
    }

    @Override
    public ContentValues contentValues() {
        v=new ContentValues();
        v.put(Colums._ID,get_id());
        v.put(Colums.COLUMN_ID_USUARIO,getIdUsuario());
        v.put(Colums.COLUMN_ID_CASILLA,getIdCasilla());
        v.put(Colums.COLUMN_ID_SECCION,getIdSeccion());
        //v.put(Colums.COLUMN_ID_USUARIO_ASIGNACION,getIdUsuarioAsignacion().get_id());
        v.put(Colums.COLUMN_URL_IMAGE,getImgUrl());
        v.put(Colums.COLUMN_TITLE,getImgTitle());
        v.put(Colums.COLUMN_COUT,getImgCout());
        v.put(Colums.COLUMN_DESCRIPTION,getImgDescription());
        v.put(Colums.COLUMN_DINFO,getImgDInfo());
        v.put(Colums.COLUMN_TIME,(fechaCaptura=DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date()).toString()));
        v.put(Colums.COLUMN_DELETED,getImgDelete());
        v.put(Colums.COLUMN_SYNC,getSync());
        v.put(Colums.COLUMN_VERSION,getVersion());
        return v;
    }

    @Override
    public Component JSONValues(JSONObject jsonObject) throws JSONException {
        return null;
    }


    @Override
    public String tableName() {
        return Colums.TABLE_HISTORIAL;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getIdCasilla() {
        return idCasilla;
    }

    public void setIdCasilla(String idCasilla) {
        this.idCasilla = idCasilla;
    }

    public int getImgCout() {
        return imgCout;
    }

    public void setImgCout(int imgCout) {
        this.imgCout = imgCout;
    }

    public String getImgDInfo() {
        return imgDInfo;
    }

    public void setImgDInfo(String imgDInfo) {
        this.imgDInfo = imgDInfo;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getImgDescription() {
        return imgDescription;
    }

    public void setImgDescription(String imgDescription) {
        this.imgDescription = imgDescription;
    }

    public int getImgDelete() {
        return imgDelete;
    }

    public void setImgDelete(int imgDelete) {
        this.imgDelete = imgDelete;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }
}
