package com.sourcey.materiallogindemo.dao.encomenda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.model.Encomenda;
import com.sourcey.materiallogindemo.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EncomendaNetworkStateChecker extends BroadcastReceiver{
    private Context context;
    private EncomendaDatabaseHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;

        db = new EncomendaDatabaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedEncomendas();
                Encomenda encomenda = new Encomenda();
                encomenda.setId(cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_ID)));
                encomenda.setDataEncomenda(new Date(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_DATAENCOMENDA))));
                encomenda.setClienteId(cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_CLIENTEID)));
                encomenda.setNomeCliente(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_NOMECLIENTE)));
                encomenda.setComentario(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_COMENTARIO)));
                encomenda.setDataEntrega(new Date(cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_DATAENTREGA))));
                encomenda.setEstabelecimento(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_ESTABELECIMENTO)));
                encomenda.setProdutoId(cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_PRODUTOID)));
                encomenda.setDescricaoProduto(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_DESCRICAOPRODUTO)));
                encomenda.setUnidadeMedida(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_UNIDADEMEDIDA)));
                encomenda.setQuantidade(cursor.getDouble(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_QUANTIDADE)));

                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveEncomenda(
                                cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_ID)), encomenda);
                    } while (cursor.moveToNext());
                }
            }
        }
    }


    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    private void saveEncomenda(final int id, final Encomenda encomenda) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateStatus(id, MainActivity.SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(MainActivity.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", encomenda.getId()+"");
                params.put("dataEncomenda", encomenda.getDataEncomenda().toString());
                params.put("dataEntrega", encomenda.getDataEntrega().toString());
                params.put("clienteId", encomenda.getClienteId()+"");
                params.put("nomeCliente", encomenda.getNomeCliente());
                params.put("estabelecimentoId", encomenda.getEstabelecimento()+"");
                params.put("quantidade", encomenda.getQuantidade()+"");
                params.put("entregue", encomenda.isEntregue()+"");
                params.put("comentario", encomenda.getComentario());
                params.put("produtoId", encomenda.getProdutoId()+"");
                params.put("descricaoProduto", encomenda.getDescricaoProduto());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
