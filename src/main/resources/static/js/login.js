"use strict";

definitePlansScripts.resentValidationEmail = function (email) {
    console.log("here");
    $.ajax({
        type: "GET", url: '/resendValidationEmail?email=' + email,
        success: function (data) {
            $('#login-error-div').html('Sent, check your email!')
        }
    });
};

$(document).ready(function() {
    $('#loginForm').parsley();

    $('#resend-confirm-email').click(function () {
        definitePlansScripts.resentValidationEmail($(this).attr('email'));
    });
});
