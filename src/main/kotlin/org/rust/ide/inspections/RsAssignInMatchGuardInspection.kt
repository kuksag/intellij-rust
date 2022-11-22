/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.inspections

import com.intellij.psi.util.descendants
import org.rust.lang.core.psi.RsBinaryExpr
import org.rust.lang.core.psi.RsBlockExpr
import org.rust.lang.core.psi.RsExpr
import org.rust.lang.core.psi.RsExprStmt
import org.rust.lang.core.psi.RsMatchExpr
import org.rust.lang.core.psi.RsPathExpr
import org.rust.lang.core.psi.RsUnaryExpr
import org.rust.lang.core.psi.RsVisitor
import org.rust.lang.core.psi.ext.isAssignBinaryExpr
import org.rust.lang.utils.RsDiagnostic
import org.rust.lang.utils.addToHolder

/**
 * Inspection that detects the E0510 error.
 */
class RsAssignInMatchGuardInspection : RsLocalInspectionTool() {
    override fun buildVisitor(holder: RsProblemsHolder, isOnTheFly: Boolean): RsVisitor = object : RsVisitor() {
        override fun visitMatchExpr(match: RsMatchExpr) = inspect(holder, match)

    }
}

private fun inspect(holder: RsProblemsHolder, match: RsMatchExpr) {
    val varName = getVarName(match.expr) ?: return
    match.matchBody?.matchArmList?.forEach { arm ->
        (arm.matchArmGuard?.expr as? RsBlockExpr)?.let { guard ->
            guard.descendants().forEach { element ->
                (element as? RsExprStmt)?.expr.let { expr ->
                    (expr as? RsBinaryExpr)?.takeIf { it.isAssignBinaryExpr }?.let { binaryExpr ->
                        getVarName(binaryExpr.left).takeIf { it == varName }?.let {
                            RsDiagnostic.AssignInMatchGuardError(binaryExpr).addToHolder(holder)
                        }
                    }
                }
            }
        }
    }
}

private fun getVarName(expr: RsExpr?): String? {
    return when (expr) {
        is RsPathExpr -> expr.path.referenceName
        is RsUnaryExpr -> expr.expr?.let { getVarName(it) }
        else -> null
    }
}
