"use strict";

Dropzone.autoDiscover = false;

let doit_img_Scripts = {
	initClientSideScripts: function () {
		doit_img_Scripts.initImageUpload();
		doit_img_Scripts.initImgScripts();
	},

	initImageUpload: function () {
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
				var thumbImgRef = storageRef.child('images/'+DOIT.curr_user_id+'/thumbs/' + doit_img_Scripts.timestamp);
				var thumbImgUploadTask = thumbImgRef.putString(dataUrl, 'data_url', thumbImgMetadata);
				thumbImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
					function(snapshot) { },
					function(error) {
						$('#uploadErrDiv').html(error);
						$('#uploadErrDiv').show();
					}, function() {
						thumbImgUploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
							console.log("thumb upload");
							doit_img_Scripts.saveProfileImg('thumb', mimeType, fileName, downloadURL, doit_img_Scripts.timestamp);
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
				var fullImgRef = storageRef.child('images/'+DOIT.curr_user_id+'/' + doit_img_Scripts.timestamp);
				var fullImgUploadTask = fullImgRef.putString(dataUrl, 'data_url', fullImgMetadata);
				fullImgUploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
					function(snapshot) { },
					function(error) {
						$('#uploadErrDiv').html(error);
						$('#uploadErrDiv').show();
					}, function() {
						fullImgUploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
							console.log("full upload");
							doit_img_Scripts.saveProfileImg('full', mimeType, fileName, downloadURL, doit_img_Scripts.timestamp);
						});
					});

			},
			complete: function (file) {
				this.removeAllFiles();
			}
		});
	},

	saveProfileImg: function (imgType, mimeType, fileName, imgUrl, d) {
		$.ajax({type: "POST", url: 'uploadProfilePic?op=save', data: {
				img_type: imgType, mime_type: mimeType, file_name: fileName, img_url: imgUrl, d: d
			},
			error: function () {
				$('#uploadErrDiv').empty().show();
				$('#uploadErrDiv').html('Sorry, there was some error.');
			}
		});
	},

	initImgScripts: function (imgType, mimeType, fileName, imgUrl, d) {

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
};

doit_img_Scripts.timestamp = Date.now();

$(document).ready(function() {
	doit_img_Scripts.initClientSideScripts();
});