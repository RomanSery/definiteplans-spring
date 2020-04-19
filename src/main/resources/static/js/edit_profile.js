"use strict";

definitePlansScripts.basicInfo = function () {

    $('#basicInfoForm').parsley();
    $('#detailInfoForm').parsley();

    $("#postalCode").on('change keyup paste', function () {
        var zip = $(this).val();
        setTimeout(function () {
            definitePlansScripts.updateCityState(zip);
        }, 1000);
    });

    $("#basicInfoForm").submit(function (event) {
        $.ajax({
            type: "POST", url: '/me/basic', data: $('#basicInfoForm').serialize(),
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                definitePlansScripts.showSaveResult(data, 'basic');
            }
        });
        return false;
    });

    $("#detailInfoForm").submit(function (event) {
        $.ajax({
            type: "POST", url: '/me/details', data: $('#detailInfoForm').serialize(),
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                definitePlansScripts.showSaveResult(data, 'detail');
            }
        });
        return false;
    });

    $("#settingsForm").submit(function (event) {
        $.ajax({
            type: "POST", url: '/me/settings', data: $('#settingsForm').serialize(),
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                definitePlansScripts.showSaveResult(data, 'settings');
            }
        });
        return false;
    });
};

definitePlansScripts.showSaveResult = function (data, prefix) {
    if(data.status == 'OK') {
        $('#'+prefix+'-info-saved').show();
        $('#'+prefix+'-info-saved').html(data.msg);
        $('#'+prefix+'-info-errors').hide();
    } else {
        $('#'+prefix+'-info-saved').hide();
        $('#'+prefix+'-info-errors').show();
        $('#'+prefix+'-info-errors').html(data.msg);
    }
};

definitePlansScripts.updateCityState = function () {
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


definitePlansScripts.initImageUpload = function () {
    $("#btnUploadPic").dropzone({
        previewsContainer: '#previewsContainer', url: "/profile/img/upload", uploadMultiple: false, maxFiles: 1, maxFilesize: 3, acceptedFiles: '.jpg,.jpeg,.png,.bmp',
        headers: { 'X-CSRF-TOKEN': $('#_csrf').attr('content') },
        createImageThumbnails: true, thumbnailHeight: 120, thumbnailWidth: 120,
        error: function (file, errorMessage) {
            console.log(errorMessage);
            $('#uploadErrDiv').html(errorMessage);
            $('#uploadErrDiv').show();
            this.removeAllFiles();
        },
        thumbnail: function (file, dataUrl) {
            const mimeType = file.type;
            const fileName = file.upload.filename;

            var storage = firebase.storage();
            var storageRef = storage.ref();
            var thumbImgMetadata = { contentType: mimeType, userId: definitePlansScripts.curr_user_id, fileName: fileName };
            var thumbImgRef = storageRef.child('images/'+definitePlansScripts.curr_user_id+'/thumbs/' + definitePlansScripts.timestamp);
            var thumbImgUploadTask = thumbImgRef.putString(dataUrl, 'data_url', thumbImgMetadata);
            thumbImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
                function(snapshot) { },
                function(error) {
                    console.log(error);
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

            console.log("full upload start");

            var storage = firebase.storage();
            var storageRef = storage.ref();
            var fullImgMetadata = { contentType: mimeType, userId: definitePlansScripts.curr_user_id, fileName: fileName };
            var fullImgRef = storageRef.child('images/'+definitePlansScripts.curr_user_id+'/' + definitePlansScripts.timestamp);
            var fullImgUploadTask = fullImgRef.putString(dataUrl, 'data_url', fullImgMetadata);
            fullImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
                function(snapshot) { },
                function(error) {
                    console.log(error);
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
};

definitePlansScripts.saveProfileImg = function (imgType, mimeType, fileName, imgUrl, d) {

    $.ajax({type: "POST", url: '/profile/img/upload', data: {
            op: 'save', img_type: imgType, mime_type: mimeType, file_name: fileName, img_url: imgUrl, d: d
        },
        beforeSend: function (xhr) {
            var token = $('#_csrf').attr('content');
            var header = $('#_csrf_header').attr('content');
            xhr.setRequestHeader(header, token);
        },
        error: function () {
            $('#uploadErrDiv').empty().show();
            $('#uploadErrDiv').html('Sorry, there was some error.');
        }
    });
};

definitePlansScripts.initImgScripts = function (imgType, mimeType, fileName, imgUrl, d) {

    $('.delete-profile-img').click(function () {
        $.ajax({
            type: "GET", url: '/profile/img/delete/' + $(this).attr('img-id'),
            beforeSend: function (xhr) {
                var token = $('#_csrf').attr('content');
                var header = $('#_csrf_header').attr('content');
                xhr.setRequestHeader(header, token);
            },
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {

            }
        });
    });

    $('.set-as-profile-img').click(function () {
        $.ajax({
            type: "GET", url: '/profile/img/set/' + $(this).attr('img-id'),
            beforeSend: function (xhr) {
                var token = $('#_csrf').attr('content');
                var header = $('#_csrf_header').attr('content');
                xhr.setRequestHeader(header, token);
            },
            error: function () {
                alert('Sorry, there was some error. Please try again.');
            },
            success: function (data) {

            }
        });
    });

};


definitePlansScripts.timestamp = Date.now();

$(document).ready(function() {
    lightGallery(document.getElementById('profile-light-gallery'), {
        selector: '.lg-img'
    });



    definitePlansScripts.basicInfo();
    definitePlansScripts.initImageUpload();
    definitePlansScripts.initImgScripts();

    $('[data-toggle="popover"]').popover();
});


// Your web app's Firebase configuration
var firebaseConfig = {
    apiKey: "AIzaSyBJxI3c-hcRfiEdaNHkqhTkeZovbR1RoLw",
    authDomain: "ultra-compound-851.firebaseapp.com",
    databaseURL: "https://ultra-compound-851.firebaseio.com",
    projectId: "ultra-compound-851",
    storageBucket: "ultra-compound-851.appspot.com",
    messagingSenderId: "947515066542",
    appId: "1:947515066542:web:413c40377db4022467453f",
    measurementId: "G-8LRFEF2NGM"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.analytics();