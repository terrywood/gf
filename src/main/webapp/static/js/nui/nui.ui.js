function initEnv() { 

	if(!$.support.leadingWhitespace){
		try {
			document.execCommand("BackgroundImageCache", false, true);
		}
		catch(e){}
	}
	
	  $.uniform = {
			    options: {
			      selectClass:   'selector',
						radioClass: 'radio',
						checkboxClass: 'checker',
						fileClass: 'uploader',
						filenameClass: 'filename',
						fileBtnClass: 'action',
						fileDefaultText: 'No file selected',
						fileBtnText: 'Choose File',
						checkedClass: 'checked',
						focusClass: 'focus',
						disabledClass: 'disabled',
						activeClass: 'active',
						hoverClass: 'hover',
						useID: true,
						idPrefix: 'uniform',
						resetSelector: false
			    },
			    elements: []
			  };
	  options = $.extend($.uniform.options);



	var ajaxbg = $("#progressBar");
	ajaxbg.hide();
	
	if('undefined' == typeof(document.body.style.maxHeight)){
		$.selectOpacity = false;
	}else{
		$.selectOpacity = true;
	}

	
	$(document).ajaxStart(function() {
		ajaxbg.show();
	}).ajaxStop(function() {
		ajaxbg.hide();
	}).ajaxError(function() {
		ajaxbg.hide();
	});
	
	initUI();
	initSidebar();
	loadInitPage();
	
	$(window).on('resize',function(){
		resetContentSize();
	});
}

function initSidebar() {
	//
	$('#sidebar ul li, .header div ul li, #sidebar ul li .submenu ul li').click(function() {
		//LOAD 		
		var hrefx = $(this).find("a").attr("hrefx");
		
		if(hrefx){
			$("#content").loadUrl(hrefx, {}, function() {
			});		
			
			
			var submenu = $(this).parents(".submenu");
			if ($(submenu).html() != null) {
				$('#sidebar ul li').removeClass("active");
				$(submenu).addClass("active");
			} else {
				$('#sidebar ul li').removeClass("active");
				
				var submenus = $('#sidebar li.submenu ul');
				var submenus_parents = $('#sidebar li.submenu');
				if(($(window).width() > 976) || ($(window).width() < 768)) {
					submenus.slideUp();			
				} else {
					submenus.fadeOut(150);			
				}
				submenus_parents.removeClass('open');	
			}
			
			
			$(this).toggleClass("active");	
			
			
			
		}		
	});
}

function loadInitPage(){
	var hash = window.location.hash;
	if(hash){
		$("#sidebar ul li").each(function(){
			var $this = $(this);
			
			var href = $this.find("a").attr("href");
			if(href==hash){
				var hrefx = $this.find("a").attr("hrefx");
				
				if(hrefx){
					$("#content").loadUrl(hrefx);		
					$this.parent().find("li").removeClass("active");
					$this.toggleClass("active");	
					
					var submenu = $this.parents('.submenu');
					var submenus = $('#sidebar li.submenu ul');
					var submenus_parents = $('#sidebar li.submenu');
					
					if(($(window).width() > 976) || ($(window).width() < 768)) {
						submenus.slideUp();			
						submenu.slideDown();
					} else {
						submenus.fadeOut(150);			
						submenu.fadeIn(150);
					}
					submenus_parents.removeClass('open');		
					submenu.addClass('open active');
				}	

			}

		});
	}else{
		var $firstLi = $("#sidebar ul li").first();
		var hrefx = $firstLi.find("a").attr("hrefx");
		if(hrefx){
			$("#content").loadUrl(hrefx);		
			$firstLi.parent().find("li").removeClass("active");		
			$firstLi.toggleClass("active");		
		}	
		
	}
}

function initUI(_box) {
	var $p = $(_box || document);
	$("a[target=dialog]", $p).each(function() {
		$(this).click(function(event) {
			var $this = $(this);
			
			var url = unescape($this.attr("href")).replaceTmById();
			NUI.debug(url);

			if (!url.isFinishedTm()) {
				toastr["error"]($this.attr("warn") || NUI.msg("alertSelectMsg"),"Error");//success/error/info/warning				
				return false;
			}
		
			
			$.pdialog.open(url);
			
			return false;
		});
	});
	
	$("a[target=ajax]", $p).each(function() {
		$(this).click(function(event) {
			var $this = $(this);
			
			var rel = $this.attr("rel");
			if(rel){
				var $rel = $("#"+rel);
				$rel.loadUrl($this.attr("href"));
			}
			event.preventDefault();
		});
	});

    $("a[target=ajaxSideBar]", $p).each(function() {
        $(this).click(function(event) {
            var $t = $(this);
            var hrefTarget = $t.attr("hrefSideBar");
            var hrefxTarget = $t.attr("hrefx");

            $("#sidebar ul li").each(function(){
                var $this = $(this);

                var href = $this.find("a").attr("href");
                if(href==hrefTarget){
                    var hrefx = hrefxTarget;

                    if(hrefx){
                        $("#content").loadUrl(hrefx);
                        $this.parent().find("li").removeClass("active");
                        $this.toggleClass("active");

                        var submenu = $this.parents('.submenu');
                        var submenus = $('#sidebar li.submenu ul');
                        var submenus_parents = $('#sidebar li.submenu');

                        if(($(window).width() > 976) || ($(window).width() < 768)) {
                            submenus.slideUp();
                            submenu.slideDown();
                        } else {
                            submenus.fadeOut(150);
                            submenu.fadeIn(150);
                        }
                        submenus_parents.removeClass('open');
                        submenu.addClass('open active');
                    }

                }

            });





        });
    });
	

	$("#dialog > .close").click(function() {
		$.pdialog.close();
		return false;
	});
	
	$('ul.icons li').hover(function() {
		$(this).addClass('ui-state-hover');
	}, function() {
		$(this).removeClass('ui-state-hover');
	});
	

	if($.fn.ajaxTodo){
		$("a[target=ajaxTodo]", $p).ajaxTodo();
	}
	
	if($.fn.combox) {
		$('.combox > select',$p).combox();
	}
	
	$(".combox").select2();
	$("select[name='selectPageSize']").on("select2-selecting", function(event){
		$("#_pageSize").val(event.val);
		$("#pagerForm").submit();
	});
	
	// setup default content height begin
	resetContentSize();
	
	$('.datetimepicker').datetimepicker();
}

//setup content height
function resetContentSize(){
	var contentMinHeight = window.innerHeight - $("#header").height() + 2;
	var sidebar_ul = $("#sidebar").children("ul");
	$("#content").css({"min-height" : contentMinHeight > (sidebar_ul.height() + 200) ? contentMinHeight : sidebar_ul.height() + 200});
}
