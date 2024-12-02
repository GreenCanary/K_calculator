package com.mike.kcl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create material objects
        Liquid liquid = new Liquid();
        SolidMaterial solidMaterial = new SolidMaterial(1000);
        LiquidMaterial liquidMaterial = new LiquidMaterial(0.8);

        // Main Layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));

        // Sections
        layout.getChildren().addAll(
                createSection("Жидкость", createLiquidSection(liquid)),
                createSection("Руда", createSolidMaterialSection(solidMaterial)),
                createSection("Маточник", createLiquidMaterialSection(liquidMaterial, solidMaterial))
        );

        // ScrollPane for better navigation
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        // Scene and Stage setup
        Scene scene = new Scene(scrollPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Расчет");
        primaryStage.show();
    }

    private TitledPane createSection(String title, GridPane content) {
        TitledPane section = new TitledPane();
        section.setText(title);
        section.setContent(content);
        section.setCollapsible(false);
        return section;
    }

    private GridPane createLiquidSection(Liquid liquid) {
        GridPane grid = createAlignedGridPane();

        Label qLabel = new Label("Кол-во всего (Q):");
        TextField qInput = new TextField();
        Button calculateButton = new Button("Ввести кол-во воды");
        Label h2oResultLabel = new Label("H2O кол-во:");
        Label h2oResultValue = new Label();

        grid.addRow(0, qLabel, qInput);
        grid.add(calculateButton, 0, 1, 2, 1);
        grid.addRow(2, h2oResultLabel, h2oResultValue);

        calculateButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());
                BigDecimal h2oAmount = q; // Assuming all Q is water
                liquid.setH2O(h2oAmount);
                liquid.setQ(q);
                h2oResultValue.setText(h2oAmount.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Введите корректное значение для Q.");
            }
        });

        return grid;
    }

    private GridPane createSolidMaterialSection(SolidMaterial solidMaterial) {
        GridPane grid = createAlignedGridPane();

        Label qLabel = new Label("Кол-во всего (Q):");
        TextField qInput = new TextField();
        Button calculateButton = new Button("Расчитать состав руды");
        Label kclResultLabel = new Label("KCl кол-во:");
        Label naclResultLabel = new Label("NaCl кол-во:");
        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label wasteResultLabel = new Label("Н.О. кол-во:");
        Label kclResultValue = new Label();
        Label naclResultValue = new Label();
        Label caso4ResultValue = new Label();
        Label wasteResultValue = new Label();

        grid.addRow(0, qLabel, qInput);
        grid.add(calculateButton, 0, 1, 2, 1);
        grid.addRow(2, kclResultLabel, kclResultValue);
        grid.addRow(3, naclResultLabel, naclResultValue);
        grid.addRow(4, caso4ResultLabel, caso4ResultValue);
        grid.addRow(5, wasteResultLabel, wasteResultValue);

        calculateButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());
                solidMaterial.setS_Q(q);

                BigDecimal kclAmount = q.multiply(BigDecimal.valueOf(0.91));
                BigDecimal naclAmount = q.multiply(BigDecimal.valueOf(0.061));
                BigDecimal caso4Amount = q.multiply(BigDecimal.valueOf(0.036));
                BigDecimal wasteAmount = q.multiply(BigDecimal.valueOf(0.02));

                solidMaterial.setSolidKCl(kclAmount);
                solidMaterial.setSolidNaCl(naclAmount);
                solidMaterial.setSolidCaSO4(caso4Amount);
                solidMaterial.setSolidWaste(wasteAmount);

                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                caso4ResultValue.setText(caso4Amount.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(wasteAmount.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Введите корректное значение для Q.");
            }
        });

        return grid;
    }

    private GridPane createLiquidMaterialSection(LiquidMaterial liquidMaterial, SolidMaterial solidMaterial) {
        GridPane grid = createAlignedGridPane();

        Label ratioLabel = new Label("Ж/Т:");
        TextField ratioInput = new TextField();
        Button calculateButton = new Button("Расчитать маточник");
        Label liquidQResultLabel = new Label("Маточник кол-во (Q):");
        Label h2oResultLabel = new Label("H2O кол-во:");
        Label kclResultLabel = new Label("KCl кол-во:");
        Label naclResultLabel = new Label("NaCl кол-во:");
        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label liquidQResultValue = new Label();
        Label h2oResultValue = new Label();
        Label kclResultValue = new Label();
        Label naclResultValue = new Label();
        Label caso4ResultValue = new Label();

        grid.addRow(0, ratioLabel, ratioInput);
        grid.add(calculateButton, 0, 1, 2, 1);
        grid.addRow(2, liquidQResultLabel, liquidQResultValue);
        grid.addRow(3, h2oResultLabel, h2oResultValue);
        grid.addRow(4, kclResultLabel, kclResultValue);
        grid.addRow(5, naclResultLabel, naclResultValue);
        grid.addRow(6, caso4ResultLabel, caso4ResultValue);

        calculateButton.setOnAction(event -> {
            try {
                BigDecimal ratio = new BigDecimal(ratioInput.getText());
                BigDecimal solidQ = solidMaterial.getS_Q();
                BigDecimal liquidQ = solidQ.multiply(ratio);

                BigDecimal h2OAmount = liquidQ.multiply(BigDecimal.valueOf(0.678));
                BigDecimal kclAmount = liquidQ.multiply(BigDecimal.valueOf(0.121));
                BigDecimal naclAmount = liquidQ.multiply(BigDecimal.valueOf(0.197));
                BigDecimal caso4Amount = liquidQ.multiply(BigDecimal.valueOf(0.004));

                liquidMaterial.setL_Q(liquidQ);
                liquidMaterial.setLiquidH2O(h2OAmount);
                liquidMaterial.setLiquidKCl(kclAmount);
                liquidMaterial.setLiquidNaCl(naclAmount);
                liquidMaterial.setLiquidCaSO4(caso4Amount);

                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                h2oResultValue.setText(h2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                caso4ResultValue.setText(caso4Amount.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Введите корректное значение для Ж/Т.");
            }
        });

        return grid;
    }

    private GridPane createAlignedGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(150); // Fixed width for labels

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // Allow TextField to grow

        grid.getColumnConstraints().addAll(col1, col2);
        return grid;
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
