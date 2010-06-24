package com.dayatang.dsmonitor.monitor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsmonitor.datasource.GeminiConnection;

public abstract class AbstractGeminiConnectionTimeoutMonitor implements
		ConnectionMonitor {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractGeminiConnectionTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static Set<GeminiConnection> aliveConnections = Collections
			.synchronizedSet(new HashSet<GeminiConnection>());

	private static Set<GeminiConnection> closedTimeoutConnections = Collections
			.synchronizedSet(new HashSet<GeminiConnection>());

	private long timeout = 1000;

	public void closeConnection(Connection conn) throws SQLException {
		GeminiConnection connection = (GeminiConnection) conn;

		if (isTimeout(connection)) {
			closedTimeoutConnections.add(connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("关闭数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】", new Object[] {
					connection.hashCode(),
					DateFormatUtils.format(connection.getCreationTime(),
							DATE_PATTERN), connection.getStopWatch() });
		}

		aliveConnections.remove(connection);
	}

	public void openConnection(Connection conn) throws SQLException {
		GeminiConnection connection = (GeminiConnection) conn;

		if (logger.isDebugEnabled()) {
			logger.debug("开启数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】", new Object[] {
					connection.hashCode(),
					connection.getMetaData().getURL(),
					DateFormatUtils.format(connection.getCreationTime(),
							DATE_PATTERN) });
		}

		aliveConnections.add(connection);
	}

	private boolean isTimeout(GeminiConnection conn) {
		if (conn.getTime() > timeout) {
			return true;
		}
		return false;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * 获取活动的超时连接集合
	 * 
	 * @return 活动的超时连接集合
	 */
	public Set<GeminiConnection> getAliveTimeoutConnections() {
		Set<GeminiConnection> aliveTimeoutConnections = new HashSet<GeminiConnection>();
		for (GeminiConnection conn : aliveConnections) {
			if (isTimeout(conn)) {
				aliveTimeoutConnections.add(conn);
			}
		}
		return aliveTimeoutConnections;
	}

	/**
	 * 获取已关闭的超时连接集合
	 * 
	 * @return 已关闭的超时连接集合
	 */
	public Set<GeminiConnection> getClosedTimeoutConnections() {
		return closedTimeoutConnections;
	}

	/**
	 * 获取所有的超时连接集合（包括活动的和已关闭的）
	 * 
	 * @return 所有的超时连接集合（包括活动的和已关闭的）
	 */
	public Set<GeminiConnection> getTimeoutConnections() {
		Set<GeminiConnection> timeoutConnections = new HashSet<GeminiConnection>();
		timeoutConnections.addAll(getClosedTimeoutConnections());
		timeoutConnections.addAll(getAliveTimeoutConnections());
		return timeoutConnections;
	}

	public abstract void monitor();
}
