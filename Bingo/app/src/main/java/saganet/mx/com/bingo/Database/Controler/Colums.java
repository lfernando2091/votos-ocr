package saganet.mx.com.bingo.Database.Controler;

import android.provider.BaseColumns;

/**
 * Created by LuisFernando on 22/03/2017.
 */

public class Colums  implements BaseColumns {
    private static final String CREATE="CREATE TABLE IF NOT EXISTS ";
    public static final String SEQ="seq";
    public static final String NAME="name";

    /*TABLA USUARIO*/
    public static final String TABLE_USUARIOS="usuarios";
    public static final String COLUMN_NICK="nick";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_NOMBRE="nombre";
    public static final String COLUMN_PATERNO="paterno";
    public static final String COLUMN_MATERNO="materno";
    public static final String COLUMN_VERSION="version";
    public static final String COLUMN_OPERACION="operacion";
    public static final String CREATE_INDEX_USUARIOS_ID=
            "CREATE INDEX index_"+
                    TABLE_USUARIOS+
                    "_id ON "+
                    TABLE_USUARIOS+
                    " (_id ASC);";
    public static final String CREATE_TABLE_USUARIOS =
            CREATE + TABLE_USUARIOS +
                    "(" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NICK + " TEXT NOT NULL," +
                    COLUMN_PASSWORD + " TEXT NOT NULL," +
                    COLUMN_NOMBRE + " TEXT NOT NULL," +
                    COLUMN_PATERNO + " TEXT," +
                    COLUMN_MATERNO + " TEXT," +
                    COLUMN_VERSION + " INTEGER NOT NULL," +
                    COLUMN_OPERACION + " INTEGER NOT NULL)" ;

    /*TABLA SECCION*/
    public static final String TABLE_SECCION="seccion";
    public static final String CREATE_INDEX_SECCION_ID=
            "CREATE INDEX index_"+
                    TABLE_SECCION+
                    "_id ON "+
                    TABLE_SECCION+
                    " (_id ASC);";
    public static final String CREATE_TABLE_SECCION =
            CREATE + TABLE_SECCION +
                    "(" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NOMBRE + " TEXT NOT NULL," +
                    COLUMN_VERSION + " INTEGER NOT NULL," +
                    COLUMN_OPERACION + " INTEGER NOT NULL)" ;

    /*TABLA CASILLA*/
    public static final String TABLE_CASILLA="casilla";
    public static final String CREATE_INDEX_CASILLA_ID=
            "CREATE INDEX index_"+
                    TABLE_CASILLA+
                    "_id ON "+
                    TABLE_CASILLA+
                    " (_id ASC);";
    public static final String CREATE_TABLE_CASILLA =
            CREATE + TABLE_CASILLA +
                    "(" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NOMBRE + " TEXT NOT NULL," +
                    COLUMN_VERSION + " INTEGER NOT NULL," +
                    COLUMN_OPERACION + " INTEGER NOT NULL)" ;

