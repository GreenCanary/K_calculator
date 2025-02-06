package com.mike.kcl;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Pos;
import javafx.stage.WindowEvent;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Application extends javafx.application.Application {

    Preferences prefs = Preferences.userNodeForPackage(Application.class);
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
        layout.setPadding(new Insets(5));
        layout.setAlignment(Pos.TOP_CENTER); // Center-align the components
        layout.setStyle("-fx-background-color: #151515;"); // Light gray background


        // Sections
        layout.getChildren().addAll(
                createSection("KCl", createCombinedSection(liquid,solidMaterial, liquidMaterial, vishelachivanie, hydrocycloneSolid, hydrocycloneLiquid, centrifugeSolid, centrifugeLiquid, sushka)));

        // ScrollPane for better navigation
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Ensures content scales with window width
        scrollPane.setStyle("-fx-background: transparent;"); // Transparent scroll background


        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY(); // Get the scroll delta (distance)
            double multiplier = 2.0; // Adjust this to increase scroll speed
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY * multiplier / scrollPane.getHeight());
            event.consume(); // Prevent default scrolling behavior
        });

        // Scene and Stage setup
        Scene scene = new Scene(scrollPane, 1600, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("К-калькулятор");
        primaryStage.setFullScreen(true);

        // Responsiveness: Adjust VBox size to window size
        layout.prefWidthProperty().bind(scene.widthProperty().multiply(2)); // Dynamic width
        layout.prefHeightProperty().bind(scene.heightProperty().multiply(2)); // Dynamic height


        // Load saved values

        primaryStage.show();
    }




    private TitledPane createSection(String title, GridPane content) {
        TitledPane section = new TitledPane();
        section.setText(title);
        section.setContent(content);
        section.setCollapsible(false);
        return section;
    }
    private void updateHints(TextField[] fields) {
        double total = 0;

        for (TextField field : fields) {
            String text = field.getText();
            if (!text.isEmpty()) {
                try {
                    total += Double.parseDouble(text.replace("%", ""));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        double remaining = Math.max(0, 100 - total);
        int emptyFields = 0;

        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                emptyFields++;
            }
        }

        for (TextField field : fields) {
            if (field.getText().isEmpty() && emptyFields > 0) {
                field.setPromptText(String.format("%.2f%%", remaining / emptyFields));
            } else {
                field.setPromptText("");
            }
        }
    }



    private GridPane createCombinedSection(Liquid liquid, SolidMaterial solidMaterial, LiquidMaterial liquidMaterial, Vishelachivanie vishelachivanie, HydrocycloneSolid hydrocycloneSolid, HydrocycloneLiquid hydrocycloneLiquid, CentrifugeSolid centrifugeSolid, CentrifugeLiquid centrifugeLiquid, Sushka sushka) {
        GridPane grid = createAlignedGridPane(7);
        // Layout
        grid.setHgap(2); // Horizontal spacing
        grid.setVgap(5); // Vertical spacing
        grid.setPadding(new Insets(5));


        // Section 1: Liquid Section
        Label liquidTitleLabel = new Label("Вода на выщелачивание");
        liquidTitleLabel.setStyle("-fx-background-color: #EEEEEE; -fx-font-size: 20px; -fx-text-fill: black;");
        TextField liquidQInput = new TextField();
        TextField Ginput = new TextField();
        Label h2oResultLabel = new Label("Q, т/ч:");
        Label h2oResultValue = new Label();

// Section 2: Solid Material Section
        Label solidMaterialTitleLabel = new Label("Концентрат на выщелачивание");
        solidMaterialTitleLabel.setStyle("-fx-background-color: #EEEEEE; -fx-font-size: 20px;-fx-text-fill: black;");
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
        liquidMaterialTitleLabel.setStyle("-fx-background-color: #EEEEEE; -fx-font-size: 20px; -fx-text-fill: black;");
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
        calculateButton.setStyle("-fx-background-color: green; -fx-text-fill: #EEEEEE; -fx-text-fill: black;");
//

        Label Qruda = new Label("Q, т/ч");
        Label KCl = new Label("KCl, %");
        Label NaCl = new Label("NaCl, %");
        Label CaSO4 = new Label("CaSO4, %");
        Label MatochinkJT = new Label("Ж/Т, маточник");
        Label Qvoda = new Label("q, кг/т");
        Label G = new Label("G, т/ч");



        grid.add(Qruda, 1, 4);
        grid.add(KCl, 1, 5);
        grid.add(NaCl, 1, 6);
        grid.add(CaSO4, 1, 7);
        grid.add(MatochinkJT, 3, 1);
        grid.add(Qvoda, 5, 1);
        grid.add(G, 5, 2);

// Liquid Section
        int liquidColumn = 4;
        int rowIndex = 0;
        grid.add(liquidTitleLabel, liquidColumn, rowIndex++, 2, 1); // Spanning two columns

// Removing "Q, т/ч:" label and using it as a hint in the TextField
        liquidQInput.setPromptText("Расход воды в кг на тонну конц.");
        Ginput.setPromptText("Производительность по конц.");
        grid.add(liquidQInput, liquidColumn, rowIndex++);
        grid.add(Ginput, liquidColumn, rowIndex++);
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
        grid.add(kclResultLabel, solidColumn, rowIndex);
        grid.add(kclResultValue, solidColumn + 1, rowIndex++);
        grid.add(naclResultLabel, solidColumn, rowIndex);
        grid.add(naclResultValue, solidColumn + 1, rowIndex++);
        grid.add(combinedResultLabel, solidColumn, rowIndex);
        grid.add(combinedResultValue, solidColumn + 1, rowIndex++);

        grid.add(solidQInput, solidColumn, rowIndex++);
        grid.add(KClPercentInput, solidColumn, rowIndex++);
        grid.add(NaClPercentInput, solidColumn, rowIndex++);
        grid.add(CaSO4PercentInput, solidColumn, rowIndex++);



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
        grid.add(calculateButton, 2, 8, 6, 1);


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
        liqRatTextFieldHydrocyclone.setPromptText("Ж/Т:");
        Label liqRatLabelHydrocyclone = new Label("Ж/Т, гидроциклон");

        TextField solQuartRatioTextFieldHydrocyclone = new TextField();
        solQuartRatioTextFieldHydrocyclone.setPromptText("Гидроциклон, отсеевание, %");
        Label solQuartRatioLabelHydrocyclone = new Label("Твёрдые менее 0,25мм, %");

        grid.add(liqRatLabelHydrocyclone, 1, 8);
        grid.add(liqRatTextFieldHydrocyclone, 0, 8);

        grid.add(solQuartRatioLabelHydrocyclone, 1, 9);
        grid.add(solQuartRatioTextFieldHydrocyclone, 0, 9);

        rowIndex = 12;
        Label titleVishelachivanie = new Label("Выщелачивание");
        titleVishelachivanie.setStyle("-fx-background-color: #EEEEEE;-fx-font-size: 20px; -fx-text-fill: black;");

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

        // Add ratio label spanning both columns
        grid.add(ratioResultLabelVishelachivanie, 2, ++rowIndex, 2, 1);
        grid.add(ratioResultValueVishelachivanie, 3, rowIndex, 2, 1);

        // Add calculate button spanning both columns
        PieChart liquidPieChartVishelachivanie = new PieChart();
        liquidPieChartVishelachivanie.setTitle("Жидкая фаза");
        liquidPieChartVishelachivanie.setLegendSide(Side.LEFT);
        liquidPieChartVishelachivanie.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        liquidPieChartVishelachivanie.setPrefWidth(10);
        liquidPieChartVishelachivanie.setPrefHeight(10);

        PieChart solidPieChartVishelachivanie = new PieChart();
        solidPieChartVishelachivanie.setTitle("Твёрдая фаза");
        solidPieChartVishelachivanie.setLegendSide(Side.LEFT);
        solidPieChartVishelachivanie.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        solidPieChartVishelachivanie.setPrefWidth(10);
        solidPieChartVishelachivanie.setPrefHeight(10);

        grid.add(liquidPieChartVishelachivanie, 0, ++rowIndex, 2, 1);
        grid.add(solidPieChartVishelachivanie, 2, rowIndex, 2, 1);



        Label titleHydrocyclone = new Label("Гидроциклон");
        titleHydrocyclone.setStyle("-fx-background-color: #EEEEEE;-fx-font-size: 20px; -fx-text-fill: black;");






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


        PieChart liquidPieChartHydrocyclone = new PieChart();
        liquidPieChartHydrocyclone.setTitle("Жидкая фаза (Пески гидроциклона)");
        liquidPieChartHydrocyclone.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        liquidPieChartHydrocyclone.setPrefWidth(10);
        liquidPieChartHydrocyclone.setPrefHeight(10);
        liquidPieChartHydrocyclone.setLegendSide(Side.LEFT);

        PieChart solidPieChartHydrocyclone = new PieChart();
        solidPieChartHydrocyclone.setTitle("Твёрдая фаза (Пески гидроциклона)");
        solidPieChartHydrocyclone.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        solidPieChartHydrocyclone.setPrefWidth(10);
        solidPieChartHydrocyclone.setPrefHeight(10);
        solidPieChartHydrocyclone.setLegendSide(Side.LEFT);

        PieChart liquidPieChart2Hydrocyclone = new PieChart();
        liquidPieChart2Hydrocyclone.setTitle("Жидкая фаза (Слив гидроциклона)");
        liquidPieChart2Hydrocyclone.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        liquidPieChart2Hydrocyclone.setPrefWidth(10);
        liquidPieChart2Hydrocyclone.setPrefHeight(10);
        liquidPieChart2Hydrocyclone.setLegendSide(Side.LEFT);


        PieChart solidPieChart2Hydrocyclone = new PieChart();
        solidPieChart2Hydrocyclone.setTitle("Твёрдая фаза (Слив гидроциклона)");
        solidPieChart2Hydrocyclone.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        solidPieChart2Hydrocyclone.setPrefWidth(10);
        solidPieChart2Hydrocyclone.setPrefHeight(10);
        solidPieChart2Hydrocyclone.setLegendSide(Side.LEFT);


        Label liquidHeaderHydrocyclone3 = new Label("Жидкая фаза");
        Label solidHeaderHydrocyclone3 = new Label("Твёрдая фаза");
        Label solidTitleCentrifuge = new Label("Кек центрифуги");
        Label liquidHeaderCentrifuge = new Label("Жидкая фаза");
        Label solidHeaderCentrifuge = new Label("Твёрдая фаза");

// Liquid result labels
        Label liquidQResultLabelCentrifuge = new Label("Q:");
        Label liquidH2oResultLabelCentrifuge = new Label("H2O:");
        Label liquidKclResultLabelCentrifuge = new Label("KCl:");
        Label liquidNaclResultLabelCentrifuge = new Label("NaCl:");
        Label liquidCaso4ResultLabelCentrifuge = new Label("CaSO4:");
        Label liquidQResultValueCentrifuge = new Label();
        Label liquidH2oResultValueCentrifuge = new Label();
        Label liquidKclResultValueCentrifuge = new Label();
        Label liquidNaclResultValueCentrifuge = new Label();
        Label liquidCaso4ResultValueCentrifuge = new Label();

// Solid result labels
        Label solidQResultLabelCentrifuge = new Label("Q:");
        Label solidKclResultLabelCentrifuge = new Label("KCl:");
        Label solidNaclResultLabelCentrifuge = new Label("NaCl:");
        Label wasteResultLabelCentrifuge = new Label("H.O. + CaSO4:");
        Label solidQResultValueCentrifuge = new Label();
        Label solidKclResultValueCentrifuge = new Label();
        Label solidNaclResultValueCentrifuge = new Label();
        Label wasteResultValueCentrifuge = new Label();

// Add text field for user input of liqRat (Ж/Т)
        TextField liqRatTextFieldCentrifuge = new TextField();
        liqRatTextFieldCentrifuge.setPromptText("Влажность кека, %"); // Hint for the input field

        Label Cetrifuge = new Label("Центрифуга");
        Cetrifuge.setStyle("-fx-background-color: #EEEEEE;-fx-font-size: 20px;-fx-text-fill: black;");
        PieChart liquidPieChartCentrifuge = new PieChart();
        liquidPieChartCentrifuge.setTitle("Жидкая фаза (Кек центрифуги)");
        liquidPieChartCentrifuge.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        liquidPieChartCentrifuge.setPrefWidth(10);
        liquidPieChartCentrifuge.setPrefHeight(10);
        liquidPieChartCentrifuge.setLegendSide(Side.LEFT);



        PieChart solidPieChartCentrifuge = new PieChart();
        solidPieChartCentrifuge.setTitle("Твёрдая фаза (Кек центрифуги)");
        solidPieChartCentrifuge.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        solidPieChartCentrifuge.setPrefWidth(10);
        solidPieChartCentrifuge.setPrefHeight(10);
        solidPieChartCentrifuge.setLegendSide(Side.LEFT);

        Label liqRatLabelCentrifuge = new Label("Влажность кека центрифуги, %");


        Label LliquidTitleCentrifuge = new Label("Фугат центрифуги");
        Label LliquidHeaderCentrifuge = new Label("Жидкая фаза");

        PieChart liquidPieChart2Centrifuge = new PieChart();
        liquidPieChart2Centrifuge.setTitle("Фугат");
        liquidPieChart2Centrifuge.setStyle("-fx-background-color: #C73659;-fx-text-fill: black;");
        liquidPieChart2Centrifuge.setPrefWidth(10);
        liquidPieChart2Centrifuge.setPrefHeight(10);
        liquidPieChart2Centrifuge.setLegendSide(Side.LEFT);

        // Liquid result labels
        Label LliquidQResultLabelCentrifuge = new Label("Q:");
        Label LliquidH2oResultLabelCentrifuge = new Label("H2O:");
        Label LliquidKclResultLabelCentrifuge = new Label("KCl:");
        Label LliquidNaclResultLabelCentrifuge = new Label("NaCl:");
        Label LliquidCaso4ResultLabelCentrifuge = new Label("CaSO4:");
        Label LliquidQResultValueCentrifuge = new Label();
        Label LliquidH2oResultValueCentrifuge = new Label();
        Label LliquidKclResultValueCentrifuge = new Label();
        Label LliquidNaclResultValueCentrifuge = new Label();
        Label LliquidCaso4ResultValueCentrifuge = new Label();

        Label Sushka = new Label("Сушка");


        Sushka.setStyle("-fx-background-color: #EEEEEE; -fx-font-size: 20px;-fx-text-fill: black;");

        Label solidQResultLabelSushka = new Label("Q:");
        Label solidKclResultLabelSushka = new Label("KCl:");
        Label solidNaclResultLabelSushka = new Label("NaCl:");
        Label wasteResultLabelSushka = new Label("H.O. + CaSO4:");
        Label h2oResultLabelSushka = new Label("H2O:");
        Label ExtractionResultLabelSushka = new Label("Извлечение узла:");
        Label solidQResultValueSushka = new Label();
        Label solidKclResultValueSushka = new Label();
        Label solidNaclResultValueSushka = new Label();
        Label wasteResultValueSushka = new Label();
        Label h2oResultValueSushka = new Label();
        Label ExtractionResultValueSushka = new Label();
        ExtractionResultValueSushka.setStyle("-fx-background-color: green; -fx-text-fill: #EEEEEE; -fx-text-fill: black;");
        PieChart finalPieChart = new PieChart();
        finalPieChart.setTitle("Готовый продукт");
        finalPieChart.setStyle("-fx-background-color: #C73659; -fx-text-fill: black;");
        finalPieChart.setPrefWidth(10);
        finalPieChart.setPrefHeight(10);
        finalPieChart.setLegendSide(Side.LEFT);



        Sushka.setStyle("-fx-background-color: #EEEEEE; -fx-font-size: 20px;-fx-text-fill: black;");


        Label ExtractionResultLabelSushka2 = new Label("Извлечение узла:");
        Label solidQResultValueSushka2 = new Label();
        Label solidKclResultValueSushka2 = new Label();
        Label solidNaclResultValueSushka2 = new Label();
        Label wasteResultValueSushka2 = new Label();
        Label h2oResultValueSushka2 = new Label();
        Label ExtractionResultValueSushka2 = new Label();
        PieChart finalPieChart2 = new PieChart();
        finalPieChart2.setTitle("Готовый продукт");
        finalPieChart2.setStyle("-fx-background-color: #C73659; -fx-text-fill: black;");
        finalPieChart2.setPrefWidth(10);
        finalPieChart2.setPrefHeight(10);



        Button saveButton = new Button("Сохранить данные");
        Button loadButton = new Button("Загрузить данные");
        Button resetButton = new Button("Очистить ячейки данных");

        saveButton.setStyle("-fx-background-color: gray; -fx-text-fill: #EEEEEE;-fx-text-fill: black;");
        loadButton.setStyle("-fx-background-color: gray; -fx-text-fill: #EEEEEE;-fx-text-fill: black;");
        resetButton.setStyle("-fx-background-color: gray; -fx-text-fill: #EEEEEE;-fx-text-fill: black;");

        TextField[] fields = {KClPercentInput, NaClPercentInput, CaSO4PercentInput};

        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> updateHints(fields));
        }
