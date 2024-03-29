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
import org.springframework.context.MessageSource;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tie.yennichi.entity.Event;
import com.tie.yennichi.entity.UserInf;
import com.tie.yennichi.form.EventForm;
import com.tie.yennichi.form.UserForm;
import com.tie.yennichi.repository.EventRepository;

import com.tie.yennichi.entity.GoodEvent;
import com.tie.yennichi.form.GoodEventForm;
import com.tie.yennichi.entity.FavoriteEvent;
import com.tie.yennichi.form.FavoriteEventForm;
import com.tie.yennichi.entity.CommentEvent;
import com.tie.yennichi.form.CommentEventForm;

import com.tie.yennichi.entity.GoodCommentEvent;
import com.tie.yennichi.form.GoodCommentEventForm;

/**
* イベントの共有の処理用controller群
*/
@Controller
public class EventsController {

	@Autowired
	private MessageSource messageSource;
	
    protected static Logger log = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventRepository repository;

    @Autowired
    private HttpServletRequest request;

    @Value("${image.local:false}")
    private String imageLocal;

	/**
	 * 投稿内容詳細の表示
	 * @param  entity : UserInf、entity : event
	 * @return ページアドレス /events/index
	 * @throws IOException
	 */
    @GetMapping(path = "/events")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();

        Iterable<Event> events = (List<Event>) repository.findByDeletedFalseOrderByUpdatedAtDesc();
        List<EventForm> list = new ArrayList<>();
        for (Event entity : events) {
            EventForm form = getEvent(user, entity);
            list.add(form);
        }
        model.addAttribute("list", list);
        model.addAttribute("userId", user.getUserId());

