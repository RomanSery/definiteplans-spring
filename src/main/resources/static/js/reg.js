"use strict";

definitePlansScripts.regPage = function () {
	var regPageValidator = $("#regForm").validate({
		submitHandler: function (form) {
			if (regPageValidator.numberOfInvalids() == 0) {
				form.submit();
			}
		},
		rules: {
			email: {
				required: true,
				email: true
			},
			password: {
				required: true,
				minlength: 8
			},
			displayName: {required: true, rangelength: [2, 15]},
			gender: {required: true},
			dob: {required: true},
			postalCode: {required: true}
		},
		messages: {
			email: "Please enter a valid email address",
			displayName: {
				required: "Please enter your first name",
				rangelength: "First name must be between 2 and 15 characters"
			},
			password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 8 characters long"
			},
			gender: "Please select your gender",
			dob: "Please enter your date of birth",
			postalCode: "Please entery your zip code"
		}
	});
};

$(document).ready(function() {
	definitePlansScripts.regPage();
});