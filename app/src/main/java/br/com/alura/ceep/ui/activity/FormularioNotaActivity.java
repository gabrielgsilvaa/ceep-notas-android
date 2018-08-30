package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.Serializable;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.VALOR_PADRAO_POSICAO_VAZIA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        Intent intentDadosRecebidos = getIntent();

        if (intentDadosRecebidos.hasExtra(CHAVE_NOTA) && intentDadosRecebidos.hasExtra(CHAVE_POSICAO)) {

            Nota notaRecebida = (Nota) intentDadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = intentDadosRecebidos.getIntExtra(CHAVE_POSICAO, VALOR_PADRAO_POSICAO_VAZIA);

            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(notaRecebida.getTitulo());

            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(notaRecebida.getDescricao());
        }
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
        setResult(CODIGO_RESULTADO_NOTA_CRIADA, resultadoInsercao);
    }

    @NonNull
    private Nota criaNota() {
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean eMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_salva;
    }
}
