package com.customer_service.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.customer_service.api.entity.Region;

import jakarta.transaction.Transactional;

@Repository
public interface RepoRegion extends JpaRepository<Region, Integer>{

	@Query(value ="SELECT * FROM region ORDER BY region", nativeQuery = true)
	List<Region> findAll();
	
	@Query(value ="SELECT * FROM region WHERE status = 1 ORDER BY region", nativeQuery = true)
	List<Region> findActive();
	
	List<Region> findByStatusOrderByRegionAsc(Integer status);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = ""
			+ "INSERT INTO region(region, tag, status) "
			+ "VALUES (:region, :tag, 1)", nativeQuery = true)
	void create(@Param("region") String region, 
			@Param("tag") String tag);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE region SET region = :region, tag = :tag "
			+ "WHERE region_id = :region_id;", nativeQuery = true)
	void update(@Param("region_id") Integer region_id, 
			@Param("region") String region, @Param("tag") String tag);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE region SET status = 1 "
			+ "WHERE region_id = :region_id;", nativeQuery = true)
	void enable(@Param("region_id") Integer region_id);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE region SET status = 0 "
			+ "WHERE region_id = :region_id;", nativeQuery = true)
	void disable(@Param("region_id") Integer region_id);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE region SET status = :status "
			+ "WHERE region_id = :region_id;", nativeQuery = true)
	void switchStatus(@Param("region_id") Integer region_id,
			@Param("status") Integer status);

}
