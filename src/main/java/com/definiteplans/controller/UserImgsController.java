package com.definiteplans.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.dao.UserImageRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserImage;
import com.definiteplans.service.UserService;

@Controller
public class UserImgsController {
    private final UserService userService;
    private final UserImageRepository userImageRepository;

    public UserImgsController(UserService userService, UserImageRepository userImageRepository) {
        this.userService = userService;
        this.userImageRepository = userImageRepository;
    }

    @RequestMapping(value = "/profile/img/upload", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse uploadImg(@RequestParam(name = "op", required = false) String op,
                                                @RequestParam(name = "img_type", required = false) String imgType,
                                                @RequestParam(name = "mime_type", required = false) String mimeType,
                                                @RequestParam(name = "file_name", required = false) String fileName,
                                                @RequestParam(name = "img_url", required = false) String imgUrl,
                                                @RequestParam(name = "d", required = false) String d) {
        if (op != null && !op.isEmpty() && "save".equals(op)) {

            UserImage img = userImageRepository.findByUserIdAndTimestamp(userService.getCurrentUserId(), d);
            if(img == null) {
                img = new UserImage();
                img.setCreationDate(LocalDateTime.now());
            }

            img.setUserId(userService.getCurrentUserId());
            img.setFileName(fileName);
            if("thumb".equals(imgType)) {
                img.setThumbImgUrl(imgUrl);
            } else {
                img.setFullImgUrl(imgUrl);
            }
            img.setMimeType(mimeType);
            img.setTimestamp(d);
            userImageRepository.save(img);
        }

        return new AjaxResponse("OK", "done");
    }


    @GetMapping("/profile/img/delete/{imageId}")
    public @ResponseBody AjaxResponse deleteProfileImg(Model model, @PathVariable int imageId) {
        User user = userService.getCurrentUser();
        if(user == null) {
            return new AjaxResponse("ERR", "user not found");
        }

        Optional<UserImage> img = userImageRepository.findById(imageId);
        if (img.isEmpty()) {
            return new AjaxResponse("ERR", "img not found");
        }
        userImageRepository.delete(img.get());
        return new AjaxResponse("OK", "img deleted");
    }

    @GetMapping("/profile/img/set/{imageId}")
    public @ResponseBody AjaxResponse updateProfileImg(Model model, @PathVariable int imageId) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new AjaxResponse("ERR", "user not found");
        }

        Optional<UserImage> img = userImageRepository.findById(imageId);
        if (img.isEmpty()) {
            return new AjaxResponse("ERR", "img not found");
        }

        currUser.setThumbImgUrl(img.get().getThumbImgUrl());
        currUser.setFullImgUrl(img.get().getFullImgUrl());
        userService.saveUser(currUser);

        return new AjaxResponse("OK", "img deleted");
    }

}
