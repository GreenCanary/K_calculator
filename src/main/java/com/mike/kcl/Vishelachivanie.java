package com.mike.kcl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Vishelachivanie {

    // Fields for Liquid Material
    private final BigDecimal liquidH2O;
    private final BigDecimal liquidKCl;
    private final BigDecimal liquidNaCl;
    private final BigDecimal liquidCaSO4;
    private final BigDecimal l_Q; // Liquid quantity

    // Fields for Solid Material
    private final BigDecimal solidKCl;
    private final BigDecimal solidNaCl;
    private final BigDecimal solidCaSO4;
    private final BigDecimal solidWaste;
    private final BigDecimal s_Q; // Solid quantity


    // Constructor accepting prerequisite classes
    public Vishelachivanie(SolidMaterial solidMaterial, LiquidMaterial liquidMaterial, Liquid liquid) {

        // Extracting values from SolidMaterial
        this.solidKCl = solidMaterial.getSolidKCl();
        this.solidNaCl = solidMaterial.getSolidNaCl();
        this.solidCaSO4 = solidMaterial.getSolidCaSO4();
        this.solidWaste = solidMaterial.getSolidWaste();
        this.s_Q = SolidMaterial.getS_Q();

        // Extracting values from LiquidMaterial
        this.liquidH2O = liquidMaterial.getLiquidH2O().add(liquid.getH2O());
        this.liquidKCl = liquidMaterial.getLiquidKCl().add(liquid.getKCl());
        this.liquidNaCl = liquidMaterial.getLiquidNaCl();
        this.liquidCaSO4 = liquidMaterial.getLiquidCaSO4();
        this.l_Q = liquidMaterial.getL_Q();

    }

    // Getters for Liquid Material
    public BigDecimal getLiquidH2O() {
        return liquidH2O;
    }

    public BigDecimal getLiquidKCl() {
        return liquidKCl;
    }

    public BigDecimal getLiquidNaCl() {
        return liquidNaCl;
    }

    public BigDecimal getLiquidCaSO4() {
        return liquidCaSO4;
    }

    public BigDecimal getL_Q() {
        return l_Q;
    }

    // Getters for Solid Material
    public BigDecimal getSolidKCl() {
        return solidKCl;
    }

    public BigDecimal getSolidNaCl() {
        return solidNaCl;
    }

    public BigDecimal getSolidCaSO4() {
        return solidCaSO4;
    }

    public BigDecimal getSolidWaste() {
        return solidWaste;
    }

    public BigDecimal getS_Q() {
        return s_Q;
    }

    // Computed Liquid-to-Solid Ratio
    public BigDecimal getLiqSolRat() {
        if (s_Q.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // Avoid division by zero
        }
        return l_Q.divide(s_Q, 4, RoundingMode.HALF_UP); // Rounded to 4 decimal places
    }

}
