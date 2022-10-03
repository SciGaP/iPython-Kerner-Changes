package org.apache.airavata.jupyter.api.repo;

import org.apache.airavata.jupyter.api.entity.ui.UIAppEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UIAppRepository extends CrudRepository<UIAppEntity, String> {

    public Optional<UIAppEntity> findFirstByName(String name);

}
