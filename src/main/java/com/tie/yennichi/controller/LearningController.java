package com.tie.yennichi.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.Learning;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.LearningForm;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.LearningRepository;

import org.springframework.context.MessageSource;

import com.tie.yennichi.entity.GoodLearning;
import com.tie.yennichi.form.GoodLearningForm;

import com.tie.yennichi.entity.FavoriteLearning;
import com.tie.yennichi.form.FavoriteLearningForm;

import com.tie.yennichi.entity.CommentLearning;
import com.tie.yennichi.form.CommentLearningForm;

import com.tie.yennichi.entity.GoodCommentLearning;
import com.tie.yennichi.form.GoodCommentLearningForm;

@Controller
public class LearningController {

	protected static Logger log = LoggerFactory.getLogger(LearningController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private LearningRepository repository;

	@Autowired
	private HttpServletRequest request;

	@Value("${image.local:false}")
	private String imageLocal;

	// 投稿処理
	@GetMapping(path = "/learning")
	public String index(Principal principal, Model model) throws IOException {
		
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		
		List<Learning> learning = (List<Learning>) repository.findByDeletedFalseOrderByUpdatedAtDesc();
		
		List<LearningForm> list = new ArrayList<>();
		
		for (Learning entity : learning) {
			LearningForm form = getLearning(user, entity);
			list.add(form);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("userId", user.getUserId());


		return "learning/index";
	}

	// 投稿内容を取得
	public LearningForm getLearning(UserInf user, Learning entity) throws FileNotFoundException, IOException {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Learning.class, LearningForm.class)
				.addMappings(mapper -> mapper.skip(LearningForm::setUser));

		modelMapper.typeMap(Learning.class, LearningForm.class)
				.addMappings(mapper -> mapper.skip(LearningForm::setFavorites));

		modelMapper.typeMap(Learning.class, LearningForm.class)
				.addMappings(mapper -> mapper.skip(LearningForm::setComments));
		modelMapper.typeMap(FavoriteLearning.class, FavoriteLearningForm.class)
				.addMappings(mapper -> mapper.skip(FavoriteLearningForm::setLearning));

		boolean isImageLocal = false;
		if (imageLocal != null) {
			isImageLocal = new Boolean(imageLocal);
		}
		LearningForm form = modelMapper.map(entity, LearningForm.class);

		if (isImageLocal) {
			try (InputStream is = new FileInputStream(new File(entity.getPath()));
					ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				byte[] indata = new byte[10240 * 16];
				int size;
				while ((size = is.read(indata, 0, indata.length)) > 0) {
					os.write(indata, 0, size);
				}
				StringBuilder data = new StringBuilder();
				data.append("data:");
				data.append(getMimeType(entity.getPath()));
				data.append(";base64,");

				data.append(new String(Base64Utils.encode(os.toByteArray()), "ASCII"));
				form.setImageData(data.toString());
			}
		}

		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);

		// 投稿に対する「いいね！」を取得してセット
		List<GoodLearningForm> goods = new ArrayList<GoodLearningForm>();
		for (GoodLearning favoriteLearningEntity : entity.getGoods()) {
			GoodLearningForm good_learning = modelMapper.map(favoriteLearningEntity, GoodLearningForm.class);
			goods.add(good_learning);
			if (user.getUserId().equals(favoriteLearningEntity.getUserId())) {
				form.setGood(good_learning);
			}
		}
		form.setGoods(goods);

		// 投稿に対する「お気に入り！」を取得してセット
		List<FavoriteLearningForm> favorites = new ArrayList<FavoriteLearningForm>();
		for (FavoriteLearning favoriteLearningEntity : entity.getFavorites()) {
			FavoriteLearningForm favorite_learning = modelMapper.map(favoriteLearningEntity,
					FavoriteLearningForm.class);
			favorites.add(favorite_learning);
			if (user.getUserId().equals(favoriteLearningEntity.getUserId())) {
				form.setFavorite(favorite_learning);
			}
		}
		form.setFavorites(favorites);

		//　投稿に対するコメントを取得してセット
		List<CommentLearningForm> comments = new ArrayList<CommentLearningForm>();
		for (CommentLearning commentLearningEntity : entity.getComments()) {
			CommentLearningForm comment = modelMapper.map(commentLearningEntity, CommentLearningForm.class);
			comments.add(comment);
			
			// 投稿されたコメントに対する「いいね！」の数を取得してセット
			List<GoodCommentLearningForm> goodComments = new ArrayList<GoodCommentLearningForm>();
		
			for (GoodCommentLearning goodCommentEntity : commentLearningEntity.getGoodComments()) {
				GoodCommentLearningForm goodComment = modelMapper.map(goodCommentEntity, GoodCommentLearningForm.class);

				if (user.getUserId().equals(goodCommentEntity.getUserId())) {
					
					comment.setGoodComment(goodComment);
				}
				goodComments.add(goodComment);
			}
			comment.setGoodComments(goodComments);
		}
		form.setComments(comments);
		
		return form;
	}

