package com.efm.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "AccountingEntries" )
public class AccountingEntry {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	
	@Column
	private Date date;
	
	@Column
	private BigDecimal value;
	
	@ManyToOne(fetch=FetchType.EAGER , cascade= {CascadeType.PERSIST , CascadeType.MERGE })
	@JoinColumn(name="sourceInputId")
	private SourceInput sourceInput;
	
	@Column(nullable=true)
	private String description;

	public Long getId() {
	    return id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public SourceInput getSourceInput() {
		return sourceInput;
	}
	
	public void setSourceInput(SourceInput sourceInput) {
		this.sourceInput = sourceInput;
	}
}
