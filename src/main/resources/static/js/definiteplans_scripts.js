"use strict";

let definitePlansScripts = {
	initClientSideScripts: function () {

		definitePlansScripts.initSelect2();
		definitePlansScripts.loginPage();
		definitePlansScripts.forgotPasswordPage();
		definitePlansScripts.resetPassword();
		definitePlansScripts.regPage();
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

	regPage: function () {
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
	},

	resetPassword: function () {
		var resetPasswordValidator = $("#resetPasswordForm").validate({
			rules: {
				currPassword: {required: true},
				password: {
					required: true,
					minlength: 8
				},
				cpassword: {
					required: true,
					minlength: 8,
					equalTo: "#password"
				}
			},
			messages: {
				currPassword: {
					required: "Please enter your current password."
				},
				password: {
					required: "Please enter your new password."
				},
				cpassword: {
					required: "Please confirm your password.",
					passwordsMatch: "Please ensure your passwords match."
				}
			}
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
	},

	loginPage: function () {
		let myLoginValidator = $("#loginForm").validate({
			submitHandler: function (form) {
				if (myLoginValidator.numberOfInvalids() == 0) {

					$.ajax({
						type: "POST",
						url: DOIT.ajax_base_url + '/emailpwdlogin',
						data: $('#loginForm').serializeArray(),
						error: function () {
							$('#loginFeedback').empty();
							$('#loginFeedback').html('Sorry, there was some error in logging in.');
							$('#loginFeedback').show();
						},
						success: function (data) {
							if (data.status == 'OK') {
								if (data.redirect) {
									location.href = data.redirect;
									return;
								}

								location.reload();
								return;
							}
							if (data.status == 'ERR') {
								if (data.msg) {
									if (data.code && data.code == 'validate') data.msg += ' <a href="#" class="resend-confirm-email">Please click to request new confirmation email with validation link.</a>';
									$('#loginFeedback').empty();
									$('#loginFeedback').html(data.msg);
									$('#loginFeedback').show();

									$('.resend-confirm-email').click(function () {
										$.ajax({
											type: "POST",
											url: DOIT.ajax_base_url + '/emailpwdlogin?op=resendprimary',
											data: $('#loginForm').serializeArray(),
											success: function (data) {
												$('.resend-confirm-email').replaceWith("A confirmation email has been sent containing a link to validate your email address.");
											}
										});
									});
								}
								return;
							}
						}
					});

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
	}
};

$(document).ready(function() {
	definitePlansScripts.initClientSideScripts();
});