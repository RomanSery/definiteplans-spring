package com.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.definiteplans.dom.enumerations.State;
import com.definiteplans.util.DateUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "pwd", length = 200)
    private String password;

    @Column(name = "user_status_id")
    private int userStatus;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "interests")
    private String interests;

    @Column(name = "gender")
    private int gender;

    @Column(name = "languages")
    private String languages;

    @Column(name = "full_img_url")
    private String fullImgUrl;

    @Column(name = "thumb_img_url")
    private String thumbImgUrl;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private int country;

    @Column(name = "ethnicity")
    private int ethnicity;

    @Column(name = "height")
    private int height;

    @Column(name = "smokes")
    private int smokes;

    @Column(name = "religion")
    private int religion;

    @Column(name = "education")
    private int education;

    @Column(name = "income")
    private int income;

    @Column(name = "kids")
    private int kids;

    @Column(name = "wants_kids")
    private int wantsKids;

    @Column(name = "marital_status")
    private int maritalStatus;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "send_notifications")
    private boolean sendNotifications;

    @Column(name = "notifications_email")
    private String notificationsEmail;

    @Column(name = "age_min")
    private int ageMin;

    @Column(name = "age_max")
    private int ageMax;

    @Column(name = "num_no_shows")
    private int numNoShows;

    @Column(name = "search_prefs")
    private String searchPrefs;

    public User() {
        super();
    }

    public boolean hasPwd() {
        return !StringUtils.isBlank(password);
    }

    public boolean hasEmailAndPwdLogin() {
        return (password != null && password.length() > 0);
    }

    public String getAboutMePretty() {
        if (this.aboutMe != null) {
            return this.aboutMe.replace("\r\n", "<br>");
        }
        return "";
    }

    public String getInterestsPretty() {
        if (this.interests != null) {
            return this.interests.replace("\r\n", "<br>");
        }
        return "";
    }

    public Set<Integer> getLanguageIds() {
        if(StringUtils.isBlank(languages)) {
            return Collections.emptySet();
        }
        return Arrays.stream(languages.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
    }




    public boolean hasProfilePic() {
        return (this.thumbImgUrl != null && this.thumbImgUrl.length() > 0);
    }


    public boolean isCompleteProfile() {
        return dob != null && DateUtil.getAge(dob) >= 18 &&
                gender > 0 && hasProfilePic() && state != null && State.valueOfAbbreviation(state) != null && !StringUtils.isBlank(postalCode);
    }

    public State getSinglePrefState(String name) {
//        JSONObject obj = getSinglePrefObj(name);
//        try {
//            return (obj != null) ? State.valueOfAbbreviation(obj.getString("state")) : null;
//        } catch (JSONException e) {
//            return null;
//        }
        return null;
    }

    public EnumValue getSinglePrefEnumVal(String name) {
//        JSONArray arr = getSinglePref(name);
//        try {
//            return (arr != null && arr.length() > 0) ? new EnumValue(arr.getInt(0), "") : null;
//        } catch (JSONException e) {
//            return null;
//        }
        return null;
    }

    public Integer getSinglePrefIntVal(String name) {
//        JSONArray arr = getSinglePref(name);
//        try {
//            return (arr != null && arr.length() > 0) ? Integer.valueOf(arr.getInt(0)) : null;
//        } catch (JSONException e) {
//            return null;
//        }
        return null;
    }


    public List<EnumValue> getMultiPref(String name) {
        //JSONArray arr = getSinglePref(name);
        ArrayList<EnumValue> lst = new ArrayList<>();
//        if (arr == null) return lst;
//        for (int i = 0; i < arr.length(); i++) {
//            try {
//                lst.add(new EnumValue(arr.getInt(i), ""));
//            } catch (JSONException e) {
//            }
//        }

        return lst;
    }

    private JSONObject getSinglePrefObj(String name) {
        if (getSearchPrefs() == null || getSearchPrefs().length() == 0) return null;

//        try {
//            return new JSONObject(getSearchPrefs());
//        } catch (JSONException e) {
//            logger.error("FAILED prefilling search prefs", e);
//
//            return null;
//        }
        return null;
    }

    private JSONArray getSinglePref(String name) {
//        if (getSearchPrefs() == null || getSearchPrefs().length() == 0) return null;
//
//        try {
//            JSONObject jsonObj = new JSONObject(getSearchPrefs());
//            return jsonObj.has(name) ? jsonObj.getJSONArray(name) : null;
//        } catch (JSONException e) {
//            logger.error("FAILED prefilling search prefs", e);
//
//            return null;
//        }
        return null;
    }

}
