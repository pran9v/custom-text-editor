package org.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditorMouseEvent extends MouseAdapter {

    private final EditorPanel panel;

    public EditorMouseEvent(EditorPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panel.handleMousePress(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        panel.handleMouseDrag(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            panel.handleDoubleClick(e.getX(), e.getY());
        } else if (e.getClickCount() == 3) {
            panel.handleTripleClick(e.getX(), e.getY());
        }
    }
}
