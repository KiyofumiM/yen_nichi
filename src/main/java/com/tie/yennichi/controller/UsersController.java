package com.tie.yennichi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tie.yennichi.entity.User;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.entity.User.Authority;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.UserRepository;

import java.security.Principal;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Controller
public class UsersController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	// ユーザ登録画面にリンク
	@GetMapping(path = "/users/new")
	public String newUser(Model model) {
		model.addAttribute("form", new UserForm());
		return "users/new";
	}

	// 新規登録
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String create(@Validated @ModelAttribute("form") UserForm form, BindingResult result, Model model,
			Locale locale) {
		String name = form.getName();
		String email = form.getEmail();
		String password = form.getPassword();
		String passwordConfirmation = form.getPasswordConfirmation();

		if (repository.findByUsername(email) != null) {
			FieldError fieldError = new FieldError(result.getObjectName(), "email",
					messageSource.getMessage("users.create.error.1", new String[] {}, locale));
			result.addError(fieldError);
		}
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("users.create.flash.1", new String[] {}, locale));
			return "users/new";
		}

		User entity = new User(email, name, passwordEncoder.encode(password), Authority.ROLE_USER);
		repository.saveAndFlush(entity);

		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", messageSource.getMessage("users.create.flash.2", new String[] {}, locale));
		return "layouts/complete";
	}

	// ユーザ情報を取得して表示
	@RequestMapping(value = "/user/edit", method = RequestMethod.GET)
	public String edite(Principal principal, Model model) {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();

		UserForm userForm = new UserForm();
		userForm.setId(user.getUserId());
		userForm.setName(user.getName());
		userForm.setEmail(user.getUsername());
		model.addAttribute("form", userForm);
		return "users/edit";
	}

	// ユーザ情報を更新
	@RequestMapping(value = "/user/edit", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") UserForm form, BindingResult result,
			Model model, Locale locale, HttpSession session) {

		Authentication authentication = (Authentication) principal;
		UserInf userInf = (UserInf) authentication.getPrincipal();

		String name = form.getName();
		String email = form.getEmail();
		String password = form.getPassword();
		String passwordConfirmation = form.getPasswordConfirmation();

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("users.create.flash.1", new String[] {}, locale));
			return "users/edit";
		}

		// 更新処理
		User user = repository.findByUsername(userInf.getUsername());
		user.setName(name);
		user.setUsername(email);
		user.setPassword(passwordEncoder.encode(password));

		repository.saveAndFlush(user);
		// セッション内の認証情報を更新
		authentication = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(),
				authentication.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", messageSource.getMessage("users.edit.flash.2", new String[] {}, locale));
		return "redirect:/contents";

	}
}