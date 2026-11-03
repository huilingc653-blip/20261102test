package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 通用类型字典表
 */
@Entity
@Table(name = "SYS_DICT")
public class Dict extends IdEntity implements Serializable {
    private DictGroup dictGroup;//类型分组
    private Dict dict;//父类型
    private String name;//类型名称
    private String code;//类型编码
    private Date createDate;//创建时间
    private String createName;//创建用户
    private Integer sort;
    private List<Dict> dicts = new ArrayList();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_GROUP_ID")
    public DictGroup getDictGroup() {
        return this.dictGroup;
    }
    public void setDictGroup(DictGroup dictGroup) {
        this.dictGroup = dictGroup;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID")
    public Dict getDict() {
        return this.dict;
    }
    public void setDict(Dict dict) {
        this.dict = dict;
    }
    @Column(name = "TYPE_NAME", length = 50)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "TYPE_CODE", length = 50)
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "CREATE_NAME", length = 36)
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dicts")
    public List<Dict> getDicts() {
        return this.dicts;
    }
    public void setDicts(List<Dict> dicts) {
        this.dicts = dicts;
    }
}
