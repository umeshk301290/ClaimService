package com.prgx.workbench.claim.workflowobjects;

import com.prgx.workbench.odata.jpa.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClaimWorkFlowObjectsRepository extends BaseRepository<ClaimWorkFlowObject, Long> {

	public List<ClaimWorkFlowObject> findByclaimIdAndWorkFlowStatus(Long claimId,
			WorkFlowStatusType workFlowStatusType);
	
	//@Query(value = "SELECT wfo from WorkflowObject wfo where wfo.claimId = ?1 order by wfo.lastModifiedDate desc")
	public ClaimWorkFlowObject findTop1ByClaimIdOrderByLastModifiedDateDesc(Long claimId);

	@Modifying
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	@Query("UPDATE ClaimWorkFlowObject CWFO set CWFO.locked = 0 where CWFO.id = :id and CWFO.locked = 1")
	int unlockWorkflowObject(@Param("id") final Long id);

	@Modifying
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	@Query("UPDATE ClaimWorkFlowObject CWFO set CWFO.locked = 1 where CWFO.id = :id and CWFO.locked = 0")
	int lockWorkflowObject(@Param("id") final Long id);

	ClaimWorkFlowObject findTop1ByClaimIdAndProcessInstanceIdOrderByLastModifiedDateDesc(Long claimId, String processInstanceId);

}