	private String getMimeType(String path) {
		String extension = FilenameUtils.getExtension(path);
		String mimeType = "image/";
		switch (extension) {
		case "jpg":
		case "jpeg":
			mimeType += "jpeg";
			break;
		case "png":
			mimeType += "png";
			break;
		case "gif":
			mimeType += "gif";
			break;
		}
		return mimeType;
	}

	// 新規投稿
	@GetMapping(path = "/learning/new")
	public String newTopic(Model model) {
		model.addAttribute("form", new LearningForm());
		return "learning/new";
	}

	// 投稿処理をリクエスト
	@RequestMapping(value = "/learning", method = RequestMethod.POST)
	public String create(Principal principal, @Validated @ModelAttribute("form") LearningForm form,
			BindingResult result, Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs,
			Locale locale) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("topics.create.flash.1", new String[] {}, locale));
			return "learning/new";
		}

		boolean isImageLocal = false;
		if (imageLocal != null) {
			isImageLocal = new Boolean(imageLocal);
		}

		Learning entity = new Learning();
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		entity.setUserId(user.getUserId());
		File destFile = null;
		if (isImageLocal) {
			destFile = saveImageLocal(image, entity);
			entity.setPath(destFile.getAbsolutePath());
		} else {
			entity.setPath("");
		}
		entity.setTitle(form.getTitle());
		entity.setDescription(form.getDescription());
		entity.setDeleted(false);
		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message",
				messageSource.getMessage("topics.create.flash.2", new String[] {}, locale));
		
		return "redirect:/learning";
	}

	private File saveImageLocal(MultipartFile image, Learning entity) throws IOException {
		File uploadDir = new File("/uploads");
		uploadDir.mkdir();

		String uploadsDir = "/uploads/";
		String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
		if (!new File(realPathToUploads).exists()) {
			new File(realPathToUploads).mkdir();
		}
		String fileName = image.getOriginalFilename();
		File destFile = new File(realPathToUploads, fileName);
		image.transferTo(destFile);

		return destFile;
	}

	// 登録情報を取得して表示
	@RequestMapping(value = "/learning/edit", method = RequestMethod.GET)
	public String edite(Principal principal, @RequestParam("learning_id") long learningId, Model model) {
		Learning entity = repository.findById(learningId);

		LearningForm learningForm = new LearningForm();
		learningForm.setId(entity.getId());

		boolean isImageLocal = false;
		if (imageLocal != null) {
			isImageLocal = new Boolean(imageLocal);
		}

		learningForm.setPath(entity.getPath());
		learningForm.setTitle(entity.getTitle());
		learningForm.setDescription(entity.getDescription());
		model.addAttribute("form", learningForm);
		return "learning/edit";
	}

	// 投稿内容を更新
	@RequestMapping(value = "/learning/update", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") LearningForm form,
			BindingResult result, Model model, Locale locale, HttpSession session, @RequestParam("id") long learningId,
			@RequestParam MultipartFile image, RedirectAttributes redirAttrs) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("topics.edit.flash.1", new String[] {}, locale));
			return "learning/edit";
		}

		// 更新処理
		Learning entity = repository.findById(learningId);

		File destFile = null;
		if (imageLocal != null) {
			destFile = saveImageLocal(image, entity);
			entity.setPath(destFile.getAbsolutePath());
		} else {
			entity.setPath("");
		}

		String path = form.getPath();
		String title = form.getTitle();
		String description = form.getDescription();

		entity.setTitle(title);
		entity.setDescription(description);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("topics.edit.flash.2", new String[] {}, locale));
		return "redirect:/learning";
	}

	// 投稿内容を削除する
	@RequestMapping(value = "/learning/delete", method = RequestMethod.GET)
	public String delete(Principal principal, Model model, Locale locale, HttpSession session,
			@RequestParam("learning_id") long learningId, RedirectAttributes redirAttrs) throws IOException {

		// 更新処理
		Learning entity = repository.findById(learningId);

		entity.setDeleted(true);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("topics.delete.flash.2", new String[] {}, locale));
		return "redirect:/learning";
	}
}
