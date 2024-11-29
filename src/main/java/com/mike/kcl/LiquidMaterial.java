package com.mike.kcl;

import java.math.BigDecimal;

public class LiquidMaterial {

    private BigDecimal l_Q = BigDecimal.ZERO;
    private BigDecimal liquidH2O = BigDecimal.ZERO;
    private BigDecimal liquidKCl = BigDecimal.ZERO;
    private BigDecimal liquidNaCl = BigDecimal.ZERO;
    private BigDecimal liquidCaSO4 = BigDecimal.ZERO;
    private BigDecimal liqSolRat = BigDecimal.ZERO;
    public LiquidMaterial(BigDecimal initialLiqSolidRatio) {
        this.liqSolRat = initialLiqSolidRatio;
    }

    public LiquidMaterial(double v) {
    }

    // Getters and Setters
    public BigDecimal getLiquidH2O() {
        return liquidH2O;
    }
    public void setLiquidH20(BigDecimal liquidH2O) {
        this.liquidH2O = liquidH2O;
    }
    public BigDecimal getLiquidKCl() {
        return liquidKCl;
    }

    public void setLiquidKCl(BigDecimal liquidKCl) {
        this.liquidKCl = liquidKCl;
    }

    public BigDecimal getLiquidNaCl() {
        return liquidNaCl;
    }

    public void setLiquidNaCl(BigDecimal liquidNaCl) {
        this.liquidNaCl = liquidNaCl;
    }

    public BigDecimal getLiquidCaSO4() { return liquidCaSO4;
    }

    public void setLiquidCaSO4(BigDecimal liquidCaSO4) {
        this.liquidCaSO4 = liquidCaSO4;
    }

    public BigDecimal getL_Q() {
        return l_Q;
    }

    public void setL_Q(BigDecimal l_Q) {
        this.l_Q = l_Q;
    }
}
