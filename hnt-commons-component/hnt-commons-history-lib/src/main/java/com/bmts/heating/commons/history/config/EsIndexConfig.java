package com.bmts.heating.commons.history.config;

import com.bmts.heating.commons.history.pojo.EsData;
import com.bmts.heating.commons.history.pojo.EsEnergyData;
import com.bmts.heating.commons.history.util.EsIndexEntry;
import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.commons.utils.es.EsIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;


@Slf4j
@Component//项目启动时自动创建索引
public class EsIndexConfig implements ApplicationRunner {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;



	private void createEsIndex() {
		EsIndex[] values = EsIndex.values();//获取所有index。暂时写在Enum中
		for (EsIndex value : values) {
			String index = EsIndex.valueOf(value.toString()).getIndex();
			EsIndexEntry.setSuffix(index);
			IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(EsData.class);
			if(!indexOperations.exists()){//判断索引是否已经建立
				log.info("创建index---{}",index);
				try{
					if (indexOperations.create() /*索引创建*/){
						log.info("创建index---{} 成功",index);
					}
					Document mapping = indexOperations.createMapping(EsData.class);//定义映射
					if(indexOperations.putMapping(mapping)){
						log.info("创建index映射成功---{}，Class--{}",index, EsData.class.getName());
						indexOperations.refresh();
					}else{
						log.warn("创建index映射失败---{}，Class--{}",index, EsData.class.getName());
					}
				}catch (Exception e){
					log.error("！！！创建index失败---{}",index);
					e.printStackTrace();
				}
			}
			EsIndexEntry.setSuffix(null);/*置空。防止影响其他服务进程*/
		}
	}
	private void createEsEnergyIndex() {
		EsEnergyIndex[] values = EsEnergyIndex.values();//获取所有index。暂时写在Enum中
		for (EsEnergyIndex value : values) {
			String index = EsEnergyIndex.valueOf(value.toString()).getIndex();
			EsIndexEntry.setSuffix(index);
			IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(EsEnergyData.class);
			if(!indexOperations.exists()){//判断索引是否已经建立
				log.info("创建index---{}",index);
				try{
					if (indexOperations.create() /*索引创建*/){
						log.info("创建index---{} 成功",index);
					}
					Document mapping = indexOperations.createMapping(EsEnergyData.class);//定义映射
					if(indexOperations.putMapping(mapping)){
						log.info("创建index映射成功---{}，Class--{}",index, EsEnergyData.class.getName());
						indexOperations.refresh();
					}else{
						log.warn("创建index映射失败---{}，Class--{}",index, EsEnergyData.class.getName());
					}
				}catch (Exception e){
					log.error("！！！创建index失败---{}",index);
					e.printStackTrace();
				}
			}
			EsIndexEntry.setSuffix(null);/*置空。防止影响其他服务进程*/
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		this.createEsIndex();
//		this.createEsEnergyIndex();
	}
}
