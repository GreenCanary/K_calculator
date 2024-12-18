package com.mike.kcl;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Pos;
import javafx.stage.WindowEvent;


public class Application extends javafx.application.Application {

    private Liquid liquid;
    private SolidMaterial solidMaterial;
    private LiquidMaterial liquidMaterial;
    private Vishelachivanie vishelachivanie;
    private HydrocycloneSolid hydrocycloneSolid;
    private HydrocycloneLiquid hydrocycloneLiquid;
    private CentrifugeSolid centrifugeSolid;
    private CentrifugeLiquid centrifugeLiquid;
    private Sushka sushka;


    @Override
    public void start(Stage primaryStage) {
        // Create material objects
        loadState();

                this.liquid = new Liquid();
                this.solidMaterial = new SolidMaterial(1000);
                this.liquidMaterial = new LiquidMaterial(0.8);
                this.vishelachivanie = new Vishelachivanie(solidMaterial, liquidMaterial, liquid);
                this.hydrocycloneSolid = new HydrocycloneSolid(vishelachivanie);
                this.hydrocycloneLiquid = new HydrocycloneLiquid(vishelachivanie, hydrocycloneSolid);
                this.centrifugeSolid = new CentrifugeSolid(hydrocycloneSolid);
                this.centrifugeLiquid = new CentrifugeLiquid();
                this.sushka = new Sushka();


        // Main Layout
        VBox layout = new VBox(20); // Vertical spacing between sections
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.TOP_CENTER); // Center-align the components
        layout.setStyle("-fx-background-color: #707070;"); // Light gray background

