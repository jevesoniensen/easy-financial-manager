package com.efm.service.iface;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.efm.model.AccountingEntry;
import org.springframework.transaction.annotation.Transactional;

public interface AccountingEntryService {

	List<AccountingEntry> findAll();

	Map<String, BigDecimal> generateReport();

	void loadFiles(String filePath, Consumer<List<String>> consumer);

	void saveAccountEntry(List<String> params);

	void saveSourceInput(List<String> params);
}
