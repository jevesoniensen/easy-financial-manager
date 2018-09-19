package com.efm.service.impl;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.efm.dao.iface.AccountingEntryDao;
import com.efm.dao.iface.SourceInputDao;
import com.efm.exception.InternalException;
import com.efm.model.AccountingEntry;
import com.efm.model.EntryType;
import com.efm.model.SourceInput;
import com.efm.service.iface.AccountingEntryService;

import static com.efm.model.EntryType.*;

@Service
public class AccountingEntryServiceImpl implements AccountingEntryService {
	
	@Autowired
	private AccountingEntryDao accountingEntryDao;
	
	@Autowired
	private SourceInputDao sourceInputDao;
	
	public List<AccountingEntry> findAll(){
		return accountingEntryDao.findAll();
	}
	
	public Map<String, BigDecimal> generateReport(){

		List<AccountingEntry> entries = accountingEntryDao.findAll();
        Map<String,BigDecimal> result = new HashMap<>();
		
		result.putAll( filterByEntryType(entries, In) );
		result.putAll( filterByEntryType(entries, Out) );
		
		return result;
	}

	private Map<String, BigDecimal> filterByEntryType(List<AccountingEntry> entries,
													  EntryType entryType) {
		Map<String,BigDecimal> result = new HashMap<>();

		entries.stream().filter(accountingEntry -> entryType == accountingEntry.getSourceInput().getEntityType())
					    .collect(Collectors.toList())
				        .forEach(accountingEntry -> addValue(accountingEntry, result));
		return result;
	}

	private void addValue(AccountingEntry accountingEntry, Map<String, BigDecimal> mIns) {

		SimpleDateFormat sdfMesAno = new SimpleDateFormat("MM/yyyy");

		String key = accountingEntry.getSourceInput().getEntityType() +"-"+ sdfMesAno.format(accountingEntry.getDate());

		mIns.put(key, add(mIns.get(key), accountingEntry.getValue()));
	}

	public BigDecimal add(BigDecimal current, BigDecimal value){
		if(current == null){
			return value;
		}
		return current.add(value);
	}

	private List<String> loadFile(String filePath) {
		try (FileInputStream fis = new FileInputStream(filePath)) {

			return IOUtils.readLines(fis, "UTF-8");
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	@Transactional
	@Override
	public void loadFiles(String filePath, Consumer<List<String>> consumer) {

		List<String> lines = loadFile(filePath);

		if(lines!=null){
			lines.stream().map(this::mapLineToParam).forEach(consumer::accept);
		}
	}

	private List<String> mapLineToParam(String line){
		try {
			List<String> params = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(line, ";");
			String token;
			while(st.hasMoreTokens()){
				token = st.nextToken();
				params.add(token!=null?token.trim():null);
			}
			return params;
		} catch (Exception e) {
			throw new InternalException(e,"Current line ["+line+"]");
		}
	}

	@Override
	public void saveAccountEntry(List<String> params){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			AccountingEntry accountingEntry = new AccountingEntry();
			accountingEntry.setDate(sdf.parse(params.get(0)));
			
			String value = params.get(1);
			value = value.replace(",", ".");
			
			accountingEntry.setValue(new BigDecimal(value));
			
			SourceInput si = sourceInputDao.findByName(params.get(2));
			
			if(si == null){
				throw new Exception("Param not fount " + params.get(2));
			}
			
			accountingEntry.setSourceInput(si);
			
			if(params.size()>3){
				accountingEntry.setDescription(params.get(3));
			}
			
			accountingEntryDao.saveAndFlush(accountingEntry);
		} catch (Exception e) {
			throw new InternalException(e,"saveAccountEntry:params ["+params+"]");
		}
	}

	@Override
	public void saveSourceInput(List<String> params) {
        try {
            SourceInput si = new SourceInput();
            si.setName(params.get(0));
            si.setEntityType(valueOf(params.get(1)));
            si.setGroupEntity(params.get(2));
            sourceInputDao.saveAndFlush(si);
        } catch (Exception e) {
            throw new InternalException(e, "saveAccountEntry:params [" + params + "]");
        }
    }
}
