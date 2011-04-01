$(document).ready(
		function() {
		    $('body').ajaxStart(function() {
		        $.loading(true, {text: 'Cargando ...'});
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
			})
		});