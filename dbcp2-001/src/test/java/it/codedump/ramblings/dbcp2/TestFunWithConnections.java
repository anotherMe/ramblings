package it.codedump.ramblings.dbcp2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

public class TestFunWithConnections {

	
	/**
	 * Overcoming the <b>max_connections</b> set on MySQL server, should raise a "Too many connections" 
	 * exception.
	 * 
	 * Default MySQL max_connections value should be 151 ( 151 + 1 CONNECT_ADMIN ). You can check 
	 * this value with:
	 * 
	 *   show variables like '%connections%';
	 * 
	 * If you have the needed permissions, you can change that value with:
	 * 
	 *   set global max_connections = 151;
	 * 
	 */
	@Test
	public void test_WhenMaxConnectionsExceeded_ShouldRaiseMySQLNonTransientConnectionException() {
		
		GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(300); // max connections allowed in the pool, should be more than MAX connections we want to test
		FunWithConnections funWithConnections = new FunWithConnections(config, 155);
		assertThrows(MySQLNonTransientConnectionException.class, () -> funWithConnections.run());
	}
	
	
	/**
	 * By default, the pool will wait forever for a connection if none is available. Setting the
	 * <b>maxWaitMillis</b> property, will cause an exception to be thrown when there are no
	 * connection and you try to get one.
	 * 
	 * 
	 */
	@Test
	public void test_WhenTimeoutWaitingForIdleObject_ShouldRaiseSQLException() {
		
		GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(3); // max connections allowed in the pool
		config.setMaxWaitMillis(2000); // two seconds before raising an exception
		FunWithConnections funWithConnections = new FunWithConnections(config, 10);
		assertThrows(SQLException.class, () -> funWithConnections.run());
	}
	
}
