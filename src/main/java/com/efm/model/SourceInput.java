package com.efm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "SourceInput" )
public class SourceInput {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int id;
	
	@Column
	public String name;
	
	@Enumerated(EnumType.STRING)
	private  EntryType entityType;
	
	@Column
	private String groupEntity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public EntryType getEntityType() {
		return entityType;
	}
	
	public void setEntityType(EntryType entityType) {
		this.entityType = entityType;
	}
	
	public void setGroupEntity(String groupEntity) {
		this.groupEntity = groupEntity;
	}
	
	public String getGroupEntity() {
		return groupEntity;
	}
}
