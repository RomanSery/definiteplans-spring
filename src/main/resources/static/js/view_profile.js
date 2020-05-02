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

definitePlansScripts.dateBtns = function () {

    $('#proposePlanBtn').click(function () {
        const profileId = $(this).attr('profile-id');
        if(confirm('Are you sure you want to propose this date?')) {
            $.ajax({
                type: "POST", url: '/dates/propose', data: $('#definiteDateForm').serialize(),
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoading('proposePlanBtn');

                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    //definitePlansScripts.showUploadErr('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    if(data.status == "ERR") {
                        $('#definiteDateFormErrors').show();
                        $('#definiteDateFormErrors').html(data.msg);
                    }
                    definitePlansScripts.stopBtnLoading('proposePlanBtn');
                    definitePlansScripts.refreshMakePlans(profileId);
                }
            });
        }
    });

    $('#proposeChange').click(function () {
        const profileId = $(this).attr('profile-id');
        if(confirm('Are you sure you want to change this date?')) {
            $.ajax({
                type: "POST", url: '/dates/change', data: $('#definiteDateForm').serialize(),
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoading('proposeChange');
                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    //definitePlansScripts.showUploadErr('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    if(data.status == "ERR") {
                        $('#definiteDateFormErrors').show();
                        $('#definiteDateFormErrors').html(data.msg);
                    }
                    definitePlansScripts.stopBtnLoading('proposeChange');
                    definitePlansScripts.refreshMakePlans(profileId);
                }
            });
        }
    });

    $('#acceptPlan').click(function () {
        const profileId = $(this).attr('profile-id');
        const dateId = $(this).attr('date-id');
        if(confirm('Are you sure you want to accept this date?')) {
            $.ajax({
                type: "POST", url: '/dates/accept/' + dateId,
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoading('acceptPlan');
                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    //definitePlansScripts.showUploadErr('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    if(data.status == "ERR") {
                        $('#definiteDateFormErrors').show();
                        $('#definiteDateFormErrors').html(data.msg);
                    }
                    definitePlansScripts.stopBtnLoading('acceptPlan');
                    definitePlansScripts.refreshMakePlans(profileId);
                }
            });
        }
    });

    $('#declinePlan').click(function () {
        const profileId = $(this).attr('profile-id');
        const dateId = $(this).attr('date-id');
        if(confirm('Are you sure you want to decline this date?')) {
            $.ajax({
                type: "POST", url: '/dates/decline/' + dateId,
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoading('declinePlan');
                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    //definitePlansScripts.showUploadErr('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    if(data.status == "ERR") {
                        $('#definiteDateFormErrors').show();
                        $('#definiteDateFormErrors').html(data.msg);
                    }
                    definitePlansScripts.stopBtnLoading('declinePlan');
                    definitePlansScripts.refreshMakePlans(profileId);
                }
            });
        }
    });
};

definitePlansScripts.refreshMakePlans = function (profileId) {
    $.ajax({
        type: "GET", url: '/refresh-make-plans/' + profileId,
        error: function () {
            alert('Sorry, there was some error. Please try again.');
        },
        success: function (data) {
            $("#make-plans-frag").html(data);
            $('#loading-indicator').hide();
        },
        beforeSend: function () {
            $("#make-plans-frag").html("");
            $('#loading-indicator').show();
        }
    });
    return false;
};


$(document).ready(function() {
    lightGallery(document.getElementById('profile-light-gallery'));
    lightGallery(document.getElementById('main-profile-pic'));

    $('#btnMakePlans').click(function () {
        $('#profileTabsDiv a[href="#tab-4"]').tab('show');
    });

    $('.clockpicker').datetimepicker({
        toolbarPlacement: 'bottom',
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down',
            previous: 'fa fa-chevron-left',
            next: 'fa fa-chevron-right',
            today: 'fa fa-arrows ',
            clear: 'fa fa-trash',
            close: 'fa fa-times'
        }, format: 'LT'
    });

    $('[data-toggle="tooltip"]').tooltip();

    definitePlansScripts.blockUser();
    definitePlansScripts.dateBtns();
});
