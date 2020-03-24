"use strict";

let dpScripts = {
	initClientSideScripts: function () {
		//fix for select2 issue on firefox
		$.fn.modal.Constructor.prototype.enforceFocus = function() {};

		dpScripts.initSelect2();
	},

	initSelect2: function () {
		$("select.dp-select2").select2({
			placeholder: 'Select one',
			allowClear: true
		});

		$("select.dp-select2-noclear").select2({
			placeholder: 'Select one',
			allowClear: false
		});

		//select2 clear-button work-around, until they fix it
		$('select').on('select2:unselecting', function(ev) {
			if (ev.params.args.originalEvent) {
				ev.params.args.originalEvent.stopPropagation();
			} else {
				$("select").each(function( index ) {
					if ($(this).hasClass("select2-hidden-accessible") && $(this).data('select2').isOpen()) {
						$(this).select2('close');
					}
				});
				$(this).one('select2:opening', function(ev) {
					ev.preventDefault();
					let self = $(this);
					setTimeout(function() {
						self.select2('close');
					}, 1);
				});
			}
		});

		//open select2 on tab
		$(document).on('focus', '.select2.select2-container', function (e) {
			if (e.originalEvent && $(this).find(".select2-selection--single").length > 0) {
				$(this).siblings('select:enabled').select2('open')
			}
		});

	}
};

$(document).ready(function() {
	dpScripts.initClientSideScripts();
});