package com.definiteplans.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.controller.model.DateFeedback;
import com.definiteplans.controller.model.DateProposal;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.UserImageRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.service.ChatService;
import com.definiteplans.service.DateService;
import com.definiteplans.service.EnumValueService;
import com.definiteplans.service.UserService;

@Controller
public class ViewProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EnumValueService enumValueService;
    private final DateService dateService;
    private final DefiniteDateRepository definiteDateRepository;
    private final UserImageRepository userImageRepository;
    private final ChatService chatService;

    public ViewProfileController(UserService userService, UserRepository userRepository, EnumValueService enumValueService, DateService dateService, DefiniteDateRepository definiteDateRepository, UserImageRepository userImageRepository, ChatService chatService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.enumValueService = enumValueService;
        this.dateService = dateService;
        this.definiteDateRepository = definiteDateRepository;
        this.userImageRepository = userImageRepository;
        this.chatService = chatService;
    }

    @GetMapping("/profiles/")
    public ModelAndView viewProfile() {
        return new ModelAndView(new RedirectView("/browse"));
    }


    @GetMapping("/profiles/{userId}")
    public ModelAndView viewProfile(@PathVariable("userId") Integer userId) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }
        Optional<User> found = userRepository.findById(userId);
        if(found.isEmpty()) {
            return new ModelAndView(new RedirectView("/browse"));
        }
        User profile = found.get();
        if(!userService.canViewProfile(currUser, profile)) {
            return new ModelAndView(new RedirectView("/browse"));
        }


        boolean isViewingSelf = currUser.getId() == profile.getId();

        ModelAndView m = new ModelAndView("view_profile");
        m.addObject("title", "View Profile - " + profile.getDisplayName());
        m.addObject("isViewingSelf", isViewingSelf);
        m.addObject("loc", UserService.getAddrDesc(profile));
        m.addObject("lastOnline", UserService.getLastOnline(profile));
        m.addObject("isOnline", UserService.isOnlineNow(profile));
        m.addObject("profileThumbImg", userService.getProfileImg(profile, true));
        m.addObject("profileFullImg", userService.getProfileImg(profile, false));
        m.addObject("age", UserService.getProfileAge(profile));
        m.addObject("gender", userService.getProfileVal(profile.getGender()));
        m.addObject("height", userService.getProfileVal(profile.getHeight()));
        m.addObject("ethnicity", userService.getProfileVal(profile.getEthnicity()));
        m.addObject("maritalStatus", userService.getProfileVal(profile.getMaritalStatus()));
        m.addObject("kids", userService.getProfileVal(profile.getKids()));
        m.addObject("wantsKids", userService.getProfileVal(profile.getWantsKids()));
        m.addObject("languages", userService.getLanguages(profile));
        m.addObject("religion", userService.getProfileVal(profile.getReligion()));
        m.addObject("education", userService.getProfileVal(profile.getEducation()));
        m.addObject("income", userService.getProfileVal(profile.getIncome()));
        m.addObject("smokes", userService.getProfileVal(profile.getSmokes()));
        m.addObject("aboutMe", profile.getAboutMePretty());
        m.addObject("interests", profile.getInterestsPretty());
        m.addObject("user_pics", userImageRepository.findByUserId(profile.getId()));
        m.addObject("chat_thread", chatService.getChatMsgs(currUser, profile));
        m.addObject("num_remaining_msgs", chatService.getNumMsgsRemaining(currUser, profile));
        setDateAttributes(currUser, profile, m.getModelMap());
        return m;
    }


    @GetMapping("/profiles/block/{userId}")
    public @ResponseBody AjaxResponse blockUser(Model model, @PathVariable int userId) {
        if(!userService.blockUser(userId)) {
            return AjaxResponse.error(List.of("Invalid request"));
        }
        return AjaxResponse.success("Blocked");
    }


    @RequestMapping("/refresh-make-plans/{userId}")
    public String refreshMakePlans(ModelMap m, @PathVariable("userId") Integer userId) {
        User currUser = userService.getCurrentUser();
        Optional<User> found = userRepository.findById(userId);
        if(currUser == null || found.isEmpty()) {
            return "";
        }
        User profile = found.get();
        setDateAttributes(currUser, profile, m);
        return "view_profile/date :: makePlans";
    }


    private void setDateAttributes(User currUser, User profile, ModelMap m) {

        m.addAttribute("profile", profile);
        m.addAttribute("otherPersonName", profile.getDisplayName());

        DefiniteDate activeDate = definiteDateRepository.getActiveDate(currUser.getId(), profile.getId());
        if(activeDate == null) {
            activeDate = dateService.createNew(currUser, profile);
        }

        m.addAttribute("date", activeDate != null ? new DateProposal(activeDate) : new DateProposal());
        m.addAttribute("has_active_date", activeDate != null && activeDate.getId() > 0);
        m.addAttribute("feedback", new DateFeedback(activeDate != null ? activeDate.getId() : 0));

        dateService.setDateAttributes(m, currUser, profile, activeDate);
    }
}