        // Sections
        layout.getChildren().addAll(
                createSection("Входные данные", createCombinedSection(liquid,solidMaterial, liquidMaterial, vishelachivanie, hydrocycloneSolid, hydrocycloneLiquid, centrifugeSolid, centrifugeLiquid, sushka)),
                createSection("Центрифуга", createCentrifugeSection(hydrocycloneSolid, centrifugeSolid, centrifugeLiquid)),
                createSection("Сушка и получение готового продукта", createSushkaSection(liquidMaterial,solidMaterial, centrifugeSolid, sushka))
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

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                saveState();
            }
        });

        primaryStage.show();
    }
    private void saveState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("appState.dat"))) {
            out.writeObject(this.liquid);
            out.writeObject(this.solidMaterial);
            out.writeObject(this.liquidMaterial);
            out.writeObject(this.vishelachivanie);
            out.writeObject(this.hydrocycloneSolid);
            out.writeObject(this.hydrocycloneLiquid);
            out.writeObject(this.centrifugeSolid);
            out.writeObject(this.centrifugeLiquid);
            out.writeObject(this.sushka);
            System.out.println("State saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("appState.dat"))) {
            this.liquid = (Liquid) in.readObject();
            this.solidMaterial = (SolidMaterial) in.readObject();
            this.liquidMaterial = (LiquidMaterial) in.readObject();
            this.vishelachivanie = (Vishelachivanie) in.readObject();
            this.hydrocycloneSolid = (HydrocycloneSolid) in.readObject();
            this.hydrocycloneLiquid = (HydrocycloneLiquid) in.readObject();
            this.centrifugeSolid = (CentrifugeSolid) in.readObject();
            this.centrifugeLiquid = (CentrifugeLiquid) in.readObject();
            this.sushka = (Sushka) in.readObject();
            System.out.println("State loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous state found, starting with new state.");
        }
    }




    private TitledPane createSection(String title, GridPane content) {
        TitledPane section = new TitledPane();
        section.setText(title);
        section.setContent(content);
        section.setCollapsible(false);
        return section;
    }

    private GridPane createCombinedSection(Liquid liquid, SolidMaterial solidMaterial, LiquidMaterial liquidMaterial, Vishelachivanie vishelachivanie, HydrocycloneSolid hydrocycloneSolid, HydrocycloneLiquid hydrocycloneLiquid, CentrifugeSolid centrifugeSolid, CentrifugeLiquid centrifugeLiquid, Sushka sushka) {
        GridPane grid = createAlignedGridPane(8);
        // Layout
        grid.setHgap(5); // Horizontal spacing
        grid.setVgap(5); // Vertical spacing
        grid.setPadding(new Insets(5));

        // Section 1: Liquid Section
        Label liquidTitleLabel = new Label("Вода на выщелачивание");
        TextField liquidQInput = new TextField();
        Label h2oResultLabel = new Label("H2O:");
        Label h2oResultValue = new Label();

// Section 2: Solid Material Section
        Label solidMaterialTitleLabel = new Label("Руда");
        TextField solidQInput = new TextField();
        TextField KClPercentInput = new TextField();
        TextField NaClPercentInput = new TextField();
        TextField CaSO4PercentInput = new TextField();
        Label kclResultLabel = new Label("KCl:");
        Label naclResultLabel = new Label("NaCl:");
        Label combinedResultLabel = new Label("H.O. + CaSO4:");
        Label kclResultValue = new Label();
        Label naclResultValue = new Label();
        Label combinedResultValue = new Label();

// Section 3: Liquid Material Section
        Label liquidMaterialTitleLabel = new Label("Маточник, поток на выщелачивание");
        TextField ratioInput = new TextField();
        Label liquidQResultLabel = new Label("Q:");
        Label liquidH2OResultLabel = new Label("H2O:");
        Label liquidKclResultLabel = new Label("KCl:");
        Label liquidNaclResultLabel = new Label("NaCl:");
        Label liquidCaso4ResultLabel = new Label("CaSO4:");
        Label liquidQResultValue = new Label();
        Label liquidH2OResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

// Combined Button
        Button calculateButton = new Button("Внести данные и расчитать Г.П.");


// Liquid Section
        int liquidColumn = 4;
        int rowIndex = 0;
        grid.add(liquidTitleLabel, liquidColumn, rowIndex++, 2, 1); // Spanning two columns

// Removing "Q, т/ч:" label and using it as a hint in the TextField
        liquidQInput.setPromptText("Q, т/ч"); // Setting hint text
        grid.add(liquidQInput, liquidColumn, rowIndex++);
        grid.add(h2oResultLabel, liquidColumn, rowIndex);
        grid.add(h2oResultValue, liquidColumn + 1, rowIndex++);

// Solid Material Section
        int solidColumn = 0;
        rowIndex = 0;
        grid.add(solidMaterialTitleLabel, solidColumn, rowIndex++, 2, 1);

// Removing "Q, т/ч:" label and using it as a hint in the TextField
        solidQInput.setPromptText("Q, т/ч"); // Setting hint text
        KClPercentInput.setPromptText("KCl, %");
        NaClPercentInput.setPromptText("NaCl, %");
        CaSO4PercentInput.setPromptText("CaSO4, %");
        grid.add(solidQInput, solidColumn, rowIndex++);
        grid.add(KClPercentInput, solidColumn, rowIndex++);
        grid.add(NaClPercentInput, solidColumn, rowIndex++);
        grid.add(CaSO4PercentInput, solidColumn, rowIndex++);

        grid.add(kclResultLabel, solidColumn, rowIndex);
        grid.add(kclResultValue, solidColumn + 1, rowIndex++);
        grid.add(naclResultLabel, solidColumn, rowIndex);
        grid.add(naclResultValue, solidColumn + 1, rowIndex++);
        grid.add(combinedResultLabel, solidColumn, rowIndex);
        grid.add(combinedResultValue, solidColumn + 1, rowIndex++);

// Liquid Material Section
        int liquidMaterialColumn = 2;
        rowIndex = 0;
        grid.add(liquidMaterialTitleLabel, liquidMaterialColumn, rowIndex++, 2, 1);

// Removing "Ж/Т:" label and using it as a hint in the TextField
        ratioInput.setPromptText("Ж/Т, маточник");

        // Setting hint text
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
        grid.add(calculateButton, 0, 11, 6, 1);

        Label liquidHeader = new Label("Жидкая фаза");
        Label solidHeader = new Label("Твёрдая фаза");




        // Liquid result labels
        Label liquidQResultLabelVishelachivanie = new Label("Q:");
        Label liquidH2oResultLabelVishelachivanie = new Label("H2O:");
        Label liquidKclResultLabelVishelachivanie = new Label("KCl:");
        Label liquidNaclResultLabelVishelachivanie = new Label("NaCl:");
        Label liquidCaso4ResultLabelVishelachivanie = new Label("CaSO4:");
        Label liquidQResultValueVishelachivanie = new Label();
        Label liquidH2oResultValueVishelachivanie = new Label();
        Label liquidKclResultValueVishelachivanie = new Label();
        Label liquidNaclResultValueVishelachivanie = new Label();
        Label liquidCaso4ResultValueVishelachivanie = new Label();

        // Solid result labels
        Label solidQResultLabelVishelachivanie = new Label("Q:");
        Label solidKclResultLabelVishelachivanie = new Label("KCl:");
        Label solidNaclResultLabelVishelachivanie = new Label("NaCl:");
        Label wasteResultLabelVishelachivanie = new Label("H.O. + CaSO4:");
        Label solidQResultValueVishelachivanie = new Label();
        Label solidKclResultValueVishelachivanie = new Label();
        Label solidNaclResultValueVishelachivanie = new Label();
        Label wasteResultValueVishelachivanie = new Label();

        Label ratioResultLabelVishelachivanie = new Label("Ж/Т:");
        Label ratioResultValueVishelachivanie = new Label();


        TextField liqRatTextFieldHydrocyclone = new TextField();
        liqRatTextFieldHydrocyclone.setPromptText("Введите Ж/Т, гидроциклон");

        TextField solQuartRatioTextFieldHydrocyclone = new TextField();
        solQuartRatioTextFieldHydrocyclone.setPromptText("Твёрдые менее 0,25мм, %");

        grid.add(liqRatTextFieldHydrocyclone, 0, 8);
        grid.add(solQuartRatioTextFieldHydrocyclone, 0, 9);

        rowIndex = 12;
        Label titleVishelachivanie = new Label("Выщелачивание");
        titleVishelachivanie.setStyle("-fx-font-size: 20px;");

        grid.add(titleVishelachivanie, 0, rowIndex);
        rowIndex++;
        // Add headers
        grid.add(liquidHeader, 0, ++rowIndex);
        grid.add(solidHeader, 2, rowIndex);


        // Add liquid result labels to the first column
        grid.add(liquidQResultLabelVishelachivanie, 0, ++rowIndex);
        grid.add(liquidQResultValueVishelachivanie, 1, rowIndex);
        grid.add(liquidH2oResultLabelVishelachivanie, 0, ++rowIndex);
        grid.add(liquidH2oResultValueVishelachivanie, 1, rowIndex);
        grid.add(liquidKclResultLabelVishelachivanie, 0, ++rowIndex);
        grid.add(liquidKclResultValueVishelachivanie, 1, rowIndex);
        grid.add(liquidNaclResultLabelVishelachivanie, 0, ++rowIndex);
        grid.add(liquidNaclResultValueVishelachivanie, 1, rowIndex);
        grid.add(liquidCaso4ResultLabelVishelachivanie, 0, ++rowIndex);
        grid.add(liquidCaso4ResultValueVishelachivanie, 1, rowIndex);

        rowIndex = rowIndex - 5;
        // Add solid result labels to the second column
        grid.add(solidQResultLabelVishelachivanie, 2, ++rowIndex);
        grid.add(solidQResultValueVishelachivanie, 3, rowIndex);
        grid.add(solidKclResultLabelVishelachivanie, 2, ++rowIndex);
        grid.add(solidKclResultValueVishelachivanie, 3, rowIndex);
        grid.add(solidNaclResultLabelVishelachivanie, 2, ++rowIndex);
        grid.add(solidNaclResultValueVishelachivanie, 3, rowIndex);
        grid.add(wasteResultLabelVishelachivanie, 2, ++rowIndex);
        grid.add(wasteResultValueVishelachivanie, 3, rowIndex);

        rowIndex++;
        // Add ratio label spanning both columns
        grid.add(ratioResultLabelVishelachivanie, 0, ++rowIndex, 2, 1);
        grid.add(ratioResultValueVishelachivanie, 1, rowIndex, 2, 1);
        rowIndex++;
        // Add calculate button spanning both columns
        PieChart liquidPieChartVishelachivanie = new PieChart();
        liquidPieChartVishelachivanie.setTitle("Жидкая фаза");


        PieChart solidPieChartVishelachivanie = new PieChart();
        solidPieChartVishelachivanie.setTitle("Твёрдая фаза");

        grid.add(liquidPieChartVishelachivanie, 0, ++rowIndex, 2, 2);
        grid.add(solidPieChartVishelachivanie, 2, rowIndex, 2, 2);



        Label titleHydrocyclone = new Label("Гидроциклон");
        titleHydrocyclone.setStyle("-fx-font-size: 20px;");

        grid.add(titleHydrocyclone, 0, 24);

        // Headers for each section
        Label solidHeaderHydrocyclone = new Label("Пески гидроциклона");
        Label liquidHeaderHydrocyclone = new Label("Слив гидроциклона");

// --------------------- Solid Section ---------------------

// Liquid result labels for Solid Section
        Label solidLiquidQResultLabelHydrocyclone = new Label("Q:");
        Label solidLiquidH2oResultLabelHydrocyclone = new Label("H2O:");
        Label solidLiquidKclResultLabelHydrocyclone = new Label("KCl:");
        Label solidLiquidNaclResultLabelHydrocyclone = new Label("NaCl:");
        Label solidLiquidCaso4ResultLabelHydrocyclone = new Label("CaSO4:");
        Label solidLiquidQResultValueHydrocyclone = new Label();
        Label solidLiquidH2oResultValueHydrocyclone = new Label();
        Label solidLiquidKclResultValueHydrocyclone = new Label();
        Label solidLiquidNaclResultValueHydrocyclone = new Label();
        Label solidLiquidCaso4ResultValueHydrocyclone = new Label();

// Solid result labels for Solid Section
        Label solidSolidQResultLabelHydrocyclone = new Label("Q:");
        Label solidSolidKclResultLabelHydrocyclone = new Label("KCl:");
        Label solidSolidNaclResultLabelHydrocyclone = new Label("NaCl:");
        Label solidWasteResultLabelHydrocyclone = new Label("H.O. + CaSO4:");
        Label solidSolidQResultValueHydrocyclone = new Label();
        Label solidSolidKclResultValueHydrocyclone = new Label();
        Label solidSolidNaclResultValueHydrocyclone = new Label();
        Label solidWasteResultValueHydrocyclone = new Label();

// --------------------- Liquid Section ---------------------

// Liquid result labels for Liquid Section
        Label liquidLiquidQResultLabel = new Label("Q:");
        Label liquidLiquidH2oResultLabel = new Label("H2O:");
        Label liquidLiquidKclResultLabel = new Label("KCl:");
        Label liquidLiquidNaclResultLabel = new Label("NaCl:");
        Label liquidLiquidCaso4ResultLabel = new Label("CaSO4:");
        Label liquidLiquidQResultValue = new Label();
        Label liquidLiquidH2oResultValue = new Label();
        Label liquidLiquidKclResultValue = new Label();
        Label liquidLiquidNaclResultValue = new Label();
        Label liquidLiquidCaso4ResultValue = new Label();

// Solid result labels for Liquid Section
        Label liquidSolidQResultLabelHydrocyclone = new Label("Q:");
        Label liquidSolidKclResultLabelHydrocyclone = new Label("KCl:");
        Label liquidSolidNaclResultLabelHydrocyclone = new Label("NaCl:");
        Label liquidWasteResultLabelHydrocyclone = new Label("H.O. + CaSO4:");
        Label liquidSolidQResultValueHydrocyclone = new Label();
        Label liquidSolidKclResultValueHydrocyclone = new Label();
        Label liquidSolidNaclResultValueHydrocyclone = new Label();
        Label liquidWasteResultValueHydrocyclone = new Label();

// Input fields for Solid Section with hints (placeholder text)



        Label liquidHeaderHydrocyclone2 = new Label("Жидкая фаза");
        Label solidHeaderHydrocyclone2 = new Label("Твёрдая фаза");
// --------------------- Adding UI Elements ---------------------
// Solid Section
        grid.add(solidHeaderHydrocyclone, 0, 25, 4, 1);
        grid.add(liquidHeaderHydrocyclone2, 0, 26);
        grid.add(solidHeaderHydrocyclone2, 2, 26);

// Solid Section - Liquid Results
        grid.add(solidLiquidQResultLabelHydrocyclone, 0, 27);
        grid.add(solidLiquidQResultValueHydrocyclone, 1, 27);
        grid.add(solidLiquidH2oResultLabelHydrocyclone, 0, 28);
        grid.add(solidLiquidH2oResultValueHydrocyclone, 1, 28);
        grid.add(solidLiquidKclResultLabelHydrocyclone, 0, 29);
        grid.add(solidLiquidKclResultValueHydrocyclone, 1, 29);
        grid.add(solidLiquidNaclResultLabelHydrocyclone, 0, 30);
        grid.add(solidLiquidNaclResultValueHydrocyclone, 1, 30);
        grid.add(solidLiquidCaso4ResultLabelHydrocyclone, 0, 31);
        grid.add(solidLiquidCaso4ResultValueHydrocyclone, 1, 31);

// Solid Section - Solid Results
        grid.add(solidSolidQResultLabelHydrocyclone, 2, 27);
        grid.add(solidSolidQResultValueHydrocyclone, 3, 27);
        grid.add(solidSolidKclResultLabelHydrocyclone, 2, 28);
        grid.add(solidSolidKclResultValueHydrocyclone, 3, 28);
        grid.add(solidSolidNaclResultLabelHydrocyclone, 2, 29);
        grid.add(solidSolidNaclResultValueHydrocyclone, 3, 29);
        grid.add(solidWasteResultLabelHydrocyclone, 2, 30);
        grid.add(solidWasteResultValueHydrocyclone, 3, 30);

        PieChart liquidPieChartHydrocyclone = new PieChart();
        liquidPieChartHydrocyclone.setTitle("Жидкая фаза");


        PieChart solidPieChartHydrocyclone = new PieChart();
        solidPieChartHydrocyclone.setTitle("Твёрдая фаза");

        PieChart liquidPieChart2Hydrocyclone = new PieChart();
        liquidPieChart2Hydrocyclone.setTitle("Жидкая фаза");
        PieChart solidPieChart2Hydrocyclone = new PieChart();
        solidPieChart2Hydrocyclone.setTitle("Твёрдая фаза");



        grid.add(liquidPieChartHydrocyclone, 0, 32, 2, 2);
        grid.add(solidPieChartHydrocyclone, 2, 32, 2, 2);

// Liquid Section
        Label liquidHeaderHydrocyclone3 = new Label("Жидкая фаза");
        Label solidHeaderHydrocyclone3 = new Label("Твёрдая фаза");
        Label liquidHeaderHydrocyclone4 = new Label("Жидкая фаза");
        Label solidHeaderHydrocyclone4 = new Label("Твёрдая фаза");
        grid.add(liquidHeaderHydrocyclone, 0, 42, 4, 1);
        grid.add(liquidHeaderHydrocyclone3, 0, 43);
        grid.add(solidHeaderHydrocyclone3, 2, 43);

// Liquid Section - Liquid Results
        grid.add(liquidLiquidQResultLabel, 0, 44);
        grid.add(liquidLiquidQResultValue, 1, 44);
        grid.add(liquidLiquidH2oResultLabel, 0, 45);
        grid.add(liquidLiquidH2oResultValue, 1, 45);
        grid.add(liquidLiquidKclResultLabel, 0, 46);
        grid.add(liquidLiquidKclResultValue, 1, 46);
        grid.add(liquidLiquidNaclResultLabel, 0, 47);
        grid.add(liquidLiquidNaclResultValue, 1, 47);
        grid.add(liquidLiquidCaso4ResultLabel, 0, 48);
        grid.add(liquidLiquidCaso4ResultValue, 1, 48);

// Liquid Section - Solid Results
        grid.add(liquidSolidQResultLabelHydrocyclone, 2, 44);
        grid.add(liquidSolidQResultValueHydrocyclone, 3, 44);
        grid.add(liquidSolidKclResultLabelHydrocyclone, 2, 45);
        grid.add(liquidSolidKclResultValueHydrocyclone, 3, 45);
        grid.add(liquidSolidNaclResultLabelHydrocyclone, 2, 46);
        grid.add(liquidSolidNaclResultValueHydrocyclone, 3, 46);
        grid.add(liquidWasteResultLabelHydrocyclone, 2, 47);
        grid.add(liquidWasteResultValueHydrocyclone, 3, 47);


        grid.add(liquidPieChart2Hydrocyclone, 0, 49, 2, 2);
        grid.add(solidPieChart2Hydrocyclone, 2, 49, 2, 2);





        // --------------------- Event Handlers ---------------------




        // Event Handling
        calculateButton.setOnAction(event -> {
            try {

                // Liquid Section
                BigDecimal liquidQInputSection = new BigDecimal(liquidQInput.getText());
                liquid.setH2O(liquidQInputSection);
                liquid.setQ(liquidQInputSection);
                h2oResultValue.setText(liquidQInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                // Solid Section
                BigDecimal solidQInputSection = new BigDecimal(solidQInput.getText());
                BigDecimal KClPercentInputSection = new BigDecimal(KClPercentInput.getText()).divide(BigDecimal.valueOf(100));
                BigDecimal NaCLPercentInputSection = new BigDecimal(NaClPercentInput.getText()).divide(BigDecimal.valueOf(100));
                BigDecimal CaSO4PercentInputSection = new BigDecimal(CaSO4PercentInput.getText()).divide(BigDecimal.valueOf(100));
                BigDecimal WastePercentInputSection = BigDecimal.ONE.subtract(KClPercentInputSection).subtract(NaCLPercentInputSection).subtract(CaSO4PercentInputSection);

                solidMaterial.setS_Q(solidQInputSection);
                BigDecimal kclAmountInputSection = solidQInputSection.multiply(KClPercentInputSection);
                BigDecimal naclAmountInputSection = solidQInputSection.multiply(NaCLPercentInputSection);
                BigDecimal combinedAmountInputSection = solidQInputSection.multiply(CaSO4PercentInputSection).add(WastePercentInputSection);
                solidMaterial.setSolidKCl(kclAmountInputSection);
                solidMaterial.setSolidNaCl(naclAmountInputSection);
                solidMaterial.setSolidCaSO4(solidQInputSection.multiply(BigDecimal.valueOf(0.036)));
                solidMaterial.setSolidWaste(solidQInputSection.multiply(BigDecimal.valueOf(0.002)));
                kclResultValue.setText(kclAmountInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                naclResultValue.setText(naclAmountInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                combinedResultValue.setText(combinedAmountInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                // Liquid Material Section
                BigDecimal ratioInputSection = new BigDecimal(ratioInput.getText());
                BigDecimal liquidMaterialQInputSection = solidQInputSection.multiply(ratioInputSection);
                liquidMaterial.setL_Q(liquidMaterialQInputSection);
                liquidMaterial.setLiquidH2O(liquidMaterialQInputSection.multiply(BigDecimal.valueOf(0.678)));
                liquidMaterial.setLiquidKCl(liquidMaterialQInputSection.multiply(BigDecimal.valueOf(0.121)));
                liquidMaterial.setLiquidNaCl(liquidMaterialQInputSection.multiply(BigDecimal.valueOf(0.197)));
                liquidMaterial.setLiquidCaSO4(liquidMaterialQInputSection.multiply(BigDecimal.valueOf(0.004)));

                liquidQResultValue.setText(liquidMaterialQInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidH2OResultValue.setText(liquidMaterial.getLiquidH2O().setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidKclResultValue.setText(liquidMaterial.getLiquidKCl().setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidNaclResultValue.setText(liquidMaterial.getLiquidNaCl().setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidCaso4ResultValue.setText(liquidMaterial.getLiquidCaSO4().setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                BigDecimal LiquidNaClRatioVishelachivanie = BigDecimal.valueOf(0.197 / 0.678);
                BigDecimal LiquidCaso4RatioVishelachivanie = BigDecimal.valueOf(0.004 / 0.678);
                BigDecimal LiquidH2OAmountVishelachivanie = liquidMaterial.getLiquidH2O().add(liquid.getH2O());

                BigDecimal LiquidNaclAmount1Vishelachivanie = LiquidH2OAmountVishelachivanie.multiply(LiquidNaClRatioVishelachivanie);
                BigDecimal LiquidNaclAmount2Vishelachivanie = liquidMaterial.getLiquidNaCl().add(solidMaterial.getSolidNaCl()).add(liquid.getNaCl());
                BigDecimal LiquidNaclAmountVishelachivanie = LiquidNaclAmount1Vishelachivanie.min(LiquidNaclAmount2Vishelachivanie);

                BigDecimal LiquidCaso4Amount1Vishelachivanie = LiquidH2OAmountVishelachivanie.multiply(LiquidCaso4RatioVishelachivanie);
                BigDecimal LiquidCaso4Amount2Vishelachivanie = liquidMaterial.getLiquidCaSO4().add(solidMaterial.getSolidCaSO4()).add(liquid.getCaSO4());
                BigDecimal LiquidCaso4AmountVishelachivanie = LiquidCaso4Amount1Vishelachivanie.min(LiquidCaso4Amount2Vishelachivanie);

                BigDecimal LiquidKclAmount1Vishelachivanie = (
                        LiquidNaclAmountVishelachivanie.divide(LiquidH2OAmountVishelachivanie.add(LiquidCaso4AmountVishelachivanie), RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(-0.497825381))
                                .add(BigDecimal.valueOf(0.2648))
                ).divide(
                        BigDecimal.ONE.subtract(
                                LiquidNaclAmountVishelachivanie.divide(LiquidH2OAmountVishelachivanie.add(LiquidCaso4AmountVishelachivanie), RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(-0.497825381))
                                        .add(BigDecimal.valueOf(0.2648))
                        ),
                        RoundingMode.HALF_UP
                ).multiply(LiquidH2OAmountVishelachivanie.add(LiquidCaso4AmountVishelachivanie).add(LiquidNaclAmountVishelachivanie));

                BigDecimal LiquidKclAmount2Vishelachivanie = liquidMaterial.getLiquidKCl().add(solidMaterial.getSolidKCl()).add(liquid.getKCl());
                BigDecimal LiquidKclAmountVishelachivanie = LiquidKclAmount1Vishelachivanie.min(LiquidKclAmount2Vishelachivanie);


                BigDecimal wasteVishelachivanie = solidMaterial.getSolidWaste();
                BigDecimal SolidKclAmountVishelachivanie = solidMaterial.getSolidKCl()
                        .add(liquidMaterial.getLiquidKCl()).add(liquid.getKCl())
                        .subtract(LiquidKclAmountVishelachivanie).max(BigDecimal.ZERO);

                BigDecimal SolidNaclAmountVishelachivanie = solidMaterial.getSolidNaCl()
                        .add(liquidMaterial.getLiquidNaCl()).add(liquid.getNaCl())
                        .subtract(LiquidNaclAmountVishelachivanie).max(BigDecimal.ZERO);

                BigDecimal SolidCaso4AmountVishelachivanie = solidMaterial.getSolidCaSO4()
                        .add(liquidMaterial.getLiquidCaSO4()).add(liquid.getCaSO4())
                        .subtract(LiquidCaso4AmountVishelachivanie).max(BigDecimal.ZERO);

                BigDecimal liquidQVishelachivanie = LiquidH2OAmountVishelachivanie.add(LiquidNaclAmountVishelachivanie)
                        .add(LiquidCaso4AmountVishelachivanie).add(LiquidKclAmountVishelachivanie);
                BigDecimal solidQVishelachivanie = wasteVishelachivanie.add(SolidNaclAmountVishelachivanie)
                        .add(SolidCaso4AmountVishelachivanie).add(SolidKclAmountVishelachivanie);
                BigDecimal ratioVishelachivanie = liquidQVishelachivanie.divide(solidQVishelachivanie, RoundingMode.HALF_UP);

                BigDecimal  WasteCaso4Vishelachivanie = wasteVishelachivanie.add(SolidCaso4AmountVishelachivanie);



                BigDecimal LiquidH2OPercentVishelachivanie = LiquidH2OAmountVishelachivanie.divide(liquidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercentVishelachivanie = LiquidNaclAmountVishelachivanie.divide(liquidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4PercentVishelachivanie = LiquidCaso4AmountVishelachivanie.divide(liquidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                BigDecimal LiquidTotalPercentVishelachivanie = LiquidH2OPercentVishelachivanie.add(LiquidNaclPercentVishelachivanie).add(LiquidCaso4PercentVishelachivanie);

                BigDecimal LiquidKclPercentVishelachivanie = (BigDecimal.valueOf(100).subtract(LiquidTotalPercentVishelachivanie));


                BigDecimal SolidKclPercentVishelachivanie = SolidKclAmountVishelachivanie.divide(solidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercentVishelachivanie = SolidNaclAmountVishelachivanie.divide(solidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidTotalPercentVishelachivanie = SolidKclPercentVishelachivanie.add(SolidNaclPercentVishelachivanie);
                BigDecimal  WasteCaso4PercentVishelachivanie = (BigDecimal.valueOf(100).subtract(SolidTotalPercentVishelachivanie));

                vishelachivanie.setH2O(liquid.getH2O());
                vishelachivanie.setL_Q(liquidQVishelachivanie);
                vishelachivanie.setLiquidH2O(LiquidH2OAmountVishelachivanie);
                vishelachivanie.setLiquidKCl(LiquidKclAmountVishelachivanie);
                vishelachivanie.setLiquidNaCl(LiquidNaclAmountVishelachivanie);
                vishelachivanie.setLiquidCaSO4(LiquidCaso4AmountVishelachivanie);


                vishelachivanie.setS_Q(solidQVishelachivanie);
                vishelachivanie.setSolidKCl(SolidKclAmountVishelachivanie);
                vishelachivanie.setSolidNaCl(SolidNaclAmountVishelachivanie);
                vishelachivanie.setSolidCaSO4(SolidCaso4AmountVishelachivanie);
                vishelachivanie.setSolidWaste(wasteVishelachivanie);

                // Update labels with calculated values
                liquidQResultValueVishelachivanie.setText(liquidQVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidH2oResultValueVishelachivanie.setText(LiquidH2OAmountVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidKclResultValueVishelachivanie.setText(LiquidKclAmountVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidNaclResultValueVishelachivanie.setText(LiquidNaclAmountVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidCaso4ResultValueVishelachivanie.setText(LiquidCaso4AmountVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                solidQResultValueVishelachivanie.setText(solidQVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidKclResultValueVishelachivanie.setText(SolidKclAmountVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidNaclResultValueVishelachivanie.setText(SolidNaclAmountVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                wasteResultValueVishelachivanie.setText(WasteCaso4Vishelachivanie.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                ratioResultValueVishelachivanie.setText(ratioVishelachivanie.setScale(2, RoundingMode.HALF_UP).toString());


                liquidPieChartVishelachivanie.getData().clear();
                liquidPieChartVishelachivanie.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercentVishelachivanie), LiquidH2OPercentVishelachivanie.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercentVishelachivanie), LiquidNaclPercentVishelachivanie.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4PercentVishelachivanie), LiquidCaso4PercentVishelachivanie.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercentVishelachivanie), LiquidKclPercentVishelachivanie.doubleValue())
                );

                // Add data to solid PieChart
                solidPieChartVishelachivanie.getData().clear();
                solidPieChartVishelachivanie.getData().addAll(
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4PercentVishelachivanie), WasteCaso4PercentVishelachivanie.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(SolidKclPercentVishelachivanie), SolidKclPercentVishelachivanie.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(SolidNaclPercentVishelachivanie), SolidNaclPercentVishelachivanie.doubleValue())
                );

                // Get the value of liqRat from the text field
                BigDecimal LiqSolRatHydrocyclone = new BigDecimal(liqRatTextFieldHydrocyclone.getText());
                BigDecimal SolQuartRatioHydrocyclone = new BigDecimal(solQuartRatioTextFieldHydrocyclone.getText()).divide(BigDecimal.valueOf(100));
                hydrocycloneSolid.setSolQuartRatio(SolQuartRatioHydrocyclone);
                hydrocycloneSolid.setLiqSolRat(LiqSolRatHydrocyclone);


                BigDecimal ssolidQHydrocyclone = BigDecimal.ONE.subtract(SolQuartRatioHydrocyclone).multiply(vishelachivanie.getS_Q());
                BigDecimal sliquidQHydrocyclone = ssolidQHydrocyclone.multiply(LiqSolRatHydrocyclone);

                // Calculation logic here (use liqRat in the formulas)
                BigDecimal sLiquidH2OAmountHydrocyclone = sliquidQHydrocyclone.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidH2O());
                BigDecimal sLiquidNaclAmountHydrocyclone = sliquidQHydrocyclone.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidNaCl());
                BigDecimal sLiquidCaso4AmountHydrocyclone = sliquidQHydrocyclone.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidCaSO4());
                BigDecimal sLiquidKclAmountHydrocyclone = sliquidQHydrocyclone.divide(vishelachivanie.getL_Q(), RoundingMode.HALF_UP).multiply(vishelachivanie.getLiquidKCl());

                BigDecimal swasteHydrocyclone = BigDecimal.ONE.subtract(SolQuartRatioHydrocyclone).multiply(vishelachivanie.getSolidWaste());
                BigDecimal sSolidKclAmountHydrocyclone = BigDecimal.ONE.subtract(SolQuartRatioHydrocyclone).multiply(vishelachivanie.getSolidKCl());
                BigDecimal sSolidNaclAmountHydrocyclone = BigDecimal.ONE.subtract(SolQuartRatioHydrocyclone).multiply(vishelachivanie.getSolidNaCl());
                BigDecimal sSolidCaso4AmountHydrocyclone = BigDecimal.ONE.subtract(SolQuartRatioHydrocyclone).multiply(vishelachivanie.getSolidCaSO4());
                BigDecimal sWasteCaso4Hydrocyclone = swasteHydrocyclone.add(sSolidCaso4AmountHydrocyclone);



                hydrocycloneSolid.setH2O(vishelachivanie.getH2O());
                hydrocycloneSolid.setL_Q(sliquidQHydrocyclone);
                hydrocycloneSolid.setLiquidH2O(sLiquidH2OAmountHydrocyclone);
                hydrocycloneSolid.setLiquidKCl(sLiquidKclAmountHydrocyclone);
                hydrocycloneSolid.setLiquidNaCl(sLiquidNaclAmountHydrocyclone);
                hydrocycloneSolid.setLiquidCaSO4(sLiquidCaso4AmountHydrocyclone);

                hydrocycloneSolid.setS_Q(ssolidQHydrocyclone);
                hydrocycloneSolid.setSolidKCl(sSolidKclAmountHydrocyclone);
                hydrocycloneSolid.setSolidNaCl(sSolidNaclAmountHydrocyclone);
                hydrocycloneSolid.setSolidCaSO4(sSolidCaso4AmountHydrocyclone);
                hydrocycloneSolid.setSolidWaste(swasteHydrocyclone);



                //Update labels with calculated values
                solidLiquidQResultValueHydrocyclone.setText(sliquidQHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidLiquidH2oResultValueHydrocyclone.setText(sLiquidH2OAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidLiquidKclResultValueHydrocyclone.setText(sLiquidKclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidLiquidNaclResultValueHydrocyclone.setText(sLiquidNaclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidLiquidCaso4ResultValueHydrocyclone.setText(sLiquidCaso4AmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                solidSolidQResultValueHydrocyclone.setText(ssolidQHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidSolidKclResultValueHydrocyclone.setText(sSolidKclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidSolidNaclResultValueHydrocyclone.setText(sSolidNaclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                solidWasteResultValueHydrocyclone.setText(sWasteCaso4Hydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");






                hydrocycloneLiquid.setSolQuartRatio(SolQuartRatioHydrocyclone);
                hydrocycloneLiquid.setLiqSolRat(LiqSolRatHydrocyclone);

                BigDecimal solidQHydrocyclone = vishelachivanie.getS_Q().multiply(SolQuartRatioHydrocyclone);
                BigDecimal liquidQHydrocyclone = vishelachivanie.getL_Q().subtract(hydrocycloneSolid.getL_Q());


                BigDecimal wasteHydrocyclone = vishelachivanie.getSolidWaste().multiply(SolQuartRatioHydrocyclone);

                BigDecimal SolidKclAmountHydrocyclone = vishelachivanie.getSolidKCl().multiply(SolQuartRatioHydrocyclone);
                BigDecimal SolidNaclAmountHydrocyclone = vishelachivanie.getSolidNaCl().multiply(SolQuartRatioHydrocyclone);
                BigDecimal SolidCaso4AmountHydrocyclone = vishelachivanie.getSolidCaSO4().multiply(SolQuartRatioHydrocyclone);
                BigDecimal WasteCaso4Hydrocyclone = wasteHydrocyclone.add(SolidCaso4AmountHydrocyclone);

                BigDecimal LiquidH2OAmountHydrocyclone = vishelachivanie.getLiquidH2O().subtract(hydrocycloneSolid.getLiquidH2O());
                BigDecimal LiquidNaclAmountHydrocyclone = vishelachivanie.getLiquidNaCl().subtract(hydrocycloneSolid.getLiquidNaCl());
                BigDecimal LiquidCaso4AmountHydrocyclone = vishelachivanie.getLiquidCaSO4().subtract(hydrocycloneSolid.getLiquidCaSO4());
                BigDecimal LiquidKclAmountHydrocyclone = vishelachivanie.getLiquidKCl().subtract(hydrocycloneSolid.getLiquidKCl());

                hydrocycloneLiquid.setH2O(hydrocycloneSolid.getH2O());
                hydrocycloneLiquid.setL_Q(liquidQHydrocyclone);
                hydrocycloneLiquid.setLiquidH2O(LiquidH2OAmountHydrocyclone);
                hydrocycloneLiquid.setLiquidKCl(LiquidKclAmountHydrocyclone);
                hydrocycloneLiquid.setLiquidNaCl(LiquidNaclAmountHydrocyclone);
                hydrocycloneLiquid.setLiquidCaSO4(LiquidCaso4AmountHydrocyclone);

                hydrocycloneLiquid.setS_Q(solidQHydrocyclone);
                hydrocycloneLiquid.setSolidKCl(SolidKclAmountHydrocyclone);
                hydrocycloneLiquid.setSolidNaCl(SolidNaclAmountHydrocyclone);
                hydrocycloneLiquid.setSolidCaSO4(SolidCaso4AmountHydrocyclone);
                hydrocycloneLiquid.setSolidWaste(wasteHydrocyclone);





                //Update labels with calculated values
                liquidLiquidQResultValue.setText(liquidQHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidLiquidH2oResultValue.setText(LiquidH2OAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidLiquidKclResultValue.setText(LiquidKclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidLiquidNaclResultValue.setText(LiquidNaclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidLiquidCaso4ResultValue.setText(LiquidCaso4AmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                liquidSolidQResultValueHydrocyclone.setText(solidQHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidSolidKclResultValueHydrocyclone.setText(SolidKclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidSolidNaclResultValueHydrocyclone.setText(SolidNaclAmountHydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                liquidWasteResultValueHydrocyclone.setText(WasteCaso4Hydrocyclone.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                BigDecimal LiquidH2OPercentHydrocyclone = sLiquidH2OAmountHydrocyclone.divide(sliquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercentHydrocyclone = sLiquidNaclAmountHydrocyclone.divide(sliquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4PercentHydrocyclone = sLiquidCaso4AmountHydrocyclone.divide(sliquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                BigDecimal LiquidTotalPercentHydrocyclone = LiquidH2OPercentHydrocyclone.add(LiquidNaclPercentHydrocyclone).add(LiquidCaso4PercentHydrocyclone);

                BigDecimal LiquidKclPercentHydrocyclone = (BigDecimal.valueOf(100).subtract(LiquidTotalPercentHydrocyclone));


                BigDecimal SolidKclPercentHydrocyclone = sSolidKclAmountHydrocyclone.divide(ssolidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercentHydrocyclone = sSolidNaclAmountHydrocyclone.divide(ssolidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidTotalPercentHydrocyclone = SolidKclPercentHydrocyclone.add(SolidNaclPercentHydrocyclone);
                BigDecimal  WasteCaso4PercentHydrocyclone = (BigDecimal.valueOf(100).subtract(SolidTotalPercentHydrocyclone));





                BigDecimal LiquidH2OPercent2Hydrocyclone = LiquidH2OAmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercent2Hydrocyclone = LiquidNaclAmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4Percent2Hydrocyclone = LiquidCaso4AmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                BigDecimal LiquidTotalPercent2Hydrocyclone = LiquidH2OPercent2Hydrocyclone.add(LiquidNaclPercent2Hydrocyclone).add(LiquidCaso4Percent2Hydrocyclone);

                BigDecimal LiquidKclPercent2Hydrocyclone = (BigDecimal.valueOf(100).subtract(LiquidTotalPercent2Hydrocyclone));



                BigDecimal SolidKclPercent2Hydrocyclone = solidQHydrocyclone.compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : SolidKclAmountHydrocyclone.divide(solidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercent2Hydrocyclone = solidQHydrocyclone.compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : SolidNaclAmountHydrocyclone.divide(solidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));



                BigDecimal  WasteCaso4Percent2Hydrocyclone = WasteCaso4Hydrocyclone.compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : WasteCaso4Hydrocyclone.divide(solidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));




                liquidPieChartHydrocyclone.getData().clear();
                liquidPieChartHydrocyclone.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercentHydrocyclone), LiquidH2OPercentHydrocyclone.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercentHydrocyclone), LiquidNaclPercentHydrocyclone.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4PercentHydrocyclone), LiquidCaso4PercentHydrocyclone.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercentHydrocyclone), LiquidKclPercentHydrocyclone.doubleValue())
                );

                // Add data to solid PieChart
                solidPieChartHydrocyclone.getData().clear();
                solidPieChartHydrocyclone.getData().addAll(
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4PercentHydrocyclone), WasteCaso4PercentHydrocyclone.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(SolidKclPercentHydrocyclone), SolidKclPercentHydrocyclone.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(SolidNaclPercentHydrocyclone), SolidNaclPercentHydrocyclone.doubleValue())
                );

                liquidPieChart2Hydrocyclone.getData().clear();
                liquidPieChart2Hydrocyclone.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercent2Hydrocyclone), LiquidH2OPercent2Hydrocyclone.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercent2Hydrocyclone), LiquidNaclPercent2Hydrocyclone.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4Percent2Hydrocyclone), LiquidCaso4Percent2Hydrocyclone.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercent2Hydrocyclone), LiquidKclPercent2Hydrocyclone.doubleValue())
                );

                solidPieChart2Hydrocyclone.getData().clear();
                solidPieChart2Hydrocyclone.getData().addAll(
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4Percent2Hydrocyclone), WasteCaso4Percent2Hydrocyclone.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(SolidKclPercent2Hydrocyclone), SolidKclPercent2Hydrocyclone.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(SolidNaclPercent2Hydrocyclone), SolidNaclPercent2Hydrocyclone.doubleValue())
                );
            } catch (NumberFormatException e) {
                showError("Введите корректные значения, Ж/Т это десятичная дробь через точку.");
            }
        });

        return grid;
    }

    String formatPercent(BigDecimal value) {
        return String.format("%.2f%%", value.doubleValue());
    }




    private GridPane createCentrifugeSection(HydrocycloneSolid hydrocycloneSolid, CentrifugeSolid centrifugeSolid, CentrifugeLiquid centrifugeLiquid) {
        GridPane grid = createAlignedGridPane(8);

        grid.setVgap(5); // Set vertical gap to 5 pixels (default is usually larger)
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        Label solidTitle = new Label("Кек центрифуги");
        Button calculateButton = new Button("Рассчитать работу центрифуги");
        Label liquidHeader = new Label("Жидкая фаза");
        Label solidHeader = new Label("Твёрдая фаза");

// Liquid result labels
        Label liquidQResultLabel = new Label("Q:");
        Label liquidH2oResultLabel = new Label("H2O:");
        Label liquidKclResultLabel = new Label("KCl:");
        Label liquidNaclResultLabel = new Label("NaCl:");
        Label liquidCaso4ResultLabel = new Label("CaSO4:");
        Label liquidQResultValue = new Label();
        Label liquidH2oResultValue = new Label();
        Label liquidKclResultValue = new Label();
        Label liquidNaclResultValue = new Label();
        Label liquidCaso4ResultValue = new Label();

// Solid result labels
        Label solidQResultLabel = new Label("Q:");
        Label solidKclResultLabel = new Label("KCl:");
        Label solidNaclResultLabel = new Label("NaCl:");
        Label wasteResultLabel = new Label("H.O. + CaSO4:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();

// Add text field for user input of liqRat (Ж/Т)
        TextField liqRatTextField = new TextField();
        liqRatTextField.setPromptText("Введите влажность, %:"); // Hint for the input field

// Add headers
        grid.add(solidTitle, 0, 0);
        grid.add(liquidHeader, 0, 1);
        grid.add(solidHeader, 2, 1);

// Add liquid result labels to the first column
        grid.add(liquidQResultLabel, 0, 2);
        grid.add(liquidQResultValue, 1, 2);
        grid.add(liquidH2oResultLabel, 0, 3);
        grid.add(liquidH2oResultValue, 1, 3);
        grid.add(liquidKclResultLabel, 0, 4);
        grid.add(liquidKclResultValue, 1, 4);
        grid.add(liquidNaclResultLabel, 0, 5);
        grid.add(liquidNaclResultValue, 1, 5);
        grid.add(liquidCaso4ResultLabel, 0, 6);
        grid.add(liquidCaso4ResultValue, 1, 6);
        PieChart liquidPieChart = new PieChart();
        liquidPieChart.setTitle("Жидкая фаза");


        PieChart solidPieChart = new PieChart();
        solidPieChart.setTitle("Твёрдая фаза");

        grid.add(liquidPieChart, 0, 7, 2, 2);
        grid.add(solidPieChart, 2, 7, 2, 2);

// Add solid result labels to the second column
        grid.add(solidQResultLabel, 2, 2);
        grid.add(solidQResultValue, 3, 2);
        grid.add(solidKclResultLabel, 2, 3);
        grid.add(solidKclResultValue, 3, 3);
        grid.add(solidNaclResultLabel, 2, 4);
        grid.add(solidNaclResultValue, 3, 4);
        grid.add(wasteResultLabel, 2, 5);
        grid.add(wasteResultValue, 3, 5);



// Add input field for liqRat (Ж/Т)
        grid.add(liqRatTextField, 0, 21);


        Label LliquidTitle = new Label("Фугат центрифуги");
        Label LliquidHeader = new Label("Жидкая фаза");

        PieChart liquidPieChart2 = new PieChart();
        liquidPieChart2.setTitle("Фугат");

        // Liquid result labels
        Label LliquidQResultLabel = new Label("Q:");
        Label LliquidH2oResultLabel = new Label("H2O:");
        Label LliquidKclResultLabel = new Label("KCl:");
        Label LliquidNaclResultLabel = new Label("NaCl:");
        Label LliquidCaso4ResultLabel = new Label("CaSO4:");
        Label LliquidQResultValue = new Label();
        Label LliquidH2oResultValue = new Label();
        Label LliquidKclResultValue = new Label();
        Label LliquidNaclResultValue = new Label();
        Label LliquidCaso4ResultValue = new Label();



        grid.add(LliquidTitle, 0, 12);
        // Add headers
        grid.add(LliquidHeader, 0, 13);

        // Add liquid result labels to the first column
        grid.add(LliquidQResultLabel,  0, 14);
        grid.add(LliquidQResultValue, 1, 14);
        grid.add(LliquidH2oResultLabel, 0, 15);
        grid.add(LliquidH2oResultValue, 1, 15);
        grid.add(LliquidKclResultLabel, 0, 16);
        grid.add(LliquidKclResultValue, 1, 16);
        grid.add(LliquidNaclResultLabel, 0, 17);
        grid.add(LliquidNaclResultValue, 1, 17);
        grid.add(LliquidCaso4ResultLabel, 0, 18);
        grid.add(LliquidCaso4ResultValue, 1, 18);

        grid.add(liquidPieChart2, 0, 19, 2, 2);
        // Add calculate button spanning both columns
        grid.add(calculateButton, 0, 22, 2, 1);

        calculateButton.setOnAction(event -> {
            try {
                // Get the value of liqRat from the text field


                BigDecimal LiqSolRat = new BigDecimal(liqRatTextField.getText());
                LiqSolRat = LiqSolRat.divide(BigDecimal.valueOf(100));
                centrifugeSolid.setLiqSolRat(LiqSolRat);

                BigDecimal solidQ = hydrocycloneSolid.getS_Q();

                BigDecimal liquidQ = solidQ.multiply(LiqSolRat);
                BigDecimal LiquidH2OAmount = liquidQ.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidH2O());
                BigDecimal LiquidNaclAmount = liquidQ.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidNaCl());
                BigDecimal LiquidCaso4Amount = liquidQ.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidCaSO4());
                BigDecimal LiquidKclAmount = liquidQ.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidKCl());

                BigDecimal waste = hydrocycloneSolid.getSolidWaste();
                BigDecimal SolidKclAmount = hydrocycloneSolid.getSolidKCl();
                BigDecimal SolidNaclAmount = hydrocycloneSolid.getSolidNaCl();
                BigDecimal SolidCaso4Amount = hydrocycloneSolid.getSolidCaSO4();
                BigDecimal WasteCaso4 = waste.add(SolidCaso4Amount);

                BigDecimal  lliquidQ = hydrocycloneSolid.getL_Q().subtract(liquidQ);
                BigDecimal lLiquidH2OAmount = hydrocycloneSolid.getLiquidH2O().subtract(LiquidH2OAmount);
                BigDecimal lLiquidNaclAmount = hydrocycloneSolid.getLiquidNaCl().subtract(LiquidNaclAmount);
                BigDecimal lLiquidCaso4Amount = hydrocycloneSolid.getLiquidCaSO4().subtract(LiquidCaso4Amount);
                BigDecimal lLiquidKclAmount = hydrocycloneSolid.getLiquidKCl().subtract(LiquidKclAmount);


                centrifugeLiquid.setL_Q(lliquidQ);
                centrifugeLiquid.setLiquidH2O(lLiquidH2OAmount);
                centrifugeLiquid.setLiquidKCl(lLiquidKclAmount);
                centrifugeLiquid.setLiquidNaCl(lLiquidNaclAmount);
                centrifugeLiquid.setLiquidCaSO4(lLiquidCaso4Amount);


                centrifugeSolid.setLiqSolRat(LiqSolRat);
                centrifugeSolid.setL_Q(liquidQ);
                centrifugeSolid.setLiquidH2O(LiquidH2OAmount);
                centrifugeSolid.setLiquidKCl(LiquidKclAmount);
                centrifugeSolid.setLiquidNaCl(LiquidNaclAmount);
                centrifugeSolid.setLiquidCaSO4(LiquidCaso4Amount);


                centrifugeSolid.setS_Q(solidQ);
                centrifugeSolid.setSolidKCl(SolidKclAmount);
                centrifugeSolid.setSolidNaCl(SolidNaclAmount);
                centrifugeSolid.setSolidCaSO4(SolidCaso4Amount);
                centrifugeSolid.setSolidWaste(waste);

                BigDecimal LiquidH2OPercent = LiquidH2OAmount.divide(liquidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercent = LiquidNaclAmount.divide(liquidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4Percent = LiquidCaso4Amount.divide(liquidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                BigDecimal LiquidTotalPercent = LiquidH2OPercent.add(LiquidNaclPercent).add(LiquidCaso4Percent);

                BigDecimal LiquidKclPercent = (BigDecimal.valueOf(100).subtract(LiquidTotalPercent));


                BigDecimal SolidKclPercent = SolidKclAmount.divide(solidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercent = SolidNaclAmount.divide(solidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidTotalPercent = SolidKclPercent.add(SolidNaclPercent);
                BigDecimal  WasteCaso4Percent = (BigDecimal.valueOf(100).subtract(SolidTotalPercent));


                BigDecimal LiquidH2OPercent2 = lLiquidH2OAmount.divide(lliquidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercent2 = lLiquidNaclAmount.divide(lliquidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4Percent2 = lLiquidCaso4Amount.divide(lliquidQ, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                BigDecimal LiquidTotalPercent2 = LiquidH2OPercent2.add(LiquidNaclPercent2).add(LiquidCaso4Percent2);

                BigDecimal LiquidKclPercent2 = (BigDecimal.valueOf(100).subtract(LiquidTotalPercent2));

                liquidPieChart.getData().clear();
                liquidPieChart.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercent), LiquidH2OPercent.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercent), LiquidNaclPercent.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4Percent), LiquidCaso4Percent.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercent), LiquidKclPercent.doubleValue())
                );

                // Add data to solid PieChart
                solidPieChart.getData().clear();
                solidPieChart.getData().addAll(
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4Percent), WasteCaso4Percent.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(SolidKclPercent), SolidKclPercent.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(SolidNaclPercent), SolidNaclPercent.doubleValue())
                );

                liquidQResultValue.setText(liquidQ.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidH2oResultValue.setText(LiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidNaclResultValue.setText(LiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidKclResultValue.setText(LiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidCaso4ResultValue.setText(LiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");


                LliquidQResultValue.setText(lliquidQ.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                LliquidH2oResultValue.setText(lLiquidH2OAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                LliquidKclResultValue.setText(lLiquidKclAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");      //Update labels with calculated values
                LliquidNaclResultValue.setText(lLiquidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                LliquidCaso4ResultValue.setText(lLiquidCaso4Amount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");


                solidQResultValue.setText(solidQ.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidKclResultValue.setText(SolidKclAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidNaclResultValue.setText(SolidNaclAmount.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                wasteResultValue.setText(WasteCaso4.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");

                liquidPieChart2.getData().clear();
                liquidPieChart2.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercent2), LiquidH2OPercent2.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercent2), LiquidNaclPercent2.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4Percent2), LiquidCaso4Percent2.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercent2), LiquidKclPercent2.doubleValue())
                );


            } catch (NumberFormatException e) {
                showError("Ошибка");
            }
        });

        return grid;
    }


    private GridPane createSushkaSection(LiquidMaterial liquidMaterial, SolidMaterial solidMaterial, CentrifugeSolid centrifugeSolid, Sushka sushka) {
        GridPane grid = createAlignedGridPane(8);
        grid.setHgap(5); // Horizontal spacing
        grid.setVgap(5); // Vertical spacing
        grid.setPadding(new Insets(5));


        Button calculateButton = new Button("Рассчитать готовый продукт");
        Label solidQResultLabel = new Label("Q:");
        Label solidKclResultLabel = new Label("KCl:");
        Label solidNaclResultLabel = new Label("NaCl:");
        Label wasteResultLabel = new Label("H.O. + CaSO4:");
        Label h2oResultLabel = new Label("H20:");
        Label ExtractionResultLabel = new Label("Извлечение узла:");
        Label solidQResultValue = new Label();
        Label solidKclResultValue = new Label();
        Label solidNaclResultValue = new Label();
        Label wasteResultValue = new Label();
        Label h2oResultValue = new Label();
        Label ExtractionResultValue = new Label();



        grid.add(calculateButton, 0, 9, 2, 1);
        grid.add(solidQResultLabel, 0, 0);
        grid.add(solidQResultValue, 1, 0);
        grid.add(solidKclResultLabel, 0, 1);
        grid.add(solidKclResultValue, 1, 1);
        grid.add(solidNaclResultLabel, 0, 2);
        grid.add(solidNaclResultValue, 1, 2);
        grid.add(wasteResultLabel, 0, 3);
        grid.add(wasteResultValue, 1, 3);
        grid.addRow(4, h2oResultLabel, h2oResultValue);
        grid.add(ExtractionResultLabel, 0, 5);
        grid.add(ExtractionResultValue, 1, 5);

        PieChart finalPieChart = new PieChart();
        finalPieChart.setTitle("Готовый продукт");

        grid.add(finalPieChart, 0, 7, 2, 2);
        calculateButton.setOnAction(event -> {
            try {

                BigDecimal gp = BigDecimal.valueOf(0.005);
                BigDecimal kcl = centrifugeSolid.getSolidKCl().add(centrifugeSolid.getLiquidKCl());
                BigDecimal nacl = centrifugeSolid.getSolidNaCl().add(centrifugeSolid.getLiquidNaCl());
                BigDecimal caso4 = centrifugeSolid.getSolidCaSO4().add(centrifugeSolid.getLiquidCaSO4());
                BigDecimal waste = centrifugeSolid.getSolidWaste();
                BigDecimal Sum = kcl.add(nacl).add(caso4).add(waste);
                BigDecimal h2o1 = BigDecimal.ONE.subtract(gp);
                BigDecimal h2o2 = Sum.divide(h2o1, RoundingMode.HALF_UP);;
                BigDecimal h2o = h2o2.multiply(gp);
                BigDecimal Q = Sum.add(h2o);
                BigDecimal ExtractionSum = liquidMaterial.getLiquidKCl().add(solidMaterial.getSolidKCl());
                BigDecimal Extration = kcl.divide(ExtractionSum, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal H2OPercent = h2o.divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal NaclPercent = nacl.divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal KclPercent = kcl.divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal TotalPercent = KclPercent.add(NaclPercent).add(H2OPercent);
                BigDecimal  WasteCaso4Percent = (BigDecimal.valueOf(100).subtract(TotalPercent));


                finalPieChart.getData().clear();
                finalPieChart.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(H2OPercent), H2OPercent.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(NaclPercent), NaclPercent.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(KclPercent), KclPercent.doubleValue()),
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4Percent), WasteCaso4Percent.doubleValue())
                );

                sushka.setQ(Q);
                sushka.setKCl(kcl);
                sushka.setNaCl(nacl);
                sushka.setCaSO4(caso4);
                sushka.setWaste(waste);
                sushka.setH2O(h2o);

                ExtractionResultValue.setText(Extration.setScale(2, RoundingMode.HALF_UP).toString()+ " %");
                solidQResultValue.setText(Q.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidKclResultValue.setText(kcl.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidNaclResultValue.setText(nacl.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                wasteResultValue.setText(waste.add(caso4).setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                h2oResultValue.setText(h2o.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");

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
