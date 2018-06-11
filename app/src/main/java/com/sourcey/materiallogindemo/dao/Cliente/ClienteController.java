package com.sourcey.materiallogindemo.dao.Cliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.config.Config;
import com.sourcey.materiallogindemo.model.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClienteController {
    private static final  String GET_DATA_URL = "http://192.168.159.1/encomendas/public/api/clientes";
    private static final  String UPDATE_DATA_URL = "http://192.168.159.1/encomendas/public/api/clientes";
    //Broadcast receiver to know the sync status
    private RequestQueue requestQueue;

    private ArrayList<Cliente> list;
    private ClienteDatabaseHelper db;
    private Context context;


    public ClienteController(Context context) {
        this.context = context;
        db = new ClienteDatabaseHelper(context);
        requestQueue = Volley.newRequestQueue(context);
        list = getLocal();
    }

    public ArrayList<Cliente> allClientes(){
        try {
            saveToLocalDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.list;
    }


    /*
     * this method will
     * load the names from the database
     * with updated sync status
     * */
    private ArrayList<Cliente> getLocal() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        Cursor cursor = db.get();
        try {
            if (cursor.moveToFirst()) {
                do {

                     int id = cursor.getInt(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_ID));
                     String nome = cursor.getString(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_NOME));
                     String nuit = cursor.getString(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_NUIT));
                     int provincia_id = cursor.getInt(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_PROVINCIA_ID));
                     int distrito_id = cursor.getInt(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_DISTRITO_ID));
                     String tipo_cliente = cursor.getString(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_TIPO_CLIENTE));
                     String email = cursor.getString(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_EMAIL));
                     String contacto = cursor.getString(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_CONTACTO));
                     String endereco = cursor.getString(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_ENDERECO));
                    int status = cursor.getInt(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_STATUS));
                    int user_id = cursor.getInt(cursor.getColumnIndex(ClienteDatabaseHelper.COLUMN_USER_ID));

                    Cliente cliente = new Cliente(id, nome, nuit, provincia_id, distrito_id, tipo_cliente, email, contacto, endereco, status, user_id);

                    clientes.add(cliente);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(context, "Erro na leitura dos dados\n" + e, Toast.LENGTH_LONG).show();
        }
        return clientes;
    }

    private ArrayList<Cliente> getServerData(){
        final ArrayList<Cliente> clientes = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                GET_DATA_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("clientes");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String nome = jsonObject.getString("nome");
                        String nuit = jsonObject.getString("nuit");
                        int provincia_id = jsonObject.getInt("provincia_id");
                        int distrito_id = jsonObject.getInt("distrito_id");
                        String tipo_cliente = jsonObject.getString("tipo_cliente");
                        String email = jsonObject.getString("email");
                        String contacto = jsonObject.getString("contacto");
                        String endereco = jsonObject.getString("endereco");
                        int status = jsonObject.getInt("status");
                        int user_id = jsonObject.getInt("user_id");

                        Cliente cliente = new Cliente(id, nome, nuit, provincia_id, distrito_id, tipo_cliente, email, contacto, endereco, status, user_id);

                        clientes.add(cliente);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        return clientes;
    }

    private void saveToLocalDB() throws Exception {
        for(Cliente item: getServerData()){
            if (item.getStatus()==1) {
                if (db.add(item)!=null)
                    updateServerStatus(item.getId(), Config.SYNCED_WITH_SERVER);
                    db.updateStatus(item.getId(), Config.SYNCED_WITH_SERVER);
            }else if (item.getStatus()==3) {
                db.updateCliente(item);
            }
        }
    }

    private void updateServerStatus(int cliente_id, final int cliente_status){
        final int id = cliente_id;
        final int status = cliente_status;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, UPDATE_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao actualizar o status do cliente\n" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", id + "");
                parameters.put("status", status + "");
                return parameters;
            }
        };
    }
}
