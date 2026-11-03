package com.bytter.core.annotation.impl;

import java.sql.Types;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;
 
/**
 * 支持oracle nclob数据类型 
 * @author 12865
 *
 */
public class MyOracleDialect extends Oracle10gDialect{
	public MyOracleDialect() {
		super();              
    registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());        
    registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());      
    registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.STRING.getName());
	registerHibernateType(Types.DECIMAL, StandardBasicTypes.DOUBLE.getName());		
	registerHibernateType(Types.NCLOB, StandardBasicTypes.STRING.getName());
	}
}
