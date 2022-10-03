package org.apache.airavata.jupyter.api.repo;

import org.apache.airavata.jupyter.api.entity.ui.UIExecutionResultEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UIExecutionResultRepository extends CrudRepository<UIExecutionResultEntity, String> {

    Optional<UIExecutionResultEntity> findFirstByExecutionId(String s);
}
