package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.VALOR_PADRAO_POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = carregaTodasNotas();

        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView campoInsereNota = findViewById(R.id.lista_notas_insere_nota);

        campoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> carregaTodasNotas() {
        NotaDAO notaDAO = new NotaDAO();

        for(int i=0; i <= 10; i++){
            notaDAO.insere(new Nota("Título " + (i+1), "Descrição posição - " + i ));
        }

        return notaDAO.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(eResultadoInsereNota(requestCode, data)){
            if(resultadoOK(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adicionaNota(notaRecebida);
            }
        }

        if(eResultadoAlteraNota(requestCode, data)){
            if(resultadoOK(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int notaPosicao = data.getIntExtra(CHAVE_POSICAO, VALOR_PADRAO_POSICAO_INVALIDA);

                if (notaPosicao > VALOR_PADRAO_POSICAO_INVALIDA) {
                    alteraNota(notaRecebida, notaPosicao);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_LONG).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alteraNota(Nota notaRecebida, int notaPosicao) {
        new NotaDAO().altera(notaPosicao, notaRecebida);
        adapter.altera(notaPosicao, notaRecebida);
    }

    private boolean eResultadoAlteraNota(int requestCode, Intent data) {
        return eCodigoRequisicaoAlteraNota(requestCode)
                && temNota(data);
    }

    private boolean eCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adicionaNota(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean eResultadoInsereNota(int requestCode,Intent data) {
        return eCodigoRequisicaoInsereNota(requestCode) && temNota(data);
    }

    private boolean temNota(Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOK(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean eCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }
    
    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback());
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioActivityAltera(Nota nota, int posicao) {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        intent.putExtra(CHAVE_NOTA, nota);
        intent.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(intent, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}
