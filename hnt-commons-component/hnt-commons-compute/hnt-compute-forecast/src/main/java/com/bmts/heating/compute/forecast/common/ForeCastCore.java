//package com.bmts.heating.compute.forecast.common;
//
//
//import com.bmts.heating.compute.forecast.pojo.ComputeLoadRateBo;
//import com.singularsys.jep.EvaluationException;
//import com.singularsys.jep.Jep;
//import com.singularsys.jep.JepException;
//import com.singularsys.jep.ParseException;
//import com.singularsys.jep.parser.Node;
//import com.singularsys.jep.reals.RealEvaluator;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Stream;
//
///**
// * @author naming
// * @description
// * @date 2021/3/24 13:14
// **/
//@Slf4j
//@Service
//public class ForeCastCore {
////    @Autowired
////    ExpressionsService expressionsService;
//
//    /**
//     * 公用计算 单个公式
//     *
//     * @param dto 传入对应参数实体
//     * @return
//     * @throws JepException
//     */
//    public BigDecimal compute(ComputeLoadRateBo dto,String expressions) throws JepException {
//        Jep jep = new Jep(new RealEvaluator());
//        Map<String, Object> maps = beanToMap(dto);
//        maps.forEach((k, v) -> {
//            try {
//                jep.addVariable(k, v);
//            } catch (JepException e) {
//                log.info("addVariable cause exception :{},params: {}", e.getMessage(), dto.toString());
//                e.printStackTrace();
//            }
//        });
//        BigDecimal result = new BigDecimal(jep.evaluate(jep.parse(expressions)).toString());
//        return result;
//    }
//
//    /**
//     * 提供辅助算法
//     * @param params
//     * @param expressions
//     * @param type
//     * @return
//     * @throws ParseException
//     * @throws EvaluationException
//     */
//    public BigDecimal compute(Map<String, Object> params, String expressions, int type) throws ParseException, EvaluationException {
//        Jep jep = new Jep(new RealEvaluator());
//        params.forEach((k, v) -> {
//            try {
//                jep.addVariable(k, v);
//            } catch (JepException e) {
//                log.info("addVariable cause exception :{}", e.getMessage());
//                e.printStackTrace();
//            }
//        });
//        switch (type) {
//            case 1: {
//                return new BigDecimal(jep.evaluate(jep.parse(expressions)).toString());
//            }
//            case 2: {
//                jep.initMultiParse(expressions);
//                Node n;
//                BigDecimal result = new BigDecimal(0);
//                while ((n = jep.continueParsing()) != null) {
//                    result = new BigDecimal(jep.evaluate(n).toString());
//                }
//                return result;
//            }
//            default:
//                return new BigDecimal(0);
//        }
//
//    }
//
////    /**
////     * 多表达式计算
////     *
////     * @param dtos
////     * @param expressionKey
////     * @return
////     * @throws JepException
////     */
////    public BigDecimal computeMulti(List<ComputeBase> dtos, String expressionKey) throws JepException {
////        Jep jep = new Jep(new RealEvaluator());
////        Expressions expression = expressionsService.getOne(Wrappers.<Expressions>lambdaQuery().eq(Expressions::getExpressionkey, expressionKey));
////        if (expression == null)
////            throw new JepException("表达式为空");
////        dtos.forEach(dto -> {
////            Map<String, Object> maps = beanToMap(dto);
////            maps.forEach((k, v) -> {
////                try {
////                    jep.addVariable(k, v);
////                } catch (JepException e) {
////                    log.info("addVariable cause exception :{},params: {}", e.getMessage(), dto.toString());
////                    e.printStackTrace();
////                }
////            });
////        });
////        jep.initMultiParse(expression.getContent());
////        Node n;
////        BigDecimal result = new BigDecimal(0);
////        while ((n = jep.continueParsing()) != null) {
////            result = new BigDecimal(jep.evaluate(n).toString());
////        }
////        return result;
////
////    }
//
//    public static Map<String, Object> beanToMap(Object object) {
//        Map<String, Object> map = new HashMap<>();
//        Field[] fields = object.getClass().getDeclaredFields();
//        Stream.of(fields).forEach(field -> {
//            try {
//                if (!"serialVersionUID".equals(field.getName())) {
//                    field.setAccessible(true);
//                    map.put(field.getName(), field.get(object));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        return map;
//    }
//
//}
