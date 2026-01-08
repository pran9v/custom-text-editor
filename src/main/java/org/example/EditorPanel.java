package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {

    List<StringBuilder> lines = new ArrayList<>();
    private final int originX = 20;
    private final int originY = 40;
    boolean caretVisible = true;

    int caretRow = 0;
    int caretCol = 0;
    int anchorRow = 0;
    int anchorCol = 0;

    public EditorPanel() {
        lines.add(new StringBuilder());

        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        setFont(new Font("Consolas", Font.PLAIN, 32));

        ActionListener blink = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                caretVisible = !caretVisible;
                repaint();
            }
        };

        Timer timer = new Timer(500, blink);
        timer.start();

        addKeyListener(new EditorKeyEvent(this));

        EditorMouseEvent editorMouseEvent = new EditorMouseEvent(this);
        addMouseListener(editorMouseEvent);
        addMouseMotionListener(editorMouseEvent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        setOpaque(true);

        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();

        int lineHeight = fm.getHeight();
        int charWidth = fm.charWidth('A');


        // highlight the current line
        g.setColor(new Color(230, 235, 245));
        int lineY = originY + caretRow * lineHeight - fm.getAscent();
        g.fillRect(0, lineY, getWidth(), lineHeight);


        // for drawing the selection background
        int startRow = anchorRow;
        int startCol = anchorCol;
        int endRow = caretRow;
        int endCol = caretCol;
        if (startRow > endRow || (startRow == endRow && startCol > endCol)) {
            int tr = startRow, tc = startCol;
            startRow = endRow;
            startCol = endCol;
            endRow = tr;
            endCol = tc;
        }

        g.setColor(new Color(100, 150, 255, 120));

        for (int row = startRow; row <= endRow; row++) {
            int lineLength = lines.get(row).length();

            int selStartCol;
            int selEndCol;

            if (row == startRow && row == endRow) {
                selStartCol = startCol;
                selEndCol = endCol;
            } else if (row == startRow) {
                selStartCol = startCol;
                selEndCol = lineLength;
            } else if (row == endRow) {
                selStartCol = 0;
                selEndCol = endCol;
            } else {
                selStartCol = 0;
                selEndCol = lineLength;
            }

            if (selStartCol == selEndCol) continue;

            int x = originX + selStartCol * charWidth;
            int y = originY + row * lineHeight - fm.getAscent();
            int width = (selEndCol - selStartCol) * charWidth;

            g.fillRect(x, y, width, lineHeight);
        }

        // drawing text
        g.setColor(Color.BLACK);
        for (int i = 0; i < lines.size(); i++) {
            int y = originY + i * lineHeight;
            g.drawString(lines.get(i).toString(), originX, y);
        }

        // drawing caret
        int caretX = originX + caretCol * charWidth;
        int caretY = originY + caretRow * lineHeight;

        int caretTop = caretY - fm.getAscent();
        int caretBottom = caretTop + lineHeight;

        if (caretVisible) {
            g.drawLine(caretX, caretTop, caretX, caretBottom);
        }
    }


    private Point getPositionFromMouse(int x, int y) {
        FontMetrics fm = getFontMetrics(getFont());
        int charWidth = fm.charWidth('A');
        int lineHeight = fm.getHeight();

        int adjustedY = y - (originY - fm.getAscent());
        int row = adjustedY / lineHeight;
        row = Math.max(0, Math.min(row, lines.size() - 1));

        int col = (x - originX) / charWidth;
        col = Math.max(0, Math.min(col, lines.get(row).length()));

        return new Point(col, row);
    }

    public void handleMousePress(int x, int y) {
        Point point = getPositionFromMouse(x, y);
        caretRow = point.y;
        caretCol = point.x;
        anchorRow = caretRow;
        anchorCol = caretCol;
        caretVisible = true;
        repaint();
    }

    public void handleMouseDrag(int x, int y) {
        Point point = getPositionFromMouse(x, y);
        caretRow = point.y;
        caretCol = point.x;
        caretVisible = true;
        repaint();
    }

    public void handleDoubleClick(int x, int y) {
        if (lines.isEmpty()) return;

        Point p = getPositionFromMouse(x, y);
        selectWord(p.y, p.x);
    }

    private void selectWord(int row, int col) {
        StringBuilder line = lines.get(row);
        int length = line.length();

        if(length == 0) return;

        if(col >= length) col = length - 1;
        if(col < 0) col = 0;

        char ch = line.charAt(col);
        int start = col;
        int end = col;
        if(Character.isWhitespace(ch)) {
            while(start > 0 && Character.isWhitespace(line.charAt(start - 1))) {
                start--;
            }
            while(end < length && Character.isWhitespace(line.charAt(end))) {
                end++;
            }
        } else {
            while (start > 0 && !Character.isWhitespace(line.charAt(start - 1))) {
                start--;
            }
            while (end < length && !Character.isWhitespace(line.charAt(end))) {
                end++;
            }
        }

        anchorRow = row;
        anchorCol = start;
        caretRow = row;
        caretCol = end;

        caretVisible = true;
        repaint();
    }

    public void handleTripleClick(int x, int y) {
        if (lines.isEmpty()) return;

        Point p = getPositionFromMouse(x, y);
        int row = p.y;

        anchorRow = row;
        anchorCol = 0;
        caretRow = row;
        caretCol = lines.get(row).length();

        caretVisible = true;
        repaint();
    }


    public void insertChar(char ch) {
        lines.get(caretRow).insert(caretCol, ch);
        caretCol++;
        anchorRow = caretRow;
        anchorCol = caretCol;
        caretVisible = true;
        repaint();
    }

    public void backspace() {
        // deleting the selected text first
        if(!(anchorRow == caretRow && anchorCol == caretCol)) {
            deleteSelection();
            caretVisible = true;
            repaint();
            return;
        }

        // deleting a character
        if (caretCol > 0) {
            lines.get(caretRow).deleteCharAt(caretCol - 1);
            caretCol--;
        } else if (caretRow > 0) {  // line merging
            int prevLen = lines.get(caretRow - 1).length();
            lines.get(caretRow - 1).append(lines.get(caretRow));
            lines.remove(caretRow);
            caretRow--;
            caretCol = prevLen;
        }

        anchorRow = caretRow;
        anchorCol = caretCol;
        caretVisible = true;
        repaint();
    }

    public void deleteSelection() {
        int startRow = anchorRow;
        int startCol = anchorCol;
        int endRow = caretRow;
        int endCol = caretCol;

        if (startRow > endRow || (startRow == endRow && startCol > endCol)) {
            int tr = startRow, tc = startCol;
            startRow = endRow;
            startCol = endCol;
            endRow = tr;
            endCol = tc;
        }

        // selecting within the same lines
        if (startRow == endRow) {
            lines.get(startRow).delete(startCol, endCol);
        }
        // multiple lines
        else {
            StringBuilder firstLine = lines.get(startRow);
            StringBuilder lastLine = lines.get(endRow);

            firstLine.delete(startCol, firstLine.length());
            firstLine.append(lastLine.substring(endCol));

            if (endRow >= startRow + 1) {
                lines.subList(startRow + 1, endRow + 1).clear();
            }
        }

        caretRow = startRow;
        caretCol = startCol;
        anchorRow = caretRow;
        anchorCol = caretCol;

        caretVisible = true;
        repaint();
    }


    public void moveLeft() {
        if (caretCol > 0) {
            caretCol--;
            anchorCol = caretCol;
            caretVisible = true;
            repaint();
        }
    }

    public void moveRight() {
        if (caretCol < lines.get(caretRow).length()) {
            caretCol++;
            anchorCol = caretCol;
            caretVisible = true;
            repaint();
        }
    }

    public void moveUp() {
        if (caretRow > 0) {
            caretRow--;
            caretCol = Math.min(caretCol, lines.get(caretRow).length());
            anchorRow = caretRow;
            anchorCol = caretCol;
            caretVisible = true;
            repaint();
        }
    }

    public void moveDown() {
        if (caretRow < lines.size() - 1) {
            caretRow++;
            caretCol = Math.min(caretCol, lines.get(caretRow).length());
            anchorRow = caretRow;
            anchorCol = caretCol;
            caretVisible = true;
            repaint();
        }
    }

    public void shiftSelectLeft() {
        if (caretCol>0) {
            caretCol--;
            caretVisible = true;
            repaint();
        }
    }

    public void shiftSelectRight() {
        if (caretCol < lines.get(caretRow).length()) {
            caretCol++;
            caretVisible = true;
            repaint();
        }
    }

    public void insertNewLine() {
        StringBuilder currentLine = lines.get(caretRow);

        String rightPart = currentLine.substring(caretCol);
        currentLine.setLength(caretCol);
        lines.add(caretRow + 1, new StringBuilder(rightPart));

        caretRow++;
        caretCol = 0;
        anchorRow = caretRow;
        anchorCol = caretCol;

        caretVisible = true;
        repaint();
    }
}