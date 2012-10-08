package com.dayatang.dsrouter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SaasDataSourceTest {
	
	private SaasDataSource instance;
	private TenantHolder tenantHolder; 
	private DataSourceRegistry dataSourceRegistry;
	private DataSource actualDataSource;

	@Before
	public void setUp() throws Exception {
		instance = new SaasDataSource();
		tenantHolder = mock(TenantHolder.class);
		instance.setTenantHolder(tenantHolder);
		dataSourceRegistry = mock(DataSourceRegistry.class);
		instance.setDataSourceRegistry(dataSourceRegistry);
		actualDataSource = mock(DataSource.class);
		when(tenantHolder.getTenant()).thenReturn("abc");
		when(dataSourceRegistry.getOrCreateDataSourceByTenant("abc")).thenReturn(actualDataSource);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLogWriter() throws SQLException {
		PrintWriter printWriter = mock(PrintWriter.class);
		when(actualDataSource.getLogWriter()).thenReturn(printWriter);
		assertSame(printWriter, instance.getLogWriter());
	}

	@Test
	public void testGetLoginTimeout() throws SQLException {
		int timeout = 1000;
		when(actualDataSource.getLoginTimeout()).thenReturn(timeout);
		assertEquals(timeout, actualDataSource.getLoginTimeout());
	}

	@Test
	public void testSetLogWriter() throws SQLException {
		PrintWriter printWriter = mock(PrintWriter.class);
		instance.setLogWriter(printWriter);
		verify(actualDataSource).setLogWriter(printWriter);
	}

	@Test
	public void testSetLoginTimeout() throws SQLException {
		int seconds = 100;
		instance.setLoginTimeout(seconds);
		verify(actualDataSource).setLoginTimeout(seconds);
	}

	@Test
	public void testIsWrapperFor() throws SQLException {
		Class<String> stringClass = String.class;
		Class<Long> longClass = Long.class;
		when(actualDataSource.isWrapperFor(stringClass)).thenReturn(true);
		assertTrue(instance.isWrapperFor(stringClass));
		when(actualDataSource.isWrapperFor(longClass)).thenReturn(false);
		assertFalse(instance.isWrapperFor(longClass));
	}

	@Test
	public void testUnwrap() throws SQLException {
		String result = "abcddddd";
		when(actualDataSource.unwrap(String.class)).thenReturn(result);
		assertSame(result, instance.unwrap(String.class));
	}

	@Test
	public void testGetConnection() throws SQLException {
		Connection result = mock(Connection.class);
		when(actualDataSource.getConnection()).thenReturn(result);
		assertSame(result, instance.getConnection());
	}

	@Test
	public void testGetConnectionStringString() throws SQLException {
		String username = "zhangsan";
		String password = "pwd";
		Connection result = mock(Connection.class);
		when(actualDataSource.getConnection(username, password)).thenReturn(result);
		assertSame(result, instance.getConnection(username, password));
	}

}