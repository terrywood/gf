/**
 * ajax form submit
 * @param {Object} form
 * @param {Object} callback
 * @param {String} confirmMsg 
 */
function ajaxSubmitCallback(form, callback, confirmMsg) {
	var $form = $(form);
	if (!$form.valid()) {
		return false;
	}
		
	var _submitFn = function(){
		$.ajax({
			type: form.method || 'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"json",
			cache: false,
			success: callback || NUI.ajaxDone,
			error: NUI.ajaxError
		});
	}
	
	if (confirmMsg) {
		bootbox.confirm(confirmMsg, function(result) {
            if(result){
            	_submitFn();
            }
         }); 
	} else {
		_submitFn();
	}
	
	return false;
}
/**
 * iframe submit file form
 * @param {Object} form
 * @param {Object} callback
 */
function iframeCallback(form, callback){
	var $form = $(form), $iframe = $("#callbackframe");
	if(!$form.valid()) {return false;}

	if ($iframe.size() == 0) {
		$iframe = $("<iframe id='callbackframe' name='callbackframe' src='about:blank' style='display:none'></iframe>").appendTo("body");
	}
	if(!form.ajax) {
		$form.append('<input type="hidden" name="ajax" value="1" />');
	}
	form.target = "callbackframe";
	
	_iframeResponse($iframe[0], callback || NUI.ajaxDone);
}
function _iframeResponse(iframe, callback){
	var $iframe = $(iframe), $document = $(document);
	
	$document.trigger("ajaxStart");
	
	$iframe.bind("load", function(event){
		$iframe.unbind("load");
		$document.trigger("ajaxStop");
		
		if (iframe.src == "javascript:'%3Chtml%3E%3C/html%3E';" || // For Safari
			iframe.src == "javascript:'<html></html>';") { // For FF, IE
			return;
		}

		var doc = iframe.contentDocument || iframe.document;

		// fixing Opera 9.26,10.00
		if (doc.readyState && doc.readyState != 'complete') return; 
		// fixing Opera 9.64
		if (doc.body && doc.body.innerHTML == "false") return;
	   
		var response;
		
		if (doc.XMLDocument) {
			// response is a xml document Internet Explorer property
			response = doc.XMLDocument;
		} else if (doc.body){
			try{
				response = $iframe.contents().find("body").text();
				response = jQuery.parseJSON(response);
			} catch (e){ // response is html document or plain text
				response = doc.body.innerHTML;
			}
		} else {
			// response is a xml document
			response = doc;
		}
		
		callback(response);
	});
}


/**
 * dialog submit form callback function
 */
function dialogAjaxDone(json){
	NUI.ajaxDone(json);
	if (json.statusCode == NUI.statusCode.ok){

		
		if ("true" == json.closeDialog) {
			$.pdialog.close();
		}
	}
}

/**
 * @param {Object} form
 */
function divSearch(form, rel){
	var $form = $(form);
	if (!$form.valid()) {
		return false;
	}
	if (rel) {
		var $box = $("#" + rel);
		$box.ajaxUrl({
			type:"POST", url:$form.attr("action"), data: $form.serializeArray()
		});
	}
	return false;
}


function ajaxTodo(url, callback){
	var $callback = callback || NUI.ajaxDone;
	if (! $.isFunction($callback)) $callback = eval('(' + callback + ')');
	$.ajax({
		type:'POST',
		url:url,
		dataType:"json",
		cache: false,
		success: $callback,
		error: NUI.ajaxError
	});
}



$.fn.extend({
	ajaxTodo:function(){
		return this.each(function(){
			var $this = $(this);
			$this.click(function(event){
				var url = unescape($this.attr("href")).replaceTmById();				
				NUI.debug(url);
				if (!url.isFinishedTm()) {
					toastr["error"]($this.attr("warn") || NUI.msg("alertSelectMsg"),"Error");
					return false;
				}
				var title = $this.attr("title");
				
				if (title) {
					bootbox.confirm(title, function(result) {
			            if(result){
//							if($('#dialog').is(":visible")) {
//								$('#dialog').remove();
//							}
			            	ajaxTodo(url, $this.attr("callback"));
			            }
			         });
					
				} else {
					ajaxTodo(url, $this.attr("callback"));
				}
				event.preventDefault();
			});
		});
	}

});
