package com.roeniss.plugin.requestMappingSafeDelete

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

internal class KtClassCollector : KtTreeVisitorVoid() {
    var ktClassList = mutableListOf<KtClass>()

    override fun visitClass(klass: KtClass) {
        ktClassList.add(klass)
        super.visitClass(klass)
    }
}
