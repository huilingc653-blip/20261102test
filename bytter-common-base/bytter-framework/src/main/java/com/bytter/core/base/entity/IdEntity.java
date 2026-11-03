package com.bytter.core.base.entity;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
/**
 * Hibernate基础实体类
 * 
 * @author zhoul
 * @since 2017/4/3.
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable {
    private String id;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.bytter.core.common.hibernate.id.UUIDGenerator")
    @Column(name = "ID", unique = true, length = 32)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
