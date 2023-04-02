package com.matrix.jab.model.dao;

import com.matrix.jab.model.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InfoDao extends JpaRepository<Info, Long>,
        JpaSpecificationExecutor<Info> {
}
