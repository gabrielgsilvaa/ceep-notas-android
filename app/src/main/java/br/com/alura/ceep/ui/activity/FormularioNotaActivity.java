package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.VALOR_PADRAO_POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida = VALOR_PADRAO_POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        inicializaCampos();

        Intent intentDadosRecebidos = getIntent();

        if (intentDadosRecebidos.hasExtra(CHAVE_NOTA)) {

            Nota notaRecebida = (Nota) intentDadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = intentDadosRecebidos.getIntExtra(CHAVE_POSICAO, VALOR_PADRAO_POSICAO_INVALIDA);

            preencheCampos(notaRecebida);
        }
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(eMenuSalvaNota(item)){
            Nota novaNota = criaNota();
            retornaNota(novaNota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    @NonNull
    private Nota criaNota() {
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean eMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_salva;
    }
}
