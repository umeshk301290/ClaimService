package com.prgx.workbench.claim.template;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prgx.workbench.claim.ClaimSource;
import com.prgx.workbench.odata.jpa.repository.BaseRepository;

	@Repository
	public interface ClaimTemplateMappingRepository extends BaseRepository<ClaimTemplateMapping, Long> {
		


		@Query("SELECT t FROM ClaimTemplateMapping t where t.claimTemplateCategeory = :claimTemplateCategeory and t.claimTemplateSource = :claimTemplateSource")
	    public Optional<List<ClaimTemplateMapping>> findByCategeoryAndSource(@Param("claimTemplateCategeory") ClaimTemplateCategeory claimTemplateCategeory,@Param("claimTemplateSource") ClaimSource claimTemplateSource);	
	    
		@Query("SELECT t FROM ClaimTemplateMapping t where t.claimTemplateCategeory = :claimTemplateCategeory")
	    public Optional<List<ClaimTemplateMapping>> findByCategeory(@Param("claimTemplateCategeory") ClaimTemplateCategeory claimTemplateCategeory);	
	
	
	}
	

