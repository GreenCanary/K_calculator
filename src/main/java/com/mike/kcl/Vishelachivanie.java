package com.mike.kcl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Vishilachivanie {

    // Fields for Liquid Material
    private BigDecimal liquidH2O = BigDecimal.ZERO;
    private BigDecimal liquidKCl = BigDecimal.ZERO;
    private BigDecimal liquidNaCl = BigDecimal.ZERO;
    private BigDecimal liquidCaSO4 = BigDecimal.ZERO;
    private BigDecimal l_Q = BigDecimal.ZERO; // Liquid quantity

    // Fields for Solid Material
    private BigDecimal solidKCl = BigDecimal.ZERO;
    private BigDecimal solidNaCl = BigDecimal.ZERO;
    private BigDecimal solidCaSO4 = BigDecimal.ZERO;
    private BigDecimal solidWaste = BigDecimal.ZERO;
    private BigDecimal s_Q = BigDecimal.ZERO; // Solid quantity

    // Constructor with initial liquid and solid quantities
    public Vishilachivanie(BigDecimal initialL_Q, BigDecimal initialS_Q) {
        this.l_Q = initialL_Q != null ? initialL_Q : BigDecimal.ZERO;
        this.s_Q = initialS_Q != null ? initialS_Q : BigDecimal.ZERO;
    }

    // Constructor with double values for liquid and solid quantities
    public Vishilachivanie(double initialL_Q, double initialS_Q) {
        this.l_Q = BigDecimal.valueOf(initialL_Q);
        this.s_Q = BigDecimal.valueOf(initialS_Q);
    }

    // Getters and Setters for Liquid Material
    public BigDecimal getLiquidH2O() {
        return liquidH2O;
    }

    public void setLiquidH2O(BigDecimal liquidH2O) {
        this.liquidH2O = liquidH2O != null ? liquidH2O : BigDecimal.ZERO;
    }

    public BigDecimal getLiquidKCl() {
        return liquidKCl;
    }

    public void setLiquidKCl(BigDecimal liquidKCl) {
        this.liquidKCl = liquidKCl != null ? liquidKCl : BigDecimal.ZERO;
    }

    public BigDecimal getLiquidNaCl() {
        return liquidNaCl;
    }

    public void setLiquidNaCl(BigDecimal liquidNaCl) {
        this.liquidNaCl = liquidNaCl != null ? liquidNaCl : BigDecimal.ZERO;
    }

    public BigDecimal getLiquidCaSO4() {
        return liquidCaSO4;
    }

    public void setLiquidCaSO4(BigDecimal liquidCaSO4) {
        this.liquidCaSO4 = liquidCaSO4 != null ? liquidCaSO4 : BigDecimal.ZERO;
    }

    public BigDecimal getL_Q() {
        return l_Q;
    }

    public void setL_Q(BigDecimal l_Q) {
        this.l_Q = l_Q != null ? l_Q : BigDecimal.ZERO;
    }

    // Getters and Setters for Solid Material
    public BigDecimal getSolidKCl() {
        return solidKCl;
    }

    public void setSolidKCl(BigDecimal solidKCl) {
        this.solidKCl = solidKCl != null ? solidKCl : BigDecimal.ZERO;
    }

    public BigDecimal getSolidNaCl() {
        return solidNaCl;
    }

    public void setSolidNaCl(BigDecimal solidNaCl) {
        this.solidNaCl = solidNaCl != null ? solidNaCl : BigDecimal.ZERO;
    }

    public BigDecimal getSolidCaSO4() {
        return solidCaSO4;
    }

    public void setSolidCaSO4(BigDecimal solidCaSO4) {
        this.solidCaSO4 = solidCaSO4 != null ? solidCaSO4 : BigDecimal.ZERO;
    }

    public BigDecimal getSolidWaste() {
        return solidWaste;
    }

    public void setSolidWaste(BigDecimal solidWaste) {
        this.solidWaste = solidWaste != null ? solidWaste : BigDecimal.ZERO;
    }

    public BigDecimal getS_Q() {
        return s_Q;
    }

    public void setS_Q(BigDecimal s_Q) {
        this.s_Q = s_Q != null ? s_Q : BigDecimal.ZERO;
    }

    // Computed Liquid-to-Solid Ratio
    public BigDecimal getLiqSolRat() {
        if (s_Q.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // Avoid division by zero
        }
        return l_Q.divide(s_Q, 4, RoundingMode.HALF_UP); // Rounded to 4 decimal places
    }

    // Example Method: Calculate total material quantity
    public BigDecimal calculateTotalQuantity() {
        return this.l_Q.add(this.s_Q);
    }
}
