package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sourcey.materiallogindemo.dao.encomenda.EncomendaAdapter;
import com.sourcey.materiallogindemo.dao.encomenda.EncomendaDatabaseHelper;
import com.sourcey.materiallogindemo.dao.encomenda.EncomendaNetworkStateChecker;
import com.sourcey.materiallogindemo.model.Cliente;
import com.sourcey.materiallogindemo.model.Encomenda;
import com.sourcey.materiallogindemo.model.Produto;
import com.sourcey.materiallogindemo.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*
     * this is the url to our webservice
     * make sure you are using the ip instead of localhost
     * it will not work if you are using localhost
     * */
    public static final String URL_SAVE_NAME = "http://192.168.1.3/psiencomendaSync/apk_sync_server.php";

    //1 means data is synced and 0 means data is not synced
    public static final boolean SYNCED_WITH_SERVER = true;
    public static final boolean NOT_SYNCED_WITH_SERVER = false;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    private Spinner sProduto;
    private Spinner sUnidadeMedida;
    private EditText etEstabelecimento;
    private AutoCompleteTextView acCliente;
    private TextView tvProduto;
    private TextView tvUnidadeMedida;
    private EditText etQuantidade;
    private EditText etCliente;
    private Button btn_addProduto, btn_saveEncomenda;
    private String produto, unidadeMEdida;
    private EditText etDataEncomenda, etDataEntrega;

    private List<String> produtos = new ArrayList<String>();
    private List<String> unidadesMEdida = Arrays.asList("Pack", "Dispensório", "Litro");
    private ArrayList<Encomenda> encomendas;

    private Produto produtoSelecionado;
    private Cliente clienteSelecionado;
    private EncomendaDatabaseHelper db;

    private RecyclerView recyclerView;
    private ImageView removeProduct;

    private ArrayAdapter<Cliente> clienteAdapter;

    private ArrayAdapter<Produto> produtoArrayAdapter;

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(new EncomendaNetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                loadEncomendas();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        encomendas = new ArrayList<>();
        produtoSelecionado = new Produto();
        clienteSelecionado = new Cliente();
        db = new EncomendaDatabaseHelper(this);

        produtos.add("Selecione o produto...");
        produtos.add("J0.1");
        produtos.add("J1");
        produtos.add("J2");
        produtos.add("J3");

        sProduto = (Spinner) findViewById(R.id.sProduto);
        sUnidadeMedida = (Spinner) findViewById(R.id.sUnidadeMedida);
        etEstabelecimento = (EditText) findViewById(R.id.etEstabelecimento);
        btn_saveEncomenda = (Button) findViewById(R.id.btn_saveEncomenda);
        etCliente = (EditText) findViewById(R.id.etCliente);
        tvProduto = (TextView) findViewById(R.id.tvProduto);
        tvUnidadeMedida = (TextView) findViewById(R.id.tvUnidadeMedida);
        etQuantidade = (EditText) findViewById(R.id.etQuantidade);
        etDataEncomenda = (EditText) findViewById(R.id.etDataEncomenda);
        etDataEntrega = (EditText) findViewById(R.id.etDataEntrega);
        btn_addProduto = (Button) findViewById(R.id.btn_addProduto);
        recyclerView = (RecyclerView) findViewById(R.id.listViewEncomenda);
        removeProduct = (ImageView) findViewById(R.id.removeProduct);

        etDataEncomenda.setText(formatDate(new Date()));
        etDataEntrega.setText(formatDate(new Date()));

        //fillSpinner(sProduto, produtos);
        fillSpinner(sProduto, getProdutos());
        fillSpinnerUM(sUnidadeMedida, unidadesMEdida);

        loadEncomendas();

        //adding click listener to button
        btn_addProduto.setOnClickListener(this);
        etCliente.setOnClickListener(this);
        btn_saveEncomenda.setOnClickListener(this);

        //onSelect do spiner
        sProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produtoSelecionado = (Produto) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        etDataEncomenda.setOnClickListener(new View.OnClickListener() {
            final Calendar myCalendar = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateLabel(myCalendar, etDataEncomenda);
                }
            };

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etDataEntrega.setOnClickListener(new View.OnClickListener() {
            final Calendar myCalendar = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateLabel(myCalendar, etDataEntrega);
                }
            };

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Auto complete
        clienteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getClientes());

    }


    private void updateLabel(Calendar myCalendar, EditText edittext) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    private void fillSpinner(Spinner spinner, ArrayList<Produto> list) {
        //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
        produtoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);

        ArrayAdapter<Produto> spinnerArrayAdapter = produtoArrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void fillSpinnerUM(Spinner spinner, List<String> list) {
        //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);

        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateList(ArrayList<Encomenda> encomendas) {
        EncomendaAdapter adapter = new EncomendaAdapter(encomendas, this);
        RecyclerView myView = (RecyclerView) findViewById(R.id.listViewEncomenda);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);
    }

    /*
     * this method will
     * load the names from the database
     * with updated sync status
     * */
    private void loadEncomendas() {
        encomendas.clear();
        Cursor cursor = db.getEncomendas();
        try {
            if (cursor.moveToFirst()) {
                do {
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
                    encomendas.add(encomenda);
                } while (cursor.moveToNext());
                updateList(encomendas);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro na leitura dos dados\n" + e, Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Produto> getProdutos() {
        ArrayList<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto(1, "J0.1", "kk-dfl2", "psiJ01-mz", "trop-jeit01", 1));
        produtos.add(new Produto(2, "J1", "kk-dfl2", "psiJ01-mz", "trop-jeit01", 1));
        produtos.add(new Produto(3, "J2", "kk-dfl2", "psiJ01-mz", "trop-jeit01", 1));
        produtos.add(new Produto(4, "J3", "kk-dfl2", "psiJ01-mz", "trop-jeit01", 1));
        produtos.add(new Produto(5, "J24", "kk-dfl2", "psiJ01-mz", "trop-jeit01", 1));
        return produtos;
    }

    private ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("Edson Chivambo", "1111252", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente("Raul Gomes", "888888", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente("Zubaida Mussumbuluco", "999999", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente("Matias Darika", "333333", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente("Edson Zandamela", "12222252", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente("Gomes Come", "1122252", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        return clientes;
    }

    private String formatDate(Date date) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        return sdf.format(date.getTime());
    }

    private void saveToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gravando...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveToLocalStorage(SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveToLocalStorage(NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveToLocalStorage(NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //saving the name to local storage
    private void saveToLocalStorage(boolean status) {
        try {
            if (!clienteSelecionado.getNome().isEmpty()) {
                Encomenda encomenda = new Encomenda();
                encomenda.setClienteId(clienteSelecionado.getId());
                encomenda.setNomeCliente(clienteSelecionado.getNome());
                encomenda.setComentario("este é o comentaario");
                encomenda.setDataEncomenda(new Date(etDataEncomenda.getText().toString().trim()));
                encomenda.setDataEntrega(new Date(etDataEntrega.getText().toString().trim()));
                encomenda.setEstabelecimento(etEstabelecimento.getText().toString().trim());
                encomenda.setProdutoId(produtoArrayAdapter.getItem(sProduto.getSelectedItemPosition()).getId());
                encomenda.setDescricaoProduto(produtoArrayAdapter.getItem(sProduto.getSelectedItemPosition()).getDescricao());
                encomenda.setUnidadeMedida(sUnidadeMedida.getSelectedItem().toString());
                encomenda.setQuantidade(Double.parseDouble(etQuantidade.getText().toString()));
                encomenda.setUser_id(1);
                encomenda.setStatus(status);

                //db.addEncomenda(encomenda);

                this.encomendas.add(db.addEncomenda(encomenda));
                updateList(this.encomendas);

            } else {
                Toast.makeText(this, "Erro! Cliente não encontrado na BD", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro:\n" + e, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addProduto: {
                saveToServer();
            }break;
            case R.id.etCliente:{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.selecionar_cliente, null);
                final AutoCompleteTextView acCliente = (AutoCompleteTextView) mView.findViewById(R.id.acCliente);

                acCliente.setAdapter(clienteAdapter);
                acCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        clienteSelecionado = (Cliente) parent.getItemAtPosition(position);
                    }
                });

                builder.setView(mView)
                    .setTitle("Procurar cliente na base de dados *")
                        // Add action buttons
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        etCliente.setText(clienteSelecionado.getNome());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }break;
            case R.id.btn_saveEncomenda:{
               clearFilds();
            }break;
        }

    }

    private void clearFilds(){
        etDataEncomenda.setText(new Date().toString());
        etDataEntrega.setText(new Date().toString());
        etQuantidade.setText(null);
        etCliente.setText(null);
        etEstabelecimento.setText(null);
        fillSpinner(sProduto, getProdutos());
        fillSpinnerUM(sUnidadeMedida, unidadesMEdida);
        clienteSelecionado = null;
        produtoSelecionado = null;
        this.encomendas.clear();
        updateList(this.encomendas);

        Toast.makeText(this, "A sua encomenda foi gravada com sucesso!", Toast.LENGTH_LONG).show();
    }

}
