package com.mike.kcl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        Vishelachivanie vishelachivanie = new Vishelachivanie(solidMaterial, liquidMaterial, liquid);
        HydrocycloneSolid hydrocycloneSolid = new HydrocycloneSolid(vishelachivanie);
        HydrocycloneLiquid hydrocycloneLiquid = new HydrocycloneLiquid(hydrocycloneSolid);

        // Main Layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));

        // Sections
        layout.getChildren().addAll(
                createSection("Вода или раствор", createLiquidSection(liquid)),
                createSection("Руда", createSolidMaterialSection(solidMaterial)),
                createSection("Поток на выщелачивание", createLiquidMaterialSection(liquidMaterial, solidMaterial)),
                createSection("Выщелачивание", createVishelachivanieSection( liquid, liquidMaterial, solidMaterial,vishelachivanie)),
                createSection("Пески гидроциклона", createHydrocycloneSolidSection(vishelachivanie, hydrocycloneSolid)),
                createSection("Слив гидроциклона", createHydrocycloneLiquidSection(vishelachivanie,hydrocycloneSolid, hydrocycloneLiquid))

        );

        // ScrollPane for better navigation
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        // Scene and Stage setup
        Scene scene = new Scene(scrollPane, 1600, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("К-кальклятор");
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

        Label qLabel = new Label("Q, м^3:");
        TextField qInput = new TextField();
        Button calculateButton = new Button("Ввести кол-во");
        Label h2oResultLabel = new Label("H2O кол-во:");
        Label h2oResultValue = new Label();

        grid.addRow(0, qLabel, qInput);
        grid.add(calculateButton, 3, 0, 2, 1);
        grid.addRow(2, h2oResultLabel, h2oResultValue);

        calculateButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());
                liquid.setH2O(q);
                liquid.setQ(q);
                h2oResultValue.setText(q.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Введите корректное значение для Q.");
            }
        });

        return grid;
    }

    private GridPane createSolidMaterialSection(SolidMaterial solidMaterial) {
        GridPane grid = createAlignedGridPane();

        Label qLabel = new Label("Q, т:");
        TextField qInput = new TextField();
        Button calculateButton = new Button("Расчитать состав руды");
        Label kclResultLabel = new Label("KCl кол-во:");
        Label naclResultLabel = new Label("NaCl кол-во:");
        Label combinedResultLabel = new Label("CaSO4 + H.O. (Общий отход):");
        Label kclResultValue = new Label();
        Label naclResultValue = new Label();
        Label combinedResultValue = new Label();

        grid.addRow(0, qLabel, qInput);
        grid.add(calculateButton, 3, 0, 2, 1);
        grid.addRow(2, kclResultLabel, kclResultValue);
        grid.addRow(3, naclResultLabel, naclResultValue);
        grid.addRow(4, combinedResultLabel, combinedResultValue);

        calculateButton.setOnAction(event -> {
            try {
                BigDecimal q = new BigDecimal(qInput.getText());
                solidMaterial.setS_Q(q);

                BigDecimal kclAmount = q.multiply(BigDecimal.valueOf(0.901));
                BigDecimal naclAmount = q.multiply(BigDecimal.valueOf(0.061));
                BigDecimal caso4Amount = q.multiply(BigDecimal.valueOf(0.036));
                BigDecimal wasteAmount = q.multiply(BigDecimal.valueOf(0.002));
                BigDecimal combinedAmount = caso4Amount.add(wasteAmount); // Combined value

                solidMaterial.setSolidKCl(kclAmount);
                solidMaterial.setSolidNaCl(naclAmount);
                solidMaterial.setSolidCaSO4(caso4Amount);
                solidMaterial.setSolidWaste(wasteAmount);


                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                combinedResultValue.setText(combinedAmount.setScale(2, RoundingMode.HALF_UP).toString());
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
        Button calculateButton = new Button("Расчитать Ж/Т на выщелачивание");
        Label liquidQResultLabel = new Label("Q, т:");
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
        grid.add(calculateButton, 3, 0, 2, 1);
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

    private GridPane createVishelachivanieSection(Liquid liquid, LiquidMaterial liquidMaterial, SolidMaterial solidMaterial, Vishelachivanie vishelachivanie) {
        GridPane grid = createAlignedGridPane();

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);

        Button calculateButton = new Button("Расчитать выщелачивание");
        Label liquidHeader = new Label("Жидкие результаты");
        Label solidHeader = new Label("Твёрдые результаты");

        // Liquid result labels
        Label liquidQResultLabel = new Label("Маточник кол-во (Q):");
        Label liquidH2oResultLabel = new Label("H2O кол-во:");
        Label liquidKclResultLabel = new Label("KCl кол-во:");
        Label liquidNaclResultLabel = new Label("NaCl кол-во:");
        Label liquidCaso4ResultLabel = new Label("CaSO4 кол-во:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

        // Solid result labels
        Label solidQResultLabel = new Label("Твёрдый материал кол-во (Q):");
        Label solidKclResultLabel = new Label("KCl кол-во:");
        Label solidNaclResultLabel = new Label("NaCl кол-во:");
        Label wasteResultLabel = new Label("Отход кол-во:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();

        Label ratioResultLabel = new Label("Ж/Т:");
        Label ratioResultValue = new Label();

        // Add headers
        grid.add(liquidHeader, 0, 0);
        grid.add(solidHeader, 2, 0);

        // Add liquid result labels to the first column
        grid.add(liquidQResultLabel, 0, 1);
        grid.add(liquidQResultValue, 1, 1);
        grid.add(liquidH2oResultLabel, 0, 2);
        grid.add(liquidH2oResultValue, 1, 2);
        grid.add(liquidKclResultLabel, 0, 3);
        grid.add(liquidKclResultValue, 1, 3);
        grid.add(liquidNaclResultLabel, 0, 4);
        grid.add(liquidNaclResultValue, 1, 4);
        grid.add(liquidCaso4ResultLabel, 0, 5);
        grid.add(liquidCaso4ResultValue, 1, 5);

        // Add solid result labels to the second column
        grid.add(solidQResultLabel, 2, 1);
        grid.add(solidQResultValue, 3, 1);
        grid.add(solidKclResultLabel, 2, 2);
        grid.add(solidKclResultValue, 3, 2);
        grid.add(solidNaclResultLabel, 2, 3);
        grid.add(solidNaclResultValue, 3, 3);
        grid.add(wasteResultLabel, 2, 4);
        grid.add(wasteResultValue, 3, 4);

        // Add ratio label spanning both columns
        grid.add(ratioResultLabel, 0, 6, 2, 1);
        grid.add(ratioResultValue, 1, 6, 2, 1);

        // Add calculate button spanning both columns
        grid.add(calculateButton, 0, 7, 2, 1);

        calculateButton.setOnAction(event -> {
            try {
                // Calculation logic here (same as your provided code)
                BigDecimal LiquidNaClRatio = BigDecimal.valueOf(0.197 / 0.678);
                BigDecimal LiquidCaso4Ratio = BigDecimal.valueOf(0.004 / 0.678);
                BigDecimal LiquidH2OAmount = liquidMaterial.getLiquidH2O().add(liquid.getH2O());
                BigDecimal LiquidNaclAmount = LiquidH2OAmount.multiply(LiquidNaClRatio);
                BigDecimal LiquidCaso4Amount = LiquidH2OAmount.multiply(LiquidCaso4Ratio);

                BigDecimal LiquidKclAmount = (
                        LiquidNaclAmount.divide(LiquidH2OAmount.add(LiquidCaso4Amount), RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(-0.497825381))
                                .add(BigDecimal.valueOf(0.2648))
                ).divide(
                        BigDecimal.ONE.subtract(
                                LiquidNaclAmount.divide(LiquidH2OAmount.add(LiquidCaso4Amount), RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(-0.497825381))
                                        .add(BigDecimal.valueOf(0.2648))
                        ),
                        RoundingMode.HALF_UP
                ).multiply(LiquidH2OAmount.add(LiquidCaso4Amount).add(LiquidNaclAmount));

                BigDecimal waste = solidMaterial.getSolidWaste();
                BigDecimal SolidKclAmount = solidMaterial.getSolidKCl()
                        .add(liquidMaterial.getLiquidKCl()).add(liquid.getKCl())
                        .subtract(LiquidKclAmount).max(BigDecimal.ZERO);

                BigDecimal SolidNaclAmount = solidMaterial.getSolidNaCl()
                        .add(liquidMaterial.getLiquidNaCl()).add(liquid.getNaCl())
                        .subtract(LiquidNaclAmount).max(BigDecimal.ZERO);

                BigDecimal SolidCaso4Amount = solidMaterial.getSolidCaSO4()
                        .add(liquidMaterial.getLiquidCaSO4()).add(liquid.getCaSO4())
                        .subtract(LiquidCaso4Amount).max(BigDecimal.ZERO);

                BigDecimal liquidQ = LiquidH2OAmount.add(LiquidNaclAmount)
                        .add(LiquidCaso4Amount).add(LiquidKclAmount);
                BigDecimal solidQ = waste.add(SolidNaclAmount)
                        .add(SolidCaso4Amount).add(SolidKclAmount);
                BigDecimal ratio = liquidQ.divide(solidQ, RoundingMode.HALF_UP);

                BigDecimal  WasteCaso4 = waste.add(SolidCaso4Amount);

                vishelachivanie.setH2O(liquid.getH2O());
                vishelachivanie.setL_Q(liquidQ);
                vishelachivanie.setLiquidH2O(LiquidH2OAmount);
                vishelachivanie.setLiquidKCl(LiquidKclAmount);
                vishelachivanie.setLiquidNaCl(LiquidNaclAmount);
                vishelachivanie.setLiquidCaSO4(LiquidCaso4Amount);


                vishelachivanie.setS_Q(solidQ);
                vishelachivanie.setSolidKCl(SolidKclAmount);
                vishelachivanie.setSolidNaCl(SolidNaclAmount);
                vishelachivanie.setSolidCaSO4(SolidCaso4Amount);
                vishelachivanie.setSolidWaste(waste);

                // Update labels with calculated values
                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                liquidH2oResultValue.setText(LiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidKclResultValue.setText(LiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidNaclResultValue.setText(LiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidCaso4ResultValue.setText(LiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString());

                solidQResultValue.setText(solidQ.setScale(2, RoundingMode.HALF_UP).toString());
                solidKclResultValue.setText(SolidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                solidNaclResultValue.setText(SolidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(WasteCaso4.setScale(2, RoundingMode.HALF_UP).toString());
                ratioResultValue.setText(ratio.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Ошибка");
            }
        });

        return grid;
    }

    private GridPane createHydrocycloneSolidSection(Vishelachivanie vishelachivanie, HydrocycloneSolid hydrocycloneSolid) {
        GridPane grid = createAlignedGridPane();

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);

        Button calculateButton = new Button("Расчитать пески гидроциклона");
        Label liquidHeader = new Label("Жидкие результаты");
        Label solidHeader = new Label("Твёрдые результаты");

        // Liquid result labels
        Label liquidQResultLabel = new Label("Маточник кол-во (Q):");
        Label liquidH2oResultLabel = new Label("H2O кол-во:");
        Label liquidKclResultLabel = new Label("KCl кол-во:");
        Label liquidNaclResultLabel = new Label("NaCl кол-во:");
        Label liquidCaso4ResultLabel = new Label("CaSO4 кол-во:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

        // Solid result labels
        Label solidQResultLabel = new Label("Твёрдый материал кол-во (Q):");
        Label solidKclResultLabel = new Label("KCl кол-во:");
        Label solidNaclResultLabel = new Label("NaCl кол-во:");
        Label wasteResultLabel = new Label("Отход кол-во:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();


        // Add text field for user input of liqRat (Ж/Т)
        Label liqRatLabel = new Label("Введите Ж/Т:");
        TextField liqRatTextField = new TextField();

        Label solQuartRatioLabel = new Label("Введите долю твердых менее 0,25мм, %");
        TextField solQuartRatioTextField = new TextField();


        // Add headers
        grid.add(liquidHeader, 0, 0);
        grid.add(solidHeader, 2, 0);

        // Add liquid result labels to the first column
        grid.add(liquidQResultLabel, 0, 1);
        grid.add(liquidQResultValue, 1, 1);
        grid.add(liquidH2oResultLabel, 0, 2);
        grid.add(liquidH2oResultValue, 1, 2);
        grid.add(liquidKclResultLabel, 0, 3);
        grid.add(liquidKclResultValue, 1, 3);
        grid.add(liquidNaclResultLabel, 0, 4);
        grid.add(liquidNaclResultValue, 1, 4);
        grid.add(liquidCaso4ResultLabel, 0, 5);
        grid.add(liquidCaso4ResultValue, 1, 5);

        // Add solid result labels to the second column
        grid.add(solidQResultLabel, 2, 1);
        grid.add(solidQResultValue, 3, 1);
        grid.add(solidKclResultLabel, 2, 2);
        grid.add(solidKclResultValue, 3, 2);
        grid.add(solidNaclResultLabel, 2, 3);
        grid.add(solidNaclResultValue, 3, 3);
        grid.add(wasteResultLabel, 2, 4);
        grid.add(wasteResultValue, 3, 4);

        // Add input field for liqRat (Ж/Т)
        grid.add(liqRatLabel, 0, 6);
        grid.add(liqRatTextField, 1, 6);

        grid.add(solQuartRatioLabel, 0, 7);
        grid.add(solQuartRatioTextField, 1, 7);

        // Add calculate button spanning both columns
        grid.add(calculateButton, 0, 8, 2, 1);

        calculateButton.setOnAction(event -> {
            try {
                // Get the value of liqRat from the text field
                BigDecimal LiqSolRat = new BigDecimal(liqRatTextField.getText());
                BigDecimal SolQuartRatio = new BigDecimal(solQuartRatioTextField.getText()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                BigDecimal solidQ = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getS_Q());
                BigDecimal liquidQ = solidQ.multiply(LiqSolRat);

                // Calculation logic here (use liqRat in the formulas)
                BigDecimal LiquidH2OAmount = liquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidH2O());
                BigDecimal LiquidNaclAmount = liquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidNaCl());
                BigDecimal LiquidCaso4Amount = liquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidCaSO4());
                BigDecimal LiquidKclAmount = liquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidKCl());

                BigDecimal waste = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidWaste());
                BigDecimal SolidKclAmount = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidKCl());
                BigDecimal SolidNaclAmount = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidNaCl());
                BigDecimal SolidCaso4Amount = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidCaSO4());
                BigDecimal WasteCaso4 = waste.add(SolidCaso4Amount);

                hydrocycloneSolid.setH2O(vishelachivanie.getH2O());
                hydrocycloneSolid.setL_Q(liquidQ);
                hydrocycloneSolid.setLiquidH2O(LiquidH2OAmount);
                hydrocycloneSolid.setLiquidKCl(LiquidKclAmount);
                hydrocycloneSolid.setLiquidNaCl(LiquidNaclAmount);
                hydrocycloneSolid.setLiquidCaSO4(LiquidCaso4Amount);
              
                hydrocycloneSolid.setS_Q(solidQ);
                hydrocycloneSolid.setSolidKCl(SolidKclAmount);
                hydrocycloneSolid.setSolidNaCl(SolidNaclAmount);
                hydrocycloneSolid.setSolidCaSO4(SolidCaso4Amount);
                hydrocycloneSolid.setSolidWaste(waste);

                hydrocycloneSolid.setSolQuartRatio(SolQuartRatio);
                hydrocycloneSolid.setLiqSolRat(LiqSolRat);


                //Update labels with calculated values
                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                liquidH2oResultValue.setText(LiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidKclResultValue.setText(LiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidNaclResultValue.setText(LiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidCaso4ResultValue.setText(LiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString());

                solidQResultValue.setText(solidQ.setScale(2, RoundingMode.HALF_UP).toString());
                solidKclResultValue.setText(SolidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                solidNaclResultValue.setText(SolidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(WasteCaso4.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Ошибка");
            }
        });

        return grid;
    }
    private GridPane createHydrocycloneLiquidSection(Vishelachivanie vishelachivanie, HydrocycloneSolid hydrocycloneSolid, HydrocycloneLiquid hydrocycloneLiquid) {
        GridPane grid = createAlignedGridPane();

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);

        Button calculateButton = new Button("Расчитать слив гидроциклона");
        Label liquidHeader = new Label("Жидкие результаты");
        Label solidHeader = new Label("Твёрдые результаты");

        // Liquid result labels
        Label liquidQResultLabel = new Label("Маточник кол-во (Q):");
        Label liquidH2oResultLabel = new Label("H2O кол-во:");
        Label liquidKclResultLabel = new Label("KCl кол-во:");
        Label liquidNaclResultLabel = new Label("NaCl кол-во:");
        Label liquidCaso4ResultLabel = new Label("CaSO4 кол-во:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

        // Solid result labels
        Label solidQResultLabel = new Label("Твёрдый материал кол-во (Q):");
        Label solidKclResultLabel = new Label("KCl кол-во:");
        Label solidNaclResultLabel = new Label("NaCl кол-во:");
        Label wasteResultLabel = new Label("Отход кол-во:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();


        // Add headers
        grid.add(liquidHeader, 0, 0);
        grid.add(solidHeader, 2, 0);

        // Add liquid result labels to the first column
        grid.add(liquidQResultLabel, 0, 1);
        grid.add(liquidQResultValue, 1, 1);
        grid.add(liquidH2oResultLabel, 0, 2);
        grid.add(liquidH2oResultValue, 1, 2);
        grid.add(liquidKclResultLabel, 0, 3);
        grid.add(liquidKclResultValue, 1, 3);
        grid.add(liquidNaclResultLabel, 0, 4);
        grid.add(liquidNaclResultValue, 1, 4);
        grid.add(liquidCaso4ResultLabel, 0, 5);
        grid.add(liquidCaso4ResultValue, 1, 5);

        // Add solid result labels to the second column
        grid.add(solidQResultLabel, 2, 1);
        grid.add(solidQResultValue, 3, 1);
        grid.add(solidKclResultLabel, 2, 2);
        grid.add(solidKclResultValue, 3, 2);
        grid.add(solidNaclResultLabel, 2, 3);
        grid.add(solidNaclResultValue, 3, 3);
        grid.add(wasteResultLabel, 2, 4);
        grid.add(wasteResultValue, 3, 4);


        // Add calculate button spanning both columns
        grid.add(calculateButton, 0, 6, 2, 1);

        calculateButton.setOnAction(event -> {
            try {
                // Get the value of liqRat from the text field
                BigDecimal LiqSolRat = hydrocycloneSolid.getLiqSolRat();
                BigDecimal SolQuartRatio = hydrocycloneSolid.getSolQuartRatio();

                BigDecimal solidQ = vishelachivanie.getS_Q().multiply(SolQuartRatio);
                BigDecimal liquidQ = vishelachivanie.getL_Q().subtract(hydrocycloneSolid.getL_Q());


                BigDecimal waste = vishelachivanie.getSolidWaste().multiply(SolQuartRatio);

                BigDecimal SolidKclAmount = vishelachivanie.getSolidKCl().multiply(SolQuartRatio);
                BigDecimal SolidNaclAmount = vishelachivanie.getSolidNaCl().multiply(SolQuartRatio);
                BigDecimal SolidCaso4Amount = vishelachivanie.getSolidCaSO4().multiply(SolQuartRatio);
                BigDecimal WasteCaso4 = waste.add(SolidCaso4Amount);

                BigDecimal LiquidH2OAmount = vishelachivanie.getLiquidH2O().subtract(hydrocycloneSolid.getLiquidH2O());
                BigDecimal LiquidNaclAmount = vishelachivanie.getLiquidNaCl().subtract(hydrocycloneSolid.getLiquidNaCl());
                BigDecimal LiquidCaso4Amount = vishelachivanie.getLiquidCaSO4().subtract(hydrocycloneSolid.getLiquidCaSO4());
                BigDecimal LiquidKclAmount = vishelachivanie.getLiquidKCl().subtract(hydrocycloneSolid.getLiquidKCl());

                hydrocycloneLiquid.setH2O(hydrocycloneSolid.getH2O());
                hydrocycloneLiquid.setL_Q(liquidQ);
                hydrocycloneLiquid.setLiquidH2O(LiquidH2OAmount);
                hydrocycloneLiquid.setLiquidKCl(LiquidKclAmount);
                hydrocycloneLiquid.setLiquidNaCl(LiquidNaclAmount);
                hydrocycloneLiquid.setLiquidCaSO4(LiquidCaso4Amount);

                hydrocycloneLiquid.setS_Q(solidQ);
                hydrocycloneLiquid.setSolidKCl(SolidKclAmount);
                hydrocycloneLiquid.setSolidNaCl(SolidNaclAmount);
                hydrocycloneLiquid.setSolidCaSO4(SolidCaso4Amount);
                hydrocycloneLiquid.setSolidWaste(waste);

                hydrocycloneLiquid.setSolQuartRatio(SolQuartRatio);
                hydrocycloneLiquid.setLiqSolRat(LiqSolRat);


                //Update labels with calculated values
                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                liquidH2oResultValue.setText(LiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidKclResultValue.setText(LiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidNaclResultValue.setText(LiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidCaso4ResultValue.setText(LiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString());

                solidQResultValue.setText(solidQ.setScale(2, RoundingMode.HALF_UP).toString());
                solidKclResultValue.setText(SolidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                solidNaclResultValue.setText(SolidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(WasteCaso4.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Ошибка");
            }
        });

        return grid;
    }



    private GridPane createAlignedGridPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setHgap(5);
        grid.setVgap(5);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(10); // Adjust the width percentage as needed
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(10); // Adjust to complement column1

        grid.getColumnConstraints().addAll(column1, column2);
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
