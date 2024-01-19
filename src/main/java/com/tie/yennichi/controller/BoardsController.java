package com.tie.yennichi.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import java.util.Locale;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.Board;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.BoardForm;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.BoardRepository;

import com.tie.yennichi.entity.GoodBoard;
import com.tie.yennichi.form.GoodBoardForm;

import com.tie.yennichi.entity.CommentBoard;
import com.tie.yennichi.form.CommentBoardForm;


import com.tie.yennichi.entity.GoodCommentBoard;
import com.tie.yennichi.form.GoodCommentBoardForm;

@Controller
public class BoardsController {

	@Autowired
	private MessageSource messageSource;
	
	protected static Logger log = LoggerFactory.getLogger(BoardsController.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BoardRepository repository;

	@GetMapping(path = "/board")
	public String index(Principal principal, Model model) throws IOException {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();

		Iterable<Board> board = repository.findByDeletedFalseOrderByUpdatedAtDesc();
		List<BoardForm> list = new ArrayList<>();
		for (Board entity : board) {
			BoardForm form = getBoard(user, entity);
			list.add(form);
		}
		model.addAttribute("list", list);
		model.addAttribute("userId", user.getUserId());

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

		modelMapper.typeMap(Board.class, BoardForm.class).addMappings(mapper -> mapper.skip(BoardForm::setGoods));
		modelMapper.typeMap(GoodBoard.class, GoodBoardForm.class)
				.addMappings(mapper -> mapper.skip(GoodBoardForm::setBoard));

		modelMapper.typeMap(Board.class, BoardForm.class).addMappings(mapper -> mapper.skip(BoardForm::setComments));
		
		BoardForm form = modelMapper.map(entity, BoardForm.class);
		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);

		List<GoodBoardForm> goods = new ArrayList<GoodBoardForm>();

		for (GoodBoard goodBoardEntity : entity.getGoods()) {
			GoodBoardForm good = modelMapper.map(goodBoardEntity, GoodBoardForm.class);
			goods.add(good);
			if (user.getUserId().equals(good.getUserId())) {
				form.setGood(good);
			}
		}

		form.setGoods(goods);
		
		List<CommentBoardForm> comments = new ArrayList<CommentBoardForm>();
		for (CommentBoard commentBoardEntity : entity.getComments()) {
			CommentBoardForm comment = modelMapper.map(commentBoardEntity, CommentBoardForm.class);
			comments.add(comment);		

			// 投稿されたコメントに対する「いいね！」の数を取得してセット
			List<GoodCommentBoardForm> goodComments = new ArrayList<GoodCommentBoardForm>();
		
			for (GoodCommentBoard goodCommentEntity : commentBoardEntity.getGoodComments()) {
				GoodCommentBoardForm goodComment = modelMapper.map(goodCommentEntity, GoodCommentBoardForm.class);

				if (user.getUserId().equals(goodCommentEntity.getUserId())) {
					
					comment.setGoodComment(goodComment);
				}
				goodComments.add(goodComment);
			}
		}
		form.setComments(comments);
		
		return form;
	}

	@RequestMapping(value = "/board", method = RequestMethod.POST)
	public String create(Principal principal, @Validated @ModelAttribute("form") BoardForm form, BindingResult result,
			Model model, RedirectAttributes redirAttrs, Locale locale) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("topics.create.flash.1", new String[] {}, locale));
			return "board/new";
		}

		Board entity = new Board();
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		entity.setUserId(user.getUserId());
		entity.setTitle(form.getTitle());
		entity.setDescription(form.getDescription());
		entity.setDeleted(false);
		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("topics.create.flash.2", new String[] {}, locale));

		return "redirect:/board";
	}
	
	// 登録情報を取得して表示
	@RequestMapping(value = "/board/edit", method = RequestMethod.GET)
	public String edite(Principal principal, @RequestParam("board_id") long boardId, Model model)
			 {
		Board entity = repository.findById(boardId);

		BoardForm boardForm = new BoardForm();
		boardForm.setId(entity.getId());

		boardForm.setTitle(entity.getTitle());
		boardForm.setDescription(entity.getDescription());
		model.addAttribute("form", boardForm);
		return "board/edit";
	}

	@RequestMapping(value = "/board/update", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") BoardForm form,
			BindingResult result, Model model, Locale locale, HttpSession session, @RequestParam("id") long boardId)
			 throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("topics.edit.flash.1", new String[] {}, locale));
			return "board/edit";
		}

		// 更新処理
		Board entity = repository.findById(boardId);

		String title = form.getTitle();
		String description = form.getDescription();

		entity.setTitle(title);
		entity.setDescription(description);

		repository.saveAndFlush(entity);

		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", messageSource.getMessage("topics.edit.flash.2", new String[] {}, locale));
		return "redirect:/board";
	}

	@RequestMapping(value = "/board/delete", method = RequestMethod.GET)
	public String delete(Principal principal, Model model, Locale locale, HttpSession session, @RequestParam("board_id") long boardId
			) throws IOException {

		// 更新処理
		Board entity = repository.findById(boardId);

		entity.setDeleted(true);

		repository.saveAndFlush(entity);

		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", messageSource.getMessage("topics.delete.flash.2", new String[] {}, locale));
		return "redirect:/board";
	}
}