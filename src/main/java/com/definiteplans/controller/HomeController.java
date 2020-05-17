package com.definiteplans.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.definiteplans.controller.model.ActionItem;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.DateStatus;
import com.definiteplans.service.DateService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;

@Controller
public class HomeController {
    private final UserService userService;
    private final DefiniteDateRepository definiteDateRepository;
    private final DateService dateService;
    private final UserRepository userRepository;

    public HomeController(UserService userService, DefiniteDateRepository definiteDateRepository, DateService dateService, UserRepository userRepository) {
        this.userService = userService;
        this.definiteDateRepository = definiteDateRepository;
        this.dateService = dateService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ModelAndView homePage(){
        User currUser = userService.getCurrentUser();

        ModelAndView m = new ModelAndView("home");

        List<DefiniteDate> upComingDates = definiteDateRepository.getMyUpcomingDates(currUser.getId());
        List<ActionItem> requiresAction = dateService.getRequiresMyAction(currUser, upComingDates);
        List<ActionItem> dateDetails = dateService.getUpComingDatesDetail(currUser, upComingDates);

        m.addObject("action_items", requiresAction);
        m.addObject("date_details", dateDetails);
        m.addObject("calendar_events", getCalendarEventsJSON(currUser, upComingDates));
        return m;
    }


    public String getCalendarEventsJSON(User currUser, List<DefiniteDate> upComingDates) {

        JSONArray dates = new JSONArray();
        for (DefiniteDate dd : upComingDates) {
            JSONObject obj = new JSONObject();

            try {
                int profileId = (currUser.getId() == dd.getOwnerUserId()) ? dd.getDateeUserId() : dd.getOwnerUserId();
                Optional<User> found = userRepository.findById(profileId);
                if(found.isEmpty()) {
                    continue;
                }
                String displayName = found.get().getDisplayName();
                String url = "/profiles/" + profileId;

                obj.put("title", "Date with " + displayName);
                obj.put("start", DateUtil.printISODateTime(dd.getDoingWhen()));
                obj.put("url", url);
                obj.put("backgroundColor", (dd.getDateStatusId() == DateStatus.APPROVED.getId()) ? "green" : "red");

                dates.put(obj);
            } catch (JSONException e) {

            }
        }
        return dates.toString();
    }
}