// --------------------- Adding UI Elements ---------------------


        grid.add(liqRatLabelCentrifuge, 1, 10);
        grid.add(liqRatTextFieldCentrifuge, 0, 10);


        grid.add(solidSolidQResultLabelHydrocyclone, 2, 38);
        grid.add(solidSolidQResultValueHydrocyclone, 3, 38);
        grid.add(solidSolidKclResultLabelHydrocyclone, 2, 39);
        grid.add(solidSolidKclResultValueHydrocyclone, 3, 39);
        grid.add(solidSolidNaclResultLabelHydrocyclone, 2, 40);
        grid.add(solidSolidNaclResultValueHydrocyclone, 3, 40);

        Label ratioLabelHydrocycloneSolid = new Label("Ж/Т:");
        Label ratioResultHydrocycloneSolid = new Label();

        Label ratioLabelHydrocycloneLuquid = new Label("Ж/Т:");
        Label ratioResultHydrocycloneLiquid = new Label();

        Label ratioLabelCentrifuge= new Label("Ж/Т:");
        Label ratioResultCentrifuge = new Label();

        grid.add(solidWasteResultLabelHydrocyclone, 2, 41);
        grid.add(solidWasteResultValueHydrocyclone, 3, 41);
        grid.add(ratioLabelHydrocycloneSolid, 2, 42);
        grid.add(ratioResultHydrocycloneSolid, 3, 42);


        grid.add(liquidPieChartHydrocyclone, 0, 43, 2, 1);


        grid.add(solidPieChartHydrocyclone, 2, 43, 2, 1);


        grid.add(titleHydrocyclone, 0, 35);

        grid.add(solidHeaderHydrocyclone, 0, 36, 4, 1);
        grid.add(liquidHeaderHydrocyclone2, 0, 37);
        grid.add(solidHeaderHydrocyclone2, 2, 37);

