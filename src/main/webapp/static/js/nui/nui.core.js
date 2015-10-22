/**
 * @author noside@126.com
 * 
 */

var NUI = {
	// sbar: show sidebar
	keyCode: {
		ENTER: 13, ESC: 27, END: 35, HOME: 36,
		SHIFT: 16, TAB: 9,
		LEFT: 37, RIGHT: 39, UP: 38, DOWN: 40,
		DELETE: 46, BACKSPACE:8
	},
	statusCode: {ok:200, info:201, warn:202, error:300, timeout:301},
	frag:{}, //page fragment
	_msg:{}, //alert message
	_set:{
		loginUrl:"", //session timeout
		debug:false
	},
	msg:function(key, args){
		var _format = function(str,args) {
			args = args || [];
			var result = str || "";
			for (var i = 0; i < args.length; i++){
				result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
			}
			return result;
		}
		return _format(this._msg[key], args);
	},
	debug:function(msg){
		if (this._set.debug) {
			if (typeof(console) != "undefined") console.log(msg);
			else alert(msg);
		}
	},
	loadLogin:function(){
		window.location = NUI._set.loginUrl;
	},
	jsonEval:function(data) {
		try{
			if ($.type(data) == 'string')
				return eval('(' + data + ')');
			else return data;
		} catch (e){
			return {};
		}
	},
	ajaxError:function(xhr, ajaxOptions, thrownError){
		var json = NUI.jsonEval(xhr.responseText);
		if (json.statusCode==NUI.statusCode.error){
			if (json.message) toastr["error"](json.message,"Error");//success/error/info/warning
		}else{
			toastr["error"]("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" 
					+ "<div>ajaxOptions: "+ajaxOptions + "</div>"
					+ "<div>thrownError: "+thrownError + "</div>"
					+ "<div>"+xhr.responseText+"</div>","Error");//success/error/info/warning
			
		}
	},
	ajaxDone:function(json){
		if(json!=null){
			if (json.statusCode === undefined && json.message === undefined) { // for iframeCallback
				return toastr["error"](json,"Error");//success/error/info/warning
			} 
			
			if(json.statusCode == NUI.statusCode.error) {
				if(json.message) toastr["error"](json.message,"Error");//success/error/info/warning
			} else if (json.statusCode == NUI.statusCode.timeout) {
				window.location = NUI._set.loginUrl;
			} else {
				if(json.message){
					toastr["success"](json.message,"Success");//success/error/info/warning
				}
				
				if(json.formSubmit){
					divSearch($('#'+json.formSubmit), json.rel);
				}
				if(json.forwardUrl){
					$('#'+json.rel).loadUrl(json.forwardUrl);
				}
			};
			
		}
	},

	init:function(loginUrl){
		this._set.loginUrl = loginUrl;
		initEnv();
	}
};


