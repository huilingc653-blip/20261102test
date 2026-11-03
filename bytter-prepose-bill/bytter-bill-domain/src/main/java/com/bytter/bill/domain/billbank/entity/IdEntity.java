package com.bytter.bill.domain.billbank.entity;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
@MappedSuperclass
public abstract class IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", unique = true, length = 32)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	@Override
	public String toString() {
		return "IdEntity [id=" + id + "]";
	}
}
