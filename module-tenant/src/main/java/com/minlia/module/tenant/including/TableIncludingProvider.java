package com.minlia.module.tenant.batis.including;

import java.util.List;

/**
 * Created by will on 10/8/17.
 */
public interface TableIncludingProvider {

  //获取到允许隔离的表名集
  //在后面进行隔离的时候使用
  public List<String> including();


}
