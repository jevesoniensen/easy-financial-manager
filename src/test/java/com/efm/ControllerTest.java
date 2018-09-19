package com.efm;

import com.efm.model.AccountingEntry;
import com.efm.model.EntryType;
import com.efm.service.iface.AccountingEntryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@ContextConfiguration(classes={TestConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ControllerTest {
	
	@Autowired
	private AccountingEntryService accountingEntryService;
	
	@Transactional
	@Test
	public void run(){

		
		accountingEntryService.loadFiles(ControllerTest.class.getResource("/InputSources.txt").getFile(),accountingEntryService::saveSourceInput);
		accountingEntryService.loadFiles(ControllerTest.class.getResource("/AccountEntry.txt").getFile(),accountingEntryService::saveAccountEntry);
		
		List<AccountingEntry> accountingEntries = accountingEntryService.findAll();
		
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfMesAno = new SimpleDateFormat("MM/yyyy");
		BigDecimal totalIn = new BigDecimal(0.0);
		BigDecimal totalOut = new BigDecimal(0.0);
		HashMap<String,BigDecimal> mapGastoMes = new HashMap<String,BigDecimal>();
		try {
			for (AccountingEntry entity : accountingEntries) {
				if(EntryType.In.equals(entity.getSourceInput().getEntityType())){
					totalIn = totalIn.add(entity.getValue());
				}else{
					totalOut = totalOut.add(entity.getValue());
					
					BigDecimal gastoMes = mapGastoMes.get(sdfMesAno.format(entity.getDate()));
					if(gastoMes == null){
						gastoMes = entity.getValue();
					}else {
						gastoMes = gastoMes.add(entity.getValue());
					}					
					mapGastoMes.put(sdfMesAno.format(entity.getDate()), gastoMes);
				}
				
				sb.append(sdf.format(entity.getDate()) + " - " + 
									 entity.getValue()  + " - " + 
									 entity.getSourceInput().getName() + " - " + 
									 entity.getDescription()+"\n");
			}
			
			Map<String, BigDecimal> report = accountingEntryService.generateReport();
			
			List<String> keyReport = new ArrayList<String>(report.keySet());
			
			Collections.sort(keyReport);
			
			for (String key : keyReport) {
				sb.append(key)
				  .append(":")
				  .append(report.get(key))
				  .append("\n");
			}
			
			sb.append("\n");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sb.append("-----------------------------------------------\n");
			List<String> keyGastoMes = new ArrayList<String>(mapGastoMes.keySet());
			Collections.sort(keyGastoMes);
			
			for (String key : keyGastoMes) {
				sb.append(key)
				  .append(":")
				  .append(mapGastoMes.get(key))
				  .append("\n");
			}
 			
			sb.append("Total Out :" + totalOut+"\n");
			sb.append("Total in  :" + totalIn+"\n");
			System.out.println(String.valueOf(sb));
		}
	}
}
