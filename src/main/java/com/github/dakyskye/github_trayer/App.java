package com.github.dakyskye.github_trayer;

import com.jthemedetecor.OsThemeDetector;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    private static TrayIcon icon;
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static final OsThemeDetector detector = OsThemeDetector.getDetector();
    private static final String title = "GitHub Trayer";

    public App() throws Exception {
        if (!SystemTray.isSupported()) {
            throw new Exception("system tray is not supported");
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image;

        if (detector.isDark()) {
            image = toolkit.getImage("./src/main/resources/github_mark_plus_light.png");
        } else {
            image = toolkit.getImage("./src/main/resources/github_mark_plus.png");
        }

        icon = new TrayIcon(image, title);
        icon.setImageAutoSize(true);

        tray.add(icon);

        detector.registerListener(isDark -> SwingUtilities.invokeLater(() -> {
            if (isDark) {
                icon.setImage(toolkit.getImage("./src/main/resources/github_mark_plus_light.png"));
            } else {
                icon.setImage(toolkit.getImage("./src/main/resources/github_mark_plus.png"));
            }
        }));
    }

    public void listenAndNotify() {
        Timer timer = new Timer();
        timer.schedule(new SubscribeTask(), 0, 12 * 1000);
    }

    static class SubscribeTask extends TimerTask {
        final private ProcessBuilder processBuilder = new ProcessBuilder("gh", "api", "notifications");

        @Override
        public void run() {
            Process proc;
            JSONArray array;
            try {
                proc = processBuilder.start();
                array = new JSONArray(new String(proc.getInputStream().readAllBytes()));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Notification.push(array.length());
        }
    }

    static abstract class Notification {
        private static int currentCount = 0;

        public static void push(int count) {
            if (count <= currentCount) {
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
            icon.displayMessage(title, msg, TrayIcon.MessageType.INFO);
        }
    }
}

