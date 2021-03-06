package com.diich.core.model;

import com.diich.core.base.BaseModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class IchProject extends IchObject {
    private Long id;

    private Long ichCategoryId;

    private Long creatorId;//创建人id

    private Long lastEditorId;

    private Date lastEditDate;

    private Integer editRank;

    private Integer type;

    private Integer status;

    private String lang;//语言

    private String uri;//项目静态页面的存储路径

    private  IchCategory ichCategory;//项目分类

    private Version version;//版本 中文版和英文版的中间表

    private List<IchMaster> ichMasterList;//传承人列表

    private List<Works> worksList;//代表作品列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIchCategoryId() {
        return ichCategoryId;
    }

    public void setIchCategoryId(Long ichCategoryId) {
        this.ichCategoryId = ichCategoryId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getLastEditorId() {
        return lastEditorId;
    }

    public void setLastEditorId(Long lastEditorId) {
        this.lastEditorId = lastEditorId;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Integer getEditRank() {
        return editRank;
    }

    public void setEditRank(Integer editRank) {
        this.editRank = editRank;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public IchCategory getIchCategory() {
        return ichCategory;
    }

    public void setIchCategory(IchCategory ichCategory) {
        this.ichCategory = ichCategory;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<IchMaster> getIchMasterList() {
        return ichMasterList;
    }

    public void setIchMasterList(List<IchMaster> ichMasterList) {
        this.ichMasterList = ichMasterList;
    }

    public List<Works> getWorksList() {
        return worksList;
    }

    public void setWorksList(List<Works> worksList) {
        this.worksList = worksList;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

}