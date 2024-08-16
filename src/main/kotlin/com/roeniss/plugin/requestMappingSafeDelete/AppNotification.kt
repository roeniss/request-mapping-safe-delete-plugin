package com.roeniss.plugin.requestMappingSafeDelete

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorNotificationPanel

internal class AppNotification(
    private val project: Project
) : EditorNotificationPanel() {
    fun notifyInfo(content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("com.roeniss.plugin.requestMappingSafeDelete")
            .createNotification(content, NotificationType.INFORMATION)
            .notify(project)
    }

    fun notifyError(content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("com.roeniss.plugin.requestMappingSafeDelete")
            .createNotification(content, NotificationType.ERROR)
            .notify(project)
    }
}
