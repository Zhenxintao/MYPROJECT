package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.db.service.DicService;
import com.bmts.heating.commons.entiy.baseInfo.request.DicDto;
import com.bmts.heating.commons.redis.service.RedisDicService;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/*
import com.bmts.heating.commons.auth.entity.response.AuthResponse;
import com.bmts.heating.commons.auth.service.AuthrityService;
*/

@Api(tags = "字典管理")
@RestController
@RequestMapping("/dic")
@Slf4j
public class DicJoggle {
    @Autowired
    DicService dicService;
    @Autowired
    RedisDicService redisDicService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody Dic info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        if (dicService.getOne(Wrappers.<Dic>lambdaQuery().eq(Dic::getCode, info.getCode())) != null) {
            return Response.fail("唯一编码code 已经存在");
        }
        if (dicService.save(info)) {
            redisDicService.clear();
            return Response.success();
        }

        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody Dic info) {
        Response response = Response.fail();
        Dic dic = dicService.getById(info.getId());
        if (dic == null) {
            return Response.fail();
        }
        Dic one = dicService.getOne(Wrappers.<Dic>lambdaQuery().eq(Dic::getCode, info.getCode()));
        if (one != null && !Objects.equals(info.getId(), one.getId())) {
            return Response.fail("唯一编码code 已经存在");
        }
        info.setUpdateTime(LocalDateTime.now());
        if (dicService.updateById(info)) {
            redisDicService.clear();
            return Response.success();
        }
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<Dic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).or().eq("pid", id);
        if (dicService.remove(queryWrapper)) {
            redisDicService.clear();
            return Response.success();
        }

        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        Dic info = dicService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody DicDto dto) {
        Response response = Response.fail();
        try {
            IPage<Dic> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<Dic> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord()))
                queryWrapper.like("name", dto.getKeyWord());
            if (StringUtils.isNotBlank(String.valueOf(dto.getPid())))
                queryWrapper.eq("pid", dto.getPid());
            return Response.success(dicService.page(page, queryWrapper));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }

    @ApiOperation("查询全部")
    @PostMapping("/queryAll")
    public Response queryCache() {
        Response response = Response.fail();
        try {
            List<Dic> redisDic = redisDicService.queryAll();
            if (redisDic == null) {
                redisDic = dicService.list();
                if (redisDic != null)
                    redisDicService.set(redisDic);
            }
            return Response.success(redisDic);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询字典出错：{}", e.getMessage());
        }
        return response;
    }

    @Autowired
    RedisPointService redisPointService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    //
////    @ApiOperation("测试")
////    @PostMapping("test")
////    public Object  test()
////    {
////        Set<String> keys = redisTemplate.keys("ESPointCache::*");
////        redisTemplate.delete(keys);
////        String key="tscc:testpoint";
////        redisTemplate.opsForZSet().add(key,"system1",5);
////        redisTemplate.opsForZSet().add(key,"system2",7);
////        redisTemplate.opsForZSet().add(key,"system3",3);
////        redisTemplate.opsForZSet().add(key,"system4",3);
////        redisTemplate.opsForZSet().add(key,"system3",9);
////        Set<Object> objects = redisTemplate.opsForZSet().reverseRangeByScore(key, 1.0, 8.0,0,10);
////        objects.forEach(x->{
////            log.error("_________________________{}",x);
////        });
////        redisTemplate.opsForZSet().remove(key,"system1");
////        return objects;
////    }
//
//    @Autowired
//    RedisCollectPointService redisCollectPointService;
//    @Autowired
//    RedisCacheService redisCacheService;
//@Autowired
//    HeatSystemMapper heatSystemMapper;
//    @ApiOperation("测试")
//    @PostMapping("test")
//    public Object test(@RequestParam int length, @RequestParam boolean isSort, @RequestParam double startScore, double endScore) throws InterruptedException, MicroException {
////      return redisPointService.queryRank("tbag",startScore,endScore,length,isSort);
////        return redisCacheService.queryRank("tbag", startScore, endScore, length, isSort);
//        return redisCacheService.queryFirstNetBase();
//    }
//    @Autowired
//    MemoryService memoryService;
//    @ApiOperation("测试1")
//    @PostMapping("test1")
//    public Object test1() throws InterruptedException, MicroException, IOException {
////      return redisPointService.queryRank("tbag",startScore,endScore,length,isSort);
//        Map<Integer, String[]> map = new HashMap<>();
//        map.put(6, new String[]{"tbag"});
//       return memoryService.getPoints();
////        return redisCacheService.queryRealDataBySystems(map);
//    }


    //


    @ApiOperation("查询redis")
    @PostMapping("/queryRedis")
    public void queryRedis() {
        List<PointL> pointLS = new ArrayList<>();
        PointL pointL=new PointL();
        pointL.setPointId(123);
        pointL.setValue("234");
        pointL.setOldValue("234234");
        pointL.setPointName("test001");
        pointLS.add(pointL);
        redisPointService.setCachePoint(pointLS);
    }

    @ApiOperation("删除rediskey")
    @PostMapping("/deleteRedis")
    public void deleteRedis(@RequestParam String key) {
        Set<String> keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }
//
//    @Autowired
//    RedisPointService redisPointService;
//
//    @ApiOperation("测试存入缓存")
//    @PostMapping("insertRedis")
//    public void insertRedis(@RequestBody PointL pointL) {
//        redisPointService.setCachePoint(pointL);
//    }
//
//    @Autowired
//    AuthrityService authrityService;
//
//    @ApiOperation("用户权限测试")
//    @GetMapping("/authTest")
//    public List<AuthResponse> auth(@RequestParam int userId) {
//        List<AuthResponse> authResponses = authrityService.queryStationsByUser(userId);
//        return authResponses;
//    }
//
//    @ApiOperation("用户功能权限测试")
//    @GetMapping("/authTest1")
//    public List<SysPermission> auth1(@RequestParam int userId) {
//        return authrityService.queryPerms(userId);
//    }
//
//    @Autowired
//    RealDataNetEntryService realDataNetEntryService;
//
//    @ApiOperation(".net grpc 测试")
//    @GetMapping("/netTest")
//    public Object testNet(HttpServletRequest request) throws MicroException {
//        Object id = request.getAttribute("user_token");
//        return realDataNetEntryService.pushPoint(null);
//    }
//    @Autowired
//    SignalRTemplate signalRTemplate;
//    @GetMapping("signalR")
//    public void  testSignalR()
//    {
//        signalRTemplate.send2AllServer("alarm","报警测试");
//    }
}

























