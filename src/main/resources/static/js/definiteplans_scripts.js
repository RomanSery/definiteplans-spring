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

$(document).ready(function() {
	definitePlansScripts.initClientSideScripts();
});