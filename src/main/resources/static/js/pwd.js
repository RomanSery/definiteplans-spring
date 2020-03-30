"use strict";

definitePlansScripts.updatePassword = function () {
	let resetPasswordValidator = $("#resetPasswordForm").validate({
		submitHandler: function (form) {
			console.log(resetPasswordValidator.numberOfInvalids());
			//if (resetPasswordValidator.numberOfInvalids() == 0) {
			//	form.submit();
			//}
		},
		rules: {
			currpwd: {required: true},
			newpwd1: {
				required: true,
				minlength: 8
			},
			newpwd2: {
				required: true,
				minlength: 8,
				equalTo: "#newpwd1"
			}
		},
		messages: {
			currpwd: {
				required: "Please enter your current password."
			},
			newpwd1: {
				required: "Please enter your new password."
			},
			newpwd2: {
				required: "Please confirm your password.",
				passwordsMatch: "Please ensure your passwords match."
			}
		}
	});
};

$(document).ready(function() {
	definitePlansScripts.updatePassword();
});