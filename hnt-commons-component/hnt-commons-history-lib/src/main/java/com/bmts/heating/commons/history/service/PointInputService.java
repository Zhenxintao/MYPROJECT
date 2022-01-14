package com.bmts.heating.commons.history.service;


import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.commons.utils.es.EsIndex;

import java.util.List;
import java.util.Map;

public interface PointInputService {

	/**数据接入
	 * @param esIndex 操作的索引
	 * @param map 保存的数据
	 */
	void save(EsIndex esIndex, Map<String, Object> map);

	/**
	 * 数据批量接入
	 * @param esIndex 索引
	 * @param map 数据集
	 */
	void saveBulk(EsIndex esIndex, List<Map<String, Object>> map);


	/**定义新增列数据类型-collect
	 * @param esIndex Es索引
	 * @param fields 字段
	 * @return 当前索引的全部映射
	 */
	Map<String, Object> addFields(EsIndex esIndex, Map<String,String> fields);

	/**定义新增列数据类型-energy
	 * @param esIndex Es索引
	 * @param field 字段
	 * @return 当前索引的全部映射
	 */
	Map<String, Object> addEnergyFields(EsEnergyIndex esIndex, String field);

}
