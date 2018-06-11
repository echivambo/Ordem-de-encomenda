package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.dao.Cliente.ClienteController;
import com.sourcey.materiallogindemo.dao.encomenda.EncomendaAdapter;
import com.sourcey.materiallogindemo.dao.encomenda.EncomendaDatabaseHelper;
import com.sourcey.materiallogindemo.model.Cliente;
import com.sourcey.materiallogindemo.model.Encomenda;
import com.sourcey.materiallogindemo.model.Produto;
import com.sourcey.materiallogindemo.util.Util;

import org.json.JSONArray;
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
    private RequestQueue requestQueue;

    private static final  String INSERT_DATA_URL = "http://192.168.159.1/encomendas/public/api/encomendas";
    private static final  String GET_DATA_URL = "http://192.168.159.1/encomendas/public/api/encomendas";

    private ClienteController clienteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        clienteController = new ClienteController(this);

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

        etDataEncomenda.setText(Util.formatDate(new Date()));
        etDataEntrega.setText(Util.formatDate(new Date()));

        //fillSpinner(sProduto, produtos);
        fillSpinner(sProduto, getProdutos());
        fillSpinnerUM(sUnidadeMedida, unidadesMEdida);

        //loadEncomendas();
        getServerData();

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
        //clienteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, clienteController.allClientes());

    }


    private void getServerData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                GET_DATA_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayEncomendas = response.getJSONArray("encomendas");

                    for(int i=0; i<jsonArrayEncomendas.length(); i++){
                        JSONObject jsonObjectEstudante = jsonArrayEncomendas.getJSONObject(i);

                        String comeCliente = jsonObjectEstudante.getString("nome_cliente");
                        Encomenda encomenda = new Encomenda();
                        encomenda.setId(jsonObjectEstudante.getInt("id"));
                        encomenda.setNumero_transacao(jsonObjectEstudante.getString("numero_transacao"));
                        encomenda.setNuemro_item_transacao(jsonObjectEstudante.getString("nuemro_item_transacao"));
                        encomenda.setData_encomenda(jsonObjectEstudante.getString("data_encomenda"));
                        encomenda.setCliente_id(jsonObjectEstudante.getInt("cliente_id"));
                        encomenda.setNome_cliente(jsonObjectEstudante.getString("nome_cliente"));
                        encomenda.setComentario(jsonObjectEstudante.getString("comentario"));
                        encomenda.setData_entrega(jsonObjectEstudante.getString("data_entrega"));
                        encomenda.setEstabelecimento(jsonObjectEstudante.getString("estabelecimento"));
                        encomenda.setProduto_id(jsonObjectEstudante.getInt("produto_id"));
                        encomenda.setDescricao_produto(jsonObjectEstudante.getString("descricao_produto"));
                        encomenda.setUnidade_medida(jsonObjectEstudante.getString("unidade_medida"));
                        encomenda.setQuantidade(jsonObjectEstudante.getDouble("quantidade"));
                        encomendas.add(encomenda);
                        updateList(encomendas);
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
                    encomenda.setNumero_transacao(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_NUMERO_TRANSACAO)));
                    encomenda.setNuemro_item_transacao(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_NUMERO_ITEM_TRANSACAO)));
                    encomenda.setData_encomenda(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_DATAENCOMENDA)));
                    encomenda.setCliente_id(cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_CLIENTEID)));
                    encomenda.setNome_cliente(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_NOMECLIENTE)));
                    encomenda.setComentario(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_COMENTARIO)));
                    encomenda.setData_entrega(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_DATAENTREGA)));
                    encomenda.setEstabelecimento(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_ESTABELECIMENTO)));
                    encomenda.setProduto_id(cursor.getInt(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_PRODUTOID)));
                    encomenda.setDescricao_produto(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_DESCRICAOPRODUTO)));
                    encomenda.setUnidade_medida(cursor.getString(cursor.getColumnIndex(EncomendaDatabaseHelper.COLUMN_UNIDADEMEDIDA)));
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
/*
    private ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1,"Edson Chivambo", "1111252", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente(2,"Raul Gomes", "888888", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente(3,"Zubaida Mussumbuluco", "999999", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente(4,"Matias Darika", "333333", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente(5,"Edson Zandamela", "12222252", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        clientes.add(new Cliente(6,"Gomes Come", "1122252", 1, 1, "Retalhista", "echivambo@psi.org.mz", "T3. Q24/216", 1));
        return clientes;
    }
*/

    private void saveToServer(Encomenda enc) {
        final Encomenda encomenda = enc;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gravando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    EncomendaDatabaseHelper encomendaDatabaseHelper = new EncomendaDatabaseHelper(getApplicationContext());

                    encomendaDatabaseHelper.updateStatus(obj.getString("nuemro_item_transacao"), true);

                    Util.showMessage(getApplicationContext(),"Encomenda registada com sucesso!", "Número de transação: "+obj.getString("numero_transacao"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.showMessage(getApplicationContext(),"Erro ao gravar!", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.showMessage(getApplicationContext(),"Erro ao actualizar status!", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Util.showMessage(getApplicationContext(),"Erro ao gravar!", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("numero_transacao", encomenda.getNumero_transacao());
                parameters.put("nuemro_item_transacao", encomenda.getNuemro_item_transacao());
                parameters.put("data_encomenda", encomenda.getData_encomenda());
                parameters.put("data_entrega", encomenda.getData_entrega());
                parameters.put("cliente_id", encomenda.getCliente_id()+"");
                parameters.put("estabelecimento", encomenda.getEstabelecimento());
                parameters.put("quantidade", encomenda.getQuantidade()+"");
                parameters.put("comentario", encomenda.getComentario());
                parameters.put("produto_id", encomenda.getProduto_id()+"");
                parameters.put("nome_cliente", encomenda.getNome_cliente());
                parameters.put("descricao_produto", encomenda.getDescricao_produto());
                parameters.put("user_id", encomenda.getUser_id()+"");
                parameters.put("unidade_medida", encomenda.getUnidade_medida());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    //saving the name to local storage
    private void saveToLocalStorage(boolean status) {
        try {
            if (!clienteSelecionado.getNome().isEmpty()) {
                Encomenda encomenda = createEncomenda();
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

    private Encomenda createEncomenda(){

        Encomenda encomenda = new Encomenda();
        encomenda.setCliente_id(clienteSelecionado.getId());
        encomenda.setNome_cliente(clienteSelecionado.getNome());
        encomenda.setComentario("este é o comentaario");
        encomenda.setData_encomenda(etDataEncomenda.getText().toString().trim());
        encomenda.setData_entrega(etDataEntrega.getText().toString().trim());
        encomenda.setEstabelecimento(etEstabelecimento.getText().toString().trim());
        encomenda.setProduto_id(produtoArrayAdapter.getItem(sProduto.getSelectedItemPosition()).getId());
        encomenda.setDescricao_produto(produtoArrayAdapter.getItem(sProduto.getSelectedItemPosition()).getDescricao());
        encomenda.setUnidade_medida(sUnidadeMedida.getSelectedItem().toString());
        encomenda.setQuantidade(Double.parseDouble(etQuantidade.getText().toString()));
        encomenda.setStatus(false);
        encomenda.setUser_id(1);
        String numero_transacao = gerarNumeroTransacao(encomenda.getUser_id(),encomenda.getCliente_id());
        encomenda.setNumero_transacao(numero_transacao);
        encomenda.setNuemro_item_transacao(numero_transacao+encomenda.getProduto_id());
        return encomenda;
    }

    private String gerarNumeroTransacao(int user_id, int cliente_id){
        String myFormat = "ddMMyy/HHmmss.SSS"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String numeroTransacao = sdf.format(new Date());
        return numeroTransacao+"-"+user_id+"-"+cliente_id;
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
                saveToLocalStorage(false);
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
                for (Encomenda encomenda:encomendas)
                    saveToServer(encomenda);
                clearFilds();
            }break;
        }

    }

    private void clearFilds(){
        updateLabel(Calendar.getInstance(), etDataEncomenda);
        updateLabel(Calendar.getInstance(), etDataEntrega);
        etQuantidade.setText(null);
        etCliente.setText(null);
        etEstabelecimento.setText(null);
        fillSpinner(sProduto, getProdutos());
        fillSpinnerUM(sUnidadeMedida, unidadesMEdida);
        clienteSelecionado = null;
        produtoSelecionado = null;
        this.encomendas.clear();
        updateList(this.encomendas);

    }



}
