package saganet.mx.com.bingo.Database.Controler;

import android.content.ContentValues;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import saganet.mx.com.bingo.logger.LoggerC;

/**
 * Created by LuisFernando on 22/03/2017.
 */

public abstract class Component {
    LoggerC log=new LoggerC(Component.class);
    /*
    * _id para obtener un id
    * */
    public int get_id() {
        return 0;
    }
    public int getVersion() {
        return 0;
    }
    public int getOperacion() {
        return 0;
    }
    /*
    * getPkey para obtener un id pkey
    * */
    public int getPkey() {
        return 0;
    }
    /*
    * ContentValues para guardar los datos son putString()
    * */
    public abstract ContentValues contentValues();
    /*
    * Component para guardar los datos JSONValues retornados por el POST
    * */
    public abstract Component JSONValues(JSONObject jsonObject) throws JSONException;
    /*
    * String para obtener SQL sequence
    * */
    public String getAutoId(){return "SQLITE_SEQUENCE";}
    /*
    * Reinicia el auto incremento de la tabla
    * */
    public int ResetAutoId(SQLiteDatabase lite){
        return lite.delete(getAutoId(), Colums.NAME+" = ?", new String[]{tableName()});
    }
    /*
    * Optener el id del ultimo auto incremento
    * */
    public Cursor AutoId(SQLiteDatabase lite){
        return lite.query(getAutoId(), new String[]{Colums.SEQ}, Colums.NAME+" = ?",
                new String[]{tableName()}, null, null, null, null);
    }
    /*
    * Optener el nombre de la tabla
    * */
    public abstract String tableName();

    @Override
    public boolean equals(Object obj) {
        Component check;

        check = (Component) obj;

        if(this.getClass().equals(obj.getClass()))
            if(this.get_id()==check.get_id())
                return true;

        return false;
    }
}
