package com.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasEmailAndPwdLogin() {
        return (getPassword() != null && getPassword().length() > 0);
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getPostalCode() {
        return (this.postalCode == null) ? "" : this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAboutMePretty() {
        if (this.aboutMe != null) {
            return this.aboutMe.replace("\r\n", "<br>");
        }
        return "";
    }

    public String getInterests() {
        return this.interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getInterestsPretty() {
        if (this.interests != null) {
            return this.interests.replace("\r\n", "<br>");
        }
        return "";
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLanguages() {
        return this.languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }


    public boolean hasProfilePic() {
        return (this.thumbImgUrl != null && this.thumbImgUrl.length() > 0);
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return this.neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getCountry() {
        return this.country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getEthnicity() {
        return this.ethnicity;
    }

    public void setEthnicity(int ethnicity) {
        this.ethnicity = ethnicity;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSmokes() {
        return this.smokes;
    }

    public void setSmokes(int smokes) {
        this.smokes = smokes;
    }

    public int getReligion() {
        return this.religion;
    }

    public void setReligion(int religion) {
        this.religion = religion;
    }

    public int getEducation() {
        return this.education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getIncome() {
        return this.income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getKids() {
        return this.kids;
    }

    public void setKids(int kids) {
        this.kids = kids;
    }

    public int getWantsKids() {
        return this.wantsKids;
    }

    public void setWantsKids(int wantsKids) {
        this.wantsKids = wantsKids;
    }

    public int getMaritalStatus() {
        return this.maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }


    public LocalDateTime getLastLoginDate() {
        return this.lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }


    public boolean isCompleteProfile() {
        return dob != null && DateUtil.getAge(dob) >= 18 &&
                gender > 0 && hasProfilePic() && state != null && State.valueOfAbbreviation(state) != null && getPostalCode() != null && getPostalCode().length() > 0;
    }


    public boolean isSendNotifications() {
        return this.sendNotifications;
    }

    public void setSendNotifications(boolean sendNotifications) {
        this.sendNotifications = sendNotifications;
    }

    public String getNotificationsEmail() {
        return this.notificationsEmail;
    }

    public void setNotificationsEmail(String notificationsEmail) {
        this.notificationsEmail = notificationsEmail;
    }

    public int getAgeMin() {
        return this.ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return this.ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public String getSearchPrefs() {
        return this.searchPrefs;
    }

    public void setSearchPrefs(String searchPrefs) {
        this.searchPrefs = searchPrefs;
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

    public int getNumNoShows() {
        return this.numNoShows;
    }

    public void setNumNoShows(int numNoShows) {
        this.numNoShows = numNoShows;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getFullImgUrl() {
        return fullImgUrl;
    }

    public void setFullImgUrl(String fullImgUrl) {
        this.fullImgUrl = fullImgUrl;
    }

    public String getThumbImgUrl() {
        return thumbImgUrl;
    }

    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }
}
