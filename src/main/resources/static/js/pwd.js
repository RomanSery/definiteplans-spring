"use strict";

definitePlansScripts.updatePassword = function () {
	let resetPasswordValidator = $("#resetPasswordForm").validate({
		submitHandler: function (form) {
			if (resetPasswordValidator.numberOfInvalids() == 0) {
				form.submit();
			}
		},
		rules: {
			currPwdField: {required: true},
			newPwd1Field: {
				required: true,
				minlength: 8
			},
			newPwd2Field: {
				required: true,
				minlength: 8,
				equalTo: "#password"
			}
		},
		messages: {
			currPwdField: {
				required: "Please enter your current password."
			},
			newPwd1Field: {
				required: "Please enter your new password."
			},
			newPwd2Field: {
				required: "Please confirm your password.",
				passwordsMatch: "Please ensure your passwords match."
			}
		}
	});
};

$(document).ready(function() {
	definitePlansScripts.updatePassword();
});