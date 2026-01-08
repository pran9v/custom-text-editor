package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EditorPanel panel = new EditorPanel();
        frame.add(panel);

        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