    /*TABLA ASIGNACION*/
    public static final String TABLE_USUARIO_ASIGNACION="usuario_sec_cas";
    public static final String CREATE_CONSTRAINT_ID_USUARIO="CONSTRAINT "+
            TABLE_USUARIO_ASIGNACION +
            "_id_"+
            TABLE_USUARIOS+
            "_id_fkey REFERENCES "+
            TABLE_USUARIOS
            +" (_id)";
    public static final String CREATE_CONSTRAINT_ID_CASILLA="CONSTRAINT "+
            TABLE_USUARIO_ASIGNACION +
            "_id_"+
            TABLE_CASILLA+
            "_id_fkey REFERENCES "+
            TABLE_CASILLA
            +" (_id)";
    public static final String CREATE_CONSTRAINT_ID_SECCION="CONSTRAINT "+
            TABLE_USUARIO_ASIGNACION +
            "_id_"+
            TABLE_SECCION+
            "_id_fkey REFERENCES "+
            TABLE_SECCION
            +" (_id)";
    public static final String COLUMN_ID_USUARIO="idUsuario";
    public static final String COLUMN_ID_CASILLA="idCasilla";
    public static final String COLUMN_ID_SECCION="idSeccion";
    public static final String CREATE_INDEX_USUARIO_ASIGNACION_ID=
            "CREATE INDEX index_"+
                    TABLE_USUARIO_ASIGNACION+
                    "_id ON "+
                    TABLE_USUARIO_ASIGNACION+
                    " (_id ASC);";
    public static final String CREATE_INDEX_USUARIO_ASIGNACION_ID_USUARIO=
            "CREATE INDEX index_"+
                    TABLE_USUARIO_ASIGNACION+
                    "idUsuario ON "+
                    TABLE_USUARIO_ASIGNACION+
                    " (idUsuario ASC);";
    public static final String CREATE_INDEX_USUARIO_ASIGNACION_ID_SECCION=
            "CREATE INDEX index_"+
                    TABLE_USUARIO_ASIGNACION+
                    "idSeccion ON "+
                    TABLE_USUARIO_ASIGNACION+
                    " (idSeccion ASC);";
    public static final String CREATE_INDEX_USUARIO_ASIGNACION_ID_CASILLA=
            "CREATE INDEX index_"+
                    TABLE_USUARIO_ASIGNACION+
                    "idCasilla ON "+
                    TABLE_USUARIO_ASIGNACION+
                    " (idCasilla ASC);";
    public static final String CREATE_TABLE_USUARIO_ASIGNACION =
            CREATE + TABLE_USUARIO_ASIGNACION +
                    "(" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_ID_USUARIO + " INTEGER NOT NULL "+CREATE_CONSTRAINT_ID_USUARIO+"," +
                    COLUMN_ID_CASILLA + " INTEGER NOT NULL "+CREATE_CONSTRAINT_ID_CASILLA+"," +
                    COLUMN_ID_SECCION + " INTEGER NOT NULL "+CREATE_CONSTRAINT_ID_SECCION+"," +
                    COLUMN_VERSION + " INTEGER NOT NULL," +
                    COLUMN_OPERACION + " INTEGER NOT NULL)" ;