(function($){
	// NUI set regional 
	$.setRegional = function(key, value){
		if (!$.regional) $.regional = {};
		$.regional[key] = value;
	};
	


    jQuery.extend({
        bindStoreZoneGroup:function(storeId, zoneId,groupId,options) {
            console.log(options);
            var zoneUrl =  "/FTRspace-CMS/admin/storeFixture/changeStoreFixtureZoneList.do" ;
            var groupUrl =  "/FTRspace-CMS/admin/storeFixture/changeStoreFixtureGroupList.do" ;

            if(options.zoneUrl){
                zoneUrl =  options.zoneUrl;
            }
            if(options.groupUrl){
                groupUrl =  options.groupUrl;
            }

            $("#"+storeId).on("change",function(){

                $("#"+zoneId).html('<option value='+ ""+'>'+ "--Any--" +'</option>');
                $("#"+groupId).html('<option value='+ ""+'>'+ "--Any--" +'</option>');


                var options ='';
                $.ajax({
                    'url':zoneUrl,
                    'data': {storeId:$(this).val()},
                    'dataType': 'json',
                    'success': function(json) {
                        options+= '<option value='+ ""+'>'+ "--Any--" +'</option>';
                        var data=json.ftrZoneList;
                        var maxFixtureName=json.maxFixtureName;
                        $("#maxFixtureName").val(maxFixtureName);
                        $("#maxFixtureNameTemp").val(maxFixtureName);
                        $.each(data, function(i, v) {
                            var zoneId=v.zoneId;
                            options +='<option value='+zoneId+'>Z'+ zoneId.toString().formatNumber("00") +'</option>';
                        });

                        $("#"+zoneId).html(options);
                    }
                });
            });

            $("#"+zoneId).on("change",function(){
                $("#"+groupId).html('<option value='+ ""+'>'+ "--Any--" +'</option>');
                var storeIdTmp=$("#"+storeId).val();
                var zoneIdVal =$(this).val();
                var options ='';
                $.ajax({
                    'url': groupUrl,
                    'data': {storeIdTmp:storeIdTmp,zoneId:zoneIdVal},
                    'dataType': 'json',
                    'success': function(data) {
                        options+= '<option value='+ ""+'>'+ "--Any--" +'</option>';
                        $.each(data, function(i, v) {
                            var groupId=v.groupId;
                            var groupIdStr="G"+groupId.toString().formatNumber("000");
                            options +='<option value='+ groupId+'>'+ groupIdStr +'</option>';
                        });
                        $("#"+groupId).html(options);
                    }
                });

                console.log("zoneId change");
            });

            $("#"+groupId).on("change",function(){
                console.log("groupId change");
            });


            return true;
        } ,

        setApDiv:function () {

        }
    });


	$.fn.extend({
		/**
		 * @param {Object} op: {type:GET/POST, url:ajax request path, data:ajax request parameters, callback:callback function }
		 */
		ajaxUrl: function(op){
			var $this = $(this);

			$.ajax({
				type: op.type || 'GET',
				url: op.url,
				data: op.data,
				cache: false,
				success: function(response){
					var json = NUI.jsonEval(response);
					
					if (json.statusCode==NUI.statusCode.timeout){
						NUI.loadLogin();
					} 
					
					if (json.statusCode==NUI.statusCode.error){
						if (json.message) toastr["error"](json.message,"Error");
					} else {
						$this.html(response).initUI();
						if ($.isFunction(op.callback)) op.callback(response);
					}
				},
				error: NUI.ajaxError,
				statusCode: {
					503: function(xhr, ajaxOptions, thrownError) {
						alert(NUI.msg("statusCode_503") || thrownError);
					}
				}
			});
		},
		loadUrl: function(url,data,callback){
			$(this).ajaxUrl({url:url, data:data, callback:callback});
		},
		initUI: function(){
			return this.each(function(){
				if($.isFunction(initUI)) initUI(this);
			});
		},
		/**
		 * output firebug log
		 * @param {Object} msg
		 */
		log:function(msg){
			return this.each(function(){
				if (console) console.log("%s: %o", msg, this);
			});
		}  ,
		/**
		 * output firebug log
		 * @param {Object} msg
		 */
		tableSort:function(headers,fields){
            var sortName = $("#_sortName").val();
            var sortValue = $("#_sortValue").val();
           for(var i=0;i<headers.length;i++){
               var $el = $(this).find("th").eq(headers[i]);
               if($el){
                   var sort;
                   if(fields[i]==sortName){
                       sort =sortValue;
                   }else{
                       sort ='asc';
                   }
                   $el.addClass("sortField") ;
                   $el.append("<a class='sort ftr-"+sort+"' data-fid='"+fields[i]+"' data-sort='"+sort+"'>&nbsp;</a>");
               }
           }
            $('.sortField').bind("click",function(){
                 var action = $("#pagerForm").attr("action");
                 var fid = $(this).find("a").attr("data-fid");
                 var sort = $(this).find("a").attr("data-sort")=='asc'?'desc':'asc';
                 $("#_sortName").val(fid);
                 $("#_sortValue").val(sort);
                 $("#pagerForm").submit();
            }) ;

		}
	});
	
	/**
	 * extend String function
	 */
	$.extend(String.prototype, {
		replaceTm:function($data){
			if (!$data) return this;
			return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})","g"), function($1){
				return $data[$1.replace(/[{}]+/g, "")];
			});
		},
		replaceTmById:function(_box){
			var $parent = _box || $(document);
			return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})","g"), function($1){
				var $input = $parent.find("#"+$1.replace(/[{}]+/g, ""));
				return $input.val() ? $input.val() : $1;
			});
		},
		isFinishedTm:function(){
			return !(new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this)); 
		} ,

        formatNumber:function(pattern){
            var num = this;
            var strarr = num?num.toString().split('.'):['0'];
            var fmtarr = pattern?pattern.split('.'):[''];
            var retstr='';

            // 整数部分
            var str = strarr[0];
            var fmt = fmtarr[0];
            var i = str.length-1;
            var comma = false;
            for(var f=fmt.length-1;f>=0;f--){
                switch(fmt.substr(f,1)){
                    case '#':
                        if(i>=0 ) retstr = str.substr(i--,1) + retstr;
                        break;
                    case '0':
                        if(i>=0) retstr = str.substr(i--,1) + retstr;
                        else retstr = '0' + retstr;
                        break;
                    case ',':
                        comma = true;
                        retstr=','+retstr;
                        break;
                }
            }
            if(i>=0){
                if(comma){
                    var l = str.length;
                    for(;i>=0;i--){
                        retstr = str.substr(i,1) + retstr;
                        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;
                    }
                }
                else retstr = str.substr(0,i+1) + retstr;
            }

            retstr = retstr+'.';
            // 处理小数部分
            str=strarr.length>1?strarr[1]:'';
            fmt=fmtarr.length>1?fmtarr[1]:'';
            i=0;
            for(var f=0;f<fmt.length;f++){
                switch(fmt.substr(f,1)){
                    case '#':
                        if(i<str.length) retstr+=str.substr(i++,1);
                        break;
                    case '0':
                        if(i<str.length) retstr+= str.substr(i++,1);
                        else retstr+='0';
                        break;
                }
            }
            return retstr.replace(/^,+/,'').replace(/\.$/,'');
        }


	});


    jQuery.extend(jQuery.validator.messages, {
        TwoCharactersAllowed : "Two characters allowed."

    });
    jQuery.validator.addMethod("TwoCharactersAllowed", function(value, element) {
        return this.optional(element) || (/^[0-9a-zA-Z]+$/.test(value) && value.length==2);
    }, jQuery.format(jQuery.validator.messages["TwoCharactersAllowed"]));

    $.validator.addClassRules("TwoCharacters", {
        TwoCharactersAllowed: true
    });


})(jQuery);
