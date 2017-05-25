package com.diich.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.diich.core.base.BaseService;
import com.diich.core.exception.ApplicationException;
import com.diich.core.model.*;
import com.diich.core.service.IchMasterService;
import com.diich.core.service.IchProjectService;
import com.diich.core.service.WorksService;
import com.diich.core.util.BuildHTMLEngine;
import com.diich.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */
@Service("worksService")
public class WorksServiceImpl extends BaseService<Works> implements WorksService{

    @Autowired
    private WorksMapper worksMapper;

    @Autowired
    private IchProjectMapper ichProjectMapper;
    @Autowired
    private IchMasterMapper ichMasterMapper;
    @Autowired
    private ContentFragmentMapper contentFragmentMapper;
    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private ContentFragmentResourceMapper contentFragmentResourceMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private IchProjectService ichProjectService;
    @Autowired
    private IchMasterService ichMasterService;

    public Works getWorks(String id) throws Exception{

        Works works = null;
        try{
           works = worksMapper.selectByPrimaryKey(Long.parseLong(id));
            if(works !=null){
                //获取所属项目信息
                IchProject ichProject = ichProjectService.getIchProjectById(works.getIchProjectId());
                works.setIchProject(ichProject);
                //获取传承人信息
                IchMaster ichMaster = ichMasterService.getIchMasterByWorks(works);
                works.setIchMaster(ichMaster);
            }
            //获取内容片断
            ContentFragment con = new ContentFragment();
            con.setTargetId(works.getId());
            con.setTargetType(2);
            List<ContentFragment> contentFragmentList = contentFragmentMapper.selectByTargetIdAndType(con);
            for (ContentFragment contentFragment : contentFragmentList) {
                Long attrId = contentFragment.getAttributeId();
                Attribute attribute = attributeMapper.selectByPrimaryKey(attrId);
                contentFragment.setAttribute(attribute);//添加属性
                List<ContentFragmentResource> contentFragmentResourceList = contentFragmentResourceMapper.selectByContentFragmentId(contentFragment.getId());
                List<Resource> resourceList = new ArrayList<>();
                for (ContentFragmentResource contentFragmentResource: contentFragmentResourceList) {
                    Resource resource = resourceMapper.selectByPrimaryKey(contentFragmentResource.getResourceId());
                    resourceList.add(resource);
                }
                contentFragment.setResourceList(resourceList);
            }
            works.setContentFragmentList(contentFragmentList);
        }catch(Exception e){
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }

        buildHTML("works.ftl", works, works.getId() + "");

        return works;
    }

    @Override
    public Page<Works> getWorksPage(Map<String, Object> params) throws Exception {

        Integer current = 1;
        Integer pageSize = 10;

        if(params != null && params.containsKey("current") && params.containsKey("pageSize")) {
            current = (Integer) params.get("current");
            pageSize = (Integer) params.get("pageSize");
        }

        Page<Works> page = new Page<Works>(current, pageSize);
        page.setCondition(params);
        List<Works> worksList = getWorksList(page);

        page.setRecords(worksList);
        return page;
    }

    @Override
    public List<Works> getWorksList(Page<Works> page) throws Exception {
        List<Works> worksList = null;
        try{
            Map<String, Object> condition = page.getCondition();
            if(condition == null){
                condition = new HashMap<>();
            }
           worksList = worksMapper.selectWorksList(page, page.getCondition());
           for (Works works:worksList) {
               //获取所属项目信息
               IchProject ichProject = ichProjectService.getIchProjectById(works.getIchProjectId());
               works.setIchProject(ichProject);
               //获取传承人信息
               IchMaster ichMaster = ichMasterService.getIchMasterByWorks(works);
               works.setIchMaster(ichMaster);

               //获取内容片断
               ContentFragment con = new ContentFragment();
               con.setTargetId(works.getId());
               con.setTargetType(2);
               List<ContentFragment> contentFragmentList = contentFragmentMapper.selectByTargetIdAndType(con);
               for (ContentFragment contentFragment : contentFragmentList) {
                   Long attrId = contentFragment.getAttributeId();
                   Attribute attribute = attributeMapper.selectByPrimaryKey(attrId);
                   contentFragment.setAttribute(attribute);//添加属性
                   List<ContentFragmentResource> contentFragmentResourceList = contentFragmentResourceMapper.selectByContentFragmentId(contentFragment.getId());
                   List<Resource> resourceList = new ArrayList<>();
                   for (ContentFragmentResource contentFragmentResource: contentFragmentResourceList) {
                       Resource resource = resourceMapper.selectByPrimaryKey(contentFragmentResource.getResourceId());
                       resourceList.add(resource);
                   }
                   contentFragment.setResourceList(resourceList);
               }
               works.setContentFragmentList(contentFragmentList);
           }
       }catch (Exception e){
            throw new ApplicationException(ApplicationException.INNER_ERROR);
       }

        return worksList;
    }

