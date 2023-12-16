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

@Controller
public class EventsController {

    protected static Logger log = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventRepository repository;

    @Autowired
    private HttpServletRequest request;

    @Value("${image.local:false}")
    private String imageLocal;

    @GetMapping(path = "/events")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();

        Iterable<Event> events = (List<Event>) repository.findAllByOrderByUpdatedAtDesc();
        List<EventForm> list = new ArrayList<>();
        for (Event entity : events) {
            EventForm form = getEvent(user, entity);
            list.add(form);
        }
        model.addAttribute("list", list);

        return "events/index";
    }

    public EventForm getEvent(UserInf user, Event entity) throws FileNotFoundException, IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setUser));

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

    @GetMapping(path = "/events/new")
    public String newTopic(Model model) {
        model.addAttribute("form", new EventForm());
        return "events/new";
    }

    @PostMapping(path = "/event")
    public String create(Principal principal, @Validated @ModelAttribute("form") EventForm form, BindingResult result,
            Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs)
            throws IOException {
    	
    	if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "投稿に失敗しました。");
            return "events/new";
        }

        boolean isImageLocal = false;
        if (imageLocal != null) {
            isImageLocal = new Boolean(imageLocal);
        }

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
        repository.saveAndFlush(entity);
        
        System.out.println("開催日入力テスト");
        System.out.println(entity);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "投稿に成功しました。");

        return "redirect:/calendars";
    }

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

}
