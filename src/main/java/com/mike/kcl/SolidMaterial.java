package com.mike.kcl;

import java.math.BigDecimal;

public class SolidMaterial {
        private BigDecimal solidKCl = BigDecimal.ZERO;
        private BigDecimal solidNaCl = BigDecimal.ZERO;
        private BigDecimal solidCaSO4 = BigDecimal.ZERO;
        private BigDecimal solidWaste = BigDecimal.ZERO;
        private BigDecimal s_Q = BigDecimal.ZERO;

        public SolidMaterial(BigDecimal initialQuantity) {
                this.s_Q = initialQuantity;
        }

        public SolidMaterial(double v) {
        }

        // Getters and Setters
        public BigDecimal getSolidKCl() {
                return solidKCl;
        }

        public void setSolidKCl(BigDecimal solidKCl) {
                this.solidKCl = solidKCl;
        }

        public BigDecimal getSolidNaCl() {
                return solidNaCl;
        }

        public void setSolidNaCl(BigDecimal solidNaCl) {
                this.solidNaCl = solidNaCl;
        }

        public BigDecimal getSolidCaSO4() {
                return solidCaSO4;
        }

        public void setSolidCaSO4(BigDecimal solidCaSO4) {
                this.solidCaSO4 = solidCaSO4;
        }

        public BigDecimal getSolidWaste() {
                return solidWaste;
        }

        public void setSolidWaste(BigDecimal solidWaste) {
                this.solidWaste = solidWaste;
        }

        public BigDecimal getS_Q() {
                return s_Q;
        }

        public void setS_Q(BigDecimal s_Q) {
                this.s_Q = s_Q;
        }
}