    /**
     * 添加或更新作品
     * @param works
     * @throws Exception
     */
    @Override
    public void saveWorks(Works works) throws Exception {
        TransactionStatus transactionStatus = getTransactionStatus();
        try{
            if(works.getId() == null){//新增
                Long worksId = IdWorker.getId();
                works .setId(worksId);
                works.setStatus(1);
                works.setLang("chi");
                works.setIsRepresent(1);
                worksMapper.insertSelective(works);
                List<ContentFragment> contentFragmentList = works.getContentFragmentList();
                for (ContentFragment contentFragment:contentFragmentList) {
                    contentFragment.setId(IdWorker.getId());//id
                    contentFragment.setStatus(1);//状态
                    contentFragment.setTargetId(worksId);//作品的id
                    contentFragment.setTargetType(2);//2表示作品
                    contentFragment.setAttributeId(contentFragment.getAttribute().getId());//属性的id 是什么字段
                    contentFragmentMapper.insertSelective(contentFragment);
                    List<Resource> resourceList = contentFragment.getResourceList();
                    for (Resource resource: resourceList) {
                        Long resourceId = IdWorker.getId();
                        resource.setId(resourceId);
                        resource.setStatus(1);
                        //保存resource
                        resourceMapper.insertSelective(resource);
                        ContentFragmentResource cfr = new ContentFragmentResource();
                        cfr.setId(IdWorker.getId());
                        cfr.setContentFragmentId(contentFragment.getId());
                        cfr.setResourceId(resourceId);
                        cfr.setStatus(1);
                        //保存中间表
                        contentFragmentResourceMapper.insertSelective(cfr);
                    }

                }
                String templateName ="";
                String fileName = works.getId().toString();
                String uri = buildHTML("", works, fileName);
                works.setUri(uri);
                worksMapper.updateByPrimaryKeySelective(works);
            }else{
                //更新
                worksMapper.updateByPrimaryKeySelective(works);
            }
            commit(transactionStatus);
        }catch (Exception e){
            rollback(transactionStatus);
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }


    @Override
    public String buildHTML(String templateName, Works works, String fileName) throws Exception {
        String uri = BuildHTMLEngine.buildHTML(templateName, works, fileName);
        return uri;
    }
    /**
     * 根据项目id获取代表作品列表
     * @param ichProjectId
     * @return
     */
    public List<Works> getWorksByIchProjectId(Long ichProjectId){
        Works works = new Works();
        works.setIchProjectId(ichProjectId);
        works.setIsRepresent(1);
        List<Works> getWorkList = worksMapper.selectWorks(works);
        List<Works> worksList = getWorkList(getWorkList);
        return worksList;
    }


    /**
     * 根据传承人id查询代表作品列表
     * @param ichMasterId
     * @return
     */
    public List<Works> getWorksByIchMasterId(Long ichMasterId){
        Works works = new Works();
        works.setIchMasterId(ichMasterId);
        works.setIsRepresent(1);
        List<Works> getWorkList = worksMapper.selectWorks(works);
        List<Works> worksList = getWorkList(getWorkList);
        return worksList;
    }

    private List<Works> getWorkList(List<Works> worksList){
        for (Works works:worksList) {
            //获取传承人信息
            IchMaster ichMaster = ichMasterService.getIchMasterByWorks(works);
            works.setIchMaster(ichMaster);
            ContentFragment con = new ContentFragment();
            con.setTargetId(works.getId());
            con.setTargetType(2);
            List<ContentFragment> contentFragments = contentFragmentMapper.selectByTargetIdAndType(con);
            for (ContentFragment contentFragment :contentFragments) {
                Long attrId = contentFragment.getAttributeId();
                Attribute attribute = attributeMapper.selectByPrimaryKey(attrId);
                contentFragment.setAttribute(attribute);//添加属性
                List<ContentFragmentResource> contentFragmentResourceList = contentFragmentResourceMapper.selectByContentFragmentId(contentFragment.getId());
                List<Resource> resourceList = new ArrayList<>();
                for (ContentFragmentResource contentFragmentResource: contentFragmentResourceList) {
                    Resource resource = resourceMapper.selectByPrimaryKey(contentFragmentResource.getResourceId());
                    resourceList.add(resource);
                }
                contentFragment.setResourceList(resourceList);
            }
            works.setContentFragmentList(contentFragments);
        }
        return worksList;
    }
}
