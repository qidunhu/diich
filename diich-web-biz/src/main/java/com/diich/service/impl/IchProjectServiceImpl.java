package com.diich.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.diich.core.Constants;
import com.diich.core.base.BaseService;
import com.diich.core.model.*;
import com.diich.core.service.IchCategoryService;
import com.diich.core.service.IchProjectService;
import com.diich.mapper.ContentFragmentMapper;
import com.diich.mapper.IchProjectMapper;
import com.diich.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
@Service("ichProjectService")
public class IchProjectServiceImpl extends BaseService<IchProject> implements IchProjectService {

    @Autowired
    private IchProjectMapper ichProjectMapper;
    @Autowired
    private UserMapper userMapper;


    @Autowired
    private ContentFragmentMapper contentFragmentMapper;

    @Autowired
    private IchCategoryService ichCategoryService;

    /*@Autowired
    private DataSourceTransactionManager transactionManager;*/

    public Map<String, Object> getIchProject(String id) {

        if(id == null || "".equals(id)) {
            return setResultMap(Constants.PARAM_ERROR, null);
        }

        IchProject ichProject = null;

        try {
            ichProject = ichProjectMapper.selectByPrimaryKey(Long.parseLong(id));

            if(ichProject != null) {
                Long ichCategoryId = ichProject.getIchCategoryId() == null ? ichProject.getIchCategoryId() : 0;
                IchCategory ichCategory = ichCategoryService.getIchCategory(ichProject.getIchCategoryId());
                if(ichCategory != null) {
                    ichProject.setIchCategory(ichCategory);
                }

                User user = userMapper.selectByPrimaryKey(ichProject.getLastEditorId());
            }
        } catch (Exception e) {
            return setResultMap(Constants.INNER_ERROR, null);
        }

        return setResultMap(Constants.SUCCESS, ichProject);
    }


    public Map<String, Object> getIchProjectList(String text) {
        Map<String, Object> params = null;
        Integer current = 1;
        Integer pageSize = 10;

        try {
            params = JSON.parseObject(text);
        } catch (Exception e) {
            return setResultMap(Constants.PARAM_ERROR, null);
        }

        if(params.containsKey("current") && params.containsKey("pageSize")) {
            current = (Integer) params.get("current");
            pageSize = (Integer) params.get("pageSize");
        }

        Page<IchProject> page = new Page<IchProject>(current, pageSize);

        List<IchProject> ichItemList = null;
        try {
            ichItemList = ichProjectMapper.selectICHItemList(page, params);
        } catch (Exception e) {
            return setResultMap(Constants.INNER_ERROR, null);
        }
        page.setRecords(ichItemList);

        return setResultMap(Constants.SUCCESS, page);
    }


    @Transactional
    public Map<String, Object> saveIchProject(String text) {
        TransactionStatus transactionStatus = getTransactionStatus();

        User user = new User();
        user.setLoginName("user");
        IchProject ichProject = null;

        try {
            ichProject = parseObject(text, IchProject.class);
        } catch (Exception e) {
            return setResultMap(Constants.PARAM_ERROR, null);
        }

        try {
            if(ichProject.getId() == null) {
                ichProject.setId(IdWorker.getId());
                long proID = ichProjectMapper.insertSelective(ichProject);

                List<Attribute> ls = ichProject.getAttributeList();

                for(int i=0;i<ls.size();i++){
                    Attribute a = ls.get(i);
                    ContentFragment c = new ContentFragment();
                    c.setStatus(a.getStatus());
                    c.setTargetId(proID);
                    c.setAttributeId(a.getId());
                    c.setTargetType(a.getTargetType());
                    if(a.getTargetType()==0){ //短文本
                        c.setTitle(a.getValue());
                    }

                    if(a.getTargetType()==1){//长文本
                        c.setContent(a.getValue());
                    }

                    contentFragmentMapper.insertSelective(c);
                }



                //userMapper.insertSelective(user);
            } else {
                ichProjectMapper.updateByPrimaryKeySelective(ichProject);
            }

        } catch (Exception e) {
            rollback(transactionStatus);
            return setResultMap(Constants.INNER_ERROR, null);
        }

        return setResultMap(Constants.SUCCESS, ichProject);
    }


}
