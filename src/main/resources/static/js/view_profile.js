"use strict";

definitePlansScripts.blockUser = function () {
    $('#btnBlockUser').click(function () {
        $.ajax({
            type: "GET", url: '/profiles/block/' + $(this).attr('profile-id'),
            beforeSend: function (xhr) {
                definitePlansScripts.makeBtnLoading('btnBlockUser');

                var token = $('#_csrf').attr('content');
                var header = $('#_csrf_header').attr('content');
                xhr.setRequestHeader(header, token);
            },
            error: function () {
                definitePlansScripts.showUploadErr('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                definitePlansScripts.stopBtnLoading('btnBlockUser');
            }
        });
    });
};



$(document).ready(function() {
    lightGallery(document.getElementById('profile-light-gallery'));
    lightGallery(document.getElementById('main-profile-pic'));

    definitePlansScripts.blockUser();
});
