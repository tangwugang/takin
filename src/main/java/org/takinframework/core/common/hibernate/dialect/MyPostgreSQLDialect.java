package org.takinframework.core.common.hibernate.dialect;


public class MyPostgreSQLDialect extends PostgreSQLDialect {

	
	public boolean useInputStreamToInsertBlob() {
		return true;
	}

}
