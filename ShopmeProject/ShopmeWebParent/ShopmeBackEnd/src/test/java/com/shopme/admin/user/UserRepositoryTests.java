package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repo.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		User user = new User("jogsatgadigital@gmail.com", "komal", "arya", "password");
		Role role = entityManager.find(Role.class, 1);
		user.addRole(role);
		User savedUser = repo.save(user);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUserWithTwoRoles() {
		User userRavi = new User("ravi@gmail.com", "ravi", "kumar", "password");
		Role roleEditor = new Role(2);
		Role roleAssistant = new Role(4);
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);
		User savedUser = repo.save(userRavi);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testAllUsers()
	{
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(
				user->System.out.println(user));
	}
	
	@Test
	public void testUserById()
	{
		Optional<User> user = repo.findById(1);
		assertThat(user.isPresent());
			}
	
	@Test
	public void updateUserDetails()
	{
		Optional<User> user = repo.findById(2);
		if(user.isPresent())
		{
			User actualUser = user.get();
			actualUser.setEnabled(true);
			actualUser.setPassword("password");
			repo.save(actualUser);
		}
	}
	
	@Test
	public void updateUserRoles()
	{
		Optional<User> user = repo.findById(3);
		if(user.isPresent())
		{
			User actualUser = user.get();
			actualUser.setEnabled(true);
			actualUser.setPassword("password");
			Role roleEditor = new Role(2);
			//actualUser.getRoles().remove(roleSalesPerson);
			actualUser.addRole(roleEditor);
			repo.save(actualUser);
		}
	}
	
	@Test
	public void deleteUserById()
	{
		Integer userId=3;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail()
	{
		String email = "bhardwajoperations@gmail.com";
		User userByEmail = repo.findByEmail(email);
		assertNotNull(userByEmail);
	}
	
	@Test
	public void testCountById()
	{
		Integer id=1;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
		
	}
	
	@Test
	public void testUserUpdateEnabledStatus2()
	{
		repo.updateEnabledStatus(1,true);
		
	}
	
	@Test
	public void testListFirstPage()
	{
		int pageNumber=0;
		int pageSize=45;
		
		Page<User> findAll = repo.findAll(PageRequest.of(pageNumber, pageSize));
		List<User> content = findAll.getContent();
		content.forEach(x-> System.out.println(x));
		assertThat(findAll.getTotalElements()).isEqualTo(23);
	}

}
