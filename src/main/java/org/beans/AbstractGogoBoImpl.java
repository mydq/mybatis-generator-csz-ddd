package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 13:55
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGogoBoImpl<D extends GogoDo, K extends GogoMapper<D, E>, E extends GogoExample> {
    protected K mapper;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String SOURCE_TYPE_INSERT = "insert";
    protected static final String SOURCE_TYPE_UPDATE = "update";
    @Autowired
    @Qualifier("gogoPvgInfo")
    protected PrivilegeInfo pvgInfo;

    public AbstractGogoBoImpl() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void setMapper(K mapper) {
        this.mapper = mapper;
    }

    public void beforeSave(D dataObject, String type) {
    }

    public void afterSave(D dataObject, String type) {
    }

    public int insert(D dataObject) {
        this.beforeSave(dataObject, "insert");
        if (this.isValidDo(dataObject)) {
            dataObject.setCreator(this.getOperator(this.pvgInfo));
            dataObject.setModifier(this.getOperator(this.pvgInfo));
            dataObject.setGmtCreate(new Date(System.currentTimeMillis()));
            dataObject.setGmtModified(new Date(System.currentTimeMillis()));
            int v = this.mapper.insertSelective(dataObject);
            this.afterSave(dataObject, "insert");
            return v;
        } else {
            throw new InvalidDoException("Invalid do:" + dataObject.toString());
        }
    }

    public int delete(Long id) {
        D record = (D) this.mapper.selectByPrimaryKey(id);
        record.setGmtModified(new Date(System.currentTimeMillis()));
        record.setModifier(this.getOperator(this.pvgInfo));
        return this.mapper.deleteByPrimaryKey(record);
    }

    public int deleteByExample(E example) {
        Map<String, Object> map = new HashMap();
        map.put("example", example);
        Map<String, Object> record = new HashMap();
        record.put("gmtModified", new Date(System.currentTimeMillis()));
        record.put("modifier", this.getOperator(this.pvgInfo));
        record.put("isDeleted", "y");
        map.put("record", record);
        return this.mapper.updateByExampleSelective(map);
    }

    public List<D> selectByExample(E example) {
        return this.mapper.selectByExample(example);
    }

    public int countByExample(E example) {
        return this.mapper.countByExample(example);
    }

    public DataResult<D> getPageByExample(E example) {
        DataResult<D> dr = new DataResult();
        dr.setData(this.selectByExample(example));
        example.setPage((Page)null);
        dr.setCount(this.countByExample(example));
        return dr;
    }

    public D selectByPrimaryKey(Long id) {
        return (D) this.mapper.selectByPrimaryKey(id);
    }

    public int update(D dataObject) {
        this.beforeSave(dataObject, "update");
        if (this.isValidDo(dataObject)) {
            dataObject.setGmtModified(new Date(System.currentTimeMillis()));
            dataObject.setModifier(this.getOperator(this.pvgInfo));
            int v = this.mapper.updateByPrimaryKeySelective(dataObject);
            this.afterSave(dataObject, "update");
            return v;
        } else {
            throw new InvalidDoException("Invalid do:" + dataObject.toString());
        }
    }

    public int updateByExample(D record, E example) {
        return this.mapper.updateByExampleSelective(record, example);
    }

    public int updateByExample(Map<String, Object> map) {
        return this.mapper.updateByExampleSelective(map);
    }

    public boolean isValidDo(D dataObject) {
        return true;
    }

    private String getOperator(PrivilegeInfo pvgInfo) {
        return pvgInfo != null && !StringUtils.isEmpty(pvgInfo.getUserId()) ? pvgInfo.getUserId() : "SYSTEM";
    }

    public void insertBatchSelective(List<D> recordLst){
        for (D dataObject : recordLst) {
            dataObject.setCreator(this.getOperator(this.pvgInfo));
            dataObject.setModifier(this.getOperator(this.pvgInfo));
            dataObject.setGmtCreate(new Date(System.currentTimeMillis()));
            dataObject.setGmtModified(new Date(System.currentTimeMillis()));
        }
        this.mapper.insertBatchSelective(recordLst);
    }

    public int updateBatchByPrimaryKeySelective(List<D> records){
        for (D dataObject : records) {
            dataObject.setModifier(this.getOperator(this.pvgInfo));
            dataObject.setGmtModified(new Date(System.currentTimeMillis()));
        }
        int i = this.mapper.updateBatchByPrimaryKeySelective(records);
        return i;
    }
}

