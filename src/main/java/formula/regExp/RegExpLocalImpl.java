/*
 * FFLOAT  Copyright (C) 2015  Riccardo De Masellis.
 *
 * This program comes with ABSOLUTELY NO WARRANTY.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; see http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package formula.regExp;

import formula.FormulaType;
import formula.ImplFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/**
 * Created by Riccardo De Masellis on 15/05/15.
 * For any issue please write to r.demasellis@trentorise.eu.
 */
public class RegExpLocalImpl extends RegExpBinary implements RegExpBoolOpLocal, ImplFormula {

    public RegExpLocalImpl(RegExp left, RegExp right) {
        super(left, right);
    }

    @Override
    public FormulaType getFormulaType() {
        return FormulaType.RE_LOCAL_IMPL;
    }

    @Override
    public PropositionalFormula regExpLocal2Propositional() {
        return ((RegExpLocal) this.nnf()).regExpLocal2Propositional();
    }
}
