package edu.bbte.idde.vnim2413.presentation;

import edu.bbte.idde.vnim2413.model.UsedCarListing;
import edu.bbte.idde.vnim2413.repository.RepositoryException;
import edu.bbte.idde.vnim2413.service.CarListingServiceInterface;
import edu.bbte.idde.vnim2413.service.ServiceFactory;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class CarListingSwing extends JFrame {
    private final JTable carTable;
    private final CarListingServiceInterface carListingService = ServiceFactory.getUserService();
    private final JTextField makeField;
    private final JTextField modelField;
    private final JTextField yearField;
    private final JTextField priceField;
    private final JTextField dateField;

    public CarListingSwing() {
        super();
        setTitle("Used Car Listings");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        String[] columnNames = {"ID", "Make", "Model", "Year", "Price", "Upload Date"};
        DefaultTableModel tableModel = new NonEditableIdTableModel(columnNames, 0);
        carTable = new JTable(tableModel);
        carTable.getModel().addTableModelListener(this::editSelectedCar);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        makeField = new JTextField();
        modelField = new JTextField();
        yearField = new JTextField();
        priceField = new JTextField();
        dateField = new JTextField();

        inputPanel.add(new JLabel("Make"));
        inputPanel.add(makeField);
        inputPanel.add(new JLabel("Model"));
        inputPanel.add(modelField);
        inputPanel.add(new JLabel("Year"));
        inputPanel.add(yearField);
        inputPanel.add(new JLabel("Price"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Upload Date"));
        inputPanel.add(dateField);

        add(inputPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Car");
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addCar());
        refreshButton.addActionListener(e -> refreshTable());
        deleteButton.addActionListener(e -> deleteSelectedCar());

        setVisible(true);
        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        model.setRowCount(0);

        Collection<UsedCarListing> cars = carListingService.getAllCarListings();
        for (UsedCarListing car : cars) {
            model.addRow(new Object[]{
                    car.getId(),
                    car.getMake(),
                    car.getModel(),
                    car.getFabricationYear(),
                    car.getPrice(),
                    car.getUploadDate()
            });
        }
    }

    private void deleteSelectedCar() {
        int row = carTable.getSelectedRow();
        if (row < 0) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        try {
            long id = (long) model.getValueAt(row, 0);
            carListingService.delete(id);
            refreshTable();
        } catch (RepositoryException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting car: " + ex.getMessage(),
                    "Deleting failed", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting car: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        }
    }

    private void addCar() {
        try {
            String make = makeField.getText();
            String model = modelField.getText();
            int year = Integer.parseInt(yearField.getText());
            double price = Double.parseDouble(priceField.getText());
            String date = dateField.getText();

            UsedCarListing newCar = new UsedCarListing(make, model, year, price, date);
            carListingService.register(newCar);
            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,  "Year and price must be numeric values.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (RepositoryException ex) {
            JOptionPane.showMessageDialog(this, "Repository exception: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCarField(UsedCarListing car, int column, Object newValue) {
        String value = newValue.toString();
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Field cannot be empty!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (column) {
            case 1 -> car.setMake(newValue.toString());
            case 2 -> car.setModel(newValue.toString());
            case 3 -> car.setFabricationYear(Integer.parseInt(newValue.toString()));
            case 4 -> car.setPrice(Double.parseDouble(newValue.toString()));
            case 5 -> car.setUploadDate(newValue.toString());
            default -> {

            }
        }
    }

    private void editSelectedCar(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();

        if (row < 0 || column < 0) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        try {
            long id = (long) model.getValueAt(row, 0);
            UsedCarListing car = carListingService.getById(id);
            updateCarField(car, column, model.getValueAt(row, column));
            carListingService.update(car);

        } catch (RepositoryException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating car: " + ex.getMessage(),
                    "Update failed", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        } catch (NumberFormatException | ClassCastException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid data type: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        }

    }

    private void clearFields() {
        makeField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        dateField.setText("");
    }

    private static class NonEditableIdTableModel extends DefaultTableModel {
        public NonEditableIdTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            // Make the ID column  non-editable
            return column != 0;
        }
    }
}


