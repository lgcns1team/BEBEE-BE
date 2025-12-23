package com.lgcns.bebee.chat.domain.repository;

import com.lgcns.bebee.chat.domain.entity.HelpCategorySync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HelpCategorySyncRepository extends JpaRepository<HelpCategorySync, Long> {

    @Query("SELECT hcp FROM HelpCategorySync hcp WHERE hcp.id in :ids")
    List<HelpCategorySync> findByIds(List<Long> ids);
}
