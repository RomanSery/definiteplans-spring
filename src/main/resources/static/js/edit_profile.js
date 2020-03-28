"use strict";

definitePlansScripts.basicInfo = function () {
    $("#basicInfoForm").validate({
        rules: {
            displayName: {required: true, rangelength: [2, 15]},
            gender: {required: true},
            dob: {required: true},
            postalCode: {required: true},
            state: {required: true}
        },
        messages: {
            displayName: {
                required: "Please enter your first name",
                rangelength: "First name must be between 2 and 15 characters"
            },
            gender: "Please select your gender",
            dob: "Please enter your date of birth",
            postalCode: "Please entery your zip code",
            state: "Please select your state"
        }
    });

    DOIT.updateCityState = function (zip) {
        $.ajax({
            type: "POST", url: 'domisc?action=findzip', data: 'zipcode=' + zip,
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                if (data.status == 'OK') {
                    $('#city').val(data.city);
                    $('#state').val(data.state);
                    return false;
                }
            }
        });
    };

    $("#postalCode").on('change keyup paste', function () {
        var zip = $(this).val();
        setTimeout(function () {
            DOIT.updateCityState(zip);
        }, 1000);
    });
};


definitePlansScripts.initImageUpload = function () {
    $("#btnUploadPic").dropzone({
        previewsContainer: '#previewsContainer', url: "uploadProfilePic", uploadMultiple: false, maxFiles: 1, maxFilesize: 3, acceptedFiles: '.jpg,.jpeg,.png,.bmp',
        createImageThumbnails: true, thumbnailHeight: 120, thumbnailWidth: 120,
        error: function (file, errorMessage) {
            $('#uploadErrDiv').html(errorMessage);
            $('#uploadErrDiv').show();
            this.removeAllFiles();
        },
        thumbnail: function (file, dataUrl) {
            const mimeType = file.type;
            const fileName = file.upload.filename;

            var storage = firebase.storage();
            var storageRef = storage.ref();
            var thumbImgMetadata = { contentType: mimeType, userId: DOIT.curr_user_id, fileName: fileName };
            var thumbImgRef = storageRef.child('images/'+DOIT.curr_user_id+'/thumbs/' + definitePlansScripts.timestamp);
            var thumbImgUploadTask = thumbImgRef.putString(dataUrl, 'data_url', thumbImgMetadata);
            thumbImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
                function(snapshot) { },
                function(error) {
                    $('#uploadErrDiv').html(error);
                    $('#uploadErrDiv').show();
                }, function() {
                    thumbImgUploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
                        console.log("thumb upload");
                        definitePlansScripts.saveProfileImg('thumb', mimeType, fileName, downloadURL, definitePlansScripts.timestamp);
                    });
                });

        },
        success: function (file, resp) {
            const mimeType = file.type;
            const fileName = file.upload.filename;
            const dataUrl = file.dataURL;

            var storage = firebase.storage();
            var storageRef = storage.ref();
            var fullImgMetadata = { contentType: mimeType, userId: DOIT.curr_user_id, fileName: fileName };
            var fullImgRef = storageRef.child('images/'+DOIT.curr_user_id+'/' + definitePlansScripts.timestamp);
            var fullImgUploadTask = fullImgRef.putString(dataUrl, 'data_url', fullImgMetadata);
            fullImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
                function(snapshot) { },
                function(error) {
                    $('#uploadErrDiv').html(error);
                    $('#uploadErrDiv').show();
                }, function() {
                    fullImgUploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
                        console.log("full upload");
                        definitePlansScripts.saveProfileImg('full', mimeType, fileName, downloadURL, definitePlansScripts.timestamp);
                    });
                });

        },
        complete: function (file) {
            this.removeAllFiles();
        }
    });
},

definitePlansScripts.saveProfileImg = function (imgType, mimeType, fileName, imgUrl, d) {
    $.ajax({type: "POST", url: 'uploadProfilePic?op=save', data: {
            img_type: imgType, mime_type: mimeType, file_name: fileName, img_url: imgUrl, d: d
        },
        error: function () {
            $('#uploadErrDiv').empty().show();
            $('#uploadErrDiv').html('Sorry, there was some error.');
        }
    });
},

definitePlansScripts.initImgScripts = function (imgType, mimeType, fileName, imgUrl, d) {

    $('.delete-profile-img').click(function () {
        $.ajax({
            type: "POST", url: 'domisc?action=deleteProfileImg&imageId=' + $(this).attr('img-id'),
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {

            }
        });
    });

    $('.set-as-profile-img').click(function () {
        $.ajax({
            type: "POST", url: 'domisc?action=updateProfileImg&imageId=' + $(this).attr('img-id'),
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {

            }
        });
    });

}

definitePlansScripts.timestamp = Date.now();

$(document).ready(function() {
    definitePlansScripts.basicInfo();
    definitePlansScripts.initImageUpload();
    definitePlansScripts.initImgScripts();

    $("#aboutMe").limiter(300, $('#aboutMeChars'));
    $("#interests").limiter(300, $('#interestsChars'));

    $('[data-toggle="popover"]').popover();
});