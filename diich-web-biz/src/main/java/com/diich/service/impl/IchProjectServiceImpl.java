package com.diich.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.diich.core.Constants;
import com.diich.core.base.BaseService;
import com.diich.core.model.*;
import com.diich.core.service.IchCategoryService;
import com.diich.core.service.IchProjectService;
import com.diich.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private AttributeMapper attributeMapper;
    @Autowired
    private ContentFragmentMapper contentFragmentMapper;
    @Autowired
    private ContentFragmentResourceMapper contentFragmentResourceMapper;
    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private IchCategoryService ichCategoryService;
    @Autowired
    private IchMasterMapper ichMasterMapper;
    @Autowired
    private WorksMapper worksMapper;

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
                 // User user = userMapper.selectByPrimaryKey(ichProject.getLastEditorId());
                //获取传承人列表
                List<IchMaster> ichMasterList = ichMasterMapper.selectByIchProjectId(Long.parseLong(id));
                ichProject.setIchMasterList(ichMasterList);
                //作品列表
                List<Works> worksList = worksMapper.selectByIchProjectId(Long.parseLong(id));
                ichProject.setWorksList(worksList);
            }

            //获取项目的field
            ContentFragment c = new ContentFragment();
            c.setTargetId(Long.parseLong(id));
            c.setTargetType(0);//标示项目
            List<ContentFragment> ls =  contentFragmentMapper.selectByTargetIdAndType(c);
            //List<ContentFragment> ls_ = new ArrayList<ContentFragment>();
            for(int i=0;i<ls.size();i++){
                ls.get(i).setAttribute(attributeMapper.selectByPrimaryKey(ls.get(i).getAttributeId()));
                Long contentFragmentId = ls.get(i).getId();
                List<ContentFragmentResource> contentFragmentResourceList = contentFragmentResourceMapper.selectByContentFragmentId(contentFragmentId);
                List<Resource> resourceList = new ArrayList<>();
                for (ContentFragmentResource contentFragmentResource: contentFragmentResourceList) {
                    Resource resource = resourceMapper.selectByPrimaryKey(contentFragmentResource.getResourceId());
                    resourceList.add(resource);
                }
                ls.get(i).setResourceList(resourceList);
            }
            ichProject.setContentFragmentList(ls);


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
            ichItemList = ichProjectMapper.selectIchProjectList(page, params);
//            System.out.println("size:"+ichItemList.size());
            for (IchProject ichProject:ichItemList) {

                if(ichProject != null) {
                    Long ichCategoryId = ichProject.getIchCategoryId() == null ? ichProject.getIchCategoryId() : 0;

                    IchCategory ichCategory = ichCategoryService.getIchCategory(ichProject.getIchCategoryId());

                    if(ichCategory != null) {
                        ichProject.setIchCategory(ichCategory);
                    }
                    // User user = userMapper.selectByPrimaryKey(ichProject.getLastEditorId());
                    //获取传承人列表
                    List<IchMaster> ichMasterList = ichMasterMapper.selectByIchProjectId(ichProject.getId());
                    ichProject.setIchMasterList(ichMasterList);
                    //作品列表
                    List<Works> worksList = worksMapper.selectByIchProjectId(ichProject.getId());
                    ichProject.setWorksList(worksList);
                }

                //获取项目的field
                ContentFragment c = new ContentFragment();
                c.setTargetId(ichProject.getId());
                c.setTargetType(0);//标示项目
                List<ContentFragment> ls =  contentFragmentMapper.selectByTargetIdAndType(c);
                //List<ContentFragment> ls_ = new ArrayList<ContentFragment>();
                for(int i=0;i<ls.size();i++){
                    ls.get(i).setAttribute(attributeMapper.selectByPrimaryKey(ls.get(i).getAttributeId()));
                    Long contentFragmentId = ls.get(i).getId();
                    List<ContentFragmentResource> contentFragmentResourceList = contentFragmentResourceMapper.selectByContentFragmentId(contentFragmentId);
                    List<Resource> resourceList = new ArrayList<>();
                    for (ContentFragmentResource contentFragmentResource: contentFragmentResourceList) {
                        Resource resource = resourceMapper.selectByPrimaryKey(contentFragmentResource.getResourceId());
                        resourceList.add(resource);
                    }
                    ls.get(i).setResourceList(resourceList);
                }
                ichProject.setContentFragmentList(ls);

            }

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

                long proID = IdWorker.getId();

                ichProject.setId(proID);
                ichProjectMapper.insertSelective(ichProject);
                System.out.println(proID);
                 List<ContentFragment> ls = ichProject.getContentFragmentList();

             for(int i=0;i<ls.size();i++){
                 ContentFragment c = ls.get(i);

                    c.setId(IdWorker.getId());

                    c.setTargetId(proID);

                    contentFragmentMapper.insertSelective(c);
                }

                //userMapper.insertSelective(user);
            } else {
                ichProjectMapper.updateByPrimaryKeySelective(ichProject);
            }
            commit(transactionStatus);
        } catch (Exception e) {
            rollback(transactionStatus);
            return setResultMap(Constants.INNER_ERROR, null);
        }

        return setResultMap(Constants.SUCCESS, ichProject);
    }


}
