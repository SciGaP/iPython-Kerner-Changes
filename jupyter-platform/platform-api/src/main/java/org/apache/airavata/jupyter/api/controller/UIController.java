package org.apache.airavata.jupyter.api.controller;

import org.apache.airavata.jupyter.api.entity.ui.UIAppEntity;
import org.apache.airavata.jupyter.api.entity.ui.UIExecutionEntity;
import org.apache.airavata.jupyter.api.entity.ui.UIExecutionResponseEntity;
import org.apache.airavata.jupyter.api.entity.ui.UIExecutionResultEntity;
import org.apache.airavata.jupyter.api.repo.UIAppRepository;
import org.apache.airavata.jupyter.api.repo.UIExecutionResultRepository;
import org.apache.airavata.jupyter.core.OrchestrationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/ui")
public class UIController {

    @Autowired
    private UIAppRepository uiAppRepository;

    @Autowired
    private UIExecutionResultRepository uiExecutionResultRepository;

    @Autowired
    private OrchestrationEngine orchestrationEngine;

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public UIAppEntity createNotebook(Authentication authentication, @RequestBody UIAppEntity uiAppEntity) {
        UIAppEntity saved = uiAppRepository.save(uiAppEntity);
        return saved;
    }

    @GetMapping(path = "/")
    public List<UIAppEntity> listUIApps(Authentication authentication) {
        Iterable<UIAppEntity> allUIs = uiAppRepository.findAll();
        List<UIAppEntity> uiList = new ArrayList<>();
        allUIs.forEach(uiList::add);
        return uiList;
    }

    @PostMapping(path = "/launch", consumes = "application/json", produces = "application/json")
    public UIExecutionResponseEntity runUI(Authentication authentication, @RequestBody UIExecutionEntity uiExecutionEntity)
            throws Exception {
        return orchestrationEngine.launchUI(uiExecutionEntity);
    }

    @GetMapping(path = "/killNoVnc/{agentId}/{containerId}")
    public String killNoVnc(Authentication authentication, @PathVariable String agentId,
                            @PathVariable String containerId) throws Exception {
        orchestrationEngine.killNoVncSession(agentId, containerId);
        return "Success";
    }

    @GetMapping(path = "/container/status/{agentId}/{containerId}")
    public Map<String, String> getUiContainerStatus(Authentication authentication,
                                                    @PathVariable String agentId, @PathVariable String containerId) throws Exception {
        String uiContainerStatus = orchestrationEngine.checkUIContainerStatus(agentId, containerId);
        return Collections.singletonMap("status", uiContainerStatus);
    }

    @PostMapping(path = "/result", consumes = "application/json", produces = "application/json")
    public UIExecutionResultEntity addUiExecutionResult(Authentication authentication,
                                                        @RequestBody UIExecutionResultEntity resultEntity)
            throws Exception {
        return uiExecutionResultRepository.save(resultEntity);
    }

    @GetMapping(path = "/result/execution/{executionId}")
    public UIExecutionResultEntity getUiExecxutionResult(Authentication authentication, @PathVariable String executionId)
            throws Exception{
        Optional<UIExecutionResultEntity> firstByExecutionId = uiExecutionResultRepository.findFirstByExecutionId(executionId);
        return firstByExecutionId.orElseThrow(
                () -> new Exception("Could not find a result entry for execution id " + executionId));
    }



}
