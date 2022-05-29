package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repo.RoleRepository;
import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class RoleRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	void testAdminRole() {
		Role entity = new Role("Admin", "manage everything");
		Role testEntity = roleRepository.save(entity);
		assertThat(testEntity.getId() > 0);
	}

	@Test
	void testRestRoles() {
		Role roleSalesPerson = new Role("Salesperson",
				"manage product price" + "customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "manage categories, brands, " + "products, articles and menus");
		Role roleShipper = new Role("Shipper", "mview products, view orders " + "and update order status ");
		Role roleAssistant = new Role("Assistant", "manage questions and " + "reviews ");

		roleRepository.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));
	}

}
