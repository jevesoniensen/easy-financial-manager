package com.efm.dao.iface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efm.model.SourceInput;

@Repository
public interface SourceInputDao extends JpaRepository<SourceInput, Long> {
	SourceInput findByName(String name);
}
