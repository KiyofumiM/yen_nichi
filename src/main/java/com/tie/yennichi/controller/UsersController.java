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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.User;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.entity.User.Authority;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.UserRepository;

import java.io.IOException;
import java.security.Principal;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
* ユーザ情報の処理用controller群
*/
@Controller
public class UsersController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	/**
	* ユーザ情報登録画面にリンクする
	* @param  Model
	* @return ページアドレス /users/new
	* @throws なし
	*/
	@GetMapping(path = "/users/new")
	public String newUser(Model model) {
		model.addAttribute("form", new UserForm());
		return "users/new";
	}

	/**
	* ユーザ情報登録処理
	* @param  form : UserForm、BindingResult、Model,　locale
	* @return layouts/complete
	* @throws なし
	*/
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

	/**
	 * 参照したユーザ情報の詳細を取得して表示
	 * @param  Principal、Model
	 * @return ページアドレス /users/edit
	 * @throws なし
	 */
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

	/**
	* ユーザ情報の更新処理
	* @param  Principal、form : UserForm、BindingResult、Model、MultipartFile、RedirectAttributes、locale、id
	* @return redirect:/learning
	* @throws なし
	*/
	@RequestMapping(value = "/user/edit", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") UserForm form, BindingResult result,
			Model model, Locale locale, RedirectAttributes redirAttrs) {

		Authentication authentication = (Authentication) principal;
		UserInf userInf = (UserInf) authentication.getPrincipal();

		String name = form.getName();
		String email = form.getEmail();
		String password = form.getPassword();
		String passwordConfirmation = form.getPasswordConfirmation();

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("users.edit.flash.1", new String[] {}, locale));
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
		
		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("users.edit.flash.2", new String[] {}, locale));
		return "redirect:/contents";

	}
}