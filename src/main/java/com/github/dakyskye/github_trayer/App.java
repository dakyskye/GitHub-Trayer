package com.github.dakyskye.github_trayer;

import com.jthemedetecor.OsThemeDetector;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public final class App {
    private static final App instance = new App();

    private final OsThemeDetector detector = OsThemeDetector.getDetector();
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final TrayIcon icon = new TrayIcon(getImage(detector.isDark()));

    public static App getInstance() {
        return instance;
    }

    private App() {
        try {
            final SystemTray tray = SystemTray.getSystemTray();

            icon.setImageAutoSize(true);
            tray.add(icon);

            detector.registerListener(isDark -> SwingUtilities.invokeLater(() -> icon.setImage(getImage(isDark))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private Image getImage(final boolean isDark) {
        if (isDark) {
            return toolkit.getImage("./src/main/resources/github_mark_plus_light.png");
        } else {
            return toolkit.getImage("./src/main/resources/github_mark_plus.png");
        }
    }

    public void listenAndNotify() {
        Timer timer = new Timer();
        timer.schedule(new SubscribeTask(icon), 0, 12 * 1000);
    }

    private static class SubscribeTask extends TimerTask {
        private final ProcessBuilder processBuilder = new ProcessBuilder("gh", "api", "notifications");
        private final TrayIcon icon;

        public SubscribeTask(final TrayIcon icon) {
            this.icon = icon;
        }

        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> {
                try {
                    Process proc = processBuilder.start();
                    JSONArray array = new JSONArray(new String(proc.getInputStream().readAllBytes()));
                    Notification.push(icon, array.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static abstract class Notification {
        private static int currentCount = 0;

        public static void push(final TrayIcon icon, final int count) {
            if (count <= currentCount) {
                currentCount = count;
                return;
            }

            String msg;
            final int diff = count - currentCount;
            if (diff == 1) {
                msg = "You have an unread notification";
            } else {
                msg = String.format("You have %d unread notifications", diff);
            }

            currentCount = count;
            icon.displayMessage("GitHub Trayer", msg, TrayIcon.MessageType.INFO);
        }
    }
}

