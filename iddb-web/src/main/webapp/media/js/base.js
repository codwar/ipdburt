$(document).ready(
		function() {
		    $('body').ajaxStart(function() {
		        showContextLoader();
		    }); 
		    $('body').ajaxStop(function() {
		        $.loading(false);      
		    });
			$('ul.messages li').each(
					function() {
						$(this).append(
								"<span class='close_button'></span>");
					});
			$('.close_button').click(function() {
				$(this).parent().remove();
			});
			$(".focus").each(
					function() {
						$(this).focus();
						$(this).select();
					}
			);
            $(window).unload( function () { showContextLoader(); } );
            
            updateServerList();

            $(".tip").tipTip({delay: 200});

});

function showContextLoader() {
    $.loading(true, {text: 'Enviando solicitud...', loadingClass: 'context-loader', update: {texts: ['Por favor, aguarde...', 'Por favor reintente.']}});
}
function checkElements() {
    if ($("input:checked").length > 0) {
  	  $("#copyall").css("visibility", "visible");
    } else {
      $("#copyall").css("visibility", "hidden");
    }	
}
function SelectText(element) {
    var text = document.getElementById(element);
    if ($.browser.msie) {
        var range = document.body.createTextRange();
        range.moveToElementText(text);
        range.select();
    } else if ($.browser.mozilla || $.browser.opera) {
        var selection = window.getSelection();
        var range = document.createRange();
        range.selectNodeContents(text);
        selection.removeAllRanges();
        selection.addRange(range);
    } else if ($.browser.safari) {
        var selection = window.getSelection();
        selection.setBaseAndExtent(text, 0, text, 1);
    }
}
function updateServerList() {
    if ($.find('.fetch-server').length > 0) {
    	var url = dutils.urls.resolve('serverinfo', {});
    	$.post(url, $("input[name=key]").serializeArray(), function(d) {
    		var res = ($.parseJSON(d));
    		for (var i = 0; i < res.list.length; i++) { 
    		    server = res.list[i];
    		    if (server.count == '0') {
    		    	value = "<span>"+server.count+"</span>";
    		    } else {
    		    	value = "<span style=\"font-weight: bold;\">"+server.count+"</span>";
    		    }
    		    $(".fetch-server[alt="+server.key+"]").replaceWith(value);
    		}
    	});       	
    }
}
String.prototype.format = function () {
	var args = arguments;
	return this.replace(/\{(\d+)\}/g, function (m, n) { return args[n]; });
};