package com.github.dakyskye.github_trayer;

public class Trayer {
    public static void main(String[] args) {
        App app = null;
        try {
            app = new App();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        app.listenAndNotify();
    }
}
