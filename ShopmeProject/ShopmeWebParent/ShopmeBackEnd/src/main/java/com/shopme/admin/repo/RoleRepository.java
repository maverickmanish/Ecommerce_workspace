package com.shopme.admin.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
