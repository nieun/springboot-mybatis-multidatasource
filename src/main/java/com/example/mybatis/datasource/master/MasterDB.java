package com.example.mybatis.datasource.master;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Transactional(transactionManager = MasterDBConfig.TX_MANAGER, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public @interface MasterDB {
}
