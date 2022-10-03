package org.apache.airavata.jupyter.api.repo;

import org.apache.airavata.jupyter.api.entity.ui.UIExecutionEntity;
import org.springframework.data.repository.CrudRepository;

public interface UIExecutionRepository extends CrudRepository<UIExecutionEntity, String> {
}
