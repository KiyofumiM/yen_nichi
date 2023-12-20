package com.tie.yennichi.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

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

import com.tie.yennichi.entity.Board;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.BoardForm;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.BoardRepository;

import java.text.SimpleDateFormat;

@Controller
public class BoardsController {

	protected static Logger log = LoggerFactory.getLogger(BoardsController.class);

	@Autowired
	private ModelMapper modelMapper;

	// @Autowired
	// private HttpServletRequest request;

	@Autowired
	private BoardRepository repository;

	@GetMapping(path = "/board")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();

        Iterable<Board> board = repository.findAllByOrderByUpdatedAtDesc();
        List<BoardForm> list = new ArrayList<>();
        for (Board entity : board) {
            BoardForm form = getBoard(user, entity);
            list.add(form);
        }
        model.addAttribute("list", list);

        return "board/index";
    }
	
	@GetMapping(path = "/board/new")
	public String newBoard(Model model) {
		model.addAttribute("form", new BoardForm());
		return "board/new";
	}

	public BoardForm getBoard(UserInf user, Board entity) throws FileNotFoundException, IOException {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Board.class, BoardForm.class).addMappings(mapper -> mapper.skip(BoardForm::setUser));

		BoardForm form = modelMapper.map(entity, BoardForm.class);
		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);

		return form;
	}

	@RequestMapping(value = "/board", method = RequestMethod.POST)
	public String create(Principal principal, @Validated @ModelAttribute("form") BoardForm form, BindingResult result,
			Model model, RedirectAttributes redirAttrs) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "投稿に失敗しました");
			return "board/new";
		}

		Board entity = new Board();
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		entity.setUserId(user.getUserId());
		entity.setTitle(form.getTitle());
		entity.setDescription(form.getDescription());
		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", "投稿に成功しました");

		return "redirect:/board";
	}

}