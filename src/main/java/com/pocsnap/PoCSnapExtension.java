package com.pocsnap;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class PoCSnapExtension implements BurpExtension {
    private MontoyaApi api;
    private HttpRequestEditor requestEditor;
    private HttpResponseEditor responseEditor;
    private DrawingOverlayPanel screenshotCanvas;
    private JLayeredPane layeredWorkspace;
    private JPanel mainContainer;
    private JPanel leftButtonsPanel;
    private JLabel targetHostLabel;
    private JLabel metricsLabel;

    private boolean isTabActive = false;
    private JPanel mainTabPanel;

    private JButton clearButton;
    private JButton undoButton;
    private JButton redoButton;

    // Configuration constants
    private static final String EXTENSION_NAME = "PoCsnap";
    private static final String DEFAULT_METRICS_TEXT = "-- bytes";
    private static final String ZERO_METRICS_TEXT = "0 bytes";

    // UPDATED: Standardized to the plain solid black circle symbol
    private static final String NOTIFICATION_DOT_TEXT = "PoCsnap ●";

    @Override
    public void initialize(MontoyaApi montoyaApi) {
        this.api = montoyaApi;
        api.extension().setName(EXTENSION_NAME);

        SwingUtilities.invokeLater(() -> {
            requestEditor = api.userInterface().createHttpRequestEditor();
            responseEditor = api.userInterface().createHttpResponseEditor();

            mainTabPanel = createTabPanel();
            api.userInterface().registerSuiteTab(EXTENSION_NAME, mainTabPanel);

            mainTabPanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    isTabActive = true;
                    updateTabTitle(EXTENSION_NAME);
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                    isTabActive = false;
                }
            });
        });

        api.userInterface().registerContextMenuItemsProvider(new ContextMenuItemsProvider() {
            @Override
            public List<Component> provideMenuItems(ContextMenuEvent event) {
                if (event.messageEditorRequestResponse().isEmpty()) {
                    return Collections.emptyList();
                }

                var requestResponseOpt = event.messageEditorRequestResponse().get().requestResponse();
                if (requestResponseOpt == null) {
                    return Collections.emptyList();
                }

                JMenuItem sendItem = new JMenuItem("Send to " + EXTENSION_NAME);
                sendItem.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                    try {
                        requestEditor.setRequest(requestResponseOpt.request());

                        HttpResponse response = requestResponseOpt.response();
                        if (response != null) {
                            responseEditor.setResponse(response);
                            int responseBytes = response.toByteArray().length();
                            metricsLabel.setText(String.format("%,d bytes", responseBytes));
                        } else {
                            responseEditor.setResponse(null);
                            metricsLabel.setText(ZERO_METRICS_TEXT);
                        }

                        HttpService service = requestResponseOpt.httpService();
                        if (service != null) {
                            String protocol = service.secure() ? "https://" : "http://";
                            String host = service.host();
                            int port = service.port();

                            String fullUrl = protocol + host;
                            if ((service.secure() && port != 443) || (!service.secure() && port != 80)) {
                                fullUrl += ":" + port;
                            }
                            targetHostLabel.setText("Target: " + fullUrl);
                        } else {
                            targetHostLabel.setText("Target: Unknown");
                        }

                        screenshotCanvas.clearBoxes();

                        if (!isTabActive) {
                            updateTabTitle(NOTIFICATION_DOT_TEXT);
                        }
                    } catch (Exception ex) {
                        api.logging().logToError("Error processing context menu item selection: " + ex.getMessage());
                    }
                }));

                return List.of(sendItem);
            }
        });
    }

    private void updateTabTitle(String title) {
        SwingUtilities.invokeLater(() -> {
            if (mainTabPanel == null) return;
            Component parent = mainTabPanel.getParent();
            while (parent != null) {
                if (parent instanceof JTabbedPane) {
                    JTabbedPane tabbedPane = (JTabbedPane) parent;
                    int index = tabbedPane.indexOfComponent(mainTabPanel);
                    if (index != -1) {
                        tabbedPane.setTitleAt(index, title);
                    }
                    break;
                }
                parent = parent.getParent();
            }
        });
    }

    private JPanel createTabPanel() {
        mainContainer = new JPanel(new BorderLayout());

        JSplitPane textSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestEditor.uiComponent(), responseEditor.uiComponent());
        textSplitPane.setResizeWeight(0.5);
        textSplitPane.setDividerLocation(0.5);

        metricsLabel = new JLabel(DEFAULT_METRICS_TEXT);
        metricsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        metricsLabel.setForeground(Color.DARK_GRAY);

        JPanel bottomTrayPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomTrayPanel.setBorder(new EmptyBorder(2, 5, 5, 10));
        bottomTrayPanel.add(metricsLabel);

        JPanel innerContentPanel = new JPanel(new BorderLayout());
        innerContentPanel.add(textSplitPane, BorderLayout.CENTER);
        innerContentPanel.add(bottomTrayPanel, BorderLayout.SOUTH);

        screenshotCanvas = new DrawingOverlayPanel();
        screenshotCanvas.setVisible(false);

        layeredWorkspace = new JLayeredPane();
        layeredWorkspace.add(innerContentPanel, JLayeredPane.DEFAULT_LAYER);
        layeredWorkspace.add(screenshotCanvas, JLayeredPane.PALETTE_LAYER);

        layeredWorkspace.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle bounds = new Rectangle(0, 0, layeredWorkspace.getWidth(), layeredWorkspace.getHeight());
                innerContentPanel.setBounds(bounds);
                screenshotCanvas.setBounds(bounds);
                innerContentPanel.revalidate();
            }
        });

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        leftButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JToggleButton drawModeButton = new JToggleButton("Draw Mode: OFF");
        JButton snapButton = new JButton("Capture PoC Screenshot");

        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        clearButton = new JButton("Clear Canvas");

        clearButton.setEnabled(false);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        leftButtonsPanel.add(drawModeButton);
        leftButtonsPanel.add(snapButton);
        leftButtonsPanel.add(undoButton);
        leftButtonsPanel.add(redoButton);
        leftButtonsPanel.add(clearButton);

        targetHostLabel = new JLabel("Target: Send a request to begin...");
        targetHostLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        targetHostLabel.setForeground(Color.BLACK);

        targetHostLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        JPanel rightTargetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightTargetPanel.add(targetHostLabel);

        headerPanel.add(leftButtonsPanel, BorderLayout.WEST);
        headerPanel.add(rightTargetPanel, BorderLayout.EAST);

        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(layeredWorkspace, BorderLayout.CENTER);

        drawModeButton.addActionListener(e -> {
            boolean selected = drawModeButton.isSelected();

            if (selected) {
                if (requestEditor.getRequest() != null) {
                    requestEditor.setRequest(requestEditor.getRequest());
                }
                if (responseEditor.getResponse() != null) {
                    responseEditor.setResponse(responseEditor.getResponse());
                }
            }

            screenshotCanvas.setDrawingActive(selected);
            screenshotCanvas.setVisible(selected);
            drawModeButton.setText(selected ? "Draw Mode: ON" : "Draw Mode: OFF");

            clearButton.setEnabled(selected);
            undoButton.setEnabled(selected);
            redoButton.setEnabled(selected);

            layeredWorkspace.repaint();
        });

        snapButton.addActionListener(e -> capturePerfectLayoutScreenshot());
        clearButton.addActionListener(e -> screenshotCanvas.clearBoxes());
        undoButton.addActionListener(e -> screenshotCanvas.undo());
        redoButton.addActionListener(e -> screenshotCanvas.redo());

        return mainContainer;
    }

    private void capturePerfectLayoutScreenshot() {
        screenshotCanvas.setVisible(true);
        leftButtonsPanel.setVisible(false);

        mainContainer.revalidate();
        mainContainer.repaint();

        BufferedImage image = new BufferedImage(mainContainer.getWidth(), mainContainer.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        mainContainer.paint(g2d);
        g2d.dispose();

        leftButtonsPanel.setVisible(true);
        if (!screenshotCanvas.isDrawingActive()) {
            screenshotCanvas.setVisible(false);
        }

        mainContainer.revalidate();
        mainContainer.repaint();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PoC Screenshot As...");
        fileChooser.setSelectedFile(new File("poc_screenshot.png"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
            }
            try {
                ImageIO.write(image, "PNG", fileToSave);
                JOptionPane.showMessageDialog(null, "Screenshot exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                api.logging().logToError("Failed saving screenshot: " + ex.getMessage());
            }
        }
    }
}