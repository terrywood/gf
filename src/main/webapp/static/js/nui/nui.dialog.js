(function($){
	$.pdialog = {
		close: function(){
			$('#dialog').modal('hide').empty();
		},
		
		open:function(url, title, options) {

//			if ($('#dialog').html() == null) {
//				$('#dialog').remove();
//				$("body").append(NUI.frag["dialogFrag"]);
//				
//			} else {
//				$('#dialog').modal("show");
				
//			}

			$('#dialog').loadUrl(url, {}, function() {
				
				$('#dialog').modal({
					backdrop:'static'
//					resizable: resize || true,
//					modal: modal || true,
//					title: title 
					});
			});
		}
	}
})(jQuery);