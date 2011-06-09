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
			//$("#donar").makeFloat({x: 'current', y: 'current', speed: 'fast'});
			$(".focus").each(
					function() {
						$(this).focus();
						$(this).select();
					}
			);
            $(window).unload( function () { showContextLoader(); } );
            $(".fetch-server").each(function() {
            	data = {key: $(this).attr("alt")};
            	$.post("/app/fetchserver",data, function(d) {
            		rs = ($.parseJSON(d));
            		if (rs.error) {
            			$(".fetch-server[alt="+rs.key+"]").attr("src","/media/images/exclamation.png");
            		} else {
            			$(".fetch-server[alt="+rs.server.key+"]").replaceWith(rs.server.count);
            		}
            	});
            });
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
                           rowText = rowText + " | " + v;
                       });
                       text = text + rowText.substring(3) + "\n<br/>";
                   }
               );
               if (text != "") {
            	   $("#copycontent").html(text);
            	   $("#copyTrigger").nm().nmCall();
               }
           });
           $("#copycontent").click(function() {SelectText("copycontent")});
           checkElements();
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
String.prototype.format = function () {
	var args = arguments;
	return this.replace(/\{(\d+)\}/g, function (m, n) { return args[n]; });
};
