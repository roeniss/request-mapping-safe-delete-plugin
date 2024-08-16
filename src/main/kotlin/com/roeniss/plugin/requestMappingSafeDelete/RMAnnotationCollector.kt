package com.roeniss.plugin.requestMappingSafeDelete

import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

internal class RMAnnotationCollector : KtTreeVisitorVoid() {
    var classRMA: KtAnnotationEntry? = null
    val methodRMAs: List<KtAnnotationEntry>
        get() = _methodRMAs.toList()
    val methodNoRMAs: List<KtNamedFunction>
        get() = _methodNoRMAs.toList()

    private val _methodNoRMAs: MutableList<KtNamedFunction> = mutableListOf()
    private val _methodRMAs: MutableList<KtAnnotationEntry> = mutableListOf()

    override fun visitAnnotationEntry(annotationEntry: KtAnnotationEntry) {
        if (isRequestMapping(annotationEntry)) {
            classRMA = annotationEntry
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        val annotationEntry = function.annotationEntries.firstOrNull {
            isRequestKindMapping(it)
        }

        if (annotationEntry != null) {
            _methodRMAs.add(annotationEntry)
        } else {
            _methodNoRMAs.add(function)
        }
    }

    private fun isRequestMapping(annotationEntry: KtAnnotationEntry) = "RequestMapping" == annotationEntry.shortName?.identifier

    private fun isRequestKindMapping(annotationEntry: KtAnnotationEntry) = annotationEntry.shortName?.identifier in listOf(
        "GetMapping",
        "PostMapping",
        "DeleteMapping",
        "PatchMapping",
        "PutMapping",
        "RequestMapping"
    )
}
