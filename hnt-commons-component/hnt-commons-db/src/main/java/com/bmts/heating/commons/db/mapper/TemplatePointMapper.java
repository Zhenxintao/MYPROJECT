package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.TemplatePoint;
import com.bmts.heating.commons.basement.model.db.response.template.TemplateControlResponse;
import com.bmts.heating.commons.basement.model.db.response.template.TemplatePointResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TemplatePointMapper extends BaseMapper<TemplatePoint> {


    @Select("select * from pointStandard ps where ps.id not in " +
            "(select tp.pointStandardId from templatePoint tp where tp.pointTemplateConfigId = #{id})")
    Page<PointStandard> loadOtherPoint(Page page, Integer id);

    @Select("select * from pointStandard ps where ps.id not in " +
            "(select tp.pointStandardId from templatePoint tp where tp.pointTemplateConfigId = #{templateId}) " +
            "and (ps.name like CONCAT('%',#{keyWord},'%') OR ps.columnName like CONCAT('%',#{keyWord},'%'))")
    Page<PointStandard> loadOtherPointAndSearch(Page page, @Param("templateId") Integer templateId, @Param("keyWord") String keyWord);


    @Select("SELECT tp.*,ps.columnName ,ps.name pointStandardName,(CASE ps.pointConfig  WHEN 1 THEN '采集量'  WHEN 2 THEN  '控制量'  ELSE '未知' END) AS pointConfigName ,\n" +
            " dic.name typeName,\n" +
            " ps.netFlag,ppt.name pointParameterTypeName,pu.unitName,pu.unitValue FROM templatePoint tp \n" +
            " LEFT JOIN pointStandard ps ON tp.pointStandardId = ps.id LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId = ppt.id\n" +
            " LEFT JOIN pointUnit pu ON pu.id = ps.pointUnitId \n" +
            " LEFT JOIN dic ON dic.id = ps.type = ppt.id ${ew.customSqlSegment}")
    Page<TemplateControlResponse> pageUnion(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("INSERT INTO templatePoint (\n" +
            "  `accidentLower`,\n" +
            "  `accidentHigh`,\n" +
            "  `runningLower`,\n" +
            "  `runningHigh`,\n" +
            "  `showSort`,\n" +
            "  `isAlarm`,\n" +
            "  `isNegation`,\n" +
            "  `DescriptionJson`,\n" +
            "  `alarmValue`,\n" +
            "  `alarmConfigId`,\n" +

//			"  `pointAddress`,\n" +

            "  `deviceConfigId`,\n" +
            "  `orderValue`,\n" +
            "  `washArray`,\n" +
            "  `pointStandardId`,\n" +
            "  `expandDesc`,\n" +
            "  `createUser`,\n" +
            "  `createTime`,\n" +
            "  `updateUser`,\n" +
            "  `updateTime`,\n" +
            "  `description`,\n" +
            "  `deleteFlag`,\n" +
            "  `deleteTime`,\n" +
            "  `deleteUser`,\n" +
            "  `userId`,\n" +
            "  `level`,\n" +
            "  `upperBound`,\n" +
            "  `lowerBound`,\n" +
            "  `correction`,\n" +
            "  `dataType`,\n" +
            "  `washDArray`,\n" +
            "  `sortFlag`,\n" +
            "  `pointTemplateConfigId`\n" +
            ")\n" +
            "SELECT\n" +
            "    pc.accidentLower,\n" +
            "    pc.accidentHigh,\n" +
            "    pc.runningLower,\n" +
            "    pc.runningHigh,\n" +
            "    pc.showSort,\n" +
            "    pc.isAlarm,\n" +
            "    pc.isNegation,\n" +
            "    pc.DescriptionJson,\n" +
            "    pc.alarmValue,\n" +
            "    pc.alarmConfigId,\n" +

//			"    pc.pointAddress,\n" +
			
            "    pc.deviceConfigId,\n" +
            "    pc.orderValue,\n" +
            "    pc.washArray,\n" +
            "    pc.pointStandardId,\n" +
            "    pc.expandDesc,\n" +
            "    pc.createUser,\n" +
            "    pc.createTime,\n" +
            "    pc.updateUser,\n" +
            "    pc.updateTime,\n" +
            "    pc.description,\n" +
            "    pc.deleteFlag,\n" +
            "    pc.deleteTime,\n" +
            "    pc.deleteUser,\n" +
            "    pc.userId,\n" +
            "    pc.level,\n" +
            "    pc.upperBound,\n" +
            "    pc.lowerBound,\n" +
            "    pc.correction,\n" +
            "    pc.dataType,\n" +
            "    pc.washDArray,\n" +
            "    pc.sortFlag,\n" +
            "   #{templateId}" +
            "  FROM pointConfig pc INNER JOIN pointStandard ps on pc.pointStandardId = ps.id ${ew.customSqlSegment}")
    void copyControlTemplate(@Param("templateId") Integer templateId, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT tc.*,ps.pointConfig,ps.netFlag,ps.name AS pointStandardName,ps.columnName FROM templatePoint tc " +
            "LEFT JOIN pointStandard ps ON ps.id =tc.pointStandardId AND tc.deleteFlag = false ${ew.customSqlSegment}")
    List<TemplatePointResponse> queryPointTemplate(@Param(Constants.WRAPPER) Wrapper wrapper);

}
