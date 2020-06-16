package com.prgx.workbench.claim.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgx.workbench.claim.dmn.builder.QueryToRuleBuilder;
import com.prgx.workbench.claim.workflow.rules.ClaimWorkflowRulesEntity;
import com.prgx.workbench.claim.workflow.rules.ClaimWorkflowRulesService;
import com.prgx.workbench.core.data.dictionary.dataobject.entity.DataObject;
import com.prgx.workbench.core.data.dictionary.dataobject.service.DataObjectService;
import com.prgx.workbench.core.data.dictionary.dataobjectfield.entity.DataObjectField;
import com.prgx.workbench.core.data.dictionary.dataobjectfield.entity.FieldType;
import com.prgx.workbench.exception.ApiRuntimeException;
import com.prgx.workbench.exception.message.GenericMessageCode;


@RestController
@RequestMapping("{clientCode}/claims/rule")
public class WorkflowRuleController {

	private static final String CLAIMS_ENTITY_NAME = "Claim";

	@Autowired
	private DataObjectService dataObjectService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	ClaimWorkflowRulesService claimWorkflowRulesService;
	
	/*
	 * This method is exposed as an API for publishing the 
	 * workflow rules into out Camunda engine. 
	 * 
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public ResponseEntity<String> publishRule(@RequestParam Long projectId, @RequestBody List<ClaimWorkflowRulesEntity> updatedRules) {
		if(updatedRules!=null && !updatedRules.isEmpty()) {
			updateWorkflowRulePriorities(updatedRules);
		}
		List<ClaimWorkflowRulesEntity> rules = claimWorkflowRulesService.findByProjectId(projectId,Arrays.asList(Boolean.FALSE,null));
		QueryToRuleBuilder queryToRuleBuilder = applicationContext.getBean(QueryToRuleBuilder.class);
		DataObject dataObject = dataObjectService.findEntityByName(CLAIMS_ENTITY_NAME);
		List<DataObjectField> standardFields = dataObject.getDataObjectFields().stream()
				.filter(dObj -> (dObj.getFieldType().getEnumName().equals(FieldType.STANDARD.getEnumName())))
				.collect(Collectors.toList());
		try {
			Collections.sort(rules, new Comparator<ClaimWorkflowRulesEntity>() {
				  @Override
				  public int compare(ClaimWorkflowRulesEntity c1, ClaimWorkflowRulesEntity c2) {
					  return c1.getPriority().compareTo(c2.getPriority());
				  }
				});
			if(rules.isEmpty()) {
				throw new ApiRuntimeException(GenericMessageCode.MSG_ERR_SYS_INTERNAL_ERROR,
						new Object[] { "Project with id "+projectId+" do not have any rules!" },
						HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			queryToRuleBuilder.deployDMNXml(projectId,rules,standardFields);
		}catch(ApiRuntimeException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("DMN deployed successfully", HttpStatus.OK);
	}

	/*
	 * This method will update the Workflow Rule priorities
	 */
	private void updateWorkflowRulePriorities(List<ClaimWorkflowRulesEntity> updatedRules) {
		List<ClaimWorkflowRulesEntity> claimWorkFlowRuleObjects = new ArrayList<ClaimWorkflowRulesEntity>();
		updatedRules.forEach(rule->{
			ClaimWorkflowRulesEntity ruleEntity = new ClaimWorkflowRulesEntity();
			ruleEntity.setId(rule.getId());
			ruleEntity.setPriority(rule.getPriority());
			claimWorkFlowRuleObjects.add(ruleEntity);
		});
		claimWorkflowRulesService.updateAll(claimWorkFlowRuleObjects);
	}

}
