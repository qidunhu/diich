package com.diich.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.diich.core.base.BaseService;
import com.diich.core.exception.ApplicationException;
import com.diich.core.model.*;
import com.diich.core.service.*;
import com.diich.core.util.SimpleUpload;
import com.diich.core.util.BuildHTMLEngine;
import com.diich.core.util.PropertiesUtil;
import com.diich.mapper.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Administrator on 2017/5/9.
 */
@Service("ichMasterService")
@Transactional
@SuppressWarnings("all")
public class IchMasterServiceImpl extends BaseService<IchMaster> implements IchMasterService {

    @Autowired
    private IchMasterMapper ichMasterMapper;
    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private ContentFragmentMapper contentFragmentMapper;
    @Autowired
    private ContentFragmentResourceMapper contentFragmentResourceMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private IchProjectService ichProjectService;
    @Autowired
    private WorksService worksService;
    @Autowired
    private WorksMapper worksMapper;
    @Autowired
    private ContentFragmentService contentFragmentService;
    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuditMapper auditMapper;

    /**
     * 根据id查询传承人
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public IchMaster getIchMaster(String id) throws Exception {

        IchMaster ichMaster = null;
        try {
            ichMaster = ichMasterMapper.selectByPrimaryKey(Long.parseLong(id));
            if (ichMaster != null) {
                ichMaster = getIchMaster(ichMaster);
            }
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }

        return ichMaster;
    }

    private IchMaster getIchMaster(IchMaster ichMaster) throws Exception {
        //所属项目
        IchProject ichProject = ichProjectService.getIchProjectById(ichMaster.getIchProjectId());
        ichMaster.setIchProject(ichProject);
        //作品列表
        List<Works> worksList = worksService.getWorksByIchMasterId(ichMaster.getId());
        ichMaster.setWorksList(worksList);
        //内容片断列表
        List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);
        ichMaster.setContentFragmentList(contentFragmentList);
        return ichMaster;
    }

    /**
     * 根据条件查询分页列表
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public Page<IchMaster> getIchMasterPage(Map<String, Object> params) throws Exception {
        Integer current = 1;
        Integer pageSize = 10;

        if (params != null && params.containsKey("current") && params.containsKey("pageSize")) {
            current = (Integer) params.get("current");
            pageSize = (Integer) params.get("pageSize");
        }

        Page<IchMaster> page = new Page<IchMaster>(current, pageSize);
        page.setCondition(params);

        List<IchMaster> ichMasterList = getIchMasterList(page);

        page.setRecords(ichMasterList);

        return page;
    }

    /**
     * 获取传承人列表
     *
     * @param page
     * @return
     */
    @Override
    public List<IchMaster> getIchMasterList(Page<IchMaster> page) throws ApplicationException {

        List<IchMaster> ichMasterList = null;
        try {
            Map<String, Object> condition = page.getCondition();
            if (condition == null) {
                condition = new HashMap<>();
            }
            ichMasterList = ichMasterMapper.selectIchMasterList(page, condition);
            for (IchMaster ichMaster : ichMasterList) {
                //所属项目
                IchProject ichProject = ichProjectService.getIchProjectById(ichMaster.getIchProjectId());
                ichMaster.setIchProject(ichProject);
                //作品列表
                List<Works> worksList = worksService.getWorksByIchMasterId(ichMaster.getId());
                ichMaster.setWorksList(worksList);
                //内容片断列表
                List<ContentFragment> contentFragmentList = getContentFragmentListByMasterId(ichMaster);
                ichMaster.setContentFragmentList(contentFragmentList);
            }
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return ichMasterList;
    }

    /**
     * 添加或更新传承人
     *
     * @param ichMaster
     * @return
     */
    @Override
    public IchMaster saveIchMaster(IchMaster ichMaster, User user) throws Exception {
        TransactionStatus transactionStatus = getTransactionStatus();
        try {
            if (ichMaster.getStatus() != null && ichMaster.getStatus() == 3) {
                checkIchMaster(ichMaster);//校验传承人信息
                if (user != null && user.getType() == 0) {//如果当前修改者是admin type代表权限  0 代表admin  1代表普通用户
                    ichMaster.setStatus(0);
                }
            }
            ichMaster.setLastEditDate(new Date());
            saveMaster(ichMaster, user);
            commit(transactionStatus);
        } catch (Exception e) {
            rollback(transactionStatus);
            if (e instanceof ApplicationException) {
                ApplicationException ae = (ApplicationException) e;
                if (ae.getCode() == 2) {
                    throw new ApplicationException(ApplicationException.PARAM_ERROR, ae.getDetailMsg());
                }
            }
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return ichMaster;
    }

    private IchMaster saveMaster(IchMaster ichMaster, User user) throws Exception {
        if (StringUtils.isEmpty(ichMaster.getLang())) {
            ichMaster.setLang("chi");
        }
        if (user.getType() != null && user.getType() == 2) {//0管理员账户 1普通用户 2传承人用户  3 机构用户
            ichMaster.setUserId(ichMaster.getLastEditorId());
        }

        if (ichMaster.getStatus() == null || ichMaster.getStatus() != 3) {
            ichMaster.setStatus(2);
        }
        if (user != null && user.getType() != null && user.getType() == 0) {
            ichMaster.setStatus(0);
        }
        if (ichMaster.getId() == null) {
            long id = IdWorker.getId();
            ichMaster.setId(id);
            ichMaster.setUri(id + ".html");
            ichMasterMapper.insertSelective(ichMaster);
        } else {
            ichMasterMapper.updateByPrimaryKeySelective(ichMaster);
        }
        List<ContentFragment> contentFragmentList = ichMaster.getContentFragmentList();
        if (contentFragmentList != null && contentFragmentList.size() > 0) {
            for (ContentFragment contentFragment : contentFragmentList) {
                //添加内容片断
                contentFragment.setTargetId(ichMaster.getId());
                contentFragment.setTargetType(1);
                contentFragmentService.saveContentFragment(contentFragment);
            }
        }
        if (user != null && user.getType() != null && user.getType() == 0) {//管理员权限
            ichMaster = getAttribute(ichMaster);
            List<ContentFragment> contentFragments = getContentFragmentByMasterId(ichMaster);
            if (contentFragments != null && contentFragments.size() > 0) {
                ichMaster.setContentFragmentList(contentFragments);
            }
            if (ichMaster != null && ichMaster.getIchProjectId() != null) {
                IchProject ichProject = ichProjectService.getIchProject(String.valueOf(ichMaster.getIchProjectId()));
                if (ichProject != null) {
                    ichMaster.setIchProject(ichProject);
                    buildAndUploadProject(ichProject);
                }
            }
            buildAndUpload(ichMaster);
        }
        return ichMaster;
    }

    /**
     * 生成传承人页面
     *
     * @param ichMaster
     * @throws Exception
     */
    private void buildAndUpload(IchMaster ichMaster) throws Exception {
        String str = PropertiesUtil.getString("freemarker.masterfilepath");
        String fileName = str + "/" + ichMaster.getId().toString() + ".html";
        buildHTML("2.0master.ftl", ichMaster, fileName);//生成静态页面
        String h5outPutPath = PropertiesUtil.getString("freemarker.h5_masterfilepath") + "/" + ichMaster.getId().toString() + ".html";
        buildHTML("h5_master.ftl", ichMaster, h5outPutPath);
        String bucketName = PropertiesUtil.getString("img_bucketName");
        String type = PropertiesUtil.getString("pc_mhtml_server");
        File file = new File(fileName);
        SimpleUpload.uploadFile(new FileInputStream(file), bucketName, type + "/" + ichMaster.getId() + ".html", file.length());//上传到阿里云
        String h5type = PropertiesUtil.getString("m_mhtml_server");
        File h5file = new File(h5outPutPath);
        SimpleUpload.uploadFile(new FileInputStream(h5file), bucketName, h5type + "/" + ichMaster.getId() + ".html", h5file.length());//上传到阿里云
    }

    /**
     * 生成项目详情页
     *
     * @param ichProject
     * @throws Exception
     */
    private void buildAndUploadProject(IchProject ichProject) throws Exception {
        String str = PropertiesUtil.getString("freemarker.projectfilepath");
        String fileName = str + "/" + ichProject.getId().toString() + ".html";
        ichProjectService.buildHTML("2.0pro.ftl", ichProject, fileName);//生成静态页面
        String h5outPutPath = PropertiesUtil.getString("freemarker.h5_projectfilepath") + "/" + ichProject.getId() + ".html";
        ichProjectService.buildHTML("h5_pro.ftl", ichProject, h5outPutPath);
        String bucketName = PropertiesUtil.getString("img_bucketName");
        String type = PropertiesUtil.getString("pc_phtml_server");
        File file = new File(fileName);
        SimpleUpload.uploadFile(new FileInputStream(file), bucketName, type + "/" + ichProject.getId() + ".html", file.length());//上传到阿里云
        String h5type = PropertiesUtil.getString("m_phtml_server");
        File h5file = new File(h5outPutPath);
        SimpleUpload.uploadFile(new FileInputStream(h5file), bucketName, h5type + "/" + ichProject.getId() + ".html", h5file.length());//上传到阿里云
    }

    private IchMaster getAttribute(IchMaster ichMaster) throws Exception {
        List<ContentFragment> contentFragmentList = ichMaster.getContentFragmentList();
        if (contentFragmentList != null && contentFragmentList.size() > 0) {
            for (ContentFragment contentFragment : contentFragmentList) {
                if (contentFragment.getAttribute() == null && contentFragment.getAttributeId() != null) {
                    Attribute attribute = attributeMapper.selectByPrimaryKey(contentFragment.getAttributeId());
                    contentFragment.setAttribute(attribute);
                }
            }
        }
        return ichMaster;
    }

    /**
     * 如果修改人不同就另存版本
     *
     * @param ichMaster
     * @return
     * @throws Exception
     */
    private IchMaster updateMaster(IchMaster ichMaster) throws Exception {
        Long mainId = ichMaster.getId();
        long branchId = IdWorker.getId();
        ichMaster.setId(branchId);
        ichMaster.setStatus(2);
        ichMaster.setUri(branchId + ".html");
        ichMasterMapper.insertSelective(ichMaster);
        copyContentFragment(ichMaster, branchId);
        //保存version表
        Version version = new Version();
        version.setId(IdWorker.getId());
        version.setStatus(0);
        version.setTargetType(1);
        version.setMainVersionId(mainId);
        version.setBranchVersionId(branchId);
        version.setVersionType(1000);//版本  修改中, 已过期
        versionMapper.insertSelective(version);
        return ichMaster;
    }

    private void copyContentFragment(IchMaster ichMaster, Long branchId) throws Exception {
        List<ContentFragment> contentFragmentList = ichMaster.getContentFragmentList();
        if (contentFragmentList != null && contentFragmentList.size() > 0) {
            for (ContentFragment contentFragment : contentFragmentList) {
                if (contentFragment.getAttribute() != null && contentFragment.getAttribute().getIsOpen() != null && contentFragment.getAttribute().getIsOpen() == 0) {
                    continue;
                }
                contentFragment.setId(null);
                contentFragment.setTargetId(branchId);
                if (contentFragment.getAttributeId() != null) {
                    Attribute attribute = attributeMapper.selectByPrimaryKey(contentFragment.getAttributeId());
                    if (attribute != null && attribute.getTargetType() != null && attribute.getTargetType() == 11) {
                        long id = IdWorker.getId();
                        attribute.setId(id);
                        attribute.setTargetId(branchId);
                        attributeMapper.insertSelective(attribute);
                        contentFragment.setAttribute(attribute);
                        contentFragment.setAttributeId(id);
                    }
                }
                List<Resource> resourceList = contentFragment.getResourceList();
                if (resourceList != null && resourceList.size() > 0) {
                    for (int i = 0; i < resourceList.size(); i++) {
                        Resource resource = resourceList.get(i);
                        resource.setId(null);
                    }
                }
                contentFragmentService.saveContentFragment(contentFragment);
            }
        }
    }

    /**
     * status 不做限制
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public IchMaster getIchMasterById(Long id) throws Exception {
        IchMaster ichMaster = null;
        try {
            ichMaster = ichMasterMapper.selectMasterById(id);
            if (ichMaster != null) {
                //内容片断列表
                List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);
                ichMaster.setContentFragmentList(contentFragmentList);
            }
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return ichMaster;
    }

    /**
     * 预览
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public String preview(Long id) throws Exception {
        try {
            IchMaster ichMaster = getIchMasterById(id);
            Long ichProjectId = ichMaster.getIchProjectId();
            IchProject ichProject = ichProjectService.getIchProjectById(ichProjectId);
            ichMaster.setIchProject(ichProject);
            String str = PropertiesUtil.getString("freemarker.masterfilepath");
            String fileName = str + "/" + ichMaster.getId().toString() + ".html";
            String url = str.substring(str.lastIndexOf("/"));
            String s = buildHTML("preview_master.ftl", ichMaster, fileName);
            String uri = "." + url + "/" + id + ".html";
            return uri;
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }

    /**
     * 生成静态页面
     *
     * @param templateName
     * @param ichMaster
     * @param fileName
     * @return
     * @throws Exception
     */
    @Override
    public String buildHTML(String templateName, IchMaster ichMaster, String fileName) throws Exception {
        try {
            Map map = getJson(ichMaster);
            String uri = BuildHTMLEngine.buildHTML(templateName, ichMaster, map, fileName);
            return uri;
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }

    @Override
    public IchMaster getIchMasterByIdAndUser(Long id, User user) throws Exception {
        if (user.getType() != null && user.getType() == 0) {//是管理员
            return getIchMaster(String.valueOf(id));
        }
        Version version = new Version();
        version.setTargetType(1);
        version.setVersionType(1000);
        version.setMainVersionId(id);
        List<Version> versionList = versionMapper.selectVersionByVersionIdAndTargetType(version);
        if (versionList.size() > 0) {
            List tempList = new ArrayList();
            for (Version ver : versionList) {
                tempList.add(ver.getBranchVersionId());
            }
            List<IchMaster> ichMasterList = ichMasterMapper.selectIchMasterByUserId(user.getId());
            for (IchMaster ichMaster : ichMasterList) {
                if (tempList.contains(ichMaster.getId())) {
                    ichMaster = getIchMaster(ichMaster);//获取传承人其他信息
                    return ichMaster;
                }
            }
        }
        IchMaster ichMaster = ichMasterMapper.selectMasterById(id);
        ichMaster = getIchMaster(ichMaster);//获取传承人其他信息
        if (ichMaster != null && (!ichMaster.getLastEditorId().equals(user.getId())) || (ichMaster.getStatus() != null && ichMaster.getStatus() == 0)) {
            ichMaster.setLastEditorId(user.getId());
            ichMaster.setLastEditDate(new Date());
            ichMaster = updateMaster(ichMaster);
        }
        return ichMaster;
    }

    @Override
    public Page<IchMaster> getIchMasterByUserId(Map<String, Object> params) throws Exception {
        Integer current = 1;
        Integer pageSize = 10;
        if (params != null && params.containsKey("current")) {
            current = (Integer) params.get("current");
        }
        if (params != null && params.containsKey("pageSize")) {
            pageSize = (Integer) params.get("pageSize");
        }
        int offset = (current - 1) * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);
        Page<IchMaster> page = new Page();
        try {
            List<IchMaster> ichMasterList = ichMasterMapper.selectIchMasterByUserAndStatus(params, rowBounds);
            for (IchMaster ichMaster : ichMasterList) {
                List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);
                ichMaster.setContentFragmentList(contentFragmentList);
            }
            page.setRecords(ichMasterList);
            int total = ichMasterMapper.selectIchMasterCountByUserAndStatus(params);
            page.setTotal(total);//查询数量
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return page;
    }

    @Override
    public int deleteIchMaster(long id) throws Exception {
        int i = -1;
        try {
            IchMaster ichMaster = ichMasterMapper.selectMasterById(id);
            ichMaster.setStatus(1);
            ichMaster.setLastEditDate(new Date());
            i = ichMasterMapper.updateByPrimaryKeySelective(ichMaster);
            Version version = new Version();
            version.setBranchVersionId(id);
            version.setTargetType(1);
            version.setVersionType(1000);
            List<Version> versionList = versionMapper.selectVersionByVersionIdAndTargetType(version);
            if (versionList.size() > 0) {
                versionList.get(0).setVersionType(1001);//过期
                versionMapper.updateByPrimaryKeySelective(versionList.get(0));
            }
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return i;
    }

    @Override
    public List<IchMaster> getIchMaster(String masterName, String worksName) throws Exception {
        List<IchMaster> ichMasterList = new ArrayList<>();
        List<Works> wList = null;
        try {
            if (StringUtils.isNotEmpty(masterName)) {
                List<IchMaster> ichMasters = ichMasterMapper.selectMasterByName(masterName);
                for (IchMaster ichMaster : ichMasters) {
                    ichMaster = getIchMaster(String.valueOf(ichMaster.getId()));
                    ichMasterList.add(ichMaster);
                }
            }
            if (StringUtils.isEmpty(masterName) && StringUtils.isNotEmpty(worksName)) {
                wList = worksMapper.selectWorksByName(worksName);
                for (Works works : wList) {
                    if (works.getIchMasterId() == null) {
                        continue;
                    }
                    IchMaster ichMaster = getIchMaster(String.valueOf(works.getIchMasterId()));
                    ichMasterList.add(ichMaster);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return ichMasterList;
    }

    /**
     * 传承人审核
     *
     * @param id
     * @param user
     * @param doi
     */
    @Override
    public void audit(Long id, User user, String doi) throws Exception{
        TransactionStatus transactionStatus = getTransactionStatus();
        try {
            IchMaster ichMaster = ichMasterMapper.selectMasterById(id);
            if (ichMaster == null) {
                throw new ApplicationException(ApplicationException.PARAM_ERROR, "该id对应的传承人不存在");
            }
            if (ichMaster != null && ichMaster.getStatus() != null && ichMaster.getStatus() != 3) {
                throw new ApplicationException(ApplicationException.PARAM_ERROR, "传承人不是待审核状态");
            }
            //根据id查询版本
            Version version = new Version();
            version.setBranchVersionId(id);
            version.setTargetType(1);
            version.setVersionType(1000);
            List<Version> versionList = versionMapper.selectVersionByVersionIdAndTargetType(version);
            //查询是否为词条认领
            Version entver = new Version();
            entver.setBranchVersionId(id);
            entver.setTargetType(1);
            entver.setVersionType(1002);
            List<Version> verList = versionMapper.selectVersionByVersionIdAndTargetType(entver);
            //判断是否有其他版本
            if (versionList.size() > 0) {//非管理员修改的项目
                Version ver = versionList.get(0);
                Long mainVersionId = ver.getMainVersionId();
                ichMaster.setStatus(0);
                List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);
                ichMaster.setContentFragmentList(contentFragmentList);
                ichMaster.setLastEditDate(new Date());
                IchMaster master = ichMasterMapper.selectByPrimaryKey(mainVersionId);
                List<ContentFragment> contentFragments = getContentFragmentByMasterId(master);
                for (ContentFragment contentFragment : contentFragmentList) {//交换主版本和分支版本内容
                    if (contentFragment.getAttributeId() == 11 && StringUtils.isNotEmpty(doi)) {
                        contentFragment.setContent(doi);
                    }
                    contentFragment.setTargetId(mainVersionId);
                    contentFragmentMapper.updateByPrimaryKeySelective(contentFragment);
                }
                master.setStatus(5);//作废状态
                master.setLastEditDate(new Date());
                for (ContentFragment contentFragment : contentFragments) {
                    contentFragment.setTargetId(id);
                    contentFragmentMapper.updateByPrimaryKeySelective(contentFragment);
                }
                ichMaster.setId(mainVersionId);
                ichMaster.setLastEditorId(master.getLastEditorId());
                ichMaster.setUri(master.getUri());
                master.setId(id);
                master.setLastEditorId(ichMaster.getLastEditorId());
                master.setUri(ichMaster.getUri());
                ver.setVersionType(1001);//已过期
                versionMapper.updateByPrimaryKeySelective(versionList.get(0));
                ichMasterMapper.updateByPrimaryKeySelective(ichMaster);
                ichMasterMapper.updateByPrimaryKeySelective(master);
                //修改审核表信息
                updateAudit(id, ichMaster.getId(), user);
                if (ichMaster.getIchProjectId() != null) {
                    IchProject ichProject = ichProjectService.getIchProject(String.valueOf(ichMaster.getIchProjectId()));
                    if (ichProject != null) {
                        ichMaster.setIchProject(ichProject);
                        buildAndUploadProject(ichProject);//生成项目详情页并上传
                    }
                }
                //生成静态页并上传
                buildAndUpload(ichMaster);
            } else if (verList.size() > 0) {
                //审核词条认领
                IchMaster master = auditEntry(ichMaster, user, verList, doi);
                if (master.getIchProjectId() != null) {
                    IchProject ichProject = ichProjectService.getIchProjectById(master.getIchProjectId());
                    if (ichProject != null) {
                        master.setIchProject(ichProject);
                    }
                }
                //生成静态页并上传
                buildAndUpload(master);
            } else {//新增待审核的机构
                //校验doi都编码是否重复
                if (isDoiValable(doi)) {
                    throw new ApplicationException(ApplicationException.PARAM_ERROR, "doi编码重复");
                }
                ichMaster.setStatus(0);
                ichMaster.setLastEditDate(new Date());
                ichMasterMapper.updateByPrimaryKeySelective(ichMaster);
                ContentFragment contentFragment = new ContentFragment();
                contentFragment.setContent(doi);
                contentFragment.setId(IdWorker.getId());
                contentFragment.setTargetType(1);
                contentFragment.setTargetId(id);
                contentFragment.setStatus(0);
                contentFragment.setAttributeId(11L);
                contentFragmentMapper.insertSelective(contentFragment);
                saveAudit(id, user);//保存到审核表
                //获取项目其他信息用以生成静态页面
                List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);
                ichMaster.setContentFragmentList(contentFragmentList);
                if (ichMaster.getIchProjectId() != null) {
                    IchProject ichProject = ichProjectService.getIchProject(String.valueOf(ichMaster.getIchProjectId()));
                    if (ichProject != null) {
                        ichMaster.setIchProject(ichProject);
                        buildAndUploadProject(ichProject);//生成项目详情页并上传
                    }
                }
                //生成静态页并上传
                buildAndUpload(ichMaster);
            }
            commit(transactionStatus);
        } catch (Exception e) {
            rollback(transactionStatus);
            if (e instanceof ApplicationException) {
                ApplicationException ae = (ApplicationException) e;
                if (ae.getCode() == 2) {
                    throw new ApplicationException(ApplicationException.PARAM_ERROR, ae.getDetailMsg());
                }
            }
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }

    private boolean isDoiValable(String doi) throws Exception {
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setAttributeId(11L);
        contentFragment.setContent(doi);
        List<ContentFragment> contentFragments = contentFragmentMapper.selectByAttIdAndContent(contentFragment);
        if (contentFragments.size() > 0) {
            return true;
        }
        return false;
    }

    private IchMaster auditEntry(IchMaster ichMaster, User user, List<Version> verList, String doi) throws Exception {
        try{
            Version version = verList.get(0);
            Long mainVersionId = version.getMainVersionId();
            ichMaster.setStatus(0);
            List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);//认领人信息
            ichMaster.setLastEditDate(new Date());
            IchMaster master = ichMasterMapper.selectByPrimaryKey(mainVersionId);//主版本内容
            master.setUserId(user.getId());//认领人id
            List<ContentFragment> contentFragments = getContentFragmentByMasterId(master);
            //对外循环做个标记
            List<ContentFragment> newList = new ArrayList<>();
            Loop:
            for (ContentFragment content : contentFragments) {
                for (ContentFragment contentFragment : contentFragmentList) {
                    if (content.getAttributeId() != null && contentFragment.getAttributeId() != null && content.getAttributeId().equals(contentFragment.getAttributeId())) {//相同属性
                        continue Loop;
                    }
                }

                //不是同一属性 当前传承人新加一条内容
                ContentFragment contentFragment = getContentFragment(content, ichMaster.getId());
                newList.add(contentFragment);
            }
            if (newList.size() > 0) {//将新加的contentFragment添加到原contentFragment列表
                contentFragmentList.addAll(newList);
            }
            //交换主副版本内容
            for (ContentFragment contentFragment : contentFragmentList) {
                if (contentFragment.getAttributeId() == 11 && StringUtils.isNotEmpty(doi)) {
                    contentFragment.setContent(doi);
                }
                contentFragment.setTargetId(mainVersionId);
                contentFragmentMapper.updateByPrimaryKeySelective(contentFragment);
            }
            master.setStatus(5);//作废状态
            master.setLastEditDate(new Date());
            for (ContentFragment contentFragment : contentFragments) {
                contentFragment.setTargetId(ichMaster.getId());
                contentFragmentMapper.updateByPrimaryKeySelective(contentFragment);
            }
            ichMaster.setId(mainVersionId);
            ichMaster.setLastEditorId(master.getLastEditorId());
            ichMaster.setUri(master.getUri());
            master.setId(version.getBranchVersionId());
            master.setLastEditorId(ichMaster.getLastEditorId());
            master.setUri(ichMaster.getUri());

            ichMasterMapper.updateByPrimaryKeySelective(ichMaster);//更新项目表
            ichMasterMapper.updateByPrimaryKeySelective(master);//更新主版本项目的userId
            version.setVersionType(1003);//认领结束
            versionMapper.updateByPrimaryKeySelective(version);//更新版本表
            updateAudit(ichMaster.getId(), mainVersionId, user);//更新审核表
            contentFragments = getContentFragmentByMasterId(master);
            master.setContentFragmentList(contentFragments);
            return master;
        }catch (Exception e){
            e.printStackTrace();
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }

    }

    private ContentFragment getContentFragment(ContentFragment content, Long targetId) throws Exception {
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setId(null);
        contentFragment.setTargetId(targetId);
        contentFragment.setContent(content.getContent());
        contentFragment.setAttributeId(content.getAttributeId());
        contentFragment.setTargetType(1);
        Attribute attribute = content.getAttribute();
        contentFragment.setAttribute(attribute);
        if (attribute != null && attribute.getTargetType() != null && attribute.getTargetType() == 11) {
            long id = IdWorker.getId();
            Attribute attr = new Attribute();
            attr.setId(id);
            attr.setTargetId(targetId);
            attr.setTargetType(11);
            attr.setCnName(attribute.getCnName());
            attr.setSeq(999);
            attr.setStatus(0);
            attr.setDataType(attribute.getDataType());
            attributeMapper.insertSelective(attribute);
            contentFragment.setAttribute(attribute);
            contentFragment.setAttributeId(id);
        }
        List<Resource> resourceList = content.getResourceList();
        if (resourceList != null && resourceList.size() > 0) {
            List<Resource> resList = new ArrayList<>();
            for (int i = 0; i < resourceList.size(); i++) {
                Resource resource = resourceList.get(i);
                Resource res = new Resource();
                res.setUri(resource.getUri());
                res.setStatus(0);
                res.setDescription(resource.getDescription());
                res.setType(resource.getType());
                resList.add(res);
            }
            contentFragment.setResourceList(resList);
        }
        contentFragmentService.saveContentFragment(contentFragment);

        return contentFragment;

    }

    private void updateAudit(Long id, Long targetId, User user) throws Exception {
        try {
            //添加信息到审核表
            Audit audit = new Audit();
            audit.setTargetType(1);
            audit.setTargetId(id);
            audit.setStatus(1);
            Audit selaudit = auditMapper.selectAuditBytargetIdAndTargetType(audit);
            if (selaudit != null) {
                audit.setStatus(0);
                selaudit.setReason(null);
                selaudit.setAuditUserId(user.getId());
                selaudit.setAuditDate(new Date());
                selaudit.setTargetId(targetId);
                auditMapper.updateByPrimaryKeySelective(selaudit);
            } else {
                audit.setId(IdWorker.getId());
                audit.setStatus(0);
                audit.setAuditDate(new Date());
                audit.setAuditUserId(user.getId());
                audit.setTargetId(targetId);
                auditMapper.insertSelective(audit);
            }
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }

    private void saveAudit(Long id, User user) throws Exception {
        try {
            //添加拒绝信息到审核表
            Audit audit = new Audit();
            audit.setTargetType(1);
            audit.setTargetId(id);
            audit.setId(IdWorker.getId());
            audit.setAuditDate(new Date());
            audit.setAuditUserId(user.getId());
            audit.setStatus(0);//已通过
            auditMapper.insertSelective(audit);
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }

    /**
     * 传承人拒绝审核
     *
     * @param id
     * @param user
     * @param reason
     * @throws Exception
     */
    @Override
    public void refuseAudit(Long id, User user, String reason) throws Exception {
        TransactionStatus transactionStatus = getTransactionStatus();
        try {
            IchMaster ichMaster = new IchMaster();
            ichMaster.setId(id);
            ichMaster.setStatus(4);
            ichMasterMapper.updateByPrimaryKeySelective(ichMaster);//更新项目状态为已拒绝
            //添加拒绝信息到审核表
            Audit audit = new Audit();
            audit.setTargetType(1);
            audit.setTargetId(id);
            audit.setStatus(1);
            Audit selaudit = auditMapper.selectAuditBytargetIdAndTargetType(audit);
            if (selaudit != null) {
                selaudit.setReason(reason);
                selaudit.setAuditUserId(user.getId());
                selaudit.setAuditDate(new Date());
                auditMapper.updateByPrimaryKeySelective(selaudit);
            } else {
                audit.setId(IdWorker.getId());
                audit.setAuditDate(new Date());
                audit.setAuditUserId(user.getId());
                audit.setReason(reason);
                auditMapper.insertSelective(audit);
            }
            commit(transactionStatus);
        } catch (Exception e) {
            rollback(transactionStatus);
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
    }

    /**
     * 根据项目id获取传承人列表
     *
     * @param ichProjectId
     * @return
     */
    public List<IchMaster> getIchMasterByIchProjectId(Long ichProjectId) throws Exception {
        List<IchMaster> ichMasterList = ichMasterMapper.selectByIchProjectId(ichProjectId);
        for (IchMaster ichMaster : ichMasterList) {
            List<ContentFragment> contentFragmentList = getContentFragmentListByMasterId(ichMaster);
            ichMaster.setContentFragmentList(contentFragmentList);
        }
        return ichMasterList;
    }

    /**
     * 根据作品获取传承人信息
     *
     * @param works
     * @return
     */
    public IchMaster getIchMasterByWorks(Works works) throws Exception {
        //所属传承人
        IchMaster ichMaster = ichMasterMapper.selectByPrimaryKey(works.getIchMasterId());
        if (ichMaster != null) {
            //内容片断列表
            List<ContentFragment> contentFragmentList = getContentFragmentListByMasterId(ichMaster);
            ichMaster.setContentFragmentList(contentFragmentList);
        }
        return ichMaster;
    }

    /**
     * 查出来的信息是用户公开的信息
     *
     * @param ichMaster
     * @return
     * @throws Exception
     */
    private List<ContentFragment> getContentFragmentListByMasterId(IchMaster ichMaster) throws Exception {
        //内容片断列表
        ContentFragment con = new ContentFragment();
        con.setTargetId(ichMaster.getId());
        con.setTargetType(1);
        List<ContentFragment> contentFragmentList = contentFragmentMapper.selectByTargetIdAndType(con);
        contentFragmentList = getContentFragmentList(contentFragmentList);
        return contentFragmentList;
    }

    /**
     * 查出来的信息是用户所有的信息
     *
     * @param ichMaster
     * @return
     * @throws Exception
     */
    private List<ContentFragment> getContentFragmentByMasterId(IchMaster ichMaster) throws Exception {
        //内容片断列表
        ContentFragment con = new ContentFragment();
        con.setTargetId(ichMaster.getId());
        con.setTargetType(1);
        List<ContentFragment> contentFragmentList = contentFragmentMapper.selectAllAttrByTargetIdAndType(con);
        contentFragmentList = getContentFragmentList(contentFragmentList);
        return contentFragmentList;
    }

    private List<ContentFragment> getContentFragmentList(List<ContentFragment> contentFragmentList) throws Exception {
        List attrlist = new ArrayList();
        for (int i = 0; i < contentFragmentList.size(); i++) {
            attrlist.add(contentFragmentList.get(i).getAttributeId());
        }
        if (attrlist.size() > 0) {
            List<Attribute> attributeList = attributeMapper.selectAttrListByIds(attrlist);//查询属性列表
            List cfrList = new ArrayList<>();
            for (ContentFragment contentFragment : contentFragmentList) {
                for (Attribute attribute : attributeList) {
                    if (contentFragment.getAttributeId() != null && contentFragment.getAttributeId().equals(attribute.getId())) {
                        contentFragment.setAttribute(attribute);
                        if ((attribute.getDataType() >= 5 && attribute.getDataType() < 100)) {//包含资源文件
                            cfrList.add(contentFragment.getId());
                        }
                        break;
                    }
                }
            }
            if (cfrList.size() > 0) {
                List<ContentFragmentResource> contentFragmentResourceList = contentFragmentResourceMapper.selectByContentFragmentIds(cfrList);//查询图片资源
                List reslist = new ArrayList();
                for (ContentFragmentResource contentFragmentResource : contentFragmentResourceList) {
                    if (contentFragmentResource.getResourceId() != null) {
                        reslist.add(contentFragmentResource.getResourceId());
                    }
                }
                if (reslist.size() > 0) {
                    List<Resource> resourceList = resourceMapper.selectByids(reslist);
                    for (ContentFragment contentFragment : contentFragmentList) {
                        List<Resource> conResList = new ArrayList<>();
                        for (ContentFragmentResource contentFragmentResource : contentFragmentResourceList) {
                            for (Resource resource : resourceList) {
                                if (contentFragmentResource.getContentFragmentId() != null && contentFragmentResource.getResourceId() != null && contentFragment.getId().equals(contentFragmentResource.getContentFragmentId()) && resource.getId().equals(contentFragmentResource.getResourceId())) {
                                    conResList.add(resource);
                                }
                            }
                        }
                        contentFragment.setResourceList(conResList);
                    }
                }
            }
        }


        return contentFragmentList;
    }

    /**
     * 获取前端所需要的资源数据
     *
     * @param ichMaster
     * @return
     */
    private Map getJson(IchMaster ichMaster) throws Exception {
        //list用于向前端传输按模块划分的图片资源   用于显示在详情页特定模块的资源
        List<Map<String, Object>> list = new ArrayList<>();
        //allMap 所有去除重复图片和视频后的资源容器  用于显示在详情页得查看所有图片
        Map<String, Object> allMap = new HashedMap();
        Map<String, Object> headMap = new HashedMap();           //放公共数据
        Set<Resource> imgdist = new HashSet<>();                //去重后的所有图片集合
        Set<Resource> videosdist = new HashSet<>();              //去重后的所有视频集合
        List<ContentFragment> ContentFragmentList = ichMaster.getContentFragmentList();
        for (ContentFragment contentFragment : ContentFragmentList) {
            Map<String, Object> map = new HashMap<>();          //存放每个模块的图片和视频
            List<Resource> img = new ArrayList<>();             //图片资源文件的集合
            List<Resource> video = new ArrayList<>();           //视频资源文件的集合
            Long contentFragmentId = contentFragment.getId();
            List<Resource> resourceList = contentFragment.getResourceList();
            if (resourceList != null && resourceList.size() > 0) {
                for (Resource resource : resourceList) {
                    if (resource.getType() != null && resource.getType() == 0) {
                        img.add(resource);
                        if (contentFragment.getAttributeId() != 113) {//头图不放到所有图片中
                            imgdist.addAll(img);
                        } else {
                            headMap.put("headImage", img);
                        }
                    }
                    if (resource.getType() != null && resource.getType() == 1) {
                        video.add(resource);
                        videosdist.addAll(video);
                    }
                }
            }
            map.put("contentFragmentId", String.valueOf(contentFragmentId));
            map.put("imgs", img);
            map.put("videos", video);
            if ("chi".equals(ichMaster.getLang())) {
                if (contentFragment.getAttributeId() == 13) {
                    headMap.put("masterName", contentFragment.getContent());
                }
            }
            if ("eng".equals(ichMaster.getLang())) {
                if (contentFragment.getAttributeId() == 14) {
                    headMap.put("masterName", contentFragment.getContent());
                }
            }

            list.add(map);
        }
        allMap.put("imgs", imgdist);
        allMap.put("videos", videosdist);
        headMap.put("lang", ichMaster.getLang());
        Map map = new HashMap();
        map.put("json", JSONObject.toJSON(list).toString());
        map.put("jsonAll", JSONObject.toJSON(allMap).toString());
        map.put("jsonHead", JSONObject.toJSON(headMap).toString());
        return map;
    }

    /**
     * 校验字段
     *
     * @param ichMaster
     * @throws Exception
     */
    private void checkIchMaster(IchMaster ichMaster) throws Exception {
        List<ContentFragment> contentFragmentList = ichMaster.getContentFragmentList();
        if (ichMaster.getStatus() == 3) {//提交
            List<Attribute> attributeList = null;
            try {
                //根据targetType获取属性列表
                Attribute attribute = new Attribute();
                attribute.setTargetType(1);
                attributeList = attributeMapper.selectAttrListByCatIdAndTarType(attribute);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApplicationException(ApplicationException.INNER_ERROR);
            }

            for (Attribute attr : attributeList) {
                checkSubmitField(attr, contentFragmentList);
            }
        }
    }

    /**
     * 提交时对字段校验 传承人
     *
     * @param attribute
     * @param contentFragmentList
     * @throws Exception
     */
    @SuppressWarnings("all")
    private void checkSubmitField(Attribute attribute, List<ContentFragment> contentFragmentList) throws Exception {

        int count = 0;
        for (ContentFragment contentFragment : contentFragmentList) {
            if (contentFragment.getAttributeId() == 0 || contentFragment.getAttributeId() == null) {
                continue;
            }
            if (attribute.getMaxLength() != null && (attribute.getId() == contentFragment.getAttributeId())) {
                if (attribute.getDataType() >= 100 && contentFragment.getContent() != null) {
                    String[] arr = contentFragment.getContent().split(",");
                    if (arr.length > attribute.getMaxLength()) {
                        throw new ApplicationException(ApplicationException.PARAM_ERROR, attribute.getCnName().toString() + " 字段不符合要求");
                    }
                }
                if (attribute.getDataType() < 100 && contentFragment.getContent() != null && contentFragment.getContent().trim().length() > attribute.getMaxLength()) {
                    throw new ApplicationException(ApplicationException.PARAM_ERROR, attribute.getCnName().toString() + " 字段不符合要求");
                }
            }
            if ((attribute.getMinLength() != null) && (attribute.getMinLength() > 0)) {//检查必填项是否已填
                if (contentFragment.getAttributeId() != attribute.getId()) {
                    continue;
                }
                String content = contentFragment.getContent();
                if (attribute.getDataType() != 7 && attribute.getDataType() != 8 && attribute.getDataType() != 9 && (content == null || (content.trim().length() < attribute.getMinLength()))) {
                    throw new ApplicationException(ApplicationException.PARAM_ERROR, attribute.getCnName().toString() + " 字段不符合要求");
                }
                if (attribute.getDataType() == 7 || attribute.getDataType() == 8 || attribute.getDataType() == 9) {
                    List<Resource> resourceList = contentFragment.getResourceList();
                    if (resourceList != null && resourceList.size() < attribute.getMinLength()) {
                        throw new ApplicationException(ApplicationException.PARAM_ERROR, attribute.getCnName().toString() + " 字段不符合要求");
                    }
                }
                count++;
            }
            if ((attribute.getMinLength() != null) && (count == 0)) {
                throw new ApplicationException(ApplicationException.PARAM_ERROR, attribute.getCnName().toString() + " 字段不符合要求");
            }

        }
    }

    public void claimEntry(Long masterId, IchMaster authInfo, User user) throws Exception {
        if (isClaimed(masterId)) {
            throw new ApplicationException(ApplicationException.INNER_ERROR, "该词条已被认领");
        }

        TransactionStatus transactionStatus = getTransactionStatus();

        authInfo.setUserId(user.getId());
        authInfo.setStatus(3);//待审核

        Version version = new Version();
        version.setId(IdWorker.getId());
        version.setTargetType(1);
        version.setMainVersionId(masterId);
        version.setVersionType(1002);
        version.setStatus(0);

        try {
            authInfo = saveMaster(authInfo, user);

            version.setBranchVersionId(authInfo.getId());
            versionMapper.insertSelective(version);

            commit(transactionStatus);
        } catch (Exception e) {
            rollback(transactionStatus);
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }

    }

    @Override
    public Page<IchMaster> getEntryByUserId(Map<String, Object> params) throws Exception {
        Integer current = 1;
        Integer pageSize = 10;
        if (params != null && params.containsKey("current")) {
            current = (Integer) params.get("current");
        }
        if (params != null && params.containsKey("pageSize")) {
            pageSize = (Integer) params.get("pageSize");
        }
        int offset = (current - 1) * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);
        Page<IchMaster> page = new Page();
        try {
            List<IchMaster> ichMasterList = ichMasterMapper.selectEntryByUserAndStatus(params, rowBounds);
            for (IchMaster ichMaster : ichMasterList) {
                List<ContentFragment> contentFragmentList = getContentFragmentByMasterId(ichMaster);
                ichMaster.setContentFragmentList(contentFragmentList);
            }
            page.setRecords(ichMasterList);
            int total = ichMasterMapper.selectEntryCountByUserAndStatus(params);
            page.setTotal(total);//查询数量
        } catch (Exception e) {
            throw new ApplicationException(ApplicationException.INNER_ERROR);
        }
        return page;
    }

    public boolean isClaimed(Long masterId) throws Exception {
        IchMaster master = ichMasterMapper.selectByPrimaryKey(masterId);
        if (master.getId() != null && master.getUserId() != null) {
            return true;
        }

        return false;
    }
}
