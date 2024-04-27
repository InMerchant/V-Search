package com.mysite.sbb.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {
	
	@Modifying
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, videoNo, createDate) VALUES(:fromUserId, :toUserId, :videoNo, SYSDATE)", nativeQuery = true)
	int mSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId, @Param("videoNo") int videoNo);
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	int mUnSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
    int mSubscribeState(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE toUserId = :toUserId", nativeQuery = true)
	int mSubscribeCount(@Param("toUserId")int toUserId);
	
	
}
