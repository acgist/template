package com.acgist.data.pojo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.acgist.core.config.AcgistConst;
import com.acgist.core.pojo.Pojo;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>Entity - 数据库实体基类</p>
 * <p>@Transient：不需要映射数据库字段</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@EntityListeners(EntityListener.class)
@MappedSuperclass
public class BaseEntity extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>class</p>
	 */
	public static final String PROPERTY_CLASS = AcgistConst.PROPERTY_CLASS;
	
	/**
	 * @see {@link #id}
	 */
	public static final String PROPERTY_ID = "id";
	/**
	 * @see {@link #createDate}
	 */
	public static final String PROPERTY_CREATE_DATE = "createDate";
	/**
	 * @see {@link #modifyDate}
	 */
	public static final String PROPERTY_MODIFY_DATE = "modifyDate";

	/**
	 * <p>ID</p>
	 */
	private String id;
	/**
	 * <p>创建时间</p>
	 */
	private Date createDate;
	/**
	 * <p>修改时间</p>
	 */
	private Date modifyDate;
	
	@Id
	@Column(length = 32)
	@GenericGenerator(name = "sequenceGenerator", strategy = "uuid")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sequenceGenerator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DateTimeFormat(pattern = AcgistConst.DATE_TIME_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AcgistConst.DATE_TIME_FORMAT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@DateTimeFormat(pattern = AcgistConst.DATE_TIME_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AcgistConst.DATE_TIME_FORMAT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof BaseEntity) {
			final BaseEntity baseEntity = (BaseEntity) obj;
			return new EqualsBuilder()
				.append(getId(), baseEntity.getId())
				.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getId())
			.toHashCode();
	}
	
}
