package com.shopme.admin.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.Utils.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService service;
	
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser
			,Model model)
	{
		String email =  loggedUser.getUsername();
		 User listUserByEmail = service.listUserByEmail(email);
		 model.addAttribute("user",listUserByEmail);
		 return "users/account_form";
	}
	

	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes attributes,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		System.out.println(user);
		
		System.out.println(multipartFile.getOriginalFilename());
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser=service.updateAccount(user);
			Integer id = savedUser.getId();
			//fileName="User"+id;
			String uploadDir = "user-photos/"+id;
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		else{
			if(user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.updateAccount(user);
		}
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		attributes.addFlashAttribute("message", "Your Account details have been updated successfully !");
		return "redirect:/account";
	}

	
	
}
