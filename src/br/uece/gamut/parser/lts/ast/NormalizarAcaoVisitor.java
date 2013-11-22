package br.uece.gamut.parser.lts.ast;

import br.uece.gamut.Grafo;
import br.uece.gamut.Transicao;
import br.uece.gamut.app.editor.GrafoEditor;
import br.uece.gamut.parser.lts.ASTNode;
import br.uece.gamut.parser.lts.Visitor;
import br.uece.gamut.parser.lts.ContextoCompilacao;
import br.uece.gamut.parser.lts.LSAVisitor;

/**
 *
 * @author emerson
 */
public class NormalizarAcaoVisitor implements Visitor {

    private final Grafo mGrafo;

    public NormalizarAcaoVisitor(ContextoCompilacao contexto) {
        mGrafo = contexto.getGrafo();
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.BACTION)) {
            return;
        }

        String label = (String) n.getFirstChildByType(LSAVisitor.ACTION_LABEL).getTag(ContextoCompilacao.TAG_TRANSICAO_NOME);
        Integer idOrigem = (Integer) n.getParent().getTag(ContextoCompilacao.TAG_PROCESSO_ID);
        Integer idDestino = (Integer) n.getFirstChildByType(LSAVisitor.LOCAL_PROCESS).getTag(ContextoCompilacao.TAG_PROCESSO_ID);

        n.tag(ContextoCompilacao.TAG_PROCESSO_ORIGEM_ID, idOrigem);
        n.tag(ContextoCompilacao.TAG_PROCESSO_DESTINO_ID, idDestino);
        n.tag(ContextoCompilacao.TAG_TRANSICAO_NOME, label);

        System.out.println("newTransicao " + idOrigem + " " + idDestino);
        
        Transicao t = mGrafo.newTransicao(idOrigem, idDestino);
        t.setTag(GrafoEditor.TAG_LABEL, label);
    }
}