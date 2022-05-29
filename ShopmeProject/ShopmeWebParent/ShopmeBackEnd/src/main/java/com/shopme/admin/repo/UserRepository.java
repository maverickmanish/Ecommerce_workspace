package com.shopme.admin.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;
import com.shopme.common.entity.Role;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);

	public Long countById(Integer id);

	/*
	 * @Query(value="SELECT u.* FROM users u, users_roles ur, roles r " +
	 * " WHERE u.id = ur.user_id and ur.role_id=r.id " +
	 * " or CONCAT(u.first_name,' '" + ",u.last_name,' ',u.email,' ',u.id)" +
	 * " LIKE %?1% " + " or r.name LIKE %?1% ", nativeQuery=true)
	 */
	@Query("SELECT distinct(u) FROM User u JOIN u.roles r WHERE u.firstName LIKE %?1%" 
	 + " OR u.lastName LIKE %?1%" + " OR u.email LIKE %?1%"
+ " OR u.id LIKE %?1%" + "OR CONCAT(u.firstName,' ',u.lastName) LIKE %?1%"
		+ "OR r.name LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);

	@Modifying
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	public void updateEnabledStatus(Integer id, boolean enabled);

	@Modifying
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
	public void updateEnabledStatus2(Integer id, boolean enabled);

}
