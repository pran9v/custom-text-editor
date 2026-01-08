package org.example;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditorKeyEvent extends KeyAdapter {

    private final EditorPanel panel;

    public EditorKeyEvent(EditorPanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if(ch > 31 && ch != 127) panel.insertChar(ch);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE) panel.backspace();
        if(event.isShiftDown()) {
            if(event.getKeyCode() == KeyEvent.VK_LEFT) panel.shiftSelectLeft();
            else if(event.getKeyCode() == KeyEvent.VK_RIGHT) panel.shiftSelectRight();
        } else {
            if(event.getKeyCode() == KeyEvent.VK_LEFT) panel.moveLeft();
            else if(event.getKeyCode() == KeyEvent.VK_RIGHT) panel.moveRight();
            else if(event.getKeyCode() == KeyEvent.VK_UP) panel.moveUp();
            else if(event.getKeyCode() == KeyEvent.VK_DOWN) panel.moveDown();
        }
        if(event.getKeyCode() == KeyEvent.VK_ENTER) {
            panel.insertNewLine();
        }
    }
}
