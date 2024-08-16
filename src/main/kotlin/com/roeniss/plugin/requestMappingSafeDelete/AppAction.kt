package com.roeniss.plugin.requestMappingSafeDelete

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.util.PsiUtilCore
import org.jetbrains.kotlin.psi.*

class AppAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val appNotification = AppNotification(project)

        val executor = AppExecutor(project, KtPsiFactory(project))
        val vFiles = ProjectRootManager.getInstance(project).contentSourceRoots

        for (vf in vFiles) {
            VfsUtilCore.iterateChildrenRecursively(vf, { true }) { f ->
                if (f.extension == "kt") {
                    val file = PsiUtilCore.getPsiFile(project, f) as KtFile
                    executor.execute(file)
                }
                return@iterateChildrenRecursively true
            }
        }
        appNotification.notifyInfo("@RequestMapping annotation on class has been safely removed")
    }
}

internal class AppExecutor(
    private val project: Project,
    private val factory: KtPsiFactory,
) {
    fun execute(file: KtFile) {
        val ktClassCollector = KtClassCollector()
        file.accept(ktClassCollector)

        // for each class
        ktClassCollector.ktClassList.forEach { klass ->
            val collector = RMAnnotationCollector()
            klass.accept(collector)

            val classRMA = collector.classRMA ?: return@forEach
            val methodRMAs = collector.methodRMAs

            val classVAs = getPathValueArguments(classRMA)
            if (classVAs.isEmpty()) { // if no path value, just remove the annotation
                updatePsi { classRMA.delete() }
                return@forEach
            }

            // for each method in the class
            methodRMAs.forEach methodRM@{ methodRM ->
                val methodValueArgs = getPathValueArguments(methodRM)
                // if no path value, just copy it from class annotation
                if (methodValueArgs.isEmpty()) {
                    // if @RequestMapping annotation has no parenthesis (KtValueArgumentList), add it
                    if (methodRM.valueArgumentList == null) {
                        updatePsi {
                            methodRM.add(factory.createValueArgumentListByPattern("()"))
                        }
                    }
                    classVAs.forEach {
                        updatePsi {
                            methodRM.valueArgumentList!!.addArgument(it)
                        }
                    }
                    return@methodRM
                }

                // collect all combinations of path values using class and method annotations
                val exprs = mutableListOf<KtStringTemplateExpression>()
                classVAs.forEach classValueArgs@{ classValueArg ->
                    methodValueArgs.forEach methodValueArg@{ methodValueArg ->
                        val expr1 = classValueArg.getArgumentExpression() ?: TODO()
                        val expr2 = methodValueArg.getArgumentExpression() ?: TODO()
                        val stringifiedExprs = collectExpressions(expr1, expr2, factory)
                        exprs.addAll(stringifiedExprs)
                    }
                }

                // create new value argument list and add it to the method
                val newValueArgList = factory.createValueArgumentListByPattern("()")
                exprs.forEach {
                    newValueArgList.addArgument(factory.createArgument(it))
                }
                updatePsi {
                    methodRM.valueArgumentList!!.replace(newValueArgList)
                }
            }

            // copy class annotation to no annotation methods
            collector.methodNoRMAs.forEach {
                updatePsi {
                    it.addFirst(classRMA)
                }
            }

            // remove class annotation
            updatePsi {
                classRMA.delete()
            }
        }
    }

    private fun isPathValue(it: ValueArgument) = !it.isNamed() || it.getArgumentName()?.referenceExpression?.text in listOf("name", "value", "path")

    private fun getPathValueArguments(classRMA: KtAnnotationEntry) = classRMA.valueArguments.filter { isPathValue(it) }.map { it as KtValueArgument }

    private fun updatePsi(action: () -> Unit) {
        WriteCommandAction.runWriteCommandAction(project) {
            action()
        }
    }
}
