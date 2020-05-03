"use strict";

definitePlansScripts.filterSearchResults = function () {
    $("#filtersForm").submit(function (event) {
        $.ajax({
            type: "POST", url: '/browse/filter', data: $('#filtersForm').serialize(),
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {

                $.ajax({
                    type: "GET", url: '/refresh-browse-results',
                    error: function () {
                        alert('Sorry, there was some error. Please try again.');
                    },
                    success: function (data) {
                        $("#search-results-cont").html(data);
                        $('#loading-indicator').hide();
                    }
                });
            },
            beforeSend: function () {
                $("#search-results-cont").html("");
                $('#loading-indicator').show();
            }
        });
        return false;
    });


    $('#collapseTwo2, #collapseOne1').on('show.bs.collapse', function () {
        definitePlansScripts.initClientSideScripts();
    })
    $('#collapseTwo2, #collapseOne1').on('shown.bs.collapse', function () {
        definitePlansScripts.initClientSideScripts();
    })
};

$(document).ready(function() {
    definitePlansScripts.filterSearchResults();

    $('[data-toggle="tooltip"]').tooltip();
});