// Solid Section - Liquid Results
        grid.add(solidLiquidQResultLabelHydrocyclone, 0, 38);
        grid.add(solidLiquidQResultValueHydrocyclone, 1, 38);
        grid.add(solidLiquidH2oResultLabelHydrocyclone, 0, 39);
        grid.add(solidLiquidH2oResultValueHydrocyclone, 1, 39);
        grid.add(solidLiquidKclResultLabelHydrocyclone, 0, 40);
        grid.add(solidLiquidKclResultValueHydrocyclone, 1, 40);
        grid.add(solidLiquidNaclResultLabelHydrocyclone, 0, 41);
        grid.add(solidLiquidNaclResultValueHydrocyclone, 1, 41);
        grid.add(solidLiquidCaso4ResultLabelHydrocyclone, 0, 42);
        grid.add(solidLiquidCaso4ResultValueHydrocyclone, 1, 42);

        grid.add(liquidHeaderHydrocyclone, 0, 45, 4, 1);
        grid.add(liquidHeaderHydrocyclone3, 0, 46);
        grid.add(solidHeaderHydrocyclone3, 2, 46);

// Liquid Section - Liquid Results
        grid.add(liquidLiquidQResultLabel, 0, 47);
        grid.add(liquidLiquidQResultValue, 1, 47);
        grid.add(liquidLiquidH2oResultLabel, 0, 48);
        grid.add(liquidLiquidH2oResultValue, 1, 48);
        grid.add(liquidLiquidKclResultLabel, 0, 49);
        grid.add(liquidLiquidKclResultValue, 1, 49);
        grid.add(liquidLiquidNaclResultLabel, 0, 50);
        grid.add(liquidLiquidNaclResultValue, 1, 50);
        grid.add(liquidLiquidCaso4ResultLabel, 0, 51);
        grid.add(liquidLiquidCaso4ResultValue, 1, 51);

