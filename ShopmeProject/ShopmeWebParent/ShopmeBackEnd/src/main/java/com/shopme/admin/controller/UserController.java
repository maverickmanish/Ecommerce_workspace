package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.CustomException.UserNotFoundException;
import com.shopme.admin.Exporter.UserCsvExporter;
import com.shopme.admin.Exporter.UserExcelExporter;
import com.shopme.admin.Exporter.UserPdfExporter;
import com.shopme.admin.Utils.FileUploadUtil;
import com.shopme.admin.service.RoleService;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import org.springframework.util.CollectionUtils;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1,model,"firstName","asc",null);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") Integer pageNum
			,Model model, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir
			,@Param("keyword") String keyword) {
		Page<User> listByPage = userService.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> listUsers = listByPage.getContent();
		long totalElements = listByPage.getTotalElements();
		int totalPages = listByPage.getTotalPages();
		String reverseSortDir= "asc".equals(sortDir)?"desc":"asc";
		long startCount = (pageNum - 1)* UserService.USER_PER_PAGE  + 1;
		long endCount = startCount + UserService.USER_PER_PAGE - 1;
		if(endCount > totalElements)
			endCount=totalElements;
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("totalPages",totalPages );
		model.addAttribute("startCount", startCount);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("endCount", endCount);
		model.addAttribute("keyword", keyword);
		return "users/users";
	}
	

	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		List<Role> listRoles = roleService.listRoles();
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes attributes, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		System.out.println(user);
		System.out.println(multipartFile.getOriginalFilename());
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser=userService.save(user);
			Integer id = savedUser.getId();
			//fileName="User"+id;
			String uploadDir = "user-photos/"+id;
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		else{
			if(user.getPhotos().isEmpty())
				user.setPhotos(null);
			userService.save(user);
		}

		attributes.addFlashAttribute("message", "The user has been saved successfully !");
		return getURLRedirectedToAffectedUser(user);
	}

	private String getURLRedirectedToAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartOfEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {
		User userById = null;
		try {
			userById = userService.getById(id);
		} catch (UserNotFoundException e) {
			attribute.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
		model.addAttribute("user", userById);
		model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
		List<Role> listRoles = roleService.listRoles();
		model.addAttribute("listRoles", listRoles);
		return "users/user_form";

	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, Model model, RedirectAttributes attribute) {

		try {
			userService.delete(id);
			attribute.addFlashAttribute("message", "The User Id: " + id + " has been deleted successfully !");
		} catch (UserNotFoundException e) {
			attribute.addFlashAttribute("message", e.getMessage());

		}
		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{enabler}")
	public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("enabler") Boolean enabler,
			RedirectAttributes attribute) {
		userService.updateEnabledStatus(id, enabler);
		attribute.addFlashAttribute("message",
				"The User Id: " + id + " has been " + (enabler ? "enabled" : "disabled") + " successfully !");

		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(Model model,HttpServletResponse response) throws IOException
	{	
		 @SuppressWarnings("unchecked")
		List<User> attribute = (List<User>) model.getAttribute("listUsers");
		 if(CollectionUtils.isEmpty(attribute))
			 attribute= userService.listAll();
		  new UserCsvExporter().export(attribute,response);
	}
	@GetMapping("/users/export/excel")
	public void exportToExcel(Model model,HttpServletResponse response) throws IOException
	{	
		 @SuppressWarnings("unchecked")
		List<User> attribute = (List<User>) model.getAttribute("listUsers");
		 if(CollectionUtils.isEmpty(attribute))
			 attribute= userService.listAll();
		 new UserExcelExporter().export(attribute,response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(Model model,HttpServletResponse response) throws IOException
	{	
		 @SuppressWarnings("unchecked")
		List<User> attribute = (List<User>) model.getAttribute("listUsers");
		 if(CollectionUtils.isEmpty(attribute))
			 attribute= userService.listAll();
		 new UserPdfExporter().export(attribute,response);
	}

}
