package com.bmts.heating.commons.basement.model.db.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TemplateTree
 * @Author naming
 * @Date 2020/11/21 13:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonTree {
   private String id;
   private String pid;
   private int level;
   private String properties;
   private String name;
}
