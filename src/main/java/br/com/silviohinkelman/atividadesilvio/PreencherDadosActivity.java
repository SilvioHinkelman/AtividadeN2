package br.com.silviohinkelman.atividadesilvio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PreencherDadosActivity extends AppCompatActivity {

    private EditText etMarca;
    private EditText etModelo;
    private EditText etValor;
    private EditText etTelefone;
  //  private EditText etUsuario;         //NOVA
    private Spinner spColors;
    private Button btnSalvar;
    private String acao;
    private Cars cars;
    private CheckBox checkBox;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preencher_dados);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etValor = findViewById(R.id.etValor);
        etTelefone = findViewById(R.id.etTelefone);
        spColors = findViewById(R.id.spColors);
        btnSalvar = findViewById(R.id.btnSalvar);
        checkBox = (CheckBox)findViewById(R.id.checkbox_id);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        acao = getIntent().getStringExtra("acao");
        if(acao.equals("editar")){
           carregarFormulario();
        }

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if ( auth.getCurrentUser() == null ){
                    finish();
                }
            }
        };
        auth.addAuthStateListener( authStateListener );
    }

    private void carregarFormulario(){
        String idCar = getIntent().getStringExtra("idCar");

        cars = new Cars();
        cars.setId( idCar );

        cars.setMarca( getIntent().getStringExtra("marca") );
        cars.setModelo( getIntent().getStringExtra("modelo") );
        cars.setValor( getIntent().getStringExtra("valor") );
        cars.setTelefone( getIntent().getStringExtra("telefone") );
        cars.setCor( getIntent().getStringExtra("cor") );
        etMarca.setText(cars.getMarca());
        etModelo.setText(cars.getModelo());
        etValor.setText(cars.getValor());
        etTelefone.setText(cars.getTelefone());
        String[] colors = getResources().getStringArray(R.array.colors);

        for(int x =0 ; x < colors.length; x++){
            if(cars.getCor().equals(colors[x])){
                spColors.setSelection(x);
                break;
            }
        }
    }

    private void salvar(){
        String marca = etMarca.getText().toString();
        String modelo = etModelo.getText().toString();
        String valor = etValor.getText().toString();
        String telefone = etTelefone.getText().toString();

        if(marca.isEmpty() || modelo.isEmpty() || valor.isEmpty() || telefone.isEmpty() || spColors.getSelectedItemPosition() ==0 || !checkBox.isChecked() ){
            Toast.makeText(this, R.string.complete, Toast.LENGTH_LONG).show();
        }else{

            if(acao.equals("inserir")) {
                cars = new Cars();
            }
            cars.setMarca( marca );
            cars.setModelo( modelo );
            cars.setValor( valor );
            cars.setTelefone( telefone );
            cars.setCor(spColors.getSelectedItem().toString());
            cars.setIdUsuario( auth.getCurrentUser().getUid() ); //Pega o Id do user

            if(acao.equals("inserir")) {

                reference.child("cars").push().setValue( cars );
                etMarca.setText("");
                etModelo.setText("");
                etValor.setText("");
                etTelefone.setText("");
                spColors.setSelection(0 , true);
                checkBox.setChecked(false);

            }else {
                String idCars = cars.getId();
                cars.setId( null );
                reference.child("cars").child( idCars ).setValue(cars);
                finish();
            }
        }
    }
}