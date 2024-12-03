package com.mike.kcl;

import java.math.BigDecimal;

public class SolidMaterial {

        private BigDecimal solidKCl = BigDecimal.ZERO;
        private BigDecimal solidNaCl = BigDecimal.ZERO;
        private BigDecimal solidCaSO4 = BigDecimal.ZERO;
        private BigDecimal solidWaste = BigDecimal.ZERO;
        private BigDecimal s_Q = BigDecimal.ZERO;

        // Constructor that takes a BigDecimal for initial quantity
        public SolidMaterial(BigDecimal initialQuantity) {
                this.s_Q = initialQuantity != null ? initialQuantity : BigDecimal.ZERO;
        }

        // Constructor that takes a double for initial quantity
        public SolidMaterial(double v) {
                this.s_Q = BigDecimal.valueOf(v);  // Convert double to BigDecimal
        }

        // Getters and Setters
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
}

