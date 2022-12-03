package com.example.autenticacao;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        TextView tvEmailLogado = findViewById(R.id.tvEmailLogado);
        EditText txtNome = findViewById(R.id.txtNome);
        EditText txtNota1 = findViewById(R.id.txtNota1);
        EditText txtNota2 = findViewById(R.id.txtNota2);
        Button btnInserir = findViewById(R.id.btnInserir);
        ListView listaAlunos = findViewById(R.id.listaAlunos);
        Button btnSair = findViewById(R.id.btnSair);

        DatabaseReference bd = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth usuarios = FirebaseAuth.getInstance();
        DatabaseReference alunos = bd.child("alunos");

        tvEmailLogado.setText(usuarios.getCurrentUser().getEmail());

        btnInserir.setOnClickListener(view -> {
            String chave = alunos.push().getKey();
            String nome = txtNome.getText().toString();
            double nota1 = Double.parseDouble(txtNota1.getText().toString());
            double nota2 = Double.parseDouble(txtNota2.getText().toString());

            Aluno aluno = new Aluno(chave, nome, nota1, nota2);

            if (chave != null) {
                alunos.child(chave).setValue(aluno);
            }

            txtNome.setText("");
            txtNota1.setText("");
            txtNota2.setText("");
        });

        btnSair.setOnClickListener(view -> {
            usuarios.signOut();
            Toast.makeText(this, "Logoff", Toast.LENGTH_SHORT).show();
            finish();
        });

        FirebaseListOptions<Aluno> options = new FirebaseListOptions.Builder<Aluno>()
                .setLayout(R.layout.item_lista)
                .setQuery(alunos, Aluno.class)
                .setLifecycleOwner(this)
                .build();

        AlunosAdapter adapter = new AlunosAdapter(options);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

    }

    private static class AlunosAdapter extends FirebaseListAdapter<Aluno> {

        public AlunosAdapter(FirebaseListOptions<Aluno> options) {
            super(options);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void populateView(View view, Aluno aluno, int position) {
            TextView txtNomeAluno = view.findViewById(R.id.txtNomeAluno);
            TextView txtNota1Aluno = view.findViewById(R.id.txtNota1Aluno);
            TextView txtNota2Aluno = view.findViewById(R.id.txtNota2Aluno);
            TextView txtMedia = view.findViewById(R.id.txtMedia);

            txtNomeAluno.setText(aluno.getNome());
            txtNota1Aluno.setText(Double.toString(aluno.getNota1()));
            txtNota2Aluno.setText(Double.toString(aluno.getNota2()));
            txtMedia.setText(Double.toString((aluno.getNota1() + aluno.getNota2()) / 2));
        }
    }
}