package com.mysite.sbb.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {
	
	@Modifying
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, SYSDATE)", nativeQuery = true)
	int mSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	int mUnSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :videoUserId", nativeQuery = true)
	int mSubscribeState(int principalId, int videoUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :videoUserId", nativeQuery = true)
	int mSubscribeCount(int videoUserId);
}
