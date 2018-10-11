package saganet.mx.com.bingo.Database.Tablas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;
import saganet.mx.com.bingo.startup.PreviewImage;

/**
 * Created by LuisFernando on 29/03/2017.
 */

public class UsuarioEO extends Component {
    private int _id;
    private String nick;
    private String password;
    private String nombre;
    private String paterno;
    private String materno;
    private int version;
    private int operacion;

    private ContentValues v;

    public UsuarioEO(){

    }
    public UsuarioEO(int _id, String nick, String password, String nombre, String paterno, String materno, int version, int operacion) {
        this._id = _id;
        this.nick = nick;
        this.password = password;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.version = version;
        this.operacion = operacion;
    }

    @Override
    public String toString() {
        return "UsuarioEO{" +
                "operacion=" + operacion +
                ", _id=" + _id +
                ", nick='" + nick + '\'' +
                ", password='" + password + '\'' +
                ", nombre='" + nombre + '\'' +
                ", paterno='" + paterno + '\'' +
                ", materno='" + materno + '\'' +
                ", version=" + version +
                '}';
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    @Override
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    @Override
    public ContentValues contentValues() {
        v=new ContentValues();
        v.put(Colums._ID,get_id());
        v.put(Colums.COLUMN_NICK,getNick());
        v.put(Colums.COLUMN_PASSWORD,getPassword());
        v.put(Colums.COLUMN_NOMBRE,getNombre());
        v.put(Colums.COLUMN_PATERNO,getPaterno());
        v.put(Colums.COLUMN_MATERNO,getMaterno());
        v.put(Colums.COLUMN_VERSION,getVersion());
        v.put(Colums.COLUMN_OPERACION,getOperacion());
        return v;
    }

    @Override
    public UsuarioEO JSONValues(JSONObject jsonObject) throws JSONException {
        return new UsuarioEO(
                jsonObject.optInt("id"),
                jsonObject.optString("nick"),
                jsonObject.optString("password"),
                jsonObject.optString("nombre"),
                jsonObject.optString("paterno"),
                jsonObject.optString("materno"),
                jsonObject.optInt("version"),
                jsonObject.optInt("operacion")
                );
    }

    @Override
    public String tableName() {
        return  Colums.TABLE_USUARIOS;
    }
}
