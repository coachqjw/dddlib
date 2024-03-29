package com.dayatang.db.oracle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.db.DBManager;


public class OracleDBManagerTest {

	DBManager dbManager ;
	String filePath = "src/test/resources/temp";

	@Before
	public void init(){
		
		try {
			dbManager = new OracleDBManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testCleanDatabase() {
		dbManager.cleanDatabase();
	}

	@Test
	public void testExecuteSQL() {
		String sqlFile = "src/test/resources/scripts/oracle.sql";
		dbManager.executeSQL(sqlFile);
	}

	@Test
	public void testExportAll() {
		dbManager.exportAll(filePath);
	}

	@Test
	public void testExportExcludes() {
		List<String> excludes = new ArrayList<String>();
		excludes.add("test_");
		dbManager.exportExcludes(filePath,excludes);
	}

	@Test
	public void testExportIncludes() {
		List<String> includes = new ArrayList<String>();
		includes.add("test_");
		dbManager.exportIncludes(filePath, includes);
	}
}
