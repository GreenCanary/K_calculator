package com.mike.kcl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.scene.control.ScrollPane;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
// Create liquid and solid material objects
        Liquid liquid = new Liquid();
        SolidMaterial solidMaterial = new SolidMaterial(1000);
        LiquidMaterial liquidMaterial = new LiquidMaterial(0.8);

// Layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));

// Liquid Section
        Label liquidLabel = new Label("Жидкость");
        liquidLabel.setFont(new Font(18));
        GridPane liquidGrid = createLiquidSection(liquid, liquidLabel);

// Solid Material Section
        Label solidMaterialLabel = new Label("Руда");
        solidMaterialLabel.setFont(new Font(18));
        GridPane solidMaterialGrid = createSolidMaterialSection(solidMaterial, solidMaterialLabel);

// Liquid Material Section
        Label liquidMaterialLabel = new Label("Маточник");
        liquidMaterialLabel.setFont(new Font(18));
        GridPane liquidMaterialGrid = createLiquidMaterialSection(liquidMaterial, liquidMaterialLabel);

// Combine all sections
        layout.getChildren().addAll(liquidLabel, liquidGrid, solidMaterialLabel, solidMaterialGrid, liquidMaterialLabel, liquidMaterialGrid);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);

// Create a scene and display the stage
        Scene scene = new Scene(scrollPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Расчет");
        primaryStage.show();
    }

    private GridPane createLiquidSection(Liquid liquid, Label liquidLabel) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

// Input Section for Liquid
        Label qLabel = new Label("Кол-во жидкости (Q):");
        TextField qInput = new TextField();
        grid.add(qLabel, 0, 0);
        grid.add(qInput, 1, 0);

        Label h2oLabel = new Label("H2O (%):");
        TextField h2oInput = new TextField();
        grid.add(h2oLabel, 0, 1);
        grid.add(h2oInput, 1, 1);

        Label kclLabel = new Label("KCl (%):");
        TextField kclInput = new TextField();
        grid.add(kclLabel, 0, 2);
        grid.add(kclInput, 1, 2);

        Label naclLabel = new Label("NaCl (%):");
        TextField naclInput = new TextField();
        grid.add(naclLabel, 0, 3);
        grid.add(naclInput, 1, 3);

        Label caso4Label = new Label("CaSO4 (%):");
        TextField caso4Input = new TextField();
        grid.add(caso4Label, 0, 4);
        grid.add(caso4Input, 1, 4);

// Button to calculate results
        Button calculateLiquidButton = new Button("Расчитать состав жидкости");
        grid.add(calculateLiquidButton, 1, 5);

