package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.VALOR_PADRAO_POSICAO_VAZIA;

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
                configuraFormularioNotaActivity();
            }
        });
    }

    private void configuraFormularioNotaActivity() {
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

        if(eResultadoComNota(requestCode, resultCode, data)){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adicionaNota(notaRecebida);
        }

        if(requestCode == CODIGO_REQUISICAO_ALTERA_NOTA
                && resultCode == CODIGO_RESULTADO_NOTA_CRIADA
                && temNota(data)
                && data.hasExtra(CHAVE_POSICAO)){

            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int  notaPosicao = data.getIntExtra(CHAVE_POSICAO, VALOR_PADRAO_POSICAO_VAZIA);

            new NotaDAO().altera(notaPosicao, notaRecebida);
            adapter.altera(notaPosicao, notaRecebida);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adicionaNota(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean eResultadoComNota(int requestCode, int resultCode, Intent data) {
        return eCodigoRequisicaoInsereNota(requestCode) && eCodigoResultadoNotaCriada(resultCode) && temNota(data);
    }

    private boolean temNota(Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean eCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean eCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }
    
    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                intent.putExtra(CHAVE_NOTA, nota);
                intent.putExtra(CHAVE_POSICAO, posicao);
                startActivityForResult(intent, CODIGO_REQUISICAO_ALTERA_NOTA);
            }
        });

    }
}
