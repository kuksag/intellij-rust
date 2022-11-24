/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.inspections

import com.intellij.psi.util.descendants
import org.rust.lang.core.psi.RsForeignModItem
import org.rust.lang.core.psi.RsFunction
import org.rust.lang.core.psi.RsVisitor
import org.rust.lang.utils.RsDiagnostic
import org.rust.lang.utils.addToHolder

class RsConstOrTypeParamsInExternInspection : RsLocalInspectionTool() {
    override fun buildVisitor(holder: RsProblemsHolder, isOnTheFly: Boolean): RsVisitor =
        object : RsVisitor() {
            override fun visitForeignModItem(item: RsForeignModItem) = inspect(holder, item)
        }
}

private fun inspect(holder: RsProblemsHolder, item: RsForeignModItem) {
    item.descendants().forEach { element ->
        (element as? RsFunction)?.let { function ->
            function.typeParameterList?.let { generics ->
                if (generics.typeParameterList.isNotEmpty() || generics.constParameterList.isNotEmpty()) {
                    RsDiagnostic.ConstOrTypeParamsInExternError(generics).addToHolder(holder)
                }
            }
        }
    }
}
