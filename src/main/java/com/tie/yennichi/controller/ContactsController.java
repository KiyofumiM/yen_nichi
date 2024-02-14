package com.tie.yennichi.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;

import java.util.Locale;
import org.springframework.context.MessageSource;

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

/**
* 問い合わせの処理用controller群
*/
@Controller
public class ContactsController {

	@Autowired
	private MessageSource messageSource;
	
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

	/**
	 * 各投稿内容の詳細を取得する
	 * @param  entity : UserInf、entity : Contact
	 * @return LearningForm
	 * @throws FileNotFoundException, IOException
	 */
	public ContactForm getContact(UserInf user, Contact entity) throws FileNotFoundException, IOException {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Contact.class, ContactForm.class).addMappings(mapper -> mapper.skip(ContactForm::setUser));
		
		ContactForm form = modelMapper.map(entity, ContactForm.class);
		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);
		
		return form;
	}

	/**
	* 問い合わせ内容投稿画面にリンクする
	* @param  Model
	* @return ページアドレス /contact/new
	* @throws なし
	*/
	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String create(Principal principal, @Validated @ModelAttribute("form") ContactForm form, BindingResult result,
			Model model, RedirectAttributes redirAttrs, Locale locale) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("contact.create.flash.1", new String[] {}, locale));
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
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("contact.create.flash.2", new String[] {}, locale));

		// post E-mail
		Context context = new Context();
		context.setVariable("title", entity.getTitle());
		context.setVariable("name", user.getName());
		context.setVariable("mailadress", user.getUsername());
		context.setVariable("description", entity.getDescription());
		
		sendMailService.sendMail(context);
		
		
		return "contact/success";
	}
	
	/**
	* お問い合わせ投稿成功画面にリンクする
	* @param  なし
	* @return ページアドレス /contact/success
	* @throws なし
	*/
    @RequestMapping("contact/success")
    public String close() {
        return "contact/success";
    }

}
