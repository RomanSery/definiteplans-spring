"use strict";

definitePlansScripts.loginPage = function () {
	let myLoginValidator = $("#loginForm").validate({
		submitHandler: function (form) {
			if (myLoginValidator.numberOfInvalids() == 0) {
				form.submit();
			}
		},
		rules: {
			loginemail: {required: true, email: true},
			loginpassword: {required: true}
		},
		messages: {
			loginemail: {
				required: "Please enter your email address",
				email: "Please enter a valid email address"
			},
			loginpassword: "Please enter your password"
		}
	});
};

$(document).ready(function() {
	definitePlansScripts.loginPage();
});