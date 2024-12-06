package com.mike.kcl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class App extends Application {


    @Override
    public void start(Stage primaryStage) {
        // Create material objects
        Liquid liquid = new Liquid();
        SolidMaterial solidMaterial = new SolidMaterial(1000);
        LiquidMaterial liquidMaterial = new LiquidMaterial(0.8);
        Vishelachivanie vishelachivanie = new Vishelachivanie(solidMaterial, liquidMaterial, liquid);
        HydrocycloneSolid hydrocycloneSolid = new HydrocycloneSolid(vishelachivanie);
        HydrocycloneLiquid hydrocycloneLiquid = new HydrocycloneLiquid(vishelachivanie, hydrocycloneSolid);
        CentrifugeSolid centrifugeSolid = new CentrifugeSolid(hydrocycloneSolid);
        CentrifugeLiquid centrifugeLiquid = new CentrifugeLiquid();
        Sushka sushka = new Sushka();

        // Main Layout
        VBox layout = new VBox(20); // Vertical spacing between sections
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER); // Center-align the components
        layout.setStyle("-fx-background-color: #222222;"); // Light gray background

        // Sections
        layout.getChildren().addAll(
                createSection("Входные данные", createCombinedSection(liquid,solidMaterial, liquidMaterial)),
                createSection("Выщелачивание", createVishelachivanieSection(liquid, liquidMaterial, solidMaterial, vishelachivanie)),
                createSection("Гидроциклон", createHydrocycloneSection(vishelachivanie, hydrocycloneSolid, hydrocycloneLiquid)),
                createSection("Кек центрифуги", createCentrifugeSolidSection(hydrocycloneSolid, centrifugeSolid)),
                createSection("Фугат центрифуги", createCentrifugeLiquidSection(hydrocycloneSolid, centrifugeSolid, centrifugeLiquid)),
                createSection("Сушка и получение готового продукта", createSushkaSection(centrifugeSolid, sushka))
        );

        // ScrollPane for better navigation
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Ensures content scales with window width
        scrollPane.setStyle("-fx-background: transparent;"); // Transparent scroll background

        // Scene and Stage setup
        Scene scene = new Scene(scrollPane, 1600, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("К-калькулятор");

        // Responsiveness: Adjust VBox size to window size
        layout.prefWidthProperty().bind(scene.widthProperty().multiply(0.95)); // Dynamic width
        layout.prefHeightProperty().bind(scene.heightProperty().multiply(0.95)); // Dynamic height

        primaryStage.show();
    }


    private TitledPane createSection(String title, GridPane content) {
        TitledPane section = new TitledPane();
        section.setText(title);
        section.setContent(content);
        section.setCollapsible(false);
        return section;
    }

    private GridPane createCombinedSection(Liquid liquid, SolidMaterial solidMaterial, LiquidMaterial liquidMaterial) {
        GridPane grid = createAlignedGridPane(8);
        // Layout
        grid.setHgap(5); // Horizontal spacing
        grid.setVgap(5); // Vertical spacing
        grid.setPadding(new Insets(5));

        // Section 1: Liquid Section
        Label liquidTitleLabel = new Label("Вода");
        TextField liquidQInput = new TextField();
        Label h2oResultLabel = new Label("H2O, т/ч:");
        Label h2oResultValue = new Label();

// Section 2: Solid Material Section
        Label solidMaterialTitleLabel = new Label("Руда");
        TextField solidQInput = new TextField();
        Label kclResultLabel = new Label("KCl, т/ч:");
        Label naclResultLabel = new Label("NaCl, т/ч:");
        Label combinedResultLabel = new Label("Отход, т/ч:");
        Label kclResultValue = new Label();
        Label naclResultValue = new Label();
        Label combinedResultValue = new Label();

// Section 3: Liquid Material Section
        Label liquidMaterialTitleLabel = new Label("Поток на выщелачивание");
        TextField ratioInput = new TextField();
        Label liquidQResultLabel = new Label("Q, т/ч:");
        Label liquidH2OResultLabel = new Label("H2O, т/ч:");
        Label liquidKclResultLabel = new Label("KCl, т/ч:");
        Label liquidNaclResultLabel = new Label("NaCl, т/ч:");
        Label liquidCaso4ResultLabel = new Label("CaSO4, т/ч:");
        Label liquidQResultValue = new Label();
        Label liquidH2OResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

// Combined Button
        Button calculateButton = new Button("Внести данные");


// Liquid Section
        int liquidColumn = 0;
        int rowIndex = 0;
        grid.add(liquidTitleLabel, liquidColumn, rowIndex++, 2, 1); // Spanning two columns

// Removing "Q, т/ч:" label and using it as a hint in the TextField
        liquidQInput.setPromptText("Q, т/ч"); // Setting hint text
        grid.add(liquidQInput, liquidColumn, rowIndex++);
        grid.add(h2oResultLabel, liquidColumn, rowIndex);
        grid.add(h2oResultValue, liquidColumn + 1, rowIndex++);

// Solid Material Section
        int solidColumn = 2;
        rowIndex = 0;
        grid.add(solidMaterialTitleLabel, solidColumn, rowIndex++, 2, 1);

// Removing "Q, т/ч:" label and using it as a hint in the TextField
        solidQInput.setPromptText("Q, т/ч"); // Setting hint text
        grid.add(solidQInput, solidColumn, rowIndex++);
        grid.add(kclResultLabel, solidColumn, rowIndex);
        grid.add(kclResultValue, solidColumn + 1, rowIndex++);
        grid.add(naclResultLabel, solidColumn, rowIndex);
        grid.add(naclResultValue, solidColumn + 1, rowIndex++);
        grid.add(combinedResultLabel, solidColumn, rowIndex);
        grid.add(combinedResultValue, solidColumn + 1, rowIndex++);

// Liquid Material Section
        int liquidMaterialColumn = 4;
        rowIndex = 0;
        grid.add(liquidMaterialTitleLabel, liquidMaterialColumn, rowIndex++, 2, 1);

// Removing "Ж/Т:" label and using it as a hint in the TextField
        ratioInput.setPromptText("Ж/Т"); // Setting hint text
        grid.add(ratioInput, liquidMaterialColumn, rowIndex++);
        grid.add(liquidQResultLabel, liquidMaterialColumn, rowIndex);
        grid.add(liquidQResultValue, liquidMaterialColumn + 1, rowIndex++);
        grid.add(liquidH2OResultLabel, liquidMaterialColumn, rowIndex);
        grid.add(liquidH2OResultValue, liquidMaterialColumn + 1, rowIndex++);
        grid.add(liquidKclResultLabel, liquidMaterialColumn, rowIndex);
        grid.add(liquidKclResultValue, liquidMaterialColumn + 1, rowIndex++);
        grid.add(liquidNaclResultLabel, liquidMaterialColumn, rowIndex);
        grid.add(liquidNaclResultValue, liquidMaterialColumn + 1, rowIndex++);
        grid.add(liquidCaso4ResultLabel, liquidMaterialColumn, rowIndex);
        grid.add(liquidCaso4ResultValue, liquidMaterialColumn + 1, rowIndex++);

// Add Combined Button (span across all columns)
        grid.add(calculateButton, 0, ++rowIndex, 6, 1);




        // Event Handling
        calculateButton.setOnAction(event -> {
            try {
                // Liquid Section
                BigDecimal liquidQ = new BigDecimal(liquidQInput.getText());
                liquid.setH2O(liquidQ);
                liquid.setQ(liquidQ);
                h2oResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());

                // Solid Section
                BigDecimal solidQ = new BigDecimal(solidQInput.getText());
                solidMaterial.setS_Q(solidQ);
                BigDecimal kclAmount = solidQ.multiply(BigDecimal.valueOf(0.901));
                BigDecimal naclAmount = solidQ.multiply(BigDecimal.valueOf(0.061));
                BigDecimal combinedAmount = solidQ.multiply(BigDecimal.valueOf(0.036)).add(solidQ.multiply(BigDecimal.valueOf(0.002)));
                solidMaterial.setSolidKCl(kclAmount);
                solidMaterial.setSolidNaCl(naclAmount);
                solidMaterial.setSolidCaSO4(solidQ.multiply(BigDecimal.valueOf(0.036)));
                solidMaterial.setSolidWaste(solidQ.multiply(BigDecimal.valueOf(0.002)));
                kclResultValue.setText(kclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                naclResultValue.setText(naclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                combinedResultValue.setText(combinedAmount.setScale(2, RoundingMode.HALF_UP).toString());

                // Liquid Material Section
                BigDecimal ratio = new BigDecimal(ratioInput.getText());
                BigDecimal liquidMaterialQ = solidQ.multiply(ratio);
                liquidMaterial.setL_Q(liquidMaterialQ);
                liquidMaterial.setLiquidH2O(liquidMaterialQ.multiply(BigDecimal.valueOf(0.678)));
                liquidMaterial.setLiquidKCl(liquidMaterialQ.multiply(BigDecimal.valueOf(0.121)));
                liquidMaterial.setLiquidNaCl(liquidMaterialQ.multiply(BigDecimal.valueOf(0.197)));
                liquidMaterial.setLiquidCaSO4(liquidMaterialQ.multiply(BigDecimal.valueOf(0.004)));
                liquidQResultValue.setText(liquidMaterialQ.setScale(2, RoundingMode.HALF_UP).toString());
                liquidH2OResultValue.setText(liquidMaterial.getLiquidH2O().setScale(2, RoundingMode.HALF_UP).toString());
                liquidKclResultValue.setText(liquidMaterial.getLiquidKCl().setScale(2, RoundingMode.HALF_UP).toString());
                liquidNaclResultValue.setText(liquidMaterial.getLiquidNaCl().setScale(2, RoundingMode.HALF_UP).toString());
                liquidCaso4ResultValue.setText(liquidMaterial.getLiquidCaSO4().setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Введите корректные значения, Ж/Т это десятичная дробь через точку.");
            }
        });

        return grid;
    }


    private GridPane createVishelachivanieSection(Liquid liquid, LiquidMaterial liquidMaterial, SolidMaterial solidMaterial, Vishelachivanie vishelachivanie) {
        GridPane grid = createAlignedGridPane(8);

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Button calculateButton = new Button("Рассчитать выщелачивание");
        Label liquidHeader = new Label("Жидкость");
        Label solidHeader = new Label("Твёрдое");

        // Liquid result labels
        Label liquidQResultLabel = new Label("Q, т/ч:");
        Label liquidH2oResultLabel = new Label("H2O, т/ч:");
        Label liquidKclResultLabel = new Label("KCl, т/ч:");
        Label liquidNaclResultLabel = new Label("NaCl, т/ч:");
        Label liquidCaso4ResultLabel = new Label("CaSO4, т/ч:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

        // Solid result labels
        Label solidQResultLabel = new Label("Q, т/ч:");
        Label solidKclResultLabel = new Label("KCl, т/ч:");
        Label solidNaclResultLabel = new Label("NaCl, т/ч:");
        Label wasteResultLabel = new Label("Отход, т/ч:");
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

                BigDecimal LiquidNaclAmount1 = LiquidH2OAmount.multiply(LiquidNaClRatio);
                BigDecimal LiquidNaclAmount2 = liquidMaterial.getLiquidNaCl().add(solidMaterial.getSolidNaCl()).add(liquid.getNaCl());
                BigDecimal LiquidNaclAmount = LiquidNaclAmount1.min(LiquidNaclAmount2);

                BigDecimal LiquidCaso4Amount1 = LiquidH2OAmount.multiply(LiquidCaso4Ratio);
                BigDecimal LiquidCaso4Amount2 = liquidMaterial.getLiquidCaSO4().add(solidMaterial.getSolidCaSO4()).add(liquid.getCaSO4());
                BigDecimal LiquidCaso4Amount = LiquidCaso4Amount1.min(LiquidCaso4Amount2);

                BigDecimal LiquidKclAmount1 = (
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

                BigDecimal LiquidKclAmount2 = liquidMaterial.getLiquidKCl().add(solidMaterial.getSolidKCl()).add(liquid.getKCl());
                BigDecimal LiquidKclAmount = LiquidKclAmount1.min(LiquidKclAmount2);


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

    private GridPane createHydrocycloneSection(Vishelachivanie vishelachivanie, HydrocycloneSolid hydrocycloneSolid, HydrocycloneLiquid hydrocycloneLiquid) {
        GridPane grid = createAlignedGridPane(8);

        grid.setVgap(5); // Set vertical gap to 5 pixels
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Button calculateButton = new Button("Рассчитать работу гидроциклона");

// Headers for each section
        Label solidHeader = new Label("Пески гидроциклона");
        Label liquidHeader = new Label("Слив гидроциклона");

// --------------------- Solid Section ---------------------
        Label solidLiquidHeader = new Label("Жидкость");
        Label solidSolidHeader = new Label("Твёрдое");

// Liquid result labels for Solid Section
        Label solidLiquidQResultLabel = new Label("Q, т/ч:");
        Label solidLiquidH2oResultLabel = new Label("H2O, т/ч:");
        Label solidLiquidKclResultLabel = new Label("KCl, т/ч:");
        Label solidLiquidNaclResultLabel = new Label("NaCl, т/ч:");
        Label solidLiquidCaso4ResultLabel = new Label("CaSO4, т/ч:");
        Label solidLiquidQResultValue = new Label();
        Label solidLiquidH2oResultValue = new Label();
        Label solidLiquidKclResultValue = new Label();
        Label solidLiquidNaclResultValue = new Label();
        Label solidLiquidCaso4ResultValue = new Label();

// Solid result labels for Solid Section
        Label solidSolidQResultLabel = new Label("Q, т/ч:");
        Label solidSolidKclResultLabel = new Label("KCl, т/ч:");
        Label solidSolidNaclResultLabel = new Label("NaCl, т/ч:");
        Label solidWasteResultLabel = new Label("Отход, т/ч:");
        Label solidSolidQResultValue = new Label();
        Label solidSolidKclResultValue = new Label();
        Label solidSolidNaclResultValue = new Label();
        Label solidWasteResultValue = new Label();

// --------------------- Liquid Section ---------------------
        Label liquidLiquidHeader = new Label("Жидкость");
        Label liquidSolidHeader = new Label("Твёрдое");

// Liquid result labels for Liquid Section
        Label liquidLiquidQResultLabel = new Label("Q, т/ч:");
        Label liquidLiquidH2oResultLabel = new Label("H2O, т/ч:");
        Label liquidLiquidKclResultLabel = new Label("KCl, т/ч:");
        Label liquidLiquidNaclResultLabel = new Label("NaCl, т/ч:");
        Label liquidLiquidCaso4ResultLabel = new Label("CaSO4, т/ч:");
        Label liquidLiquidQResultValue = new Label();
        Label liquidLiquidH2oResultValue = new Label();
        Label liquidLiquidKclResultValue = new Label();
        Label liquidLiquidNaclResultValue = new Label();
        Label liquidLiquidCaso4ResultValue = new Label();

// Solid result labels for Liquid Section
        Label liquidSolidQResultLabel = new Label("Q, т/ч:");
        Label liquidSolidKclResultLabel = new Label("KCl, т/ч:");
        Label liquidSolidNaclResultLabel = new Label("NaCl, т/ч:");
        Label liquidWasteResultLabel = new Label("Отход, т/ч:");
        Label liquidSolidQResultValue = new Label();
        Label liquidSolidKclResultValue = new Label();
        Label liquidSolidNaclResultValue = new Label();
        Label liquidWasteResultValue = new Label();

// Input fields for Solid Section with hints (placeholder text)
        TextField liqRatTextField = new TextField();
        liqRatTextField.setPromptText("Введите Ж/Т");

        TextField solQuartRatioTextField = new TextField();
        solQuartRatioTextField.setPromptText("Твердые менее 0,25мм, %");

// --------------------- Adding UI Elements ---------------------
// Solid Section
        grid.add(solidHeader, 0, 0, 4, 1);
        grid.add(solidLiquidHeader, 0, 1);
        grid.add(solidSolidHeader, 2, 1);

// Solid Section - Liquid Results
        grid.add(solidLiquidQResultLabel, 0, 2);
        grid.add(solidLiquidQResultValue, 1, 2);
        grid.add(solidLiquidH2oResultLabel, 0, 3);
        grid.add(solidLiquidH2oResultValue, 1, 3);
        grid.add(solidLiquidKclResultLabel, 0, 4);
        grid.add(solidLiquidKclResultValue, 1, 4);
        grid.add(solidLiquidNaclResultLabel, 0, 5);
        grid.add(solidLiquidNaclResultValue, 1, 5);
        grid.add(solidLiquidCaso4ResultLabel, 0, 6);
        grid.add(solidLiquidCaso4ResultValue, 1, 6);

// Solid Section - Solid Results
        grid.add(solidSolidQResultLabel, 2, 2);
        grid.add(solidSolidQResultValue, 3, 2);
        grid.add(solidSolidKclResultLabel, 2, 3);
        grid.add(solidSolidKclResultValue, 3, 3);
        grid.add(solidSolidNaclResultLabel, 2, 4);
        grid.add(solidSolidNaclResultValue, 3, 4);
        grid.add(solidWasteResultLabel, 2, 5);
        grid.add(solidWasteResultValue, 3, 5);


// Liquid Section
        grid.add(liquidHeader, 0, 17, 4, 1);
        grid.add(liquidLiquidHeader, 0, 18);
        grid.add(liquidSolidHeader, 2, 18);

// Liquid Section - Liquid Results
        grid.add(liquidLiquidQResultLabel, 0, 19);
        grid.add(liquidLiquidQResultValue, 1, 19);
        grid.add(liquidLiquidH2oResultLabel, 0, 20);
        grid.add(liquidLiquidH2oResultValue, 1, 20);
        grid.add(liquidLiquidKclResultLabel, 0, 21);
        grid.add(liquidLiquidKclResultValue, 1, 21);
        grid.add(liquidLiquidNaclResultLabel, 0, 22);
        grid.add(liquidLiquidNaclResultValue, 1, 22);
        grid.add(liquidLiquidCaso4ResultLabel, 0, 23);
        grid.add(liquidLiquidCaso4ResultValue, 1, 23);

// Liquid Section - Solid Results
        grid.add(liquidSolidQResultLabel, 2, 19);
        grid.add(liquidSolidQResultValue, 3, 19);
        grid.add(liquidSolidKclResultLabel, 2, 20);
        grid.add(liquidSolidKclResultValue, 3, 20);
        grid.add(liquidSolidNaclResultLabel, 2, 21);
        grid.add(liquidSolidNaclResultValue, 3, 21);
        grid.add(liquidWasteResultLabel, 2, 22);
        grid.add(liquidWasteResultValue, 3, 22);

        grid.add(liqRatTextField, 0, 24);
        grid.add(solQuartRatioTextField, 0, 25);
// Single Calculate Button
        grid.add(calculateButton, 0, 26, 2, 1);


        // --------------------- Event Handlers ---------------------
        calculateButton.setOnAction(event -> {
                        try {
                            // Get the value of liqRat from the text field
                            BigDecimal LiqSolRat = new BigDecimal(liqRatTextField.getText());
                            BigDecimal SolQuartRatio = new BigDecimal(solQuartRatioTextField.getText()).divide(BigDecimal.valueOf(100));
                            hydrocycloneSolid.setSolQuartRatio(SolQuartRatio);
                            hydrocycloneSolid.setLiqSolRat(LiqSolRat);


                            BigDecimal ssolidQ = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getS_Q());
                            BigDecimal sliquidQ = ssolidQ.multiply(LiqSolRat);

                            // Calculation logic here (use liqRat in the formulas)
                            BigDecimal sLiquidH2OAmount = sliquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidH2O());
                            BigDecimal sLiquidNaclAmount = sliquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidNaCl());
                            BigDecimal sLiquidCaso4Amount = sliquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidCaSO4());
                            BigDecimal sLiquidKclAmount = sliquidQ.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidKCl());

                            BigDecimal swaste = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidWaste());
                            BigDecimal sSolidKclAmount = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidKCl());
                            BigDecimal sSolidNaclAmount = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidNaCl());
                            BigDecimal sSolidCaso4Amount = BigDecimal.ONE.subtract(SolQuartRatio).multiply(vishelachivanie.getSolidCaSO4());
                            BigDecimal sWasteCaso4 = swaste.add(sSolidCaso4Amount);


                            hydrocycloneSolid.setH2O(vishelachivanie.getH2O());
                            hydrocycloneSolid.setL_Q(sliquidQ);
                            hydrocycloneSolid.setLiquidH2O(sLiquidH2OAmount);
                            hydrocycloneSolid.setLiquidKCl(sLiquidKclAmount);
                            hydrocycloneSolid.setLiquidNaCl(sLiquidNaclAmount);
                            hydrocycloneSolid.setLiquidCaSO4(sLiquidCaso4Amount);

                            hydrocycloneSolid.setS_Q(ssolidQ);
                            hydrocycloneSolid.setSolidKCl(sSolidKclAmount);
                            hydrocycloneSolid.setSolidNaCl(sSolidNaclAmount);
                            hydrocycloneSolid.setSolidCaSO4(sSolidCaso4Amount);
                            hydrocycloneSolid.setSolidWaste(swaste);



                            //Update labels with calculated values
                            solidLiquidQResultValue.setText(sliquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                            solidLiquidH2oResultValue.setText(sLiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                            solidLiquidKclResultValue.setText(sLiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                            solidLiquidNaclResultValue.setText(sLiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                            solidLiquidCaso4ResultValue.setText(sLiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString());

                            solidSolidQResultValue.setText(ssolidQ.setScale(2, RoundingMode.HALF_UP).toString());
                            solidSolidKclResultValue.setText(sSolidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                            solidSolidNaclResultValue.setText(sSolidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                            solidWasteResultValue.setText(sWasteCaso4.setScale(2, RoundingMode.HALF_UP).toString());






                        hydrocycloneLiquid.setSolQuartRatio(SolQuartRatio);
                        hydrocycloneLiquid.setLiqSolRat(LiqSolRat);

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




                        //Update labels with calculated values
                        liquidLiquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidLiquidH2oResultValue.setText(LiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidLiquidKclResultValue.setText(LiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidLiquidNaclResultValue.setText(LiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidLiquidCaso4ResultValue.setText(LiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString());

                        liquidSolidQResultValue.setText(solidQ.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidSolidKclResultValue.setText(SolidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidSolidNaclResultValue.setText(SolidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                        liquidWasteResultValue.setText(WasteCaso4.setScale(2, RoundingMode.HALF_UP).toString());
                    } catch (NumberFormatException e) {
                        showError("Ошибка");
                    }
                });

        return grid;
    }

    private GridPane createCentrifugeLiquidSection(HydrocycloneSolid hydrocycloneSolid, CentrifugeSolid centrifugeSolid, CentrifugeLiquid centrifugeLiquid) {
        GridPane grid = createAlignedGridPane(8);

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Button calculateButton = new Button("Рассчитать фугат центрифуги");
        Label liquidHeader = new Label("Жидкость");

        // Liquid result labels
        Label liquidQResultLabel = new Label("Q, т/ч:");
        Label liquidH2oResultLabel = new Label("H2O, т/ч:");
        Label liquidKclResultLabel = new Label("KCl, т/ч:");
        Label liquidNaclResultLabel = new Label("NaCl, т/ч:");
        Label liquidCaso4ResultLabel = new Label("CaSO4, т/ч:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();




        // Add headers
        grid.add(liquidHeader, 0, 0);

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


        // Add calculate button spanning both columns
        grid.add(calculateButton, 0, 6, 2, 1);

        calculateButton.setOnAction(event -> {
            try {
                // Get the value of liqRat from the text field



                BigDecimal liquidQ = hydrocycloneSolid.getL_Q().subtract(centrifugeSolid.getL_Q());
                BigDecimal LiquidH2OAmount = hydrocycloneSolid.getLiquidH2O().subtract(centrifugeSolid.getLiquidH2O());
                BigDecimal LiquidNaclAmount = hydrocycloneSolid.getLiquidNaCl().subtract(centrifugeSolid.getLiquidNaCl());
                BigDecimal LiquidCaso4Amount = hydrocycloneSolid.getLiquidCaSO4().subtract(centrifugeSolid.getLiquidCaSO4());
                BigDecimal LiquidKclAmount = hydrocycloneSolid.getLiquidKCl().subtract(centrifugeSolid.getLiquidKCl());


                centrifugeLiquid.setL_Q(liquidQ);
                centrifugeLiquid.setLiquidH2O(LiquidH2OAmount);
                centrifugeLiquid.setLiquidKCl(LiquidKclAmount);
                centrifugeLiquid.setLiquidNaCl(LiquidNaclAmount);
                centrifugeLiquid.setLiquidCaSO4(LiquidCaso4Amount);

                //Update labels with calculated values
                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString());
                liquidH2oResultValue.setText(LiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidKclResultValue.setText(LiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidNaclResultValue.setText(LiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString());
                liquidCaso4ResultValue.setText(LiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString());

            } catch (NumberFormatException e) {
                showError("Ошибка");
            }
        });

        return grid;
    }

    private GridPane createCentrifugeSolidSection(HydrocycloneSolid hydrocycloneSolid, CentrifugeSolid centrifugeSolid) {
        GridPane grid = createAlignedGridPane(8);

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Button calculateButton = new Button("Рассчитать кек центрифуги");
        Label liquidHeader = new Label("Жидкость");
        Label solidHeader = new Label("Твёрдое");

// Liquid result labels
        Label liquidQResultLabel = new Label("Q, т/ч:");
        Label liquidH2oResultLabel = new Label("H2O, т/ч:");
        Label liquidKclResultLabel = new Label("KCl, т/ч:");
        Label liquidNaclResultLabel = new Label("NaCl, т/ч:");
        Label liquidCaso4ResultLabel = new Label("CaSO4, т/ч:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

// Solid result labels
        Label solidQResultLabel = new Label("Q, т/ч:");
        Label solidKclResultLabel = new Label("KCl, т/ч:");
        Label solidNaclResultLabel = new Label("NaCl, т/ч:");
        Label wasteResultLabel = new Label("Отход, т/ч:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();

// Add text field for user input of liqRat (Ж/Т)
        TextField liqRatTextField = new TextField();
        liqRatTextField.setPromptText("Введите Ж/Т:"); // Hint for the input field

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
        grid.add(liqRatTextField, 0, 6);

// Add calculate button spanning both columns
        grid.add(calculateButton, 0, 7, 2, 1);

        calculateButton.setOnAction(event -> {
            try {
                // Get the value of liqRat from the text field
                BigDecimal LiqSolRat = new BigDecimal(liqRatTextField.getText());
                centrifugeSolid.setLiqSolRat(LiqSolRat);

                BigDecimal solidQ = hydrocycloneSolid.getS_Q();

                BigDecimal liquidQ = solidQ.multiply(LiqSolRat);
                BigDecimal LiquidH2OAmount = liquidQ.divide(hydrocycloneSolid.getL_Q()).multiply(hydrocycloneSolid.getLiquidH2O());
                BigDecimal LiquidNaclAmount = liquidQ.divide(hydrocycloneSolid.getL_Q()).multiply(hydrocycloneSolid.getLiquidNaCl());
                BigDecimal LiquidCaso4Amount = liquidQ.divide(hydrocycloneSolid.getL_Q()).multiply(hydrocycloneSolid.getLiquidCaSO4());
                BigDecimal LiquidKclAmount = liquidQ.divide(hydrocycloneSolid.getL_Q()).multiply(hydrocycloneSolid.getLiquidKCl());

                BigDecimal waste = hydrocycloneSolid.getSolidWaste();
                BigDecimal SolidKclAmount = hydrocycloneSolid.getSolidKCl();
                BigDecimal SolidNaclAmount = hydrocycloneSolid.getSolidNaCl();
                BigDecimal SolidCaso4Amount = hydrocycloneSolid.getSolidCaSO4();
                BigDecimal WasteCaso4 = waste.add(SolidCaso4Amount);

                centrifugeSolid.setLiqSolRat(LiqSolRat);
                centrifugeSolid.setL_Q(liquidQ);
                centrifugeSolid.setLiquidH2O(LiquidH2OAmount);
                centrifugeSolid.setLiquidKCl(LiquidKclAmount);
                centrifugeSolid.setLiquidNaCl(LiquidNaclAmount);
                centrifugeSolid.setLiquidCaSO4(LiquidCaso4Amount);

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

    private GridPane createSushkaSection(CentrifugeSolid centrifugeSolid, Sushka sushka) {
        GridPane grid = createAlignedGridPane(8);
        grid.setHgap(5); // Horizontal spacing
        grid.setVgap(5); // Vertical spacing
        grid.setPadding(new Insets(5));


        Button calculateButton = new Button("Рассчитать результат");
        Label solidQResultLabel = new Label("Q, т/ч:");
        Label solidKclResultLabel = new Label("KCl, т/ч:");
        Label solidNaclResultLabel = new Label("NaCl, т/ч:");
        Label wasteResultLabel = new Label("Отход, т/ч:");
        Label h2oResultLabel = new Label("H20, т/ч:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();
        Label h2oResultValue = new Label();



        grid.add(calculateButton, 0, 5, 2, 1);
        grid.add(solidQResultLabel, 0, 0);
        grid.add(solidQResultValue, 1, 0);
        grid.add(solidKclResultLabel, 0, 1);
        grid.add(solidKclResultValue, 1, 1);
        grid.add(solidNaclResultLabel, 0, 2);
        grid.add(solidNaclResultValue, 1, 2);
        grid.add(wasteResultLabel, 0, 3);
        grid.add(wasteResultValue, 1, 3);
        grid.addRow(4, h2oResultLabel, h2oResultValue);

        calculateButton.setOnAction(event -> {
            try {
                BigDecimal gp = BigDecimal.valueOf(0.005);
                BigDecimal kcl = centrifugeSolid.getSolidKCl().add(centrifugeSolid.getLiquidKCl());
                BigDecimal nacl = centrifugeSolid.getSolidNaCl().add(centrifugeSolid.getLiquidNaCl());
                BigDecimal caso4 = centrifugeSolid.getSolidCaSO4().add(centrifugeSolid.getLiquidCaSO4());
                BigDecimal waste = centrifugeSolid.getSolidWaste();
                BigDecimal Sum = kcl.add(nacl).add(caso4).add(waste);
                BigDecimal h2o1 = BigDecimal.ONE.subtract(gp).multiply(gp);
                BigDecimal h2o = Sum.divide(h2o1, RoundingMode.HALF_UP);
                BigDecimal Q = Sum.add(h2o);



                sushka.setQ(Q);
                sushka.setKCl(kcl);
                sushka.setNaCl(nacl);
                sushka.setCaSO4(caso4);
                sushka.setWaste(waste);
                sushka.setH2O(h2o);

                solidQResultValue.setText(Q.setScale(2, RoundingMode.HALF_UP).toString());
                solidKclResultValue.setText(kcl.setScale(2, RoundingMode.HALF_UP).toString());
                solidNaclResultValue.setText(nacl.setScale(2, RoundingMode.HALF_UP).toString());
                wasteResultValue.setText(waste.add(caso4).setScale(2, RoundingMode.HALF_UP).toString());
                h2oResultValue.setText(h2o.setScale(2, RoundingMode.HALF_UP).toString());

            } catch (NumberFormatException e) {
                showError("Введите корректное значение для влажности готового продукта, например 0.5");
            }
        });

        return grid;
    }

    private GridPane createAlignedGridPane(int numColumns) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setHgap(5);
        grid.setVgap(5);

        // Define consistent column constraints for all columns
        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / numColumns); // Divide the width equally
            grid.getColumnConstraints().add(column);
        }

        // Apply a border to the entire grid
        grid.setStyle("-fx-border-color: white; -fx-border-width: 2px;");

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
