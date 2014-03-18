package br.uece.lotus.parser.lts;

import br.uece.lotus.parser.lts.ast.CriarVerticesProcessBodyVisitor;
import br.uece.lotus.parser.lts.ast.NormalizarAcaoVisitor;
import br.uece.lotus.parser.lts.ast.BaixarProcessBodyVisitor;
import br.uece.lotus.parser.lts.ast.ResolverActionLabelsVisitor;
import br.uece.lotus.parser.lts.ast.ResolverBaseProcessVisitor;
import br.uece.lotus.parser.lts.ast.RemoverTailsVisitor;
import br.uece.lotus.parser.lts.ast.CriarVerticesAuxiliaresVisitor;
import br.uece.lotus.parser.lts.ast.CriarVerticesBaseProcessLabelsVisitor;
import br.uece.lotus.model.ComponentModel;
import br.uece.lotus.parser.GrafoUnmarshaller;
import br.uece.lotus.parser.lts.ast.GraphvizVisitor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class LSACompiler implements ContextoCompilacao, GrafoUnmarshaller {
    
    private Map<String, Integer> mTabelaSimbolos;
    private ComponentModel mGrafo;
    private int novoId = 1;
    private boolean mDebug = true;           

    @Override
    public Map<String, Integer> getTabelaSimbolos() {
        return mTabelaSimbolos;
    }

    @Override
    public ComponentModel getGrafo() {
        return mGrafo;
    }

    @Override
    public int gerarNovoId() {
        return novoId++;
    }

    @Override
    public void unmarshaller(InputStream input, ComponentModel g) throws Exception {
        mGrafo = g;
        mTabelaSimbolos = new HashMap<>();
        
        LSAScanner scanner = new LSAScanner(input);
        LSAParser parser = new LSAParser(scanner);
        ASTNode ast = parser.parse();
        if (mDebug) dumpAST(ast, 0);
        ast.accept(new RemoverTailsVisitor());                
        if (mDebug) dumpAST(ast, 1);
        ast.accept(new ResolverActionLabelsVisitor());        
        if (mDebug) dumpAST(ast, 2);
        ast.accept(new CriarVerticesProcessBodyVisitor(this));        
        if (mDebug) dumpAST(ast, 3);
        ast.accept(new CriarVerticesBaseProcessLabelsVisitor(this));        
        if (mDebug) dumpAST(ast, 4);
        ast.accept(new ResolverBaseProcessVisitor());        
        if (mDebug) dumpAST(ast, 5);
        ast.accept(new BaixarProcessBodyVisitor());        
        if (mDebug) dumpAST(ast, 6);
        ast.accept(new CriarVerticesAuxiliaresVisitor(this));        
        if (mDebug) dumpAST(ast, 7);
        ast.accept(new NormalizarAcaoVisitor(this));        
        if (mDebug) dumpAST(ast, 8);
    }
    
    private void dumpAST(ASTNode ast, int i) throws Exception {        
        ast.accept(new GraphvizVisitor(new PrintStream(new FileOutputStream("dump" + i + ".dot"))));
    }
    
}