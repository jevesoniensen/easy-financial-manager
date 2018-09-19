package com.efm.dao.iface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efm.model.AccountingEntry;

@Repository
public interface AccountingEntryDao extends JpaRepository<AccountingEntry, Long> {

}
