package com.interview.esphere.domain.repository;

import com.interview.esphere.domain.model.User;
import com.interview.esphere.domain.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("operationRepository")
public interface OperationRepository extends JpaRepository<Operation, Long> {
	 List<Operation> findTop20ByUserOrderByExecutionTimeDesc(User user);
}
