package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceEvaluation;
import com.bmts.heating.commons.db.mapper.ForecastSourceEvaluationMapper;
import com.bmts.heating.commons.db.service.ForecastSourceEvaluationService;
import org.springframework.stereotype.Service;

@Service
public class ForecastSourceEvaluationServiceImpl extends ServiceImpl<ForecastSourceEvaluationMapper, ForecastSourceEvaluation> implements ForecastSourceEvaluationService {
}
