package org.tutorial.venusbackend.model.enums;

import java.math.BigDecimal;

public enum ShippingMethod {
    STANDARD("Standard (3–5 days)", BigDecimal.ZERO),
    EXPRESS("Express (1–2 days)", BigDecimal.valueOf(2)),
    STORE_PICKUP("Store Pickup", BigDecimal.ZERO),
    OVERNIGHT("Overnight", BigDecimal.valueOf(5));

    private final String label;
    private final BigDecimal cost;

    ShippingMethod(String label, BigDecimal cost) {
        this.label = label;
        this.cost = cost;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getCost() {
        return cost;
    }
}