package com.sourcey.materiallogindemo.dao.Cliente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcey.materiallogindemo.config.Config;
import com.sourcey.materiallogindemo.model.Cliente;
import com.sourcey.materiallogindemo.model.Encomenda;
import com.sourcey.materiallogindemo.model.Provincia;

public class ClienteDatabaseHelper extends SQLiteOpenHelper{

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = Config.DB_NAME;
    public static final String TABLE_NAME = "clientes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_NUIT = "nuit";
    public static final String COLUMN_PROVINCIA_ID = "provincia_id";
    public static final String COLUMN_DISTRITO_ID= "distrito_id";
    public static final String COLUMN_TIPO_CLIENTE = "tipo_cliente";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_CONTACTO = "contacto";
    public static final String COLUMN_ENDERECO = "endereco";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_USER_ID = "user_id";

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public ClienteDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NOME + " VARCHAR, " +
                COLUMN_NUIT + " VARCHAR, " +
                COLUMN_PROVINCIA_ID + " INTEGER, " +
                COLUMN_DISTRITO_ID + " INTEGER, " +
                COLUMN_TIPO_CLIENTE + " VARCHAR, " +
                COLUMN_EMAIL + " VARCHAR, " +
                COLUMN_CONTACTO + " VARCHAR, " +
                COLUMN_ENDERECO + " VARCHAR, " +
                COLUMN_STATUS + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER);";
        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }

    /*
     * This method is taking two arguments
     * first one is the name that is to be saved
     * second one is the status
     * 0 means the name is synced with the server
     * 1 means the name is not synced with the server
     * */
    public Cliente add(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOME, cliente.getNome());
        contentValues.put(COLUMN_NUIT, cliente.getNuit());
        contentValues.put(COLUMN_PROVINCIA_ID, cliente.getProvincia_id());
        contentValues.put(COLUMN_DISTRITO_ID, cliente.getDistrito_id());
        contentValues.put(COLUMN_TIPO_CLIENTE, cliente.getTipo_cliente());
        contentValues.put(COLUMN_EMAIL, cliente.getEmail());
        contentValues.put(COLUMN_CONTACTO, cliente.getContacto());
        contentValues.put(COLUMN_ENDERECO, cliente.getEndereco());
        contentValues.put(COLUMN_STATUS, cliente.getStatus());
        contentValues.put(COLUMN_USER_ID, cliente.getUser_id());


        //db.insert(TABLE_NAME, null, contentValues);
        cliente.setId(Integer.parseInt(db.insert(TABLE_NAME, null, contentValues)+""));
        db.close();

        return cliente;
    }

    /*
     * this method will give us all the name stored in sqlite
     * */
    public Cursor get() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    public boolean updateStatus(int id, int status) throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, id + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOME, cliente.getNome());
        contentValues.put(COLUMN_NUIT, cliente.getNuit());
        contentValues.put(COLUMN_PROVINCIA_ID, cliente.getProvincia_id());
        contentValues.put(COLUMN_DISTRITO_ID, cliente.getDistrito_id());
        contentValues.put(COLUMN_TIPO_CLIENTE, cliente.getTipo_cliente());
        contentValues.put(COLUMN_EMAIL, cliente.getEmail());
        contentValues.put(COLUMN_CONTACTO, cliente.getContacto());
        contentValues.put(COLUMN_ENDERECO, cliente.getEndereco());
        contentValues.put(COLUMN_STATUS, cliente.getStatus());
        contentValues.put(COLUMN_USER_ID, cliente.getUser_id());


        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + cliente.getId(), null);
        db.close();
        return true;
    }

}
