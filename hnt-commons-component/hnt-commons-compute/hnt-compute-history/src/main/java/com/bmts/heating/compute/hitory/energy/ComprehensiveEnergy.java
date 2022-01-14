package com.bmts.heating.compute.hitory.energy;

import java.math.BigDecimal;

/**
 * 综合能耗算法类
 *
 * @author hjw
 */
public class ComprehensiveEnergy {
	/**
	 * E = Er+Ed+Es
	 * E - 综合能耗值
	 * Er=Q·q(r) - 用热量折算标煤的量，Kg
	 * Q - 用热量, MJ
	 * q(r) - 热力折标煤系数。0.03412kgce/MJ
	 * <p>
	 * Ed = N·q(d) - 用电量折算标煤的量，Kg
	 * N - 用电量, kw·h
	 * q(d) - 电力折标煤系数，0.1229kgce/(kw·h)
	 * <p>
	 * Es = G·q(s) 用水量折算标煤的量，Kg
	 * G - 用水量, t
	 * q(s) - 用水折标煤系数，0.2571kgce/t
	 * <p>
	 * kgce是能源消耗量， 用标准煤表示，kgce／t的意思就是：千克标准煤／吨，。
	 * kgce／t 是衡量一款设备能耗的单位，Kgce 是1公斤标准煤的能量，t 应该投入燃烧物的质量以吨为单位。即一吨物资（石油，天然气，木材都可以）产生多少公斤煤的能量。
	 * <p>
	 * ce是英语consumed energy缩写，是消耗能源的意思
	 */
	private static final BigDecimal QR = BigDecimal.valueOf(0.03412);
	private static final BigDecimal QD = BigDecimal.valueOf(0.1229);
	private static final BigDecimal QS = BigDecimal.valueOf(0.2571);


	public static BigDecimal comprehensiveEnergy(BigDecimal q/*热*/, BigDecimal n/*电*/, BigDecimal g/*水*/) {
		BigDecimal e = q.multiply(QR).add(n.multiply(QD)).add(g.multiply(QS));
		/*
		BigDecimal.ROUND_HALF_EVEN
		四舍五入
	    居中情况下向偶数舍入
		 */
		return e.setScale(3, BigDecimal.ROUND_HALF_EVEN);
	}
}
