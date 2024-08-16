package com.roeniss.plugin.requestMappingSafeDelete

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.base.psi.replaced
import org.jetbrains.kotlin.idea.intentions.ConvertToStringTemplateIntention.Holder.buildReplacement
import org.jetbrains.kotlin.psi.*

internal fun KtExpression.addFirst(elem: PsiElement) {
    val firstChild = this.firstChild
    if (firstChild != null) {
        this.addBefore(elem, firstChild)
    } else {
        this.add(elem)
    }
}


internal fun collectExpressions(expr1: KtExpression, expr2: KtExpression, factory: KtPsiFactory): List<KtStringTemplateExpression> {
    if (expr1 is KtCollectionLiteralExpression) {
        return expr1.children.flatMap { collectExpressions(it as KtExpression, expr2, factory) }
    }

    if (expr2 is KtCollectionLiteralExpression) {
        return expr2.children.flatMap { collectExpressions(expr1, it as KtExpression, factory) }
    }

    val expr3 = factory.createExpressionByPattern("$0 + $1", expr1, expr2) as KtBinaryExpression
    val stringifiedExpr = expr3.replaced(buildReplacement(expr3))
    return listOf(stringifiedExpr)
}
