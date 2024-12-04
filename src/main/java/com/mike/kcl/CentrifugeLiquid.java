package com.mike.kcl;

import java.math.BigDecimal;

public class CentrifugeLiquid {

    // Fields for Liquid Material
    private BigDecimal liquidH2O;
    private BigDecimal liquidKCl;
    private BigDecimal liquidNaCl;
    private BigDecimal liquidCaSO4;
    private BigDecimal l_Q; // Liquid quantity


    // Solid quantity

    // Constructor accepting prerequisite classes
    public CentrifugeLiquid(HydrocycloneSolid hydrocycloneSolid, CentrifugeSolid centrifugeSolid) {
        // Extracting values from SolidMaterial

        // Extracting values from LiquidMaterial
        this.l_Q = hydrocycloneSolid.getL_Q().subtract(centrifugeSolid.getL_Q());
        this.liquidH2O = hydrocycloneSolid.getLiquidH2O().subtract(centrifugeSolid.getLiquidH2O());
        this.liquidKCl = hydrocycloneSolid.getLiquidKCl().subtract(centrifugeSolid.getLiquidKCl());
        this.liquidNaCl = hydrocycloneSolid.getLiquidNaCl().subtract(centrifugeSolid.getLiquidNaCl());
        this.liquidCaSO4 = hydrocycloneSolid.getLiquidCaSO4().subtract(centrifugeSolid.getLiquidCaSO4());

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



    // Setters for Liquid Material
    public void setLiquidH2O(BigDecimal liquidH2O) {
        this.liquidH2O = liquidH2O;
    }

    public void setLiquidKCl(BigDecimal liquidKCl) {
        this.liquidKCl = liquidKCl;
    }

    public void setLiquidNaCl(BigDecimal liquidNaCl) {
        this.liquidNaCl = liquidNaCl;
    }

    public void setLiquidCaSO4(BigDecimal liquidCaSO4) {
        this.liquidCaSO4 = liquidCaSO4;
    }

    public void setL_Q(BigDecimal l_Q) {
        this.l_Q = l_Q;
    }



}
