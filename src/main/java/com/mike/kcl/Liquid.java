package com.mike.kcl;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class Liquid  {
    @Serial
    private static final long serialVersionUID = 5;
    private BigDecimal H2O = BigDecimal.ZERO;
    private BigDecimal KCl = BigDecimal.ZERO;
    private BigDecimal NaCl = BigDecimal.ZERO;
    private BigDecimal CaSO4 = BigDecimal.ZERO;
    private BigDecimal Q = BigDecimal.ZERO;

    public BigDecimal getH2O() {
        return H2O;
    }

    public void setH2O(BigDecimal h2O) {
        H2O = h2O;
    }

    public BigDecimal getKCl() {
        return KCl;
    }

    public void setKCl(BigDecimal kCl) {
        KCl = kCl;
    }

    public BigDecimal getNaCl() {
        return NaCl;
    }

    public void setNaCl(BigDecimal naCl) {
        NaCl = naCl;
    }

    public BigDecimal getCaSO4() {
        return CaSO4;
    }

    public void setCaSO4(BigDecimal caSO4) {
        CaSO4 = caSO4;
    }

    public BigDecimal getQ() {
        return Q;
    }

    public void setQ(BigDecimal q) {
        Q = q;
    }
}
