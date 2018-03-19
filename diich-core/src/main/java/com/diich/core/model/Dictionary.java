package com.diich.core.model;

import com.diich.core.base.BaseModel;

import java.util.List;

public class Dictionary extends BaseModel {
    private Long id;

    private String code;

    private String name;

    private Integer type;

    private Long parentId;

    private Integer status;

    private String lang;

    private List<Dictionary> children;


    private int childCount;

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Dictionary> getChildren() {
        return children;
    }

    public void setChildren(List<Dictionary> children) {
        this.children = children;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}