package saganet.mx.com.bingo.Database.Tablas;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.Database.Controler.Colums;
import saganet.mx.com.bingo.Database.Controler.Component;

/**
 * Created by LuisFernando on 26/04/2017.
 */

public class DatosEO extends Component {
    private int _id;
    private int seccionContar;
    private int casillaContar;
    private int usuarioSeccionContar;
    private int usuarioSeccionPaquetes;
    private int ultimoId;
    private ContentValues v;

    @Override
    public String toString() {
        return "DatosEO{" +
                "_id=" + _id +
                ", seccionContar=" + seccionContar +
                ", casillaContar=" + casillaContar +
                ", usuarioSeccionContar=" + usuarioSeccionContar +
                ", usuarioSeccionPaquetes=" + usuarioSeccionPaquetes +
                ", ultimoId=" + ultimoId +
                '}';
    }

    public DatosEO() {}

    public DatosEO(int _id, int seccionContar, int casillaContar, int usuarioSeccionContar, int usuarioSeccionPaquetes, int ultimoId) {
        this._id = _id;
        this.seccionContar = seccionContar;
        this.casillaContar = casillaContar;
        this.usuarioSeccionContar = usuarioSeccionContar;
        this.usuarioSeccionPaquetes = usuarioSeccionPaquetes;
        this.ultimoId = ultimoId;
    }

    @Override
    public ContentValues contentValues() {
        v=new ContentValues();
        v.put(Colums._ID,get_id());
        v.put(Colums.COLUMN_SECCION_CONTAR,getSeccionContar());
        v.put(Colums.COLUMN_CASILLA_CONTAR,getCasillaContar());
        v.put(Colums.COLUMN_USUARIO_SECCION_CONTAR,getUsuarioSeccionContar());
        v.put(Colums.COLUMN_USUARIO_SECCION_PAQUETES,getUsuarioSeccionPaquetes());
        v.put(Colums.COLUMN_USUARIO_SECCION_ULTIMO_ID,getUltimoId());
        return v;
    }

    @Override
    public Component JSONValues(JSONObject jsonObject) throws JSONException {
        int c;
        this.ultimoId=(c=jsonObject.optInt("usuario_seccion_ultimo_id"));
        this.usuarioSeccionPaquetes=(c=jsonObject.optInt("usuario_seccion_paquetes"))!=0?c:this.usuarioSeccionPaquetes;
        this.seccionContar=(c=jsonObject.optInt("seccion_contar"))!=0?c:this.seccionContar;
        this.casillaContar=(c=jsonObject.optInt("casilla_contar"))!=0?c:this.casillaContar;
        this.usuarioSeccionContar=(c=jsonObject.optInt("usuario_seccion_contar"))!=0?c:this.usuarioSeccionContar;
        this._id=(c=jsonObject.optInt("id"))!=0?c:this._id;
        return new DatosEO(this._id,this.seccionContar,this.casillaContar,this.usuarioSeccionContar,this.usuarioSeccionPaquetes,this.ultimoId){};
    }

    @Override
    public String tableName() {
        return Colums.TABLE_DATOS;
    }

    @Override
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getSeccionContar() {
        return seccionContar;
    }

    public void setSeccionContar(int seccionContar) {
        this.seccionContar = seccionContar;
    }

    public int getCasillaContar() {
        return casillaContar;
    }

    public void setCasillaContar(int casillaContar) {
        this.casillaContar = casillaContar;
    }

    public int getUsuarioSeccionContar() {
        return usuarioSeccionContar;
    }

    public void setUsuarioSeccionContar(int usuarioSeccionContar) {
        this.usuarioSeccionContar = usuarioSeccionContar;
    }

    public int getUsuarioSeccionPaquetes() {
        return usuarioSeccionPaquetes;
    }

    public void setUsuarioSeccionPaquetes(int usuarioSeccionPaquetes) {
        this.usuarioSeccionPaquetes = usuarioSeccionPaquetes;
    }

    public int getUltimoId() {
        return ultimoId;
    }

    public void setUltimoId(int ultimoId) {
        this.ultimoId = ultimoId;
    }
}
