/* SEARCH */
function doSearch(query, server) {
	var url = dutils.urls.resolve('search', {'query': query});
	if (server != undefined) {
		url = url + "?server=" + server; 	
	}
	window.location = url;
}

function pagination(parent, offset, hasMore, pages, total, func) {
    prev = $(parent).find("#prev-alias");
    $(prev).unbind('click');
	if (offset == 1) {
		$(prev).removeClass('prev').addClass('prev-na');
	} else {
	    $(prev).removeClass('prev-na').addClass('prev');
	    $(prev).click({'offset': offset-1}, func);
	}
    next = $(parent).find("#next-alias");
    $(next).unbind('click');
	if (hasMore) {
		$(next).removeClass('next-na').addClass('next');
		$(next).click({'offset': offset+1}, func);
	} else {
	    $(next).removeClass('next').addClass('next-na');
	}
	if (pages == 0) offset = 0;
	$(parent).find("#curr-alias").html("{0}-{1}".format(offset,pages));
	$(parent).find("#total-alias").html(total);
}
function getAlias(key, offset, callback) {
	url = dutils.urls.resolve('alias', { key: key}) + "?o=" + offset;
	$.getJSON(url, function(data) {
		var rows = new Array();
		$.each(data.items, function(key, value) {
			var html = "";
			html += "<tr class=\"aliasrow\">";
            html += "<td><a href=\"" + value.nickname_url + "\">";
            html += value.nickname;
            html += "</a></td>";
			html += "<td>";
			html += value.updated;
			html += "</td>";
			html += "<td style='text-align: right;'>";
			html += value.count;
			html += "</td>";
			html += "</tr>";
			rows[key] = $(html);
		});
		callback(data.offset, rows, data.hasMore, data.pages, data.total);
	});
}
function addExtraIpData(value) {
	return "&nbsp;<a target=\"_blank\" href=\"" + value.whois + "\" title=\"Whois\" class=\"icon vcard\"></a>" + Encoder.htmlDecode(value.geo_img);
}
function getAliasIP(key, offset, callback) {
	url = dutils.urls.resolve('aliasip', { key: key}) + "?o=" + offset;
	$.getJSON(url, function(data) {
		var rows = new Array();
		$.each(data.items, function(key, value) {
			var html = "";
			html += "<tr class=\"aliasrow\">";
            html += "<td><a href=\"";
            html += value.ip_url;
            html += "\">";
            html += value.ip + "</a>" + addExtraIpData(value);
			html += "</td>";
			html += "<td>";
			html += value.updated;
			html += "</td>";
			html += "<td style='text-align: right;'>";
			html += value.count;
			html += "</td>";
			html += "</tr>";
			rows[key] = $(html);
		});
		callback(data.offset, rows, data.hasMore, data.pages, data.total);
	});
}

