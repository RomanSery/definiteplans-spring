"use strict";

definitePlansScripts.resentValidationEmail = function (email) {
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

    $('#find-out-more-btn').click(function () {
        $(this).hide();
        $('#login-form-container').removeClass('invisible');
    });


    // Smooth scrolling using jQuery easing
    $('a.js-scroll-trigger[href*="#"]:not([href="#"])').click(function() {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html, body').animate({
                    scrollTop: (target.offset().top - 72)
                }, 1000, "easeInOutExpo");
                return false;
            }
        }
    });

    // Closes responsive menu when a scroll trigger link is clicked
    $('.js-scroll-trigger').click(function() {
        $('.navbar-collapse').collapse('hide');
    });

    // Activate scrollspy to add active class to navbar items on scroll
    $('body').scrollspy({
        target: '#mainNav',
        offset: 75
    });

    // Collapse Navbar
    var navbarCollapse = function() {
        if ($("#mainNav").offset().top > 100) {
            $("#mainNav").addClass("navbar-scrolled");
        } else {
            $("#mainNav").removeClass("navbar-scrolled");
        }
    };
    // Collapse now if page is not at top
    navbarCollapse();
    // Collapse the navbar when page is scrolled
    $(window).scroll(navbarCollapse);
});
