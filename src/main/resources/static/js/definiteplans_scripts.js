"use strict";

let definitePlansScripts = {
	initClientSideScripts: function () {
		definitePlansScripts.initSelect2();

		$('[data-toggle="popover"]').popover();
	},

	initSelect2: function () {
		$("select.doit-select2").select2({
			placeholder: 'Select one',
			allowClear: true
		});

		$("select.doit-select2-noclear").select2({
			placeholder: 'Select one',
			allowClear: false
		});
	}
};

definitePlansScripts.makeBtnLoading = function (btnId) {
	const origHtml = $('#'+btnId).html();
	definitePlansScripts.origBtnHtml.set(btnId, origHtml);

	$('#'+btnId+' i').remove();
	$('#'+btnId).html(definitePlansScripts.loadingIcon + $('#'+btnId).html());
};
definitePlansScripts.stopBtnLoading = function (btnId) {
	$('#'+btnId).html(definitePlansScripts.origBtnHtml.get(btnId));
	definitePlansScripts.origBtnHtml.delete(btnId);
};

definitePlansScripts.makeBtnLoadingObj = function (btn) {
	const origHtml = btn.html();
	definitePlansScripts.origBtnHtml.set(btn.attr('id'), origHtml);

	$('#'+btn.attr('id')+' i').remove();
	btn.html(definitePlansScripts.loadingIcon + btn.html());
};
definitePlansScripts.stopBtnLoadingObj = function (btn) {
	btn.html(definitePlansScripts.origBtnHtml.get(btn.attr('id')));
	definitePlansScripts.origBtnHtml.delete(btn.attr('id'));
};

definitePlansScripts.showErrorMsg = function (msg) {
	$('#progressMsgDiv').empty().hide();
	$('#customSuccessMsgDiv').empty().hide();

	$('#customErrMsgDiv').empty().show();
	$('#customErrMsgDiv').html(msg);
};
definitePlansScripts.showUploadProgress = function (msg) {
	$('#customErrMsgDiv').empty().hide();
	$('#customSuccessMsgDiv').empty().hide();

	$('#progressMsgDiv').empty().show();
	$('#progressMsgDiv').html(msg);
};
definitePlansScripts.showSuccess = function (msg) {
	$('#customErrMsgDiv').empty().hide();
	$('#progressMsgDiv').empty().hide();

	$('#customSuccessMsgDiv').empty().show();
	$('#customSuccessMsgDiv').html(msg);
};


definitePlansScripts.origBtnHtml = new Map();

definitePlansScripts.loadingIcon = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';

$(document).ready(function() {
	definitePlansScripts.initClientSideScripts();
});