// Results Section for Liquid
        Label resultsLabel = new Label("Результат:");
        grid.add(resultsLabel, 0, 6, 2, 1);

        Label h2oResultLabel = new Label("H2O кол-во:");
        Label h2oResultValue = new Label();
        grid.add(h2oResultLabel, 0, 7);
        grid.add(h2oResultValue, 1, 7);

        Label kclResultLabel = new Label("KCl кол-во:");
        Label kclResultValue = new Label();
        grid.add(kclResultLabel, 0, 8);
        grid.add(kclResultValue, 1, 8);

        Label naclResultLabel = new Label("NaCl кол-во:");
        Label naclResultValue = new Label();
        grid.add(naclResultLabel, 0, 9);
        grid.add(naclResultValue, 1, 9);

        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label caso4ResultValue = new Label();
        grid.add(caso4ResultLabel, 0, 10);
        grid.add(caso4ResultValue, 1, 10);

        // Calculate Liquid
        calculateLiquidButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());
                BigDecimal h2oPercentage = new BigDecimal(h2oInput.getText());
                BigDecimal kclPercentage = new BigDecimal(kclInput.getText());
                BigDecimal naclPercentage = new BigDecimal(naclInput.getText());
                BigDecimal caso4Percentage = new BigDecimal(caso4Input.getText());

                BigDecimal totalPercentage = h2oPercentage.add(kclPercentage).add(naclPercentage).add(caso4Percentage);
                if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new IllegalArgumentException("Сумма не равна 100%");
                }

                BigDecimal h2oAmount = q.multiply(h2oPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal kclAmount = q.multiply(kclPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal naclAmount = q.multiply(naclPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal caso4Amount = q.multiply(caso4Percentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                liquid.setH2O(h2oAmount);
                liquid.setKCl(kclAmount);
                liquid.setNaCl(naclAmount);
                liquid.setCaSO4(caso4Amount);
                liquid.setQ(q);

                h2oResultValue.setText(h2oAmount.setScale(2, RoundingMode.HALF_UP).toString());
                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                caso4ResultValue.setText(caso4Amount.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        return grid;
    }


    private GridPane createSolidMaterialSection(SolidMaterial solidMaterial, Label solidMaterialLabel) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        // Input Section for Solid Material
        Label qLabel = new Label("Руда кол-во (Q):");
        TextField qInput = new TextField();
        grid.add(qLabel, 0, 0);
        grid.add(qInput, 1, 0);

        Label kclLabel = new Label("KCl (%):");
        TextField kclInput = new TextField();
        grid.add(kclLabel, 0, 1);
        grid.add(kclInput, 1, 1);

        Label naclLabel = new Label("NaCl (%):");
        TextField naclInput = new TextField();
        grid.add(naclLabel, 0, 2);
        grid.add(naclInput, 1, 2);

        Label caso4Label = new Label("CaSO4 (%):");
        TextField caso4Input = new TextField();
        grid.add(caso4Label, 0, 3);
        grid.add(caso4Input, 1, 3);

        Label wasteLabel = new Label("Н.О. (%):");
        TextField wasteInput = new TextField();
        grid.add(wasteLabel, 0, 4);
        grid.add(wasteInput, 1, 4);

        // Button to calculate solid material
        Button calculateSolidButton = new Button("Расчитать состав руды");
        grid.add(calculateSolidButton, 1, 5);

        // Results Section for Solid Material
        Label resultsLabel = new Label("Результат:");
        grid.add(resultsLabel, 0, 6, 2, 1);

        Label kclResultLabel = new Label("KCl кол-во:");
        Label kclResultValue = new Label();
        grid.add(kclResultLabel, 0, 7);
        grid.add(kclResultValue, 1, 7);

        Label naclResultLabel = new Label("NaCl кол-во:");
        Label naclResultValue = new Label();
        grid.add(naclResultLabel, 0, 8);
        grid.add(naclResultValue, 1, 8);

        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label caso4ResultValue = new Label();
        grid.add(caso4ResultLabel, 0, 9);
        grid.add(caso4ResultValue, 1, 9);

        Label wasteResultLabel = new Label("Н.О. кол-во:");
        Label wasteResultValue = new Label();
        grid.add(wasteResultLabel, 0, 10);
        grid.add(wasteResultValue, 1, 10);




        // Calculate Solid Material Logic
        calculateSolidButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());
                BigDecimal kclPercentage = new BigDecimal(kclInput.getText());
                BigDecimal naclPercentage = new BigDecimal(naclInput.getText());
                BigDecimal caso4Percentage = new BigDecimal(caso4Input.getText());
                BigDecimal wastePercentage = new BigDecimal(wasteInput.getText());

                BigDecimal totalPercentage = kclPercentage.add(naclPercentage).add(caso4Percentage).add(wastePercentage);
                if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new IllegalArgumentException("Сумма не равна 100%");
                }

                BigDecimal kclAmount = q.multiply(kclPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal naclAmount = q.multiply(naclPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal caso4Amount = q.multiply(caso4Percentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal wasteAmount = q.multiply(wastePercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                solidMaterial.setSolidKCl(kclAmount);
                solidMaterial.setSolidNaCl(naclAmount);
                solidMaterial.setSolidCaSO4(caso4Amount);
                solidMaterial.setSolidWaste(wasteAmount);
                solidMaterial.setS_Q(q);

                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                caso4ResultValue.setText(caso4Amount.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(wasteAmount.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        return grid;
    }
    private GridPane createLiquidMaterialSection(LiquidMaterial liquidMaterial, Label liquidMaterialLabel) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        // Input Section for Liquid Material
        Label liqSolRatLabel = new Label("Ж/Т:");
        TextField liqSolRatInput = new TextField();
        grid.add(liqSolRatLabel, 0, 0);
        grid.add(liqSolRatInput, 1, 0);

        Label h2OLabel = new Label("H2O (%):");
        TextField h2OInput = new TextField();
        grid.add(h2OLabel, 0, 1);
        grid.add(h2OInput, 1, 1);

        Label kclLabel = new Label("KCl (%):");
        TextField kclInput = new TextField();
        grid.add(kclLabel, 0, 2);
        grid.add(kclInput, 1, 2);

        Label naclLabel = new Label("NaCl (%):");
        TextField naclInput = new TextField();
        grid.add(naclLabel, 0, 3);
        grid.add(naclInput, 1, 3);

        Label caso4Label = new Label("CaSO4 (%):");
        TextField caso4Input = new TextField();
        grid.add(caso4Label, 0, 4);
        grid.add(caso4Input, 1, 4);

        // Button to calculate liquid material
        Button calculateLiquidButton = new Button("Расчитать маточник");
        grid.add(calculateLiquidButton, 1, 5);

        // Results Section for Liquid Material
        Label resultsLabel = new Label("Результат:");
        grid.add(resultsLabel, 0, 6, 2, 1);

        Label liquidQResultLabel = new Label("Маточник кол-во (Q):");
        Label liquidQResultValue = new Label();
        grid.add(liquidQResultLabel, 0, 7);
        grid.add(liquidQResultValue, 1, 7);

        Label wasteResultLabel = new Label("H2O кол-во:");
        Label wasteResultValue = new Label();
        grid.add(wasteResultLabel, 0, 8);
        grid.add(wasteResultValue, 1, 8);

        Label kclResultLabel = new Label("KCl кол-во:");
        Label kclResultValue = new Label();
        grid.add(kclResultLabel, 0, 9);
        grid.add(kclResultValue, 1, 9);

        Label naclResultLabel = new Label("NaCl кол-во:");
        Label naclResultValue = new Label();
        grid.add(naclResultLabel, 0, 10);
        grid.add(naclResultValue, 1, 10);

        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label caso4ResultValue = new Label();
        grid.add(caso4ResultLabel, 0, 11);
        grid.add(caso4ResultValue, 1, 11);

        // Calculate Liquid Material Logic
        calculateLiquidButton.setOnAction(event -> {
            try {
                BigDecimal liqSolRat = new BigDecimal(liqSolRatInput.getText());
                BigDecimal solidMaterialQ = SolidMaterial.getS_Q();
                BigDecimal q = solidMaterialQ.multiply(liqSolRat);

                BigDecimal h2OPercentage = new BigDecimal(h2OInput.getText());
                BigDecimal kclPercentage = new BigDecimal(kclInput.getText());
                BigDecimal naclPercentage = new BigDecimal(naclInput.getText());
                BigDecimal caso4Percentage = new BigDecimal(caso4Input.getText());

                BigDecimal totalPercentage = h2OPercentage.add(kclPercentage).add(naclPercentage).add(caso4Percentage);
                if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new IllegalArgumentException("Сумма не равна 100%");
                }

                BigDecimal h2OAmount = q.multiply(h2OPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal kclAmount = q.multiply(kclPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal naclAmount = q.multiply(naclPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal caso4Amount = q.multiply(caso4Percentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal liquidQ = liqSolRat.multiply((SolidMaterial.getS_Q())); // Simplified assumption for example purposes

                liquidMaterial.setLiquidH2O(h2OAmount);
                liquidMaterial.setLiquidKCl(kclAmount);
                liquidMaterial.setLiquidNaCl(naclAmount);
                liquidMaterial.setLiquidCaSO4(caso4Amount);
                liquidMaterial.setL_Q(liquidQ);


                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(h2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                caso4ResultValue.setText(caso4Amount.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

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