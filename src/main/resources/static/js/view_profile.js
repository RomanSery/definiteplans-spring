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
                    definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    definitePlansScripts.stopBtnLoading('btnBlockUser');
                    window.location = '/browse?blocked=1';
                }
            });
        }
    });
};

definitePlansScripts.dateBtns = function () {

    $('#proposePlanBtn').click(function () {
        const profileId = $(this).attr('profile-id');
        if(confirm('Are you sure you want to propose this date?')) {
            definitePlansScripts.dateFormParsley.validate();
            if(definitePlansScripts.dateFormParsley.isValid() == true) {
                $.ajax({
                    type: "POST", url: '/dates/propose', data: $('#definiteDateForm').serialize(),
                    beforeSend: function (xhr) {
                        definitePlansScripts.makeBtnLoading('proposePlanBtn');

                        var token = $('#_csrf').attr('content');
                        var header = $('#_csrf_header').attr('content');
                        xhr.setRequestHeader(header, token);
                    },
                    error: function () {
                        definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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
        }
    });

    $('#proposeChange').click(function () {
        const profileId = $(this).attr('profile-id');
        if(confirm('Are you sure you want to change this date?')) {
            definitePlansScripts.dateFormParsley.validate();
            if(definitePlansScripts.dateFormParsley.isValid() == true) {
                $.ajax({
                    type: "POST", url: '/dates/change', data: $('#definiteDateForm').serialize(),
                    beforeSend: function (xhr) {
                        definitePlansScripts.makeBtnLoading('proposeChange');
                        var token = $('#_csrf').attr('content');
                        var header = $('#_csrf_header').attr('content');
                        xhr.setRequestHeader(header, token);
                    },
                    error: function () {
                        definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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
                    definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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
                    definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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
            definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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


definitePlansScripts.feedbackForm = function() {
    $("#dateFeedbackForm").submit(function (event) {
        const profileId = $('#submitFeedbackBtn').attr('profile-id');
        if(confirm('Are you sure you want to submit this feedback?')) {
            $.ajax({
                type: "POST", url: '/dates/feedback', data: $('#dateFeedbackForm').serialize(),
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoading('submitFeedbackBtn');
                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    definitePlansScripts.stopBtnLoading('submitFeedbackBtn');
                    definitePlansScripts.refreshMakePlans(profileId);
                    $('#dateFeedbackForm').hide();
                }
            });
            return false;
        }
    });
};


definitePlansScripts.chat = function () {
    $("#chatForm").submit(function (event) {
        if(confirm('Are you sure you want to send this message?')) {
            $.ajax({
                type: "POST", url: '/chat/send', data: $('#chatForm').serialize(),
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoading('sendChatBtn');
                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    definitePlansScripts.stopBtnLoading('sendChatBtn');
                    definitePlansScripts.refreshChatThread();
                }
            });
            return false;
        }
    });
};

definitePlansScripts.refreshChatThread = function () {
    $.ajax({
        type: "GET", url: '/refresh-chat-thread/' + definitePlansScripts.viewing_profile_id,
        error: function () {
            definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
        },
        success: function (data) {
            $("#chatThreadDiv").html(data);
            $('#loading-indicator').hide();
            definitePlansScripts.chat();
            $('.btnMakePlans').click(function () {
                $('#profileTabsDiv a[href="#tab-5"]').tab('show');
            });
        },
        beforeSend: function () {
            $("#chatThreadDiv").html("");
            $('#loading-indicator').show();
        }
    });
    return false;
};

$(document).ready(function() {
    lightGallery(document.getElementById('profile-light-gallery'));
    lightGallery(document.getElementById('main-profile-pic'));

    $('.btnMakePlans').click(function () {
        $('#profileTabsDiv a[href="#tab-5"]').tab('show');
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

    $('.dp-datepicker').datetimepicker({
        minDate: moment().subtract(1, 'days'),
        maxDate: moment().add(14, 'days'),
        format: 'L'
    }).next().on("click", function(){
        jQuery(this).prev().focus();
    });

    $('#chatForm').parsley();
    definitePlansScripts.dateFormParsley = $('#definiteDateForm').parsley();

    $("#greetingMsg").limiter(200, $('#greetingMsgChars'));
    $("#chatMessage").limiter(500, $('#chatMsgChars'));

    $('[data-toggle="tooltip"]').tooltip();

    definitePlansScripts.blockUser();
    definitePlansScripts.dateBtns();
    definitePlansScripts.feedbackForm();
    definitePlansScripts.chat();
});
