package com.ntankard.Tracking.Util.Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageJPanel extends JPanel {

    /**
     * The image to display
     */
    private ImageIcon baseImage;

    /**
     * The resize tracker
     */
    private Future<Integer> resizeWorkerResult;

    /**
     * The main image
     */
    private JLabel imageLabel = new JLabel();

    /**
     * Constructor
     *
     * @param baseImage The image to display
     */
    public ImageJPanel(ImageIcon baseImage) {
        this.baseImage = baseImage;
        createUIComponents();
    }

    /**
     * Constructor
     */
    public ImageJPanel() {
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();

        if (baseImage != null) {
            imageLabel.setIcon(new ImageIcon(baseImage.getImage().getScaledInstance(500, -1, Image.SCALE_DEFAULT)));
        }
        this.add(imageLabel);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (resizeWorkerResult == null) {
                    resizeWorkerResult = new ResizeWorker().resize();
                }
            }
        });
    }

    /**
     * Set the base image
     *
     * @param baseImage The image to set
     */
    public void setBaseImage(ImageIcon baseImage) {
        this.baseImage = baseImage;
        doResize();
    }

    /**
     * Resize the image to fill the panel
     */
    private void doResize() {
        if (baseImage != null) {
            double heightRatio = (double) baseImage.getIconHeight() / this.getSize().getHeight();
            double widthRatio = (double) baseImage.getIconWidth() / this.getSize().getWidth();

            if (heightRatio > widthRatio) {
                imageLabel.setIcon(new ImageIcon(baseImage.getImage().getScaledInstance(-1, (int) this.getSize().getHeight() - 10, Image.SCALE_SMOOTH)));
            } else {
                imageLabel.setIcon(new ImageIcon(baseImage.getImage().getScaledInstance((int) this.getSize().getWidth() - 10, -1, Image.SCALE_SMOOTH)));
            }
        } else {
            imageLabel.setIcon(null);
        }
    }

    /**
     * Worker thread to resize the image after a small pause
     */
    private class ResizeWorker {

        private ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> resize() {
            return executor.submit(() -> {
                Thread.sleep(100);
                doResize();
                resizeWorkerResult = null;
                return -1;
            });
        }
    }
}
