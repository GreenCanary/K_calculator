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
        Label qLabel = new Label("Кол-во всего (Q):");
        TextField qInput = new TextField();
        grid.add(qLabel, 0, 0);
        grid.add(qInput, 1, 0);


// Button to calculate results
        Button calculateLiquidButton = new Button("Ввести кол-во воды");
        grid.add(calculateLiquidButton, 1, 2);

// Results Section for Liquid
        Label resultsLabel = new Label("Результат:");
        grid.add(resultsLabel, 0, 3, 2, 1);

        Label h2oResultLabel = new Label("H2O кол-во:");
        Label h2oResultValue = new Label();
        grid.add(h2oResultLabel, 0, 4);
        grid.add(h2oResultValue, 1, 4);


        // Calculate Liquid
        calculateLiquidButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());

                BigDecimal h2oAmount = q.multiply(BigDecimal.valueOf(1));

                liquid.setH2O(h2oAmount);
                liquid.setQ(q);

                h2oResultValue.setText(h2oAmount.setScale(2, RoundingMode.HALF_UP).toString());
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
        Label qLabel = new Label("Кол-во всего (Q):");
        TextField qInput = new TextField();
        grid.add(qLabel, 0, 0);
        grid.add(qInput, 1, 0);


        // Button to calculate solid material
        Button calculateSolidButton = new Button("Расчитать состав руды");
        grid.add(calculateSolidButton, 1, 1);

        // Results Section for Solid Material
        Label resultsLabel = new Label("Результат:");
        grid.add(resultsLabel, 0, 2, 2, 1);

        Label kclResultLabel = new Label("KCl кол-во:");
        Label kclResultValue = new Label();
        grid.add(kclResultLabel, 0, 3);
        grid.add(kclResultValue, 1, 3);

        Label naclResultLabel = new Label("NaCl кол-во:");
        Label naclResultValue = new Label();
        grid.add(naclResultLabel, 0, 4);
        grid.add(naclResultValue, 1, 4);

        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label caso4ResultValue = new Label();
        grid.add(caso4ResultLabel, 0, 5);
        grid.add(caso4ResultValue, 1, 5);

        Label wasteResultLabel = new Label("Н.О. кол-во:");
        Label wasteResultValue = new Label();
        grid.add(wasteResultLabel, 0, 6);
        grid.add(wasteResultValue, 1, 6);




        // Calculate Solid Material Logic
        calculateSolidButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());


                BigDecimal kclAmount = q.multiply(BigDecimal.valueOf(0.91));
                BigDecimal naclAmount = q.multiply(BigDecimal.valueOf(0.061));
                BigDecimal caso4Amount = q.multiply(BigDecimal.valueOf(0.036));
                BigDecimal wasteAmount = q.multiply(BigDecimal.valueOf(0.02));

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

        // Button to calculate liquid material
        Button calculateLiquidButton = new Button("Расчитать маточник");
        grid.add(calculateLiquidButton, 1, 1);

        // Results Section for Liquid Material
        Label resultsLabel = new Label("Результат:");
        grid.add(resultsLabel, 0, 2, 2, 1);

        Label liquidQResultLabel = new Label("Маточник кол-во (Q):");
        Label liquidQResultValue = new Label();
        grid.add(liquidQResultLabel, 0, 3);
        grid.add(liquidQResultValue, 1, 3);

        Label wasteResultLabel = new Label("H2O кол-во:");
        Label wasteResultValue = new Label();
        grid.add(wasteResultLabel, 0, 4);
        grid.add(wasteResultValue, 1, 4);

        Label kclResultLabel = new Label("KCl кол-во:");
        Label kclResultValue = new Label();
        grid.add(kclResultLabel, 0, 5);
        grid.add(kclResultValue, 1, 5);

        Label naclResultLabel = new Label("NaCl кол-во:");
        Label naclResultValue = new Label();
        grid.add(naclResultLabel, 0, 6);
        grid.add(naclResultValue, 1, 6);

        Label caso4ResultLabel = new Label("CaSO4 кол-во:");
        Label caso4ResultValue = new Label();
        grid.add(caso4ResultLabel, 0, 7);
        grid.add(caso4ResultValue, 1, 7);

        // Calculate Liquid Material Logic
        calculateLiquidButton.setOnAction(event -> {
            try {
                BigDecimal liqSolRat = new BigDecimal(liqSolRatInput.getText());
                BigDecimal solidMaterialQ = SolidMaterial.getS_Q();
                BigDecimal q = solidMaterialQ.multiply(liqSolRat);

                BigDecimal h2OPercentage = BigDecimal.valueOf(0.678);
                BigDecimal kclPercentage = BigDecimal.valueOf(0.121);
                BigDecimal naclPercentage = BigDecimal.valueOf(0.197);
                BigDecimal caso4Percentage = BigDecimal.valueOf(0.04);

                BigDecimal totalPercentage = h2OPercentage.add(kclPercentage).add(naclPercentage).add(caso4Percentage);
                if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new IllegalArgumentException("Сумма не равна 100%");
                }

                BigDecimal h2OAmount = q.multiply(h2OPercentage);
                BigDecimal kclAmount = q.multiply(kclPercentage);
                BigDecimal naclAmount = q.multiply(naclPercentage);
                BigDecimal caso4Amount = q.multiply(caso4Percentage);
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