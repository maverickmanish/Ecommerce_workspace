package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.CustomException.UserNotFoundException;
import com.shopme.admin.repo.UserRepository;
import com.shopme.common.entity.User;

@Transactional
@Service
public class UserService {

	public static final int USER_PER_PAGE = 5;

	@Autowired
	private UserRepository repository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public List<User> listAll() {
		return repository.findAll(Sort.by("firstName").ascending());
	}
	
	public User listUserByEmail(String email)
	{
		return repository.findByEmail(email);
	}

	public Page<User> listByPage(int PageNum, String sortField, String sortDir, String keyword) {
		Direction direction = "asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest withSort = PageRequest.of(PageNum - 1, USER_PER_PAGE).withSort(Sort.by(direction, sortField));
		if(keyword!=null)
			return repository.findAll(keyword,withSort);
		return repository.findAll(withSort);
	}

	public User save(User user) {
		Integer id = user.getId();
		boolean isUpdatingUser = (id != null);
		if (isUpdatingUser) {
			User existingUser = repository.findById(id).get();
			if (user.getPassword().isEmpty())
				user.setPassword(existingUser.getPassword());
			else
				encodeUser(user);
		} else {
			encodeUser(user);
		}

		return repository.save(user);
	}

	public User updateAccount(User userInForm)
	{
		User userInDB = repository.findById(userInForm.getId()).get();
		if(!userInForm.getPassword().isEmpty())
			userInDB.setPassword(userInForm.getPassword());
		if(!userInForm.getPhotos().isEmpty())
			userInDB.setPhotos(userInForm.getPhotos());
		
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		return repository.save(userInDB);
	}
	
	public void encodeUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public Boolean isEmailUnique(Integer id, String email) {
		User userExists = repository.findByEmail(email);
		if (userExists == null)
			return true;
		Boolean isCreatingNew = (id == null);
		if (isCreatingNew)
			return false;
		else if (userExists.getId() != id)
			return false;

		return true;
	}

	public User getById(Integer id) throws UserNotFoundException {
		Optional<User> findById = repository.findById(id);
		try {
			return findById.get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long countById = repository.countById(id);
		if (countById == null || countById == 0)
			throw new UserNotFoundException("Could not find any user with ID " + id);
		repository.deleteById(id);
	}

	public void updateEnabledStatus(Integer id, Boolean enabled) {
		repository.updateEnabledStatus(id, enabled);
	}

}
