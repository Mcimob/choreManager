package ch.pfaditools.choreManager.util;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class Notifier {

    public static void showNotification(String message) {
        Notification.show(message);
    }

    public static void showSuccessNotification(String message) {
        Notification n = Notification.show(message);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static void showErrorNotification(String message) {
        Notification n = Notification.show(message);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static void showWarningNotification(String message) {
        Notification n = Notification.show(message);
        n.addThemeVariants(NotificationVariant.LUMO_WARNING);
    }

}
