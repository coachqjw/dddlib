package com.dayatang.dsrouter.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.utils.Slf4jLogger;

public abstract class AbstractDataSourceCreator implements DataSourceCreator {
	
	private static final Slf4jLogger LOGGER = Slf4jLogger.of(AbstractDataSourceCreator.class);

	protected Properties properties = new Properties();
	private JdbcUrlTranslator urlTranslator;

	public AbstractDataSourceCreator(Properties properties) {
		this.properties = properties;
	}

	public JdbcUrlTranslator getUrlTranslator() {
		if (urlTranslator == null) {
			throw new DataSourceCreationException(getClass() +  "'s urlTranslator not setted!");
		}
		return urlTranslator;
	}

	public void setUrlTranslator(JdbcUrlTranslator urlTranslator) {
		this.urlTranslator = urlTranslator;
	}

	public DataSource createDataSourceForTenant(String tenant) {
		DataSource result = createDataSource();
		try {
			fillProperties(result);
			fillStandardProperties(result, tenant);
			return result;
		} catch (Exception e) {
			String message = "Create data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	protected abstract DataSource createDataSource();

	private void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		for (Object key : properties.keySet()) {
			BeanUtils.setProperty(dataSource, key.toString(), properties.get(key));
		}
	}
	
	private void fillStandardProperties(DataSource dataSource, String tenant) throws IllegalAccessException, InvocationTargetException {
		Map<String, String> standardProperties = getStandardPropMappings();
		for (Object key : standardProperties.keySet()) {
			BeanUtils.setProperty(dataSource, standardProperties.get(key), properties.get(key));
		}
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_URL), getUrlTranslator().translateUrl(tenant, properties));
	}
	
	protected abstract Map<String, String> getStandardPropMappings();

	private String getUrl(String tenant) {
		return getUrlTranslator().translateUrl(tenant, properties);
		//DbType dbType = DbType.valueOf(properties.getProperty(Constants.DB_TYPE));
		//return dbType.getJdbcUrl(tenant, properties);
	}

	@SuppressWarnings("unused")
	private static void printDsProps(DataSource result) throws IllegalAccessException, InvocationTargetException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> dsProps = BeanUtils.describe(result);
			for (String key : dsProps.keySet()) {
				//LOGGER.debug("----------------{}: {}", key, dsProps.get(key));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
