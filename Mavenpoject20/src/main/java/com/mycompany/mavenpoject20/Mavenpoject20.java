package com.mycompany.mavenpoject20;

import javax.swing.*;
import java.awt.*;

public class Mavenpoject20 extends JFrame implements Runnable {
    private String text;
    private int x;
    private int width;
    private BannerPanel bannerPanel;
    private JButton addProductButton;
    private static Mavenpoject20 instance;

    public Mavenpoject20(String text) {
        this.text = text;
        instance = this;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        bottomPanel.add(addProductButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        addProductButton.addActionListener(e -> {
            ProductForm productForm = new ProductForm();
            productForm.setMavenpoject20Instance(this);
            productForm.setVisible(true);
        });

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void updateBannerText(String newText) {
        this.text = newText;
        bannerPanel.repaint();
    }

    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(text, x, getHeight() / 2);
        }
    }

    @Override
    public void run() {
        width = getWidth();
        while (true) {
            text = text.substring(1) + text.charAt(0);
            bannerPanel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static Mavenpoject20 getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Mavenpoject20("Menu yang tersedia: Americano | Pandan Latte | Aren Latte | Matcha Frappucino | Jus Apel");
    }
}