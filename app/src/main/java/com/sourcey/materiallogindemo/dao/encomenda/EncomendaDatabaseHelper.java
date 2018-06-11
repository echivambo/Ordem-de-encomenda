package com.sourcey.materiallogindemo.dao.encomenda;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.config.Config;
import com.sourcey.materiallogindemo.model.Encomenda;

public class EncomendaDatabaseHelper extends SQLiteOpenHelper{

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = Config.DB_NAME;
    public static final String TABLE_NAME = "encomendas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NUMERO_TRANSACAO = "numero_transacao";
    public static final String COLUMN_NUMERO_ITEM_TRANSACAO = "nuemro_item_transacao";
    public static final String COLUMN_DATAENCOMENDA= "data_encomenda";
    public static final String COLUMN_DATAENTREGA = "data_entrega";
    public static final String COLUMN_CLIENTEID = "cliente_id";
    public static final String COLUMN_NOMECLIENTE = "nomeCliente";
    public static final String COLUMN_ESTABELECIMENTO = "estabelecimento";
    public static final String COLUMN_QUANTIDADE = "quantidade";
    public static final String COLUMN_ENTREGUE = "entregue";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COMENTARIO = "comentario";
    public static final String COLUMN_UNIDADEMEDIDA = "unidade_medida";
    public static final String COLUMN_PRODUTOID = "produto_id";
    public static final String COLUMN_DESCRICAOPRODUTO = "descricao_produto";
    public static final String COLUMN_USERID = "user_id";

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public EncomendaDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NUMERO_TRANSACAO + " VARCHAR, " +
                COLUMN_NUMERO_ITEM_TRANSACAO + " VARCHAR, " +
                COLUMN_DATAENCOMENDA + " VARCHAR, " +
                COLUMN_DATAENTREGA + " VARCHAR, " +
                COLUMN_CLIENTEID + " INTEGER, " +
                COLUMN_NOMECLIENTE + " VARCHAR, " +
                COLUMN_ESTABELECIMENTO + " VARCHAR, " +
                COLUMN_QUANTIDADE + " DOUBLE, " +
                COLUMN_ENTREGUE + " TINYINT, " +
                COLUMN_COMENTARIO + " VARCHAR, " +
                COLUMN_UNIDADEMEDIDA + " VARCHAR, " +
                COLUMN_PRODUTOID + " INTEGER, " +
                COLUMN_DESCRICAOPRODUTO + " VARCHAR, " +
                COLUMN_USERID + " INTEGER, " +
                COLUMN_STATUS + " TINYINT);";
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
    public Encomenda addEncomenda(Encomenda encomenda) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NUMERO_TRANSACAO, encomenda.getNumero_transacao());
        contentValues.put(COLUMN_NUMERO_ITEM_TRANSACAO, encomenda.getNuemro_item_transacao());
        contentValues.put(COLUMN_DATAENCOMENDA, encomenda.getData_encomenda());
        contentValues.put(COLUMN_DATAENTREGA, encomenda.getData_entrega());
        contentValues.put(COLUMN_CLIENTEID, encomenda.getCliente_id());
        contentValues.put(COLUMN_NOMECLIENTE, encomenda.getNome_cliente());
        contentValues.put(COLUMN_ESTABELECIMENTO, encomenda.getEstabelecimento());
        contentValues.put(COLUMN_QUANTIDADE, encomenda.getQuantidade());
        contentValues.put(COLUMN_ENTREGUE, encomenda.isEntregue());
        contentValues.put(COLUMN_COMENTARIO, encomenda.getComentario());
        contentValues.put(COLUMN_UNIDADEMEDIDA, encomenda.getUnidade_medida());
        contentValues.put(COLUMN_PRODUTOID, encomenda.getProduto_id());
        contentValues.put(COLUMN_DESCRICAOPRODUTO, encomenda.getDescricao_produto());
        contentValues.put(COLUMN_USERID, encomenda.getUser_id());
        contentValues.put(COLUMN_STATUS, encomenda.isStatus());


        //db.insert(TABLE_NAME, null, contentValues);
        encomenda.setId(Integer.parseInt(db.insert(TABLE_NAME, null, contentValues)+""));
        db.close();

        return encomenda;
    }

    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    public boolean updateStatus(String nuemro_item_transacao, boolean status) throws Exception{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_STATUS, status);
            db.update(TABLE_NAME, contentValues, COLUMN_NUMERO_ITEM_TRANSACAO + "=" + nuemro_item_transacao, null);
            db.close();
            return true;
    }

/*
    public boolean updateEncomenda(int id, Encomenda encomenda) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_DATAENCOMENDA, encomenda.getDataEncomenda());
        contentValues.put(COLUMN_DATAENTREGA, encomenda.getDataEntrega());
        contentValues.put(COLUMN_CLIENTEID, encomenda.getClienteId());
        contentValues.put(COLUMN_NOMECLIENTE, encomenda.getNomeCliente());
        contentValues.put(COLUMN_ESTABELECIMENTO, encomenda.getEstabelecimento());
        contentValues.put(COLUMN_QUANTIDADE, encomenda.getQuantidade());
        contentValues.put(COLUMN_ENTREGUE, encomenda.isEntregue());
        contentValues.put(COLUMN_COMENTARIO, encomenda.getComentario());
        contentValues.put(COLUMN_UNIDADEMEDIDA, encomenda.getUnidadeMedida());
        contentValues.put(COLUMN_PRODUTOID, encomenda.getProdutoId());
        contentValues.put(COLUMN_DESCRICAOPRODUTO, encomenda.getDescricaoProduto());
        contentValues.put(COLUMN_USERID, encomenda.getUser_id());
        contentValues.put(COLUMN_STATUS, encomenda.isStatus());


        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }
*/
    /*
     * this method will give us all the name stored in sqlite
     * */
    public Cursor getEncomendas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    public Cursor getUnsyncedEncomendas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean deleteEncomendas(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_NAME,COLUMN_ID + "=" + id, null) > 0;
    }
}
