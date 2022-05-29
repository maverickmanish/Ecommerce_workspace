package com.shopme.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.repo.RoleRepository;
import com.shopme.common.entity.Role;

@Service
public class RoleService {

	@Autowired RoleRepository repository;
	
	public List<Role> listRoles()
	{
		return repository.findAll();
	}
}