// Liquid Section - Solid Results
        grid.add(liquidSolidQResultLabelHydrocyclone, 2, 47);
        grid.add(liquidSolidQResultValueHydrocyclone, 3, 47);
        grid.add(liquidSolidKclResultLabelHydrocyclone, 2, 48);
        grid.add(liquidSolidKclResultValueHydrocyclone, 3, 48);
        grid.add(liquidSolidNaclResultLabelHydrocyclone, 2, 49);
        grid.add(liquidSolidNaclResultValueHydrocyclone, 3, 49);
        grid.add(liquidWasteResultLabelHydrocyclone, 2, 50);
        grid.add(liquidWasteResultValueHydrocyclone, 3, 50);

        grid.add(ratioLabelHydrocycloneLuquid, 2, 51);
        grid.add(ratioResultHydrocycloneLiquid, 3, 51);


        grid.add(liquidPieChart2Hydrocyclone, 0, 52, 2, 1);
        grid.add(solidPieChart2Hydrocyclone, 2, 52, 2, 1);



// Add headers
        grid.add(Cetrifuge, 0, 54);
        grid.add(solidTitleCentrifuge, 0, 55);
        grid.add(liquidHeaderCentrifuge, 0, 56);
        grid.add(solidHeaderCentrifuge, 2, 56);

// Add liquid result labels to the first column
        grid.add(liquidQResultLabelCentrifuge, 0, 57);
        grid.add(liquidQResultValueCentrifuge, 1, 57);
        grid.add(liquidH2oResultLabelCentrifuge, 0, 58);
        grid.add(liquidH2oResultValueCentrifuge, 1, 58);
        grid.add(liquidKclResultLabelCentrifuge, 0, 59);
        grid.add(liquidKclResultValueCentrifuge, 1, 59);
        grid.add(liquidNaclResultLabelCentrifuge, 0, 60);
        grid.add(liquidNaclResultValueCentrifuge, 1, 60);
        grid.add(liquidCaso4ResultLabelCentrifuge, 0, 61);
        grid.add(liquidCaso4ResultValueCentrifuge, 1, 61);

        grid.add(solidQResultLabelCentrifuge, 2, 57);
        grid.add(solidQResultValueCentrifuge, 3, 57);
        grid.add(solidKclResultLabelCentrifuge, 2, 58);
        grid.add(solidKclResultValueCentrifuge, 3, 58);
        grid.add(solidNaclResultLabelCentrifuge, 2, 59);
        grid.add(solidNaclResultValueCentrifuge, 3, 59);
        grid.add(wasteResultLabelCentrifuge, 2, 60);
        grid.add(wasteResultValueCentrifuge, 3, 60);
        grid.add(ratioLabelCentrifuge, 2, 61);
        grid.add(ratioResultCentrifuge, 3, 61);




        grid.add(liquidPieChartCentrifuge, 0, 62, 2, 1);
        grid.add(solidPieChartCentrifuge, 2, 62, 2, 1);


// Add solid result labels to the second column