        return "events/index";
    }

	/**
	 * 各投稿内容の詳細を取得する
	 * @param  entity : UserInf、entity : Event
	 * @return EventForm
	 * @throws FileNotFoundException, IOException
	 */
    public EventForm getEvent(UserInf user, Event entity) throws FileNotFoundException, IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setUser));
        
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setGoodList));
        modelMapper.typeMap(GoodEvent.class, GoodEventForm.class).addMappings(mapper -> mapper.skip(GoodEventForm::setEvent));
        
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setFavoriteList));
        modelMapper.typeMap(FavoriteEvent.class, FavoriteEventForm.class).addMappings(mapper -> mapper.skip(FavoriteEventForm::setEvent));
        
		modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setCommentList));
        boolean isImageLocal = false;
        if (imageLocal != null) {
            isImageLocal = new Boolean(imageLocal);
        }
        EventForm form = modelMapper.map(entity, EventForm.class);

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
        List<GoodEventForm> goodList = new ArrayList<GoodEventForm>();
        
        for (GoodEvent goodEventEntity : entity.getGoodList()) {
        	GoodEventForm good = modelMapper.map(goodEventEntity, GoodEventForm.class);
        	goodList.add(good);
        	if (user.getUserId().equals(good.getUserId())) {
        		form.setGood(good);
        	}
        }

        form.setGoodList(goodList);
        
        // 投稿に対する「お気に入り！」を取得してセット
        List<FavoriteEventForm> favoriteList = new ArrayList<FavoriteEventForm>();
        for (FavoriteEvent favoriteEventEntity : entity.getFavoriteList()) {
        	FavoriteEventForm favorite = modelMapper.map(favoriteEventEntity, FavoriteEventForm.class);
        	favoriteList.add(favorite);
        	if (user.getUserId().equals(favorite.getUserId())) {
        		form.setFavorite(favorite);
        	}
        }

        form.setFavoriteList(favoriteList);
        
        // 投稿に対するコメントを取得してセット
        List<CommentEventForm> commentList = new ArrayList<CommentEventForm>();
		for (CommentEvent commentEventEntity : entity.getCommentList()) {
			CommentEventForm comment = modelMapper.map(commentEventEntity, CommentEventForm.class);
			commentList.add(comment);
			
			// 投稿されたコメントに対する「いいね！」の数を取得してセット
			List<GoodCommentEventForm> goodCommentList = new ArrayList<GoodCommentEventForm>();
		
			for (GoodCommentEvent goodCommentEntity : commentEventEntity.getGoodCommentList()) {
				GoodCommentEventForm goodComment = modelMapper.map(goodCommentEntity, GoodCommentEventForm.class);

				if (user.getUserId().equals(goodCommentEntity.getUserId())) {
					
					comment.setGoodComment(goodComment);
				}
				goodCommentList.add(goodComment);
			}
		}
		form.setCommentList(commentList);
		
        return form;
    }

	/**
	 * 投稿された画像のpathの処理
	 * @param  path
	 * @return String
	 * @throws なし
	 */
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

	/**
	* 新規投稿画面にリンクする
	* @param  Model
	* @return ページアドレス /events/new
	* @throws なし
	*/
    @GetMapping(path = "/events/new")
    public String newTopic(Model model) {
        model.addAttribute("form", new EventForm());
        return "events/new";
    }

	/**
	* 投稿処理
	* @param  Principal、form : EventForm、BindingResult、Model、MultipartFile、RedirectAttributes,　locale
	* @return redirect:/calendar/events
	* @throws IOException
	*/
    @PostMapping(path = "/event")
    public String create(Principal principal, @Validated @ModelAttribute("form") EventForm form, BindingResult result,
            Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
    	
    	if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("topics.create.flash.1", new String[] {}, locale));
            return "events/new";
        }

    	/*
        boolean isImageLocal = false;
        if (imageLocal != null) {
            isImageLocal = new Boolean(imageLocal);
        }
        */
    	boolean isImageLocal = true;

        Event entity = new Event();
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
        entity.setEvent_at(form.getEvent_at());
        entity.setTitle(form.getTitle());
        entity.setDescription(form.getDescription());
        entity.setDeleted(false);
        repository.saveAndFlush(entity);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("topics.create.flash.2", new String[] {}, locale));

        return "redirect:/calendar/events";
    }

	/**
	* 画像投稿処理
	* @param  MultipartFile、entity : event
	* @return destFile
	* @throws IOException
	*/
    private File saveImageLocal(MultipartFile image, Event entity) throws IOException {
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
    
	/**
	 * 参照した投稿内容の詳細を取得して表示
	 * @param  Principal、event_id、Model
	 * @return ページアドレス /events/edit
	 * @throws IOException
	 */
	@RequestMapping(value = "/event/edit", method = RequestMethod.GET)
	public String edit(Principal principal, @RequestParam("event_id") long eventId, Model model)
			 {
		Event entity = repository.findById(eventId);

		EventForm eventForm = new EventForm();
		eventForm.setId(entity.getId());

		boolean isImageLocal = false;
		if (imageLocal != null) {
			isImageLocal = new Boolean(imageLocal);
		}

		eventForm.setPath(entity.getPath());
		eventForm.setEvent_at(entity.getEvent_at());
		eventForm.setTitle(entity.getTitle());
		eventForm.setDescription(entity.getDescription());
		model.addAttribute("form", eventForm);
		return "events/edit";
	}

	/**
	* 投稿内容の更新処理
	* @param  Principal、form : EventForm、BindingResult、Model、MultipartFile、RedirectAttributes、locale、HttpSession、id
	* @return redirect:/learning
	* @throws IOException
	*/
	@RequestMapping(value = "/event/update", method = RequestMethod.POST)
	public String update(Principal principal, @Validated @ModelAttribute("form") EventForm form,
			BindingResult result, Model model, Locale locale, @RequestParam("id") long eventId,
			@RequestParam MultipartFile image, RedirectAttributes redirAttrs) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", messageSource.getMessage("topics.edit.flash.1", new String[] {}, locale));
			return "events/edit";
		}

		// 更新処理
		Event entity = repository.findById(eventId);

		File destFile = null;
		if (imageLocal != null) {
			destFile = saveImageLocal(image, entity);
			entity.setPath(destFile.getAbsolutePath());
		} else {
			entity.setPath("");
		}

		String path = form.getPath();
		String eventAt = form.getEvent_at();
		String title = form.getTitle();
		String description = form.getDescription();

		entity.setEvent_at(eventAt);
		entity.setTitle(title);
		entity.setDescription(description);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("topics.edit.flash.2", new String[] {}, locale));
		return "redirect:/events";
	}

	/**
	* 投稿内容の論理削除処理
	* @param  Principal、form : EventForm、BindingResult、Model、MultipartFile、RedirectAttributes、locale、HttpSession、id
	* @return redirect:/events
	* @throws IOException
	*/
	@RequestMapping(value = "/event/delete", method = RequestMethod.GET)
	public String delete(Principal principal, Model model, Locale locale, HttpSession session, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs
			) throws IOException {

		// 更新処理
		Event entity = repository.findById(eventId);

		entity.setDeleted(true);

		repository.saveAndFlush(entity);

		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", messageSource.getMessage("topics.delete.flash", new String[] {}, locale));
		return "redirect:/events";
	}
}