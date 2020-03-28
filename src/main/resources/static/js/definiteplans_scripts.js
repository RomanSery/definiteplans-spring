"use strict";

let definitePlansScripts = {
	initClientSideScripts: function () {

		definitePlansScripts.initSelect2();
		definitePlansScripts.forgotPasswordPage();
		definitePlansScripts.validateEmailPage();
	},

	initSelect2: function () {
		$("select.doit-select2").select2({
			placeholder: 'Select one',
			allowClear: true
		});

		$("select.doit-select2-noclear").select2({
			placeholder: 'Select one',
			allowClear: false
		});

	},

	validateEmailPage: function () {
		$('.resend-confirm-email').click(function () {
			$.ajax({
				type: "POST",
				url: DOIT.ajax_base_url + '/emailpwdlogin?op=resendprimary',
				data: 'q=' + DOIT.validation_string,
				success: function (data) {
					$('.resend-confirm-email').replaceWith("Validation link emailed!");
				}
			});
		});
	},



	forgotPasswordPage: function() {
		var forgotPwdValidator = $("#passwordForm").validate({
			submitHandler: function (form) {
				if (forgotPwdValidator.numberOfInvalids() == 0) {
					form.submit();
				}
			},
			rules: {
				email: {required: true, email: true}
			},
			messages: {
				email: {
					required: "Please enter your email address",
					email: "Please enter a valid email address"
				}
			}
		});
	}
};

$(document).ready(function() {
	definitePlansScripts.initClientSideScripts();
});