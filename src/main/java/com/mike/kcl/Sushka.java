package com.mike.kcl;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class Sushka implements Serializable {
    @Serial
    private static final long serialVersionUID = 8;

    // Fields for Liquid Material
    private BigDecimal H2O;
    private BigDecimal KCl;
    private BigDecimal NaCl;
    private BigDecimal CaSO4;
    private BigDecimal Q; // Liquid quantity
    private BigDecimal Waste;

    // Solid quantity

    // Constructor accepting prerequisite classes
    public Sushka() {
        // Extracting values from SolidMaterial



    }

    // Getters for Liquid Material
    public BigDecimal getH2O() {
        return H2O;
    }

    public BigDecimal getKCl() {
        return KCl;
    }

    public BigDecimal getNaCl() {
        return NaCl;
    }

    public BigDecimal getCaSO4() {
        return CaSO4;
    }

    public BigDecimal getQ() {
        return Q;
    }

    // Getters for Solid Material



    // Setters for Liquid Material
    public void setH2O(BigDecimal h2O) {
        this.H2O = h2O;
    }

    public void setKCl(BigDecimal KCl) {
        this.KCl = KCl;
    }

    public void setNaCl(BigDecimal naCl) {
        this.NaCl = naCl;
    }

    public void setCaSO4(BigDecimal CaSO4) {this.CaSO4 = CaSO4;
    }

    public void setQ(BigDecimal Q) {
        this.Q = Q;
    }


    public BigDecimal getWaste() {
        return Waste;
    }

    public void setWaste(BigDecimal waste) {
        Waste = waste;
    }
}
