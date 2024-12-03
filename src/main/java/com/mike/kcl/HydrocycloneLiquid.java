package com.mike.kcl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HydrocycloneLiquid {

    // Fields for Liquid Material
    private BigDecimal liquidH2O;
    private BigDecimal liquidKCl;
    private BigDecimal liquidNaCl;
    private BigDecimal liquidCaSO4;
    private BigDecimal l_Q; // Liquid quantity

    // Fields for Solid Material
    private BigDecimal solidKCl;
    private BigDecimal solidNaCl;
    private BigDecimal solidCaSO4;
    private BigDecimal solidWaste;
    private BigDecimal s_Q;
    private BigDecimal LiqSolRat;
    private BigDecimal SolQuartRatio;
    // Solid quantity

    private BigDecimal H2O;
    // Constructor accepting prerequisite classes
    public HydrocycloneLiquid(HydrocycloneSolid hydrocycloneSolid) {
        // Extracting values from SolidMaterial
        this.solidKCl = hydrocycloneSolid.getSolidKCl();
        this.solidNaCl = hydrocycloneSolid.getSolidNaCl();
        this.solidCaSO4 = hydrocycloneSolid.getSolidCaSO4();
        this.solidWaste = hydrocycloneSolid.getSolidWaste();
        this.s_Q = hydrocycloneSolid.getS_Q();

        // Extracting values from LiquidMaterial
        this.liquidH2O = hydrocycloneSolid.getLiquidH2O().add(hydrocycloneSolid.getH2O());
        this.liquidKCl = hydrocycloneSolid.getLiquidKCl();
        this.liquidNaCl = hydrocycloneSolid.getLiquidNaCl();
        this.liquidCaSO4 = hydrocycloneSolid.getLiquidCaSO4();
        this.l_Q = hydrocycloneSolid.getL_Q();

        this.H2O = hydrocycloneSolid.getH2O();
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

    public BigDecimal getH2O() {
        return H2O;
    }

    public BigDecimal getSolQuartRatio() {
        return SolQuartRatio;
    }

    public BigDecimal getLiqSolRat() {
        return LiqSolRat;
    }
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

    // Setters for Solid Material
    public void setSolidKCl(BigDecimal solidKCl) {
        this.solidKCl = solidKCl;
    }

    public void setSolidNaCl(BigDecimal solidNaCl) {
        this.solidNaCl = solidNaCl;
    }

    public void setSolidCaSO4(BigDecimal solidCaSO4) {
        this.solidCaSO4 = solidCaSO4;
    }

    public void setSolidWaste(BigDecimal solidWaste) {
        this.solidWaste = solidWaste;
    }

    public void setS_Q(BigDecimal s_Q) {
        this.s_Q = s_Q;
    }

    public void setLiqSolRat(BigDecimal LiqSolRat) {
        this.LiqSolRat = LiqSolRat;
    }
    public void setSolQuartRatio(BigDecimal SolQuartRatio) {
        this.SolQuartRatio = SolQuartRatio;
    }
    public void setH2O(BigDecimal H2O) {
        this.H2O = H2O;
    }



}