$(function() {
	$(".plus").each(function(key) {

		$(this).click(function() {

			var elem = $(this);
			var type = elem.attr("alt");
			if (type == "ip") {
				var key = elem.attr("id").substring("plus-ip-".length);
				var reminus = "minus-" + key;
			} else {
				var key = elem.attr("id").substring("plus-".length);
				var reminus = "minus-ip-" + key;
			}
			
			$("#"+reminus+":visible").click();
			
			var minus = elem.next();
			var sibling = elem.parent().parent().next();

			var updateFun = function(offset, rows, hasMore, pages, total) {

				var table = sibling.find("table");

				$(table).find("tbody").html("");
				$.each(rows, function(key, value) {
					$(table).find("tbody").append(value);
				});

				var func = function(e) {
					if (type=="ip") {
						getAliasIP(key, e.data.offset, updateFun);
					} else {
						getAlias(key, e.data.offset, updateFun);
					}
				};

				pagination(table, offset, hasMore, pages, total, func);
				
				elem.hide();
				minus.show();
				sibling.show();
			};

			if (type=="ip") {
				getAliasIP(key, 1, updateFun);
			} else {
				getAlias(key, 1, updateFun);
			}

		});
	});

	$(".minus").each(function(key) {
		$(this).click(function() {
			var elem = $(this);

			var plus = elem.prev();
			var sibling = elem.parent().parent().next();

			sibling.hide();
			sibling.find(".aliasrow").remove();
			elem.hide();
			plus.show();
		});
	});

	$("#copycontent").click(function() {SelectText("copycontent")});
	
    $('.bbcode').nyroModal();
    
    $('.advsearch > a').click(function(e) {
    	$('#advsearch').toggle();
    	$("#advsearch").offset({left: e.pageX});
    });
    
    $(".search").keypress(function(e) {
    	if ( e.which == 13 ) {
    		if ($(this).val().length > 0) {
    			var server; 
    			if ($("#advsearch").is(":visible")) {
    				server = $("#advsearch").find("input[name='server']:checked").val();	
    			}
    			doSearch($(this).val(), server);
    		}
    	}
    });
    
    $("#advsearch").find(":button").click(function() {
		var query = $('input[name=q]').val();
		var server = $("#advsearch").find("input[name='server']:checked").val();
		if (query.length > 0) {
			doSearch(query, server);
		}
	});
    
});    

/*--- PLAYER INFO -------------*/

function paginationPlayer(key, offset, hasMore, pages, total) {
    prev = $("#prev-"+key);
    $(prev).unbind('click');
	if (offset == 1) {
		$(prev).removeClass('prev').addClass('prev-na');
	} else {
	    $(prev).removeClass('prev-na').addClass('prev');
	    $(prev).click({'offset': offset-1, 'elem': key}, function(e) {getHTML(e.data.elem,e.data.offset);});
	}
    next = $("#next-"+key);
    $(next).unbind('click');
	if (hasMore) {
		$(next).removeClass('next-na').addClass('next');
		$(next).click({'offset': offset+1, 'elem': key}, function(e) {getHTML(e.data.elem,e.data.offset);});
	} else {
	    $(next).removeClass('next').addClass('next-na');
	}
	if (pages == 0) offset = 0;
	$("#curr-"+key).html("{0}-{1}".format(offset,pages));
	$("#total-"+key).html(total);
}
function getHTML(key, offset) {
	if (key == 'alias') {
		getAliasPlayer(offset);
	} else {
	    getAliasPlayerIP(offset);
	}
}
function getAliasPlayer(offset) {
    url = dutils.urls.resolve('alias', { key: clientKey}) + "?o=" + offset;
	$.getJSON(url , function(data) {
		$("#tablealias").html("");
		$.each(data.items, function(key, value) {
			var html = "";
			html += "<tr class=\"aliasrow\">";
			html += "<td><a href=\"" + value.nickname_url + "\">";
			html += value.nickname;
			html += "</a></td>";
			html += "<td>";
			html += value.updated;
			html += "</td>";
			html += "<td style='text-align: right;'>";
			html += value.count;
			html += "</td>";
			html += "</tr>";
			$("#tablealias").append(html);
		});
		paginationPlayer('alias', data.offset, data.hasMore, data.pages, data.total);
	});
}
function getAliasPlayerIP(offset) {
    url = dutils.urls.resolve('aliasip', { key: clientKey}) + "?o=" + offset;
	$.getJSON(url, function(data) {
		$("#tableip").html("");
		$.each(data.items, function(key, value) {
			var html = "";
			html += "<tr class=\"aliasrow\">";
			html += "<td><a href=\"";
			html += value.ip_url;
			html += "\">";
			html += value.ip + "</a>" + addExtraIpData(value);
			html += "</td>";
			html += "<td>";
			html += value.updated;
			html += "</td>";
			html += "<td style='text-align: right;'>";
			html += value.count;
			html += "</td>";
			html += "</tr>";
			$("#tableip").append(html);
		});
		paginationPlayer('ip', data.offset, data.hasMore, data.pages, data.total);
	});
}