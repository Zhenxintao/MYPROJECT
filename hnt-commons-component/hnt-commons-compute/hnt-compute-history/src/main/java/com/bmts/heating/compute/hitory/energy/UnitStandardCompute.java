package com.bmts.heating.compute.hitory.energy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 折算能耗计算类
 */
@Component
public class UnitStandardCompute {

	/**
	 *
	 * @param outdoorTemp 室外折标温度 t`w
	 * @param indoorTemp 室内温度 tn
	 * @param outdoorRealTemp 室外真实温度  tw
	 * @param var 实际量
	 * @return 折标量 GJ/万㎡
	 */
	public static BigDecimal unitStandard(BigDecimal outdoorTemp, BigDecimal indoorTemp, BigDecimal outdoorRealTemp, BigDecimal var){
		return var.multiply(unitStandard(outdoorTemp, indoorTemp, outdoorRealTemp)).setScale(3,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * (tn-t`w)/(tn-tw)
	 *
	 */
	private static BigDecimal unitStandard(BigDecimal outdoorTemp, BigDecimal indoorTemp, BigDecimal outdoorRealTemp){
		BigDecimal var1 = indoorTemp.subtract(outdoorRealTemp);
		BigDecimal var2 = indoorTemp.subtract(outdoorTemp);
		/*
		* 除数不为0比较
		* */
		if(var2.compareTo(BigDecimal.valueOf(0)) != 0) {
			return var1.divide(var2,3,BigDecimal.ROUND_HALF_UP);
		}
		throw new RuntimeException("除数为0");
	}
}
