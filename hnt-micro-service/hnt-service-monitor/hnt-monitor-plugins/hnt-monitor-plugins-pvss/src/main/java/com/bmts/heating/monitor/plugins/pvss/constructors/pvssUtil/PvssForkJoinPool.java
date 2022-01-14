//package com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil;
//
//import com.bmts.heating.commons.utils.msmq.PointL;
//import com.bmts.heating.monitor.dirver.config.MonitorMuster;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.RecursiveTask;
//
//public class PvssForkJoinPool extends RecursiveTask<List<PointL>>{
//
//        private Integer startValue;// 子任务开始计算的值
//        private Integer endValue;// 子任务结束计算的值
//        private RedisCacheService redisCacheService;
//        private MonitorMuster.Plugin plugin;
//        public PvssForkJoinPool(Integer startValue, Integer endValue, RedisCacheService redisCacheService, MonitorMuster.Plugin plugin) {
//            this.startValue = startValue;
//            this.endValue = endValue;
//            this.redisCacheService = redisCacheService;
//            this.plugin = plugin;
//        }
//
//        @Override
//        protected List<PointL> compute() {
//            if(endValue - startValue <= 10000) {
////                System.out.println("开始计算的部分：startValue = " + startValue + ";endValue = " + endValue);
//                List<PointL> points = null;
//                try {
//                    points = redisCacheService.getPoints(plugin.getDevice_id(),startValue,endValue);
//                } catch (IOException | MicroException e) {
//                    e.printStackTrace();
//                }
//                return points;
//            }else {// 否则再进行任务拆分，拆分成两个任务
//                PvssForkJoinPool pFJP = new PvssForkJoinPool(startValue, (startValue + endValue) / 2, redisCacheService, plugin);
//                pFJP.fork();
//                PvssForkJoinPool pFJP1 = new PvssForkJoinPool((startValue + endValue) / 2 + 1 , endValue,redisCacheService,plugin);
//                pFJP1.fork();
//                List<PointL> point=pFJP.join();
//                point.addAll(pFJP1.join());
//                return point;
//            }
//
//        }
//}
