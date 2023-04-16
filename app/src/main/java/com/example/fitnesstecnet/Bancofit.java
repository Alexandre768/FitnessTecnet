package com.example.fitnesstecnet;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class Bancofit extends SQLiteOpenHelper {
    private static final String DB_NAME = "Bancofit.db";
    private static final int DB_VERSION = 1;
    private static Bancofit INSTANCE;
    static Bancofit getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new Bancofit(context);
        return INSTANCE;
    }
    private Bancofit(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE calc (id INTEGER primary key, tipo TEXT, resultado DECIMAL, dtcriacao DATETIME)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Teste", "on Upgrade disparado");
    }
    @SuppressLint("Range")
List<Registro>getRegidtersBy(String tipo){
        List<Registro> registros=new ArrayList<>();
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM calc WHERE tipo=?",new String[]{tipo});
        try {
            if (cursor.moveToFirst()) {
                do {
                    Registro registro = new Registro();
                    registro.tipo = cursor.getString(cursor.getColumnIndex("tipo"));
                    registro.resultado = cursor.getDouble(cursor.getColumnIndex("resultado"));
                    registro.dtcriacao = cursor.getString(cursor.getColumnIndex("dtcriacao"));
                    registros.add(registro);

                } while (cursor.moveToNext());

            }
        }catch (Exception e){
            Log.e("Erro SQLite", e.getMessage(),e);
        }finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return  registros;
}
    Long addItem(String type, double response) {
        SQLiteDatabase db = getWritableDatabase();
        long calcId = 0;
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("tipo", type);
            values.put("resultado", response);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            String now = sdf.format(new Date());
            values.put("dtcriacao", now);
            calcId = db.insertOrThrow("calc", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if (db.isOpen())
                db.endTransaction();
        }
        return calcId;
    }
}