    /*TABLA HISTORIAL*/
    public static final String TABLE_HISTORIAL="historial";
    public static final String COLUMN_URL_IMAGE="imgUrl";
    public static final String COLUMN_TITLE="imgTitle";
    public static final String COLUMN_DESCRIPTION="imgDescription";
    public static final String COLUMN_DINFO="imgDInfo";
    public static final String COLUMN_COUT="imgCout";
    public static final String COLUMN_DELETED="imgDelete";
    public static final String COLUMN_SYNC="sync";
    public static final String COLUMN_TIME="fechaCaptura";
    public static final String COLUMN_PKEY="pkey";
    public static final String CREATE_INDEX_HISTORIAL_ID=
            "CREATE INDEX index_"+
                    TABLE_HISTORIAL+
                    "_id ON "+
                    TABLE_HISTORIAL+
                    " (_id ASC);";
    public static final String CREATE_INDEX_HISTORIAL_ID_USUARIO=
            "CREATE INDEX index_"+
                    TABLE_HISTORIAL+
                    "idUsuario ON "+
                    TABLE_HISTORIAL+
                    " (idUsuario ASC);";
    public static final String CREATE_INDEX_HISTORIAL_ID_SECCION=
            "CREATE INDEX index_"+
                    TABLE_HISTORIAL+
                    "idSeccion ON "+
                    TABLE_HISTORIAL+
                    " (idSeccion ASC);";
    public static final String CREATE_INDEX_HISTORIAL_ID_CASILLA=
            "CREATE INDEX index_"+
                    TABLE_HISTORIAL+
                    "idCasilla ON "+
                    TABLE_HISTORIAL+
                    " (idCasilla ASC);";
    public static final String CREATE_TABLE_HISTORIAL =
            CREATE + TABLE_HISTORIAL +
                    "(" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ID_USUARIO + " INTEGER NOT NULL "+CREATE_CONSTRAINT_ID_USUARIO+"," +
                    COLUMN_ID_CASILLA + " TEXT NOT NULL "+CREATE_CONSTRAINT_ID_CASILLA+"," +
                    COLUMN_ID_SECCION + " INTEGER NOT NULL "+CREATE_CONSTRAINT_ID_SECCION+"," +
                    COLUMN_URL_IMAGE + " TEXT NOT NULL," +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_COUT + " INTEGER NOT NULL," +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                    COLUMN_DINFO + " TEXT NOT NULL," +
                    COLUMN_TIME + " DATETIME NOT NULL," +
                    COLUMN_DELETED + " INTEGER NOT NULL," +
                    COLUMN_SYNC + " INTEGER NOT NULL," +
                    COLUMN_VERSION + " INTEGER NOT NULL)" ;
    /*TABLA CONFIGURACION*/
    public static final String TABLE_CONFIG="configuracion";
    public static final String COLUMN_RESOLUCION_DP="resolucionDp";
    public static final String COLUMN_RADIO_MAX="radMax";
    public static final String COLUMN_RADIO_MIN="radMin";
    public static final String COLUMN_DIST_MIN="distMin";
    public static final String COLUMN_THRES_EDGE="thresEdge";
    public static final String COLUMN_THRES_CENTER="thresCenter";
    public static final String COLUMN_SEG_PLANO="segPlano";
    public static final String COLUMN_IMP_LOG="impLog";
    public static final String COLUMN_REC_IMG="recImg";
    public static final String CREATE_TABLE_CONFIG=
            CREATE + TABLE_CONFIG +
            "(" +
            _ID + " INTEGER PRIMARY KEY," +
             COLUMN_RESOLUCION_DP + " DOUBLE NOT NULL," +
             COLUMN_RADIO_MAX + " INTEGER NOT NULL," +
             COLUMN_RADIO_MIN + " INTEGER NOT NULL," +
             COLUMN_DIST_MIN + " DOUBLE NOT NULL," +
             COLUMN_THRES_EDGE + " DOUBLE NOT NULL," +
             COLUMN_THRES_CENTER + " DOUBLE NOT NULL," +
             COLUMN_SEG_PLANO + " BOOLEAN NOT NULL," +
             COLUMN_IMP_LOG + " BOOLEAN NOT NULL," +
             COLUMN_REC_IMG + " BOOLEAN NOT NULL)" ;
    public static final String CREATE_INDEX_CONFIG_ID=
            "CREATE INDEX index_"+
                    TABLE_CONFIG+
                    "_id ON "+
                    TABLE_CONFIG+
                    " (_id ASC);";
    /*TABLA DATOS*/
    public static final String TABLE_DATOS="datos";
    public static final String COLUMN_SECCION_CONTAR="seccionContar";
    public static final String COLUMN_CASILLA_CONTAR="casillaContar";
    public static final String COLUMN_USUARIO_SECCION_CONTAR="usuarioSeccionContar";
    public static final String COLUMN_USUARIO_SECCION_PAQUETES="usuarioSeccionPaquetes";
    public static final String COLUMN_USUARIO_SECCION_ULTIMO_ID="usuarioSeccionUltimoId";
    public static final String CREATE_CONSTRAINT_ID= "REFERENCES "+ TABLE_USUARIOS +" (_id)";
    public static final String CREATE_TABLE_DATOS=
            CREATE + TABLE_DATOS +
                    "(" +
                    _ID + " INTEGER PRIMARY KEY "+CREATE_CONSTRAINT_ID+"," +
                    COLUMN_SECCION_CONTAR + " INTEGER NOT NULL," +
                    COLUMN_CASILLA_CONTAR + " INTEGER NOT NULL," +
                    COLUMN_USUARIO_SECCION_CONTAR + " INTEGER NOT NULL," +
                    COLUMN_USUARIO_SECCION_PAQUETES + " INTEGER NOT NULL," +
                    COLUMN_USUARIO_SECCION_ULTIMO_ID + " INTEGER NOT NULL)" ;
    public static final String CREATE_INDEX_DATOS_ID=
            "CREATE INDEX index_"+
                    TABLE_DATOS+
                    "_id ON "+
                    TABLE_DATOS+
                    " (_id ASC);";
}
