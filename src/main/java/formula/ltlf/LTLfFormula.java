/*
 * FFLOAT  Copyright (C) 2015  Riccardo De Masellis.
 *
 * This program comes with ABSOLUTELY NO WARRANTY.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; see http://www.gnu.org/licenses/gpl-3.0.html for details.
 */

package formula.ltlf;

import formula.Formula;
import formula.ldlf.LDLfFormula;

/**
 * Created by Riccardo De Masellis on 14/05/15.
 * For any issue please write to r.demasellis@trentorise.eu.
 */
public interface LTLfFormula extends Formula {

    /*
    To be used only inside this package!
    */
    LDLfFormula toLDLfRec();

    /*
    To be used publicly
    */
    default LDLfFormula toLDLf() {
        return this.antinnf().toLDLfRec();
    }

    LTLfFormula antinnf();
}
