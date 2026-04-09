package com.jobbot.repository;

import com.jobbot.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
	List<JobEntity> findByJobUrl(String jobUrl);

	// Use Optional to safely handle null and multiple results
	Optional<JobEntity> findFirstByJobUrlOrderByIdAsc(String jobUrl);
}
