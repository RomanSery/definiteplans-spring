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
};

$(document).ready(function() {
    definitePlansScripts.filterSearchResults();
});
