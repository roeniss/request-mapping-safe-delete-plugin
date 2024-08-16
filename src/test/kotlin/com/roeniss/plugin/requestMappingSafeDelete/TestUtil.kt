package com.roeniss.plugin.requestMappingSafeDelete

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory

fun String.toFile(project: Project): KtFile =
    PsiFileFactory
        .getInstance(project)
        .createFileFromText("TestController.kt", KotlinLanguage.INSTANCE, this) as KtFile

fun String.execute(project: Project): String =
    this.toFile(project).apply {
        AppExecutor(project, KtPsiFactory(project)).execute(this)
    }.text
