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
								"<img class='close_button' src='/media/images/close.gif'/>");
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
            
            $(".search").keypress(function(e) {
            	if ( e.which == 13 ) {
            		if ($(this).val().length > 0) window.location = dutils.urls.resolve('search', {'query': $(this).val()});
            	}
            });
/*
           $(":checkbox").change(checkElements);
           $("#multiselect").click(function(){
        	   $("input[name='selector']").attr('checked',$(this).is(':checked'));
           });
           $("#copyall").click(function() {
               var text = "";
               $("input[name='selector']").filter(":checked").each(
                   function() {
                       var id = $(this).val();
                       var rowText = "";
                       $("#"+id).children("td").not('[copiable="false"]').each(function() {
                           var v = $.trim($(this).text());
                           if (v.substring(0,3)=='[+]') {
                               v = $.trim(v.substring(12));
                           }
                           rowText = rowText + "[td]" + v + "[/td]";
                       });
                       text = text + "[tr]" + rowText + "[/tr]<br/>";
                   }
               );
               if (text != "") {
            	   $("#copycontent").html("[table]"+text+"[/table]");
            	   $("#copyTrigger").nm().nmCall();
               }
           });
           checkElements();
*/           
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
    		    $(".fetch-server[alt="+server.key+"]").replaceWith("<span>"+server.count+"</span>");
    		}
    	});       	
    }
}
String.prototype.format = function () {
	var args = arguments;
	return this.replace(/\{(\d+)\}/g, function (m, n) { return args[n]; });
};