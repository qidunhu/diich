package com.diich.core.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface SearchService {

    //搜索框联想使用
     List<String> searchText(String keyword,int size);


    //搜索结果菜单使用
    Map searchText(Map<String,Object> map );

    //搜索存储过程调用
    Map searchTextByProcedure(Map<String,Object> map );


    //搜索结果菜单使用
    Map searchTextNew(Map<String,Object> map );


}
