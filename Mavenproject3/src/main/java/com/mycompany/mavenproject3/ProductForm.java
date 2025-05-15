package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ProductForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, priceField, stockField;
    private JComboBox<String> categoryField;
    private JButton saveButton, updateButton, deleteButton, updateBannerButton;
    private List<Product> products;
    private int selectedRow = -1;
    private Mavenproject3 mavenproject3;

    public ProductForm() {
        products = new ArrayList<>();
        products.add(new Product(1, "P001", "Americano", "Coffee", 18000, 10));
        products.add(new Product(2, "P002", "Pandan Latte", "Coffee", 15000, 8));
        
        initializeUI();
    }

    public void setMavenproject3Instance(Mavenproject3 instance) {
        this.mavenproject3 = instance;
    }

    private void initializeUI() {
        setTitle("WK. Cuan | Stok Barang");
        setSize(900, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        formPanel.add(new JLabel("Kode Barang:"));
        codeField = new JTextField();
        formPanel.add(codeField);
        
        formPanel.add(new JLabel("Nama Barang:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea", "Cocktail"});
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField();
        formPanel.add(priceField);
        
        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField();
        formPanel.add(stockField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        saveButton = new JButton("Tambah");
        updateButton = new JButton("Ubah");
        deleteButton = new JButton("Hapus");
        updateBannerButton = new JButton("Update Banner");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateBannerButton);
        
        formPanel.add(new JLabel());
        formPanel.add(buttonPanel);
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Kategori", "Harga Jual", "Stok"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        drinkTable = new JTable(tableModel);
        drinkTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        drinkTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && drinkTable.getSelectedRow() != -1) {
                selectedRow = drinkTable.getSelectedRow();
                populateFormFromTable(selectedRow);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(drinkTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        loadProductData();
        
        saveButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        updateBannerButton.addActionListener(e -> updateBanner());
        
        add(mainPanel);
    }

    private void updateBanner() {
        StringBuilder bannerText = new StringBuilder(" Menu yang tersedia: ");
        for (Product product : products) {
            bannerText.append(product.getName()).append(" | ");
        }
        
        // Hapus " | " terakhir
        if (bannerText.length() > 4) {
            bannerText.setLength(bannerText.length() - 3);
        }
        
        if (mavenproject3 != null) {
            mavenproject3.updateBannerText(bannerText.toString());
            JOptionPane.showMessageDialog(this, "Banner berhasil diupdate!");
        } else {
            JOptionPane.showMessageDialog(this, "Tidak dapat mengupdate banner", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ... (method-method lainnya tetap sama seperti sebelumnya)
    private void loadProductData() {
        tableModel.setRowCount(0);
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                product.getCode(), 
                product.getName(), 
                product.getCategory(), 
                product.getPrice(), 
                product.getStock()
            });
        }
    }
    
    private void populateFormFromTable(int row) {
        codeField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        categoryField.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        priceField.setText(tableModel.getValueAt(row, 3).toString());
        stockField.setText(tableModel.getValueAt(row, 4).toString());
    }
    
    private void addProduct() {
        try {
            String code = codeField.getText();
            String name = nameField.getText();
            String category = (String) categoryField.getSelectedItem();
            int price = Integer.parseInt(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            
            int newId = products.isEmpty() ? 1 : products.get(products.size()-1).getId() + 1;
            
            Product newProduct = new Product(newId, code, name, category, price, stock);
            products.add(newProduct);
            
            tableModel.addRow(new Object[]{code, name, category, price, stock});
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang akan diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String code = codeField.getText();
            String name = nameField.getText();
            String category = (String) categoryField.getSelectedItem();
            int price = Integer.parseInt(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            
            Product productToUpdate = products.get(selectedRow);
            productToUpdate.setCode(code);
            productToUpdate.setName(name);
            productToUpdate.setCategory(category);
            productToUpdate.setPrice(price);
            productToUpdate.setStock(stock);
            
            tableModel.setValueAt(code, selectedRow, 0);
            tableModel.setValueAt(name, selectedRow, 1);
            tableModel.setValueAt(category, selectedRow, 2);
            tableModel.setValueAt(price, selectedRow, 3);
            tableModel.setValueAt(stock, selectedRow, 4);
            
            clearForm();
            JOptionPane.showMessageDialog(this, "Produk berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProduct() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Apakah Anda yakin ingin menghapus produk ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            products.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            clearForm();
            selectedRow = -1;
            JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!");
        }
    }
    
    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        categoryField.setSelectedIndex(0);
        priceField.setText("");
        stockField.setText("");
        drinkTable.clearSelection();
        selectedRow = -1;
    }
}