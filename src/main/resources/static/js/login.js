"use strict";

definitePlansScripts.resentValidationEmail = function (email) {
    console.log("here");
    $.ajax({
        type: "GET", url: '/resendValidationEmail',
        beforeSend: function (xhr) {
            //definitePlansScripts.makeBtnLoading('btnBlockUser');
        },
        error: function () {
            //definitePlansScripts.showUploadErr('Sorry, there was some error. Please try again.');
        },
        success: function (data) {
            //definitePlansScripts.stopBtnLoading('btnBlockUser');
            //alert("User has been blocked");
            //window.location = '/browse';
        }
    });
};

$(document).ready(function() {
    $('#loginForm').parsley();

    $('#resend-confirm-email').click(function () {
        definitePlansScripts.resentValidationEmail($(this).attr('email'));
    });
});
