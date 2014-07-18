package org.palladiosimulator.experimentautomation.application.variation.valueprovider;

import org.palladiosimulator.experimentautomation.experiments.PolynomialValueProvider;

public class PolynomialValueProviderStrategy implements IValueProviderStrategy {

    private final PolynomialValueProvider specification;

    public PolynomialValueProviderStrategy(final PolynomialValueProvider specification) {
        this.specification = specification;
    }

    @Override
    public long valueAtPosition(final int position) {
        final double result = Math.pow(position, this.specification.getExponent()) * this.specification.getFactor();
        return new Double(result).longValue();
    }

}