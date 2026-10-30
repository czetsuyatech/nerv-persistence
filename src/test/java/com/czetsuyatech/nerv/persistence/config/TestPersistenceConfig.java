package com.czetsuyatech.nerv.persistence.config;

import com.czetsuyatech.nerv.persistence.entity.NervEntityPackage;
import com.czetsuyatech.nerv.persistence.repository.NervRepositoryPackage;
import com.czetsuyatech.nerv.persistence.repository.SimpleSliceJpaRepositoryImpl;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Test persistence configuration enabling entity scanning and custom repository base support.
 */
@EnableJpaRepositories(
    repositoryBaseClass = SimpleSliceJpaRepositoryImpl.class,
    basePackageClasses = {NervRepositoryPackage.class})
@EntityScan(basePackageClasses = NervEntityPackage.class)
@EnableTransactionManagement
public class TestPersistenceConfig {

}
