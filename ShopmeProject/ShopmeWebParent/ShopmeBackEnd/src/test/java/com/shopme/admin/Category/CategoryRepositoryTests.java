package com.shopme.admin.Category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.repo.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repository.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);

	}

	@Test
	void testCreateChildrenCategory() {
		Category ParentCategory = new Category(1);
		Category subCategory = new Category("Desktops", ParentCategory);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);

	}

	@Test
	void testCreateChildrenCategory2() {
		Category ParentCategory = entityManager.find(Category.class, 1);
		Category subCategory = new Category("Laptop", ParentCategory);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);

	}

	@Test
	void testCreateChildrenCategory3() {
		Category ParentCategory = entityManager.find(Category.class, 1);
		Category subCategory = new Category("Computer Components", ParentCategory);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);

	}

	@Test
	void testCreateChildrenCategory4() {
		Category electronics = entityManager.find(Category.class, 2);
		Category camera = new Category("Camera", electronics);
		Category smartPhones = new Category("Smartphones", electronics);
		repository.saveAll(List.of(camera, smartPhones));
	}

	@Test
	void testCreateChildrenCategory5() {
		Category ParentCategory = entityManager.find(Category.class, 5);
		Category subCategory = new Category("Memory", ParentCategory);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	void testCreateChildrenCategory6() {
		Category ParentCategory = entityManager.find(Category.class, 4);
		Category subCategory = new Category("Gaming Latop", ParentCategory);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	void testCreateChildrenCategory7() {
		Category ParentCategory = entityManager.find(Category.class, 7);
		Category subCategory = new Category("Iphone", ParentCategory);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	void testGettingCategoryAndchildren() {
		Category findById = repository.findById(1).get();
		System.out.println("Root->" + findById.getName());
		for (Category child : findById.getChildren()) {
			System.out.println("         child--->" + child.getName());
		}

	}

	@Test
	void testGettingCategoryAndchildrenHierarchical() {
		Iterable<Category> findAll = repository.findAll();
		for (Category category : findAll) {
			if (category.getParent() == null)
				{System.out.println(category.getName());
			    subChildren(category,1);
				}
		}

	}

	private void subChildren(Category category, int parentLevel) {
		
		for (Category child : category.getChildren()) {
			for(int level=0;level<parentLevel;level++)
				System.out.print("--");
			System.out.println("> " + child.getName());
			if(child.getChildren()!=null)
				subChildren(child,parentLevel+1);
		}
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category findByName = repository.findByName(name);
		assertThat(findByName.getName()).isNotNull().isEqualTo(name);
	}

}
