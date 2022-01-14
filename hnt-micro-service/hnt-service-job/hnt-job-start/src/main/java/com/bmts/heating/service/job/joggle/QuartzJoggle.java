package com.bmts.heating.service.job.joggle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.QuartzJob;
import com.bmts.heating.commons.container.quartz.JobTimeStageType;
import com.bmts.heating.commons.container.quartz.service.JobService;
import com.bmts.heating.commons.db.service.QuartzJobService;
import com.bmts.heating.commons.entiy.quartzjob.request.QueryJobDto;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author naming
 * @description
 * @date 2021/3/17 11:02
 **/
@RestController
@Slf4j
@RequestMapping("quartz")
public class QuartzJoggle {
    @Autowired
    private QuartzJobService quartzJobService;
    @Autowired
    private JobService jobService;

    @ApiOperation("新增定时任务")
    @PostMapping("/insertJob")
    public Response insertJob(@RequestBody QuartzJob dto) {
        try {
            if (AddJob(dto)) {
                Boolean result = quartzJobService.save(dto);
                return result ? Response.success() : Response.fail();
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/removeJob")
    public Response removeJob(@RequestParam Integer id) {
        try {
            QuartzJob quartzJob = quartzJobService.getById(id);
            Boolean jobStatus = jobService.deleteJob(quartzJob.getName(), quartzJob.getGroupName());
            if (jobStatus) {
                Boolean result = quartzJobService.removeById(id);
                return result ? Response.success() : Response.fail();
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("修改定时任务")
    @PutMapping("/updJob")
    public Response updJob(@RequestBody QuartzJob dto) {
        try {
            QuartzJob quartzJob = quartzJobService.getById(dto.getId());
            Boolean jobStatus = jobService.deleteJob(quartzJob.getName(), quartzJob.getGroupName());
            if (jobStatus) {
                if (AddJob(dto)) {
                    if (!dto.getStatus()) {
                        jobService.pauseJob(dto.getName(), dto.getGroupName());
                    }
                    Boolean result = quartzJobService.updateById(dto);
                    return result ? Response.success() : Response.fail();
                }
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    public Boolean AddJob(QuartzJob dto) {
        Class classInfo = SpringBeanFactory.getBean(dto.getBeanName(), Job.class).getClass();
        if (dto.getType() == 1) {
            return jobService.addCronJob(dto.getName(), dto.getGroupName(), dto.getCron(), classInfo, null);
        }
        if (dto.getType() == JobTimeStageType.MINUTE.type()) {
            return jobService.addJob(dto.getName(), dto.getGroupName(), Integer.parseInt(dto.getCron()), classInfo, null, JobTimeStageType.MINUTE.type());
        }
        if (dto.getType() == JobTimeStageType.SECOND.type()) {
            return jobService.addJob(dto.getName(), dto.getGroupName(), Integer.parseInt(dto.getCron()), classInfo, null, JobTimeStageType.SECOND.type());
        }
        if (dto.getType() == JobTimeStageType.HOUR.type()) {
            return jobService.addJob(dto.getName(), dto.getGroupName(), Integer.parseInt(dto.getCron()), classInfo, null, JobTimeStageType.HOUR.type());
        }
        return true;
    }

    @ApiOperation("查询定时任务")
    @PostMapping("/queryJob")
    public Response queryJob(@RequestBody QueryJobDto dto) {
        try {
            QueryWrapper<QuartzJob> wrapper = new QueryWrapper<>();
            IPage<QuartzJob> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            if (dto.getStatus() != null) {
                wrapper.eq("status", dto.getStatus());
            }
            if (dto.getType() != null) {
                wrapper.eq("type", dto.getType());
            }
            return Response.success(quartzJobService.page(page, wrapper));
        } catch (Exception e) {
            return Response.fail();
        }
    }

}
