package com.stisbolivia.acloapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBAcloAppHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "acloapp.db";

    public DBAcloAppHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_tareas" + "(" +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "   id_tarea INTEGER NOT NULL, " +
                "   nombre TEXT NOT NULL," +
                "   descripcion TEXT," +
                "   fecha TEXT NOT NULL," +
                "   estado TEXT default 'ACTIVO'" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE tbl_tareas");
        onCreate(db);
    }
}
