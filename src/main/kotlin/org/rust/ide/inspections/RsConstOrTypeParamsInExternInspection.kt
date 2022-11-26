/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.inspections

import org.rust.lang.core.psi.RsFunction
import org.rust.lang.core.psi.RsTypeParameterList
import org.rust.lang.core.psi.RsVisitor
import org.rust.lang.core.psi.ext.RsAbstractableOwner
import org.rust.lang.core.psi.ext.owner
import org.rust.lang.utils.RsDiagnostic
import org.rust.lang.utils.addToHolder

class RsConstOrTypeParamsInExternInspection : RsLocalInspectionTool() {
    override fun buildVisitor(holder: RsProblemsHolder, isOnTheFly: Boolean): RsVisitor =
        object : RsVisitor() {
            override fun visitTypeParameterList(list: RsTypeParameterList) = inspect(holder, list)
        }

    private fun inspect(holder: RsProblemsHolder, list: RsTypeParameterList) {
        list.context?.let { parent ->
            (parent as? RsFunction)?.takeIf {
                it.owner == RsAbstractableOwner.Foreign
            }?.let { function ->
                function.typeParameterList?.let { generics ->
                    if (generics.typeParameterList.isNotEmpty() || generics.constParameterList.isNotEmpty()) {
                        RsDiagnostic.ConstOrTypeParamsInExternError(generics).addToHolder(holder)
                    }
                }
            }
        }
    }
}
