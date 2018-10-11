package saganet.mx.com.bingo.Database.Tablas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;

/**
 * Created by LuisFernando on 29/03/2017.
 */

public class SeccionEO extends Component {
    private int _id;
    private String nombre;
    private int version;
    private int operacion;

    private ContentValues v;

    public SeccionEO(){

    }

    public SeccionEO(int _id, String nombre, int version, int operacion) {
        this._id = _id;
        this.nombre = nombre;
        this.version = version;
        this.operacion = operacion;
    }

    @Override
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return "SeccionEO{" +
                "operacion=" + operacion +
                ", _id=" + _id +
                ", nombre='" + nombre + '\'' +
                ", version=" + version +
                '}';
    }

    @Override
    public ContentValues contentValues() {
        v=new ContentValues();
        v.put(Colums._ID,get_id());
        v.put(Colums.COLUMN_NOMBRE,getNombre());
        v.put(Colums.COLUMN_VERSION,getVersion());
        v.put(Colums.COLUMN_OPERACION,getOperacion());
        return v;
    }

    @Override
    public Component JSONValues(JSONObject jsonObject) throws JSONException {
        return new SeccionEO(jsonObject.optInt("pkey"),jsonObject.optString("nombre"),jsonObject.optInt("version"),jsonObject.optInt("operacion"));
    }

    @Override
    public String tableName() {
        return  Colums.TABLE_SECCION;
    }
}
