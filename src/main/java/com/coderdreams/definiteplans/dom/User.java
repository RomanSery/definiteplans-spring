package com.coderdreams.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;


@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID=1L;

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

	@Column(name = "fb_id")
	private String fbId;

	@Column(name = "google_sub_id")
	private String googleSubId;

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

	@Column(name = "num_no_shows")
	private String profilePicImg;

	@Column(name = "state")
	private String state;

	@Column(name = "city")
	private String city;

	@Column(name = "neighborhood")
	private String neighborhood;

	@Column(name = "postalCode")
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



	public User() {
		super();
	}

	public boolean hasPwd() {
		return !StringUtils.isBlank(password);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getGoogleSubId() {
		return googleSubId;
	}

	public void setGoogleSubId(String googleSubId) {
		this.googleSubId = googleSubId;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getProfilePicImg() {
		return profilePicImg;
	}

	public void setProfilePicImg(String profilePicImg) {
		this.profilePicImg = profilePicImg;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(int ethnicity) {
		this.ethnicity = ethnicity;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSmokes() {
		return smokes;
	}

	public void setSmokes(int smokes) {
		this.smokes = smokes;
	}

	public int getReligion() {
		return religion;
	}

	public void setReligion(int religion) {
		this.religion = religion;
	}

	public int getEducation() {
		return education;
	}

	public void setEducation(int education) {
		this.education = education;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getKids() {
		return kids;
	}

	public void setKids(int kids) {
		this.kids = kids;
	}

	public int getWantsKids() {
		return wantsKids;
	}

	public void setWantsKids(int wantsKids) {
		this.wantsKids = wantsKids;
	}

	public int getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(int maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public boolean isSendNotifications() {
		return sendNotifications;
	}

	public void setSendNotifications(boolean sendNotifications) {
		this.sendNotifications = sendNotifications;
	}

	public String getNotificationsEmail() {
		return notificationsEmail;
	}

	public void setNotificationsEmail(String notificationsEmail) {
		this.notificationsEmail = notificationsEmail;
	}

	public int getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(int ageMin) {
		this.ageMin = ageMin;
	}

	public int getAgeMax() {
		return ageMax;
	}

	public void setAgeMax(int ageMax) {
		this.ageMax = ageMax;
	}

	public int getNumNoShows() {
		return numNoShows;
	}

	public void setNumNoShows(int numNoShows) {
		this.numNoShows = numNoShows;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User that = (User) o;

		if (id != that.id) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id;
	}
}