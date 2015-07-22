/*
 * FFLOAT  Copyright (C) 2015  Riccardo De Masellis.
 *
 * This program comes with ABSOLUTELY NO WARRANTY.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; see http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

import evaluations.PropositionLast;
import formula.ldlf.*;
import formula.quotedFormula.*;
import generatedParsers.LDLfFormulaParserLexer;
import generatedParsers.LDLfFormulaParserParser;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import visitors.LDLfVisitors.LDLfVisitor;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Riccardo De Masellis on 10/07/15.
 */
public class DeltaFunctionTest {

    @Ignore
    @Test
    public void deltaAtomicTest() {
        LDLfttFormula tt = new LDLfttFormula();
        QuotedVar quoted = new QuotedVar(tt);
        PossibleWorld world = new PossibleWorld();
        QuotedFormula result = quoted.delta(world);
        QuotedFormula expected = new QuotedTrueFormula();
        Assert.assertEquals(expected, result);

        LDLfffFormula ff = new LDLfffFormula();
        quoted = new QuotedVar(ff);
        world = new PossibleWorld();
        result = quoted.delta(world);
        expected = new QuotedFalseFormula();
        Assert.assertEquals(expected, result);


        String input = "( !(a && b) && !( a || b ) ) || ((!d) && ciao)";

        LDLfFormulaParserLexer lexer = new LDLfFormulaParserLexer(new ANTLRInputStream(input));
        LDLfFormulaParserParser parser = new LDLfFormulaParserParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.expression();
        LDLfVisitor visitor = new LDLfVisitor();
        LDLfLocalFormula formula = (LDLfLocalFormula) visitor.visit(tree);
        quoted = new QuotedVar(formula);

        PropositionalFormula pf = formula.LDLfLocal2Prop();

        // Try with a model of the formula
        Set<PossibleWorld> models = pf.getModels();
        if (models.isEmpty())
            throw new RuntimeException("The specified formula is unsatisfiable!");
        Iterator<PossibleWorld> it = models.iterator();
        world = it.next();
        result = quoted.delta(world);
        expected = new QuotedTrueFormula();
        System.out.println(pf);
        System.out.println("Model: " + world);
        Assert.assertEquals(expected, result);

        // Try with a non-model of the formula
        PropositionalSignature sig = formula.getSignature();
        Set<PossibleWorld> nonModels = PossibleWorld.getAllPossibleWorlds(sig);
        nonModels.removeAll(models);
        if (nonModels.isEmpty())
            throw new RuntimeException("The specified formula is a tautology!");
        it = nonModels.iterator();
        world = it.next();
        result = quoted.delta(world);
        expected = new QuotedFalseFormula();
        System.out.println("Non-model: " + world);
        Assert.assertEquals(expected, result);
    }

    @Ignore
    @Test
    public void deltaQuotedAndTest() {
        String input = "( !(a && b) && !( a || b ) ) || ((!d) && ciao)";

        LDLfFormulaParserLexer lexer = new LDLfFormulaParserLexer(new ANTLRInputStream(input));
        LDLfFormulaParserParser parser = new LDLfFormulaParserParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.expression();
        LDLfVisitor visitor = new LDLfVisitor();
        LDLfLocalFormula formula = (LDLfLocalFormula) visitor.visit(tree);
        QuotedFormula quotedLeft = new QuotedVar(formula);
        LDLfLocalVar ldlfRight = new LDLfLocalVar(new Proposition("q"));
        QuotedFormula quotedRight = new QuotedVar(ldlfRight);
        QuotedFormula quotedAnd = new QuotedAndFormula(quotedLeft, quotedRight);

        // Try with a model
        PossibleWorld world = new PossibleWorld();
        world.add(new Proposition("ciao"));
        world.add(new Proposition("a"));
        world.add(new Proposition("b"));
        world.add(new Proposition("q"));

        QuotedFormula result = quotedAnd.delta(world);
        QuotedFormula expLeft = new QuotedTrueFormula();
        QuotedFormula expRight = new QuotedTrueFormula();
        QuotedFormula expected = new QuotedAndFormula(expLeft, expRight);

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expected);
        Assert.assertEquals(expected, result);

