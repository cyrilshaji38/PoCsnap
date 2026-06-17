package com.pocsnap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DrawingOverlayPanel extends JPanel {
    private List<Rectangle> boxes = new ArrayList<>();
    private final Stack<List<Rectangle>> historyStack = new Stack<>();
    private final Stack<List<Rectangle>> redoStack = new Stack<>();
    private Point startPoint = null;
    private Point currentPoint = null;
    private boolean isDrawingActive = false;

    public DrawingOverlayPanel() {
        setOpaque(false);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isDrawingActive) return;
                startPoint = e.getPoint();
                currentPoint = startPoint;
                repaint();
                e.consume();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isDrawingActive) return;
                currentPoint = e.getPoint();
                repaint();
                e.consume();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isDrawingActive) return;
                if (startPoint != null && currentPoint != null) {
                    Rectangle rect = getPointsToRectangle(startPoint, currentPoint);
                    if (rect.width > 4 && rect.height > 4) {
                        saveToHistory();
                        boxes.add(rect);
                        redoStack.clear();
                    }
                }
                startPoint = null;
                currentPoint = null;
                repaint();
                e.consume();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void saveToHistory() {
        historyStack.push(new ArrayList<>(boxes));
    }

    public void setDrawingActive(boolean active) {
        this.isDrawingActive = active;
        setCursor(active ? Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR) : Cursor.getDefaultCursor());
    }

    public boolean isDrawingActive() {
        return this.isDrawingActive;
    }

    public void clearBoxes() {
        if (!boxes.isEmpty()) {
            saveToHistory();
            boxes.clear();
            redoStack.clear();
            repaint();
        }
    }

    public void undo() {
        if (!historyStack.isEmpty()) {
            redoStack.push(new ArrayList<>(boxes));
            boxes = historyStack.pop();
            repaint();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            saveToHistory();
            boxes = redoStack.pop();
            repaint();
        }
    }

    private Rectangle getPointsToRectangle(Point p1, Point p2) {
        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int width = Math.abs(p1.x - p2.x);
        int height = Math.abs(p1.y - p2.y);
        return new Rectangle(x, y, width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2.5f));

        for (Rectangle rect : boxes) {
            g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
        }

        if (startPoint != null && currentPoint != null) {
            Rectangle currentRect = getPointsToRectangle(startPoint, currentPoint);
            g2d.drawRect(currentRect.x, currentRect.y, currentRect.width, currentRect.height);
        }
    }
}