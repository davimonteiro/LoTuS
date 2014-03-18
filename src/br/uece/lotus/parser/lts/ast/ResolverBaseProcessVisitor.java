/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.parser.lts.ast;

import br.uece.lotus.parser.lts.ASTNode;
import br.uece.lotus.parser.lts.Visitor;
import br.uece.lotus.parser.lts.ContextoCompilacao;
import br.uece.lotus.parser.lts.LSAVisitor;

/**
 *
 * @author emerson
 */
public class ResolverBaseProcessVisitor implements Visitor {

    public ResolverBaseProcessVisitor() {
        
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.BASE_PROCESS)) return;
        
        ASTNode pai = n.getParent();
        pai.tag(ContextoCompilacao.TAG_PROCESSO_ID, n.getTag(ContextoCompilacao.TAG_PROCESSO_ID));
        pai.tag(ContextoCompilacao.TAG_PROCESSO_NOME, n.getTag(ContextoCompilacao.TAG_PROCESSO_NOME));
        pai.removeAllChildren();        
    }
    
}