        // Try with a non-model
        world = new PossibleWorld();
        world.add(new Proposition("ciao"));
        world.add(new Proposition("a"));
        world.add(new Proposition("b"));
        //world.add(new Proposition("q"));

        result = quotedAnd.delta(world);
        expLeft = new QuotedTrueFormula();
        expRight = new QuotedFalseFormula();
        expected = new QuotedAndFormula(expLeft, expRight);

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expected);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void deltaTestGeneric() {
        String input = "<( (a)? ; true )*>b";

        LDLfFormulaParserLexer lexer = new LDLfFormulaParserLexer(new ANTLRInputStream(input));
        LDLfFormulaParserParser parser = new LDLfFormulaParserParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.expression();
        LDLfVisitor visitor = new LDLfVisitor();
        LDLfFormula formula = visitor.visit(tree);

        QuotedFormula quoted = new QuotedVar(formula);

        // Try with model Pi={a, last}
        PossibleWorld world = new PossibleWorld();
        world.add(new Proposition("a"));
        world.add(new PropositionLast());

        QuotedFormula result = quoted.delta(world);

        QuotedFormula andLeft = new QuotedTrueFormula();
        QuotedFormula andRight = new QuotedFalseFormula();
        QuotedFormula and = new QuotedAndFormula(andLeft, andRight);
        QuotedFormula orLeft = new QuotedFalseFormula();
        QuotedFormula expected = new QuotedOrFormula(orLeft, and);

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expected);
        Assert.assertEquals(expected, result);

        //Testing the models of the formula:
        Set<Set<QuotedVar>> QuotedModels = result.getMinimalModels();
        System.out.println(QuotedModels);

        // Try with model Pi={a, b}
        world = new PossibleWorld();
        world.add(new Proposition("a"));
        world.add(new Proposition("b"));

        result = quoted.delta(world);

        andRight = new QuotedVar(formula);
        orLeft = new QuotedTrueFormula();
        and = new QuotedAndFormula(andLeft, andRight);
        expected = new QuotedOrFormula(orLeft, and);

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expected);
        Assert.assertEquals(expected, result);

        //Testing the models of the formula:
        QuotedModels = result.getMinimalModels();
        System.out.println(QuotedModels);

        /////////////////////////////////////////////

        input = "[(a + (b;c)) + (!f ; d*)]g";
        lexer = new LDLfFormulaParserLexer(new ANTLRInputStream(input));
        parser = new LDLfFormulaParserParser(new CommonTokenStream(lexer));
        tree = parser.expression();
        visitor = new LDLfVisitor();
        formula = visitor.visit(tree);

        quoted = new QuotedVar(formula);

        // Try with model Pi={a, c, g}
        world = new PossibleWorld();
        world.add(new Proposition("a"));
        world.add(new Proposition("c"));
        world.add(new Proposition("g"));

        result = quoted.delta(world);

        QuotedFormula quotedG = new QuotedVar(new LDLfLocalVar(new Proposition("g")));
        input = "[d*]g";
        lexer = new LDLfFormulaParserLexer(new ANTLRInputStream(input));
        parser = new LDLfFormulaParserParser(new CommonTokenStream(lexer));
        tree = parser.expression();
        visitor = new LDLfVisitor();
        LDLfFormula formula2 = visitor.visit(tree);
        QuotedFormula dStarG = new QuotedVar(formula2);

        QuotedFormula innerAnd = new QuotedAndFormula(quotedG, new QuotedTrueFormula());
        expected = new QuotedAndFormula(innerAnd, dStarG);

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expected);
        Assert.assertEquals(expected, result);

        // Try with model Pi={b, f, last}
        world = new PossibleWorld();
        world.add(new Proposition("b"));
        world.add(new Proposition("f"));
        world.add(new PropositionLast());

        result = quoted.delta(world);

        innerAnd = new QuotedAndFormula(new QuotedTrueFormula(), new QuotedTrueFormula());
        expected = new QuotedAndFormula(innerAnd, new QuotedTrueFormula());

        System.out.println("Result: " + result);
        System.out.println("Expected: " + expected);
        Assert.assertEquals(expected, result);

    }
}