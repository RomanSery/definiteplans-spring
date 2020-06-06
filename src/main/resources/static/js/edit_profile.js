"use strict";

definitePlansScripts.basicInfo = function () {

    $('#basicInfoForm').parsley();
    $('#detailInfoForm').parsley();

    $("#basicInfoForm").submit(function (event) {
        $.ajax({
            type: "POST", url: '/me/basic', data: $('#basicInfoForm').serialize(),
            error: function () {
                definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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
                definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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
                definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
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


definitePlansScripts.refreshUserPics = function () {
    $.ajax({
        type: "GET", url: '/me/refresh-user-pics',
        error: function () {
            definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
        },
        beforeSend: function () {
            $('#loading-indicator').show();
        },
        success: function (data) {
            $('#loading-indicator').hide();
            $("#profile-light-gallery").html(data);

            definitePlansScripts.initImgScripts();
            definitePlansScripts.initLg();
        }
    });
};




definitePlansScripts.initImageUpload = function () {
    $("#btnUploadPic").dropzone({
        previewsContainer: '#previewsContainer', url: "/profile/img/upload", uploadMultiple: false, maxFiles: 1, maxFilesize: 5, acceptedFiles: '.jpg,.jpeg,.png,.bmp',
        headers: { 'X-CSRF-TOKEN': $('#_csrf').attr('content') },
        createImageThumbnails: true, thumbnailHeight: 128, thumbnailWidth: 128,
        error: function (file, errorMessage) {
            definitePlansScripts.showErrorMsg(errorMessage);
            this.removeAllFiles();
        },
        sending: function() {
            definitePlansScripts.showUploadProgress('Sending...');
            $('#loading-indicator').show();
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
                function(snapshot){
                    var progress = Math.round((snapshot.bytesTransferred / snapshot.totalBytes) * 100);
                    definitePlansScripts.showUploadProgress('Uploading thumbnail: ' + progress + '% done');
                },
                function(error) {
                    definitePlansScripts.showErrorMsg(error);
                }, function() {
                    thumbImgUploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
                        definitePlansScripts.showUploadProgress('Saving thumbnail...');
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
            var fullImgMetadata = { contentType: mimeType, userId: definitePlansScripts.curr_user_id, fileName: fileName };
            var fullImgRef = storageRef.child('images/'+definitePlansScripts.curr_user_id+'/' + definitePlansScripts.timestamp);
            var fullImgUploadTask = fullImgRef.putString(dataUrl, 'data_url', fullImgMetadata);
            fullImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
                function(snapshot){
                    var progress = Math.round((snapshot.bytesTransferred / snapshot.totalBytes) * 100);
                    definitePlansScripts.showUploadProgress('Uploading full size image: ' + progress + '% done');
                },
                function(error) {
                    definitePlansScripts.showErrorMsg(error);
                }, function() {
                    fullImgUploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
                        definitePlansScripts.showUploadProgress('Saving full size image...');
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
            definitePlansScripts.showErrorMsg('Sorry, there was some error.');
        }, success: function (data) {
            if(imgType == 'full') {
                $('#loading-indicator').hide();
                definitePlansScripts.showSuccess('Done');
                definitePlansScripts.refreshUserPics();
            }
        }
    });
};


definitePlansScripts.initImgScripts = function (imgType, mimeType, fileName, imgUrl, d) {

    $('.delete-profile-img').click(function () {
        $.ajax({
            type: "GET", url: '/profile/img/delete/' + $(this).attr('img-id'),
            beforeSend: function (xhr) {
                $('#loading-indicator').show();
                var token = $('#_csrf').attr('content');
                var header = $('#_csrf_header').attr('content');
                xhr.setRequestHeader(header, token);
            },
            error: function () {
                definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                $('#loading-indicator').hide();
                definitePlansScripts.refreshUserPics();
            }
        });
    });

    $('.set-as-profile-img').click(function () {
        $.ajax({
            type: "GET", url: '/profile/img/set/' + $(this).attr('img-id'),
            beforeSend: function (xhr) {
                $('#loading-indicator').show();
                var token = $('#_csrf').attr('content');
                var header = $('#_csrf_header').attr('content');
                xhr.setRequestHeader(header, token);
            },
            error: function () {
                definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                $('#nav-profile-pic').attr('src', data.msg);
                $('#loading-indicator').hide();
                definitePlansScripts.refreshUserPics();
            }
        });
    });

};

definitePlansScripts.initLg = function () {
    if(definitePlansScripts.lg_profile_gallery != null) {
        window.lgData[definitePlansScripts.lg_profile_gallery.getAttribute('lg-uid')].destroy(true);
    }
    definitePlansScripts.lg_profile_gallery = document.getElementById('profile-light-gallery');

    lightGallery(definitePlansScripts.lg_profile_gallery, {
        selector: '.lg-img'
    });
};

definitePlansScripts.unblockUser = function () {
    $('.unblockLink').click(function () {
        if(confirm('Are you sure you want to unblock this person?')) {
            let unlockBtn = $(this);
            $.ajax({
                type: "GET", url: '/me/unblock/' + $(this).attr('profile-id'),
                beforeSend: function (xhr) {
                    definitePlansScripts.makeBtnLoadingObj(unlockBtn);

                    var token = $('#_csrf').attr('content');
                    var header = $('#_csrf_header').attr('content');
                    xhr.setRequestHeader(header, token);
                },
                error: function () {
                    definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
                },
                success: function (data) {
                    definitePlansScripts.stopBtnLoadingObj(unlockBtn);
                    definitePlansScripts.refreshBlockedList();
                }
            });
        }
    });
};

definitePlansScripts.refreshBlockedList = function () {
    $.ajax({
        type: "GET", url: '/me/refresh-blocked-list',
        error: function () {
            definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
        },
        beforeSend: function () {
            $('#blocked-loading-indicator').show();
        },
        success: function (data) {
            $('#blocked-loading-indicator').hide();
            $("#blocked-users-list").html(data);
            definitePlansScripts.unblockUser();
        }
    });
};


definitePlansScripts.deleteAccount = function () {

    $('#deleteAccountForm').parsley();

    $("#deleteAccountForm").submit(function (event) {
        $.ajax({
            type: "POST", url: '/me/delete/account', data: $('#deleteAccountForm').serialize(),
            beforeSend: function (xhr) {
                var token = $('#_csrf').attr('content');
                var header = $('#_csrf_header').attr('content');
                xhr.setRequestHeader(header, token);
            },
            error: function () {
                definitePlansScripts.showErrorMsg('Sorry, there was some error. Please try again.');
            },
            success: function (data) {
                if(data.status == "ERR") {
                    $('#deleteAccountResult').show();
                    $('#deleteAccountResult').html(data.msg);
                } else {
                    window.location = '/login?deleted=1';
                }
            }
        });
        return false;
    });
};


definitePlansScripts.lg_profile_gallery = null;
definitePlansScripts.timestamp = Date.now();

$(document).ready(function() {

    definitePlansScripts.deleteAccount();
    definitePlansScripts.initLg();
    definitePlansScripts.basicInfo();
    definitePlansScripts.initImageUpload();
    definitePlansScripts.initImgScripts();
    definitePlansScripts.unblockUser();
});


// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyD04U3zDvt8bVsQQIVvlQO5Gok1TRX5wgY",
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