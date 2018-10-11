package saganet.mx.com.bingo.Database.Tablas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;

/**
 * Created by LuisFernando on 29/03/2017.
 */

public class UsuarioAsignacionEO extends Component {
    private int _id;
    private UsuarioEO idUsuario;
    private SeccionEO idSeccion;
    private CasillaEO idCasilla;
    private int version;
    private int operacion;

    public UsuarioAsignacionEO() {
    }

    public UsuarioAsignacionEO(int _id, UsuarioEO idUsuario, SeccionEO idSeccion, CasillaEO idCasilla, int version, int operacion) {
        this._id = _id;
        this.idUsuario = idUsuario;
        this.idSeccion = idSeccion;
        this.idCasilla = idCasilla;
        this.version = version;
        this.operacion = operacion;
    }

    @Override
    public String toString() {
        return "UsuarioAsignacionEO{" +
                "operacion=" + operacion +
                ", _id=" + _id +
                ", idUsuario=" + idUsuario +
                ", idSeccion=" + idSeccion +
                ", idCasilla=" + idCasilla +
                ", version=" + version +
                '}';
    }


    @Override
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public UsuarioEO getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UsuarioEO idUsuario) {
        this.idUsuario = idUsuario;
    }

    public SeccionEO getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(SeccionEO idSeccion) {
        this.idSeccion = idSeccion;
    }

    public CasillaEO getIdCasilla() {
        return idCasilla;
    }

    public void setIdCasilla(CasillaEO idCasilla) {
        this.idCasilla = idCasilla;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }

    private ContentValues v;

    @Override
    public ContentValues contentValues() {
        v=new ContentValues();
        v.put(Colums._ID,get_id());
        v.put(Colums.COLUMN_ID_USUARIO,getIdUsuario().get_id());
        v.put(Colums.COLUMN_ID_CASILLA,getIdCasilla().get_id());
        v.put(Colums.COLUMN_ID_SECCION,getIdSeccion().get_id());
        v.put(Colums.COLUMN_VERSION,getVersion());
        v.put(Colums.COLUMN_OPERACION,getOperacion());
        return v;
    }

    @Override
    public UsuarioAsignacionEO JSONValues(JSONObject jsonObject) throws JSONException {
        return new UsuarioAsignacionEO(
                jsonObject.optInt("pkey"),
                new UsuarioEO(jsonObject.optInt("id_usuario"),"","","","","",0,0),
                new SeccionEO(jsonObject.optInt("id_seccion"),"",0,0),
                new CasillaEO(jsonObject.optInt("id_casilla"),"",0,0),
                jsonObject.optInt("version"),
                jsonObject.optInt("operacion")
        );
    }

    @Override
    public String tableName() {
        return  Colums.TABLE_USUARIO_ASIGNACION;
    }
}
