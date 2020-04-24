"use strict";

definitePlansScripts.blockUser = function () {

    $('#btnBlockUser').click(function () {
        if(confirm('Are you sure you want to block this person?')) {
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
                    alert("User has been blocked");
                    window.location = '/browse';
                }
            });
        }
    });
};



$(document).ready(function() {
    lightGallery(document.getElementById('profile-light-gallery'));
    lightGallery(document.getElementById('main-profile-pic'));

    $('#btnMakePlans').click(function () {
        $('#profileTabsDiv a[href="#tab-4"]').tab('show');
    });


    definitePlansScripts.blockUser();
});
