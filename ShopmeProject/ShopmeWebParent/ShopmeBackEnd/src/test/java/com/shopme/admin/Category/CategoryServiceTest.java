package com.shopme.admin.Category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.category.repo.CategoryRepository;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Category;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@MockBean
	private CategoryRepository categoryRepository;
	

	@InjectMocks
	private CategoryService categoryService;
	
	@Test
	void testUniqueCheck() {
		Integer id=null;
		String name="Computers";
		String alias="abc";

		Category category = new Category(id,name,alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
		String checkUnique = categoryService.checkUnique(id, name, alias);
		assertThat(checkUnique).isEqualTo("DuplicateName");
	}
	
	@Test
	void testUniqueCheckAlias() {
		Integer id=1;
		String name="Computers";
		String alias="abc";

		Category category = new Category(2,name,alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		String checkUnique = categoryService.checkUnique(id, name, alias);
		assertThat(checkUnique).isEqualTo("DuplicateName");
	}
	
	@Test
	void testUniqueCheckOK() {
		Integer id=1;
		String name="NameABC";
		String alias="computers";

		Category category = new Category(id,name,alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		String checkUnique = categoryService.checkUnique(id, name, alias);
		assertThat(checkUnique).isEqualTo("OK");
	}

}
