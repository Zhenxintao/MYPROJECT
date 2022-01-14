package com.bmts.heating.commons.utils.calcu;

import java.math.BigDecimal;

@FunctionalInterface
public interface ToBigDecimalFunction<T> {
	BigDecimal applyAsBigDecimal(T value);
}