// Add input field for liqRat (Ж/Т)




        grid.add(LliquidTitleCentrifuge, 0, 67);
        // Add headers
        grid.add(LliquidHeaderCentrifuge, 0, 68);

        // Add liquid result labels to the first column
        grid.add(LliquidQResultLabelCentrifuge,  0, 69);
        grid.add(LliquidQResultValueCentrifuge, 1, 69);
        grid.add(LliquidH2oResultLabelCentrifuge, 0, 70);
        grid.add(LliquidH2oResultValueCentrifuge, 1, 70);
        grid.add(LliquidKclResultLabelCentrifuge, 0, 71);
        grid.add(LliquidKclResultValueCentrifuge, 1, 71);
        grid.add(LliquidNaclResultLabelCentrifuge, 0, 72);
        grid.add(LliquidNaclResultValueCentrifuge, 1, 72);
        grid.add(LliquidCaso4ResultLabelCentrifuge, 0, 73);
        grid.add(LliquidCaso4ResultValueCentrifuge, 1, 73);

        grid.add(liquidPieChart2Centrifuge, 0, 75, 2, 1);



        grid.add(Sushka, 0, 79);

        grid.add(solidQResultLabelSushka, 0, 80);
        grid.add(solidQResultValueSushka, 1, 80);
        grid.add(solidKclResultLabelSushka, 0, 81);
        grid.add(solidKclResultValueSushka, 1, 81);
        grid.add(solidNaclResultLabelSushka, 0, 82);
        grid.add(solidNaclResultValueSushka, 1, 82);
        grid.add(wasteResultLabelSushka, 0, 83);
        grid.add(wasteResultValueSushka, 1, 83);
        grid.add(h2oResultLabelSushka, 0, 84);
        grid.add(h2oResultValueSushka, 1, 84);
        grid.add(ExtractionResultLabelSushka, 0, 85);
        grid.add(ExtractionResultValueSushka, 1, 85);


        grid.add(finalPieChart, 0, 87, 2, 10);



        grid.add(ExtractionResultLabelSushka2, 4, 4);
        grid.add(ExtractionResultValueSushka2, 5, 4);


        grid.add(finalPieChart2, 4, 6, 2, 10);


        grid.add(saveButton, 2, 9, 6, 1);
        grid.add(loadButton, 2, 10, 6, 1);
        grid.add(resetButton, 2, 11, 6, 1);


        Label Final = new Label("  ");
        grid.add(Final, 0, 100);

        // --------------------- Event Handlers ---------------------






        // Event Handling
        calculateButton.setOnAction(event -> {
            try {




                // Liquid Section
                BigDecimal GinputSection = new BigDecimal(Ginput.getText());
                BigDecimal liquidQInputSection = new BigDecimal(liquidQInput.getText()).divide(BigDecimal.valueOf(1000)).multiply(GinputSection);
                liquid.setH2O(liquidQInputSection);
                liquid.setQ(liquidQInputSection);
                h2oResultValue.setText(liquidQInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                if (liquidQInputSection.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Вода должна быть больше нуля!");
                    return; // Stop further execution
                }
                // Solid Section
                BigDecimal solidQInputSection = new BigDecimal(solidQInput.getText());
                BigDecimal KClPercentInputSection = new BigDecimal(KClPercentInput.getText()).divide(BigDecimal.valueOf(100));
                BigDecimal NaCLPercentInputSection = new BigDecimal(NaClPercentInput.getText()).divide(BigDecimal.valueOf(100));
                BigDecimal CaSO4PercentInputSection = new BigDecimal(CaSO4PercentInput.getText()).divide(BigDecimal.valueOf(100));
                BigDecimal WastePercentInputSection = BigDecimal.ONE.subtract(KClPercentInputSection).subtract(NaCLPercentInputSection).subtract(CaSO4PercentInputSection);
                BigDecimal CaSO4PercentWaste = CaSO4PercentInputSection.add(WastePercentInputSection);

                if (WastePercentInputSection.compareTo(BigDecimal.ZERO) < 0) {
                    showError("Процент состава руды больше 100%.");
                    return; // Stop further execution
                }
                if (WastePercentInputSection.compareTo(BigDecimal.ZERO) != 0) {
                    showError("Процент состава руды меньше 100%.");
                    return; // Stop further execution
                }


                solidMaterial.setS_Q(solidQInputSection);
                BigDecimal kclAmountInputSection = solidQInputSection.multiply(KClPercentInputSection);
                BigDecimal naclAmountInputSection = solidQInputSection.multiply(NaCLPercentInputSection);
                BigDecimal CaSO4AmountInputSection = solidQInputSection.multiply(CaSO4PercentInputSection);
                BigDecimal TotalInputSectionPercent =  BigDecimal.ONE.subtract(KClPercentInputSection).subtract(NaCLPercentInputSection).subtract(CaSO4PercentInputSection);

                BigDecimal WasteAmountInputSection = solidQInputSection.multiply(TotalInputSectionPercent);

                solidMaterial.setSolidKCl(kclAmountInputSection);
                solidMaterial.setSolidNaCl(naclAmountInputSection);
                solidMaterial.setSolidCaSO4(CaSO4AmountInputSection);

                solidMaterial.setSolidWaste(solidQInputSection.multiply(TotalInputSectionPercent));
                kclResultValue.setText(kclAmountInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                naclResultValue.setText(naclAmountInputSection.setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");
                combinedResultValue.setText(WasteAmountInputSection.add(CaSO4AmountInputSection).setScale(2, RoundingMode.HALF_UP).toString()+" т/ч");

                // Liquid Material Section
                BigDecimal ratioInputSection = new BigDecimal(ratioInput.getText());

                if (ratioInputSection.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Соотношение Ж/Т должно быть больше нуля!");
                    return; // Stop further execution
                }
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


                BigDecimal LiquidKclPercentVishelachivanie = LiquidKclAmountVishelachivanie.divide(liquidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));



                BigDecimal SolidKclPercentVishelachivanie = SolidKclAmountVishelachivanie.divide(solidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercentVishelachivanie = SolidNaclAmountVishelachivanie.divide(solidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal  WasteCaso4PercentVishelachivanie = WasteCaso4Vishelachivanie.divide(solidQVishelachivanie, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

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

                if (LiqSolRatHydrocyclone.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Соотношение Ж/Т должно быть больше нуля!");
                    return; // Stop further execution
                }

                BigDecimal SolQuartRatioHydrocyclone = new BigDecimal(solQuartRatioTextFieldHydrocyclone.getText()).divide(BigDecimal.valueOf(100));

                if (SolQuartRatioHydrocyclone.compareTo(BigDecimal.ZERO) < 0) {
                    showError("Доля твердых должна быть больше или равнаиге нулю!");
                    return; // Stop further execution
                }
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


                BigDecimal LiquidKclPercentHydrocyclone = sLiquidKclAmountHydrocyclone.divide(sliquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal SolidKclPercentHydrocyclone = sSolidKclAmountHydrocyclone.divide(ssolidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercentHydrocyclone = sSolidNaclAmountHydrocyclone.divide(ssolidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal  WasteCaso4PercentHydrocyclone = sWasteCaso4Hydrocyclone.divide(ssolidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));





                BigDecimal LiquidH2OPercent2Hydrocyclone = LiquidH2OAmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercent2Hydrocyclone = LiquidNaclAmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4Percent2Hydrocyclone = LiquidCaso4AmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal LiquidKclPercent2Hydrocyclone = LiquidKclAmountHydrocyclone.divide(liquidQHydrocyclone, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));



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


                // Get the value of liqRat from the text field


                BigDecimal LiqSolRatCentrifuge = new BigDecimal(liqRatTextFieldCentrifuge.getText());
                if (LiqSolRatHydrocyclone.compareTo(BigDecimal.ZERO) < 0) {
                    showError("Соотношение Ж/Т должно быть больше нуля!");
                    return; // Stop further execution
                }

// Check if the value is greater than 0 and smaller than 101
                if (LiqSolRatCentrifuge.compareTo(BigDecimal.ZERO) > 0 && LiqSolRatCentrifuge.compareTo(BigDecimal.valueOf(101)) < 0) {
                    // Proceed with the division if the condition is satisfied
                    LiqSolRatCentrifuge = LiqSolRatCentrifuge.divide(BigDecimal.valueOf(100));
                    System.out.println("LiqSolRatCentrifuge: " + LiqSolRatCentrifuge);
                } else {
                    // Handle invalid input
                    System.out.println("Влажность должна быть больше 0 и меньше или равно 100%.");
                }
                centrifugeSolid.setLiqSolRat(LiqSolRatCentrifuge);

                BigDecimal solidQCentrifuge = hydrocycloneSolid.getS_Q();

                BigDecimal liquidQCentrifuge = solidQCentrifuge.multiply(LiqSolRatCentrifuge);
                BigDecimal LiquidH2OAmountCentrifuge = liquidQCentrifuge.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidH2O());
                BigDecimal LiquidNaclAmountCentrifuge = liquidQCentrifuge.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidNaCl());
                BigDecimal LiquidCaso4AmountCentrifuge = liquidQCentrifuge.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidCaSO4());
                BigDecimal LiquidKclAmountCentrifuge = liquidQCentrifuge.divide(hydrocycloneSolid.getL_Q(), RoundingMode.HALF_UP).multiply(hydrocycloneSolid.getLiquidKCl());

                BigDecimal wasteCentrifuge = hydrocycloneSolid.getSolidWaste();
                BigDecimal SolidKclAmountCentrifuge = hydrocycloneSolid.getSolidKCl();
                BigDecimal SolidNaclAmountCentrifuge = hydrocycloneSolid.getSolidNaCl();
                BigDecimal SolidCaso4AmountCentrifuge = hydrocycloneSolid.getSolidCaSO4();
                BigDecimal WasteCaso4Centrifuge = wasteCentrifuge.add(SolidCaso4AmountCentrifuge);

                BigDecimal  lliquidQCentrifuge = hydrocycloneSolid.getL_Q().subtract(liquidQCentrifuge);
                BigDecimal lLiquidH2OAmountCentrifuge = hydrocycloneSolid.getLiquidH2O().subtract(LiquidH2OAmountCentrifuge);
                BigDecimal lLiquidNaclAmountCentrifuge = hydrocycloneSolid.getLiquidNaCl().subtract(LiquidNaclAmountCentrifuge);
                BigDecimal lLiquidCaso4AmountCentrifuge = hydrocycloneSolid.getLiquidCaSO4().subtract(LiquidCaso4AmountCentrifuge);
                BigDecimal lLiquidKclAmountCentrifuge = hydrocycloneSolid.getLiquidKCl().subtract(LiquidKclAmountCentrifuge);


                centrifugeLiquid.setL_Q(lliquidQCentrifuge);
                centrifugeLiquid.setLiquidH2O(lLiquidH2OAmountCentrifuge);
                centrifugeLiquid.setLiquidKCl(lLiquidKclAmountCentrifuge);
                centrifugeLiquid.setLiquidNaCl(lLiquidNaclAmountCentrifuge);
                centrifugeLiquid.setLiquidCaSO4(lLiquidCaso4AmountCentrifuge);


                centrifugeSolid.setLiqSolRat(LiqSolRatCentrifuge);
                centrifugeSolid.setL_Q(liquidQCentrifuge);
                centrifugeSolid.setLiquidH2O(LiquidH2OAmountCentrifuge);
                centrifugeSolid.setLiquidKCl(LiquidKclAmountCentrifuge);
                centrifugeSolid.setLiquidNaCl(LiquidNaclAmountCentrifuge);
                centrifugeSolid.setLiquidCaSO4(LiquidCaso4AmountCentrifuge);


                centrifugeSolid.setS_Q(solidQCentrifuge);
                centrifugeSolid.setSolidKCl(SolidKclAmountCentrifuge);
                centrifugeSolid.setSolidNaCl(SolidNaclAmountCentrifuge);
                centrifugeSolid.setSolidCaSO4(SolidCaso4AmountCentrifuge);
                centrifugeSolid.setSolidWaste(wasteCentrifuge);

                BigDecimal LiquidH2OPercentCentrifuge = LiquidH2OAmountCentrifuge.divide(liquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercentCentrifuge = LiquidNaclAmountCentrifuge.divide(liquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4PercentCentrifuge = LiquidCaso4AmountCentrifuge.divide(liquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal LiquidKclPercentCentrifuge = LiquidKclAmountCentrifuge.divide(liquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));



                BigDecimal SolidKclPercentCentrifuge = SolidKclAmountCentrifuge.divide(solidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal SolidNaclPercentCentrifuge = SolidNaclAmountCentrifuge.divide(solidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal  WasteCaso4PercentCentrifuge = WasteCaso4Centrifuge.divide(solidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal LiquidH2OPercent2Centrifuge = lLiquidH2OAmountCentrifuge.divide(lliquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidNaclPercent2Centrifuge = lLiquidNaclAmountCentrifuge.divide(lliquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal LiquidCaso4Percent2Centrifuge = lLiquidCaso4AmountCentrifuge.divide(lliquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal LiquidKclPercent2Centrifuge = lLiquidKclAmountCentrifuge.divide(lliquidQCentrifuge, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                liquidPieChartCentrifuge.getData().clear();
                liquidPieChartCentrifuge.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercentCentrifuge), LiquidH2OPercentCentrifuge.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercentCentrifuge), LiquidNaclPercentCentrifuge.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4PercentCentrifuge), LiquidCaso4PercentCentrifuge.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercentCentrifuge), LiquidKclPercentCentrifuge.doubleValue())
                );

                // Add data to solid PieChart
                solidPieChartCentrifuge.getData().clear();
                solidPieChartCentrifuge.getData().addAll(
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4PercentCentrifuge), WasteCaso4PercentCentrifuge.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(SolidKclPercentCentrifuge), SolidKclPercentCentrifuge.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(SolidNaclPercentCentrifuge), SolidNaclPercentCentrifuge.doubleValue())
                );

                liquidQResultValueCentrifuge.setText(liquidQCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidH2oResultValueCentrifuge.setText(LiquidH2OAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidNaclResultValueCentrifuge.setText(LiquidNaclAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidKclResultValueCentrifuge.setText(LiquidKclAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                liquidCaso4ResultValueCentrifuge.setText(LiquidCaso4AmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");


                LliquidQResultValueCentrifuge.setText(lliquidQCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                LliquidH2oResultValueCentrifuge.setText(lLiquidH2OAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                LliquidKclResultValueCentrifuge.setText(lLiquidKclAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");      //Update labels with calculated values
                LliquidNaclResultValueCentrifuge.setText(lLiquidNaclAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                LliquidCaso4ResultValueCentrifuge.setText(lLiquidCaso4AmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");


                solidQResultValueCentrifuge.setText(solidQCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidKclResultValueCentrifuge.setText(SolidKclAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidNaclResultValueCentrifuge.setText(SolidNaclAmountCentrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                wasteResultValueCentrifuge.setText(WasteCaso4Centrifuge.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");

                liquidPieChart2Centrifuge.getData().clear();
                liquidPieChart2Centrifuge.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(LiquidH2OPercent2Centrifuge), LiquidH2OPercent2Centrifuge.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(LiquidNaclPercent2Centrifuge), LiquidNaclPercent2Centrifuge.doubleValue()),
                        new PieChart.Data("CaSO4, " + formatPercent(LiquidCaso4Percent2Centrifuge), LiquidCaso4Percent2Centrifuge.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(LiquidKclPercent2Centrifuge), LiquidKclPercent2Centrifuge.doubleValue())
                );

                BigDecimal gp = BigDecimal.valueOf(0.005);
                BigDecimal kcl = centrifugeSolid.getSolidKCl().add(centrifugeSolid.getLiquidKCl());
                BigDecimal nacl = centrifugeSolid.getSolidNaCl().add(centrifugeSolid.getLiquidNaCl());
                BigDecimal caso4 = centrifugeSolid.getSolidCaSO4().add(centrifugeSolid.getLiquidCaSO4());
                BigDecimal wasteSushka = centrifugeSolid.getSolidWaste();
                BigDecimal Sum = kcl.add(nacl).add(caso4).add(wasteSushka);
                BigDecimal h2o1 = BigDecimal.ONE.subtract(gp);
                BigDecimal h2o2 = Sum.divide(h2o1, RoundingMode.HALF_UP);;
                BigDecimal h2oSushka = h2o2.multiply(gp);
                BigDecimal Q = Sum.add(h2oSushka);

                BigDecimal Extration = kcl.divide(kclAmountInputSection, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                Extration = Extration.max(BigDecimal.ZERO).min(BigDecimal.valueOf(100));


                BigDecimal H2OPercent = h2oSushka.divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal NaclPercent = nacl.divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                BigDecimal KclPercent = kcl.divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                BigDecimal  WasteCaso4Percent =wasteSushka.add(caso4).divide(Q, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));


                finalPieChart.getData().clear();
                finalPieChart.getData().addAll(
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4Percent), WasteCaso4Percent.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(NaclPercent), NaclPercent.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(KclPercent), KclPercent.doubleValue()),
                        new PieChart.Data("H2O, " + formatPercent(H2OPercent), H2OPercent.doubleValue())


                );
                finalPieChart2.getData().clear();
                finalPieChart2.getData().addAll(
                        new PieChart.Data("H2O, " + formatPercent(H2OPercent), H2OPercent.doubleValue()),
                        new PieChart.Data("NaCl, " + formatPercent(NaclPercent), NaclPercent.doubleValue()),
                        new PieChart.Data("KCl, " + formatPercent(KclPercent), KclPercent.doubleValue()),
                        new PieChart.Data("H.O. + CaSO4, " + formatPercent(WasteCaso4Percent), WasteCaso4Percent.doubleValue())

                );

                sushka.setQ(Q);
                sushka.setKCl(kcl);
                sushka.setNaCl(nacl);
                sushka.setCaSO4(caso4);
                sushka.setWaste(wasteSushka);
                sushka.setH2O(h2oSushka);

                if (Extration.compareTo(BigDecimal.valueOf(94.0)) > 0){
                    ExtractionResultValueSushka.setStyle("-fx-background-color: green; -fx-text-fill: #EEEEEE; -fx-text-fill: black;");
                    ExtractionResultValueSushka2.setStyle("-fx-background-color: green; -fx-text-fill: #EEEEEE; -fx-text-fill: black;");
                }
                else {
                    ExtractionResultValueSushka.setStyle("-fx-background-color: brown; -fx-text-fill: #EEEEEE; -fx-text-fill: black;");
                    ExtractionResultValueSushka2.setStyle("-fx-background-color: brown; -fx-text-fill: #EEEEEE; -fx-text-fill: black;");
                }

                ExtractionResultValueSushka.setText(Extration.setScale(2, RoundingMode.HALF_UP).toString()+ " %");
                solidQResultValueSushka.setText(Q.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidKclResultValueSushka.setText(kcl.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidNaclResultValueSushka.setText(nacl.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                wasteResultValueSushka.setText(wasteSushka.add(caso4).setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                h2oResultValueSushka.setText(h2oSushka.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");

                ExtractionResultValueSushka2.setText(Extration.setScale(2, RoundingMode.HALF_UP).toString()+ " %");
                solidQResultValueSushka2.setText(Q.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidKclResultValueSushka2.setText(kcl.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                solidNaclResultValueSushka2.setText(nacl.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                wasteResultValueSushka2.setText(wasteSushka.add(caso4).setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");
                h2oResultValueSushka2.setText(h2oSushka.setScale(2, RoundingMode.HALF_UP).toString()+ " т/ч");


                BigDecimal ratioHydrocycloneSolid = hydrocycloneSolid.getL_Q().divide(hydrocycloneSolid.getS_Q(), RoundingMode.HALF_UP);
                ratioResultHydrocycloneSolid.setText(ratioHydrocycloneSolid.setScale(2, RoundingMode.HALF_UP).toString());
                BigDecimal ratioHydrocycloneLiquid;

                if (hydrocycloneLiquid.getS_Q().compareTo(BigDecimal.ZERO) == 0) {
                    ratioHydrocycloneLiquid = BigDecimal.ZERO;
                } else {
                    ratioHydrocycloneLiquid = hydrocycloneLiquid.getL_Q().divide(hydrocycloneLiquid.getS_Q(), RoundingMode.HALF_UP);
                }

                ratioResultHydrocycloneLiquid.setText(ratioHydrocycloneLiquid.setScale(2, RoundingMode.HALF_UP).toString());

                BigDecimal ratioCentrifuge = centrifugeSolid.getL_Q().divide(centrifugeSolid.getS_Q(), RoundingMode.HALF_UP);
                ratioResultCentrifuge.setText(ratioCentrifuge.setScale(2, RoundingMode.HALF_UP).toString());
            } catch (NumberFormatException e) {
                showError("Введите корректные значения. Десятичная дробь должна быть записана через точку. ");
            }
        });

        saveButton.setOnAction(event -> {
            prefs.put("Ginput", Ginput.getText());
            prefs.put("liquidQInput", liquidQInput.getText());
            prefs.put("solidQInput", solidQInput.getText());
            prefs.put("KClPercentInput", KClPercentInput.getText());
            prefs.put("NaClPercentInput", NaClPercentInput.getText());
            prefs.put("CaSO4PercentInput", CaSO4PercentInput.getText());
            prefs.put("ratioInput", ratioInput.getText());
            prefs.put("liqRatTextFieldHydrocyclone", liqRatTextFieldHydrocyclone.getText());
            prefs.put("solQuartRatioTextFieldHydrocyclone", solQuartRatioTextFieldHydrocyclone.getText());
            prefs.put("liqRatTextFieldCentrifuge", liqRatTextFieldCentrifuge.getText());
            System.out.println("Data saved!");
        });
        loadButton.setOnAction(event -> {
            Ginput.setText(prefs.get("Ginput", ""));
            liquidQInput.setText(prefs.get("liquidQInput", ""));
            solidQInput.setText(prefs.get("solidQInput", ""));
            KClPercentInput.setText(prefs.get("KClPercentInput", ""));
            NaClPercentInput.setText(prefs.get("NaClPercentInput", ""));
            CaSO4PercentInput.setText(prefs.get("CaSO4PercentInput", ""));
            ratioInput.setText(prefs.get("ratioInput", ""));
            liqRatTextFieldHydrocyclone.setText(prefs.get("liqRatTextFieldHydrocyclone", ""));
            solQuartRatioTextFieldHydrocyclone.setText(prefs.get("solQuartRatioTextFieldHydrocyclone", ""));
            liqRatTextFieldCentrifuge.setText(prefs.get("liqRatTextFieldCentrifuge", ""));
            System.out.println("Data loaded!");





        });
        resetButton.setOnAction(event -> {

            // Reset all text fields to default (empty)
            Ginput.setText("");
            liquidQInput.setText("");
            solidQInput.setText("");
            KClPercentInput.setText("");
            NaClPercentInput.setText("");
            CaSO4PercentInput.setText("");
            ratioInput.setText("");
            liqRatTextFieldHydrocyclone.setText("");
            solQuartRatioTextFieldHydrocyclone.setText("");
            liqRatTextFieldCentrifuge.setText("");



        });





        return grid;
    }

    String formatPercent(BigDecimal value) {
        return String.format("%.2f%%", value.doubleValue());
    }




    private GridPane createAlignedGridPane(int numColumns) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(2));
        grid.setHgap(2);
        grid.setVgap(2);


        // Define consistent column constraints for all columns
        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / numColumns); // Divide the width equally
            grid.getColumnConstraints().add(column);
        }

        // Apply a border to the entire grid
        grid.setStyle("-fx-border-color: #EEEEEE; -fx-border-width: 2px;-fx-text-fill: black;");

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
