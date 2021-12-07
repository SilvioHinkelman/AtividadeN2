package br.com.silviohinkelman.atividadesilvio;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvCars;
    private List<Cars> listaCars;
    private ArrayAdapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;
    private Query query;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PreencherDadosActivity.class);
                intent.putExtra("acao", "inserir");
                startActivity(intent);
            }
        });

        lvCars = findViewById(R.id.lvProdutos);
        listaCars = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listaCars);
        lvCars.setAdapter(adapter);

        lvCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cars carSelecionado = listaCars.get( i );
                if ( carSelecionado.getIdUsuario().equals (  auth.getCurrentUser().getUid() )  ){

                Intent intent = new Intent(MainActivity.this, PreencherDadosActivity.class);
                intent.putExtra("acao", "editar");
                intent.putExtra("idCar", carSelecionado.getId());
                intent.putExtra("marca", carSelecionado.getMarca());
                intent.putExtra("modelo", carSelecionado.getModelo());
                intent.putExtra("valor", carSelecionado.getValor());
                intent.putExtra("telefone", carSelecionado.getTelefone());
                intent.putExtra("cor", carSelecionado.getCor());
                startActivity(intent);
               }
                Toast.makeText(MainActivity.this,  R.string.danger  ,Toast.LENGTH_LONG).show();
            }
        });
        lvCars.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cars carSelecionado = listaCars.get( i );
                if ( carSelecionado.getIdUsuario().equals (  auth.getCurrentUser().getUid() )  ) {
                    excluir(i);
                    return true;
                }
                return false;
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();

        listaCars.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        query = reference.child("cars").orderByChild("modelo");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String idUserCars = snapshot.child("idUsuario").getValue(String.class); //APAGAR

                    Cars cars = new Cars();
                    cars.setId(snapshot.getKey());
                    cars.setMarca(snapshot.child("marca").getValue(String.class));
                    cars.setModelo(snapshot.child("modelo").getValue(String.class));
                    cars.setValor(snapshot.child("valor").getValue(String.class));
                    cars.setTelefone(snapshot.child("telefone").getValue(String.class));
                    cars.setCor(snapshot.child("cor").getValue(String.class));
                    cars.setIdUsuario(snapshot.child("idUsuario").getValue(String.class));
                    listaCars.add(cars);
                    adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (Cars p : listaCars) {
                    if (p.getId().equals(snapshot.getKey())) {
                        p.setMarca(snapshot.child("marca").getValue(String.class));
                        p.setModelo(snapshot.child("modelo").getValue(String.class));
                        p.setValor(snapshot.child("valor").getValue(String.class));
                        p.setTelefone(snapshot.child("telefone").getValue(String.class));
                        p.setCor(snapshot.child("cor").getValue(String.class));
                        p.setIdUsuario(snapshot.child("setIdUsuario").getValue(String.class));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (Cars p : listaCars) {
                    if (p.getId().equals(snapshot.getKey())) {
                        listaCars.remove(p);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener( childEventListener );
    }

    private void excluir(int posicao){
        Cars carSelecionado = listaCars.get(posicao);
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Excluir");
        msg.setIcon(android.R.drawable.ic_menu_delete);
        msg.setMessage("Deseja realmente excluir este veículo -" + carSelecionado + " ?");
        msg.setNegativeButton("Cancelar", null);
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                reference.child("cars").child( carSelecionado.getId() ).removeValue();
            }
        });
        msg.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carregarLista();
    }

    private void carregarLista(){

        Toast.makeText(this, R.string.atualized, Toast.LENGTH_SHORT).show();
        if(listaCars.size() == 0){
            Cars fake = new Cars("Lista vazia", "", "", "", "","","");
            listaCars.add(fake);
            lvCars.setEnabled(false);
        }else{
            lvCars.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuLogout) {
            auth.signOut();
        }
        if (id == R.id.menuAddProduto) {
            Intent intent = new Intent(MainActivity.this, PreencherDadosActivity.class);
            intent.putExtra("acao", "inserir");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}