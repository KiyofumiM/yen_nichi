package com.tie.yennichi.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
// import java.util.ArrayList;
// import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.Contact;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.ContactForm;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.ContactRepository;

import org.thymeleaf.context.Context;
import com.tie.yennichi.service.SendMailService;

@Controller
public class ContactsController {

	protected static Logger log = LoggerFactory.getLogger(BoardsController.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ContactRepository repository;

	@Autowired
	private SendMailService sendMailService;
	
	@GetMapping(path = "/contact/new")
	public String newBoard(Model model) {
		model.addAttribute("form", new ContactForm());
		return "contact/new";
	}

	public ContactForm getContact(UserInf user, Contact entity) throws FileNotFoundException, IOException {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Contact.class, ContactForm.class).addMappings(mapper -> mapper.skip(ContactForm::setUser));
		
		ContactForm form = modelMapper.map(entity, ContactForm.class);
		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);
		
		return form;
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String create(Principal principal, @Validated @ModelAttribute("form") ContactForm form, BindingResult result,
			Model model, RedirectAttributes redirAttrs) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "送信に失敗しました");
			return "contact/new";
		}

		Contact entity = new Contact();
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		entity.setUserId(user.getUserId());
		entity.setTitle(form.getTitle());
		entity.setDescription(form.getDescription());
		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", "送信に成功しました");

		// post E-mail
		Context context = new Context();
		context.setVariable("title", entity.getTitle());
		context.setVariable("name", user.getName());
		context.setVariable("mailadress", user.getUsername());
		context.setVariable("description", entity.getDescription());
		
		sendMailService.sendMail(context);
		
		
		return "contact/success";
	}

}
