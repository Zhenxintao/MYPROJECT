package com.bmts.heating.compute.forecast.heatsource;

import com.bmts.heating.commons.entiy.forecast.algorithm.*;
import com.bmts.heating.commons.entiy.forecast.response.ForecastTempResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class HeatSourceArithmetic {
    /**
     * 在室外某时刻室外温度下标准工况热负荷率ηz
     */
    public float forecastHeatLoadRate(ForecastHeatLoadRate dto) {
        try {
            float loadRate = (dto.getDesignCountTemp().floatValue() - dto.getDesignForecastTemp().floatValue()) /
                    (dto.getDesignCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue());
            return loadRate;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 当室内温度由室内设计计算温度t´n变为tn下的严寒季热负荷指标q(W/㎡)
     */
    public float forecastTempHeatLoadIndex(ForecastTempHeatLoadIndex dto) {
        try {
            float loadIndex = dto.getAreaHeatIndex().floatValue() * (dto.getDesignRealCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue()) / (dto.getDesignCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue());
            return loadIndex;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 在室外某时刻室外温度下的热负荷率η´
     */
    public float forecastChangeHeatLoadRate(ForecastChangeHeatLoadRate dto) {
        try {
            float loadRate = (dto.getDesignRealCountTemp().floatValue() - dto.getDesignForecastTemp().floatValue()) / (dto.getDesignRealCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue());
            return loadRate;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 在室外某时刻室外温度下的热负荷指标q”
     */
    public float forecastHeatLoadIndex(ForecastHeatLoadIndex dto) {
        try {
            float loadIndex = dto.getHeatLoadIndex().floatValue() * dto.getHeatLoadRate().floatValue();
            return loadIndex;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 在室外某时刻室外温度下，在热负荷调整系数β下的实际计算热负荷指标q´
     */
    public float forecastCoefficientHeatLoadIndex(ForecastCoefficientHeatLoadIndex dto) {
        try {
            float loadIndex = dto.getHeatLoadIndex().floatValue() * dto.getCoefficient().floatValue();
            return loadIndex;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 实际计算热负荷率η
     */
    public float forecastRealHeatLoadRate(ForecastRealHeatLoadRate dto) {
        try {
            float loadRate = dto.getCoefficientHeatLoadIndex().floatValue() / dto.getHeatLoadIndex().floatValue();
            return loadRate;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 预测用热量信息（小时）
     */
    public float forecastWithHeatHour(ForecastWithHeatHour dto) {
        try {
            float heatWithHour = dto.getRealCountHeatLoadIndex().floatValue()
                    * dto.getArea().floatValue() * (float) 3.6 * (float) Math.pow(10, -6);
            return heatWithHour;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 预测用热量信息（阶段、天）
     */
    public float forecastWithHeat(ForecastWithHeat dto) {
        try {
            float heatWith = dto.getRealCountHeatLoadIndex().floatValue()
                    * dto.getArea().floatValue() * (float) 3.6 * (float) Math.pow(10, -6) * dto.getHrNumber().floatValue();
            return heatWith;
        } catch (Exception e) {
            return 0;
        }
    }
//    /**
//     * 预测用热量信息（阶段、天）
//     */
//    public float forecastWithHeat(ForecastWithHeat dto) {
//        try {
//            float heatWith = dto.getRealCountHeatLoadIndex().floatValue()
//                    * ((dto.getRealCountTemp().floatValue() - dto.getForecastTemp().floatValue())
//                    / (dto.getRealCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue()))
//                    * dto.getArea().floatValue() * (float) 3.6 * (float) Math.pow(10, -6) * dto.getHrNumber().floatValue();
//            return heatWith;
//        } catch (Exception e) {
//            return 0;
//        }
//    }

    /**
     * 预测散热器供暖二次网供回水温度
     */
    public ForecastTempResponse forecastRadiatorSTemp(ForecastRadiatorSTemp dto) {
        try {
            double b = 0.3;
            float startValue = dto.getDesignCountTemp().floatValue() + ((dto.getDesignTempG().floatValue() + dto.getDesignTempH().floatValue()) / 2 - dto.getRealCountTemp().floatValue())
                    * (float) Math.pow(dto.getLoadRateStandardConditions().doubleValue(), 1 / (1 + b));
            float endValue = ((dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue()) / 2)
                    * (dto.getLoadRateStandardConditions().floatValue() / dto.getRelativeDischargeS().floatValue());
            float tg = startValue + endValue;
            float th = startValue - endValue;
            ForecastTempResponse forecastTempResponse = new ForecastTempResponse();
            forecastTempResponse.setTg(tg);
            forecastTempResponse.setTh(th);
            return forecastTempResponse;
        } catch (Exception e) {
            return new ForecastTempResponse();
        }
    }

    /**
     * 预测地板辐射供暖二次网供回水温度
     */
    public ForecastTempResponse forecastFloorSTemp(ForecastFloorSTemp dto) {
        try {
            float startValue = dto.getDesignCountTemp().floatValue() + (dto.getFloorAvgTemp().floatValue() - dto.getDesignCountTemp().floatValue()) * (float) Math.pow(dto.getLoadRateStandardConditions().doubleValue(), 0.969);
            float endValueF = ((dto.getDesignTempG().floatValue() + dto.getDesignTempH().floatValue()) / 2 - dto.getDesignOutdoorAvgTemp().floatValue() + (dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue()) / 2 * dto.getRelativeDischargeS().floatValue()) * dto.getLoadRateStandardConditions().floatValue();
            float endValueS = ((dto.getDesignTempG().floatValue() + dto.getDesignTempH().floatValue()) / 2 - dto.getDesignOutdoorAvgTemp().floatValue() - (dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue()) / 2 * dto.getRelativeDischargeS().floatValue()) * dto.getLoadRateStandardConditions().floatValue();
            float tg = startValue + endValueF;
            float th = startValue + endValueS;
            ForecastTempResponse forecastTempResponse = new ForecastTempResponse();
            forecastTempResponse.setTg(tg);
            forecastTempResponse.setTh(th);
            return forecastTempResponse;
        } catch (Exception e) {
            return new ForecastTempResponse();
        }
    }

    /**
     * 地板表面平均温度t''pj
     */
    public float forecastFloorAvgTemp(ForecastFloorAvgTemp dto) {
        try {
            float result = (float) Math.pow(dto.getHeatDissipatingCapacity().doubleValue() / 100, 0.969);
            float floorAvgTemp = dto.getDesignCountTemp().floatValue() + (float) 9.82 + result;
            return floorAvgTemp;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 系数d计算
     */
    public float forecastCoefficient(ForecastCoefficient dto) {
        try {
            float coefficient = (dto.getRelativeDischargeS().floatValue() * (dto.getDesignFTempG().floatValue() - dto.getDesignFTempH().floatValue()) - dto.getRelativeDischargeF().floatValue() * (dto.getDesignSTempG().floatValue() - dto.getDesignSTempH().floatValue()))
                    / ((float) Math.pow(dto.getRelativeDischargeF().doubleValue(), 0.5) * (float) Math.pow(dto.getRelativeDischargeS().doubleValue(), 0.5)
                    * (((dto.getDesignFTempG().floatValue() - dto.getDesignSTempG().floatValue()) - (dto.getDesignFTempH().floatValue() - dto.getDesignSTempH().floatValue()))
                    / (float) Math.log10((dto.getDesignFTempG().floatValue() - dto.getDesignSTempG().floatValue()) / (dto.getDesignFTempH().floatValue() - dto.getDesignSTempH().floatValue()))));
            return coefficient;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 预测一次网供回水温度
     */
    public ForecastTempResponse forecastFirstTemp(ForecastFirstTemp dto) {
        try {
            float tg = (((dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue()) * (dto.getLoadRateStandardConditions().floatValue() / dto.getRelativeDischargeF().floatValue()) + dto.getForecastSTempH().floatValue())
                    * (float) Math.pow(Math.E, dto.getCoefficient().doubleValue()) - dto.getForecastSTempG().floatValue())
                    / ((float) Math.pow(Math.E, dto.getCoefficient().doubleValue()) - 1);
            float th = tg - (((dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue())
                    * dto.getLoadRateStandardConditions().floatValue())
                    / dto.getRelativeDischargeF().floatValue());
            ForecastTempResponse forecastTempResponse = new ForecastTempResponse();
            forecastTempResponse.setTg(tg);
            forecastTempResponse.setTh(th);
            return forecastTempResponse;
        } catch (Exception e) {
            return new ForecastTempResponse();
        }
    }

    /**
     * 最大流量的调整Grw
     */
    public float forecastMaxFlow(ForecastMaxFlow dto) {
        try {
            float value = (dto.getHeatLoadIndex().floatValue() * dto.getArea().floatValue() * (float) Math.pow(10, -3))
                    / ((float) 1.163 * (dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue()));
            return value;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 一次网的相对流量(当室内温度由室内设计计算温度t´n变为tn下的流量的调整)
     */
    public float forecastRelativeFlowRate(ForecastRelativeFlowRate dto) {
        try {
            float value = dto.getNetStageFlow().floatValue() / dto.getMaxFlow().floatValue();
            return value;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 当室内温度由室内设计计算温度t´n变为tn下的二网供回水温度-散热器采暖方式或地板采暖方式严寒季供回水温度的调整
     */
    public ForecastTempResponse forecastChangeTemp(ForecastChangeTemp dto) {
        try {
            float commonValue = (dto.getRealCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue()) / (dto.getDesignCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue());
            float startValue = (dto.getDesignTempG().floatValue() + dto.getDesignTempH().floatValue() - 2 * dto.getDesignCountTemp().floatValue()) + 2 * dto.getRealCountTemp().floatValue();
            float endValue = dto.getDesignTempG().floatValue() - dto.getDesignTempH().floatValue();
            return forecastChangeTempCalculate(startValue, commonValue, endValue);
        } catch (Exception e) {
            return new ForecastTempResponse();
        }
    }

    /**
     * 当室内温度由室内设计计算温度t´n变为tn下的二网供回水温度-一次网严寒季供回水温度的调整
     */
    public ForecastTempResponse forecastChangeFirstTemp(ForecastChangeFirstTemp dto) {
        try {
            float startValue = (dto.getRealCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue()) / (dto.getDesignCountTemp().floatValue() - dto.getDesignOutCountTemp().floatValue());
            float commonValue = dto.getDesignFirstTempG().floatValue() - dto.getDesignFirstTempH().floatValue();
            float endValue = (dto.getDesignSecondTempGChange().floatValue() - dto.getDesignSecondTempHChange().floatValue()) / (dto.getDesignSecondTempG().floatValue() - dto.getDesignSecondTempH().floatValue());
            return forecastChangeTempCalculate(startValue, commonValue, endValue);
        } catch (Exception e) {
            return new ForecastTempResponse();
        }
    }

    public ForecastTempResponse forecastChangeTempCalculate(float startValue, float commonValue, float endValue) {
        float tg = (startValue * commonValue + endValue * commonValue) / 2;
        float th = tg - commonValue * endValue;
        ForecastTempResponse response = new ForecastTempResponse();
        response.setTg(tg);
        response.setTh(th);
        return response;
    }


    /**
     * 预测一次网供回水温度
     */
    public BigDecimal forecastFirstTg(BigDecimal forecastHeat, BigDecimal forecastFlow, BigDecimal forecastTh) {
        try {
            // 计算公式 ：预测热量（Gj/h）× 1000 ÷ 4.18 ÷ 预测流量（t/h） + 预测回温
            BigDecimal multiply = forecastHeat.multiply(new BigDecimal(1000));
            BigDecimal divideValue = multiply.divide(new BigDecimal(4.18), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal divide = divideValue.divide(forecastFlow, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal addValue = divide.add(forecastTh);
            return addValue;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

}
