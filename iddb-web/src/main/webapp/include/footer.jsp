<%@page import="iddb.core.util.SystemProperties"%>
<%@page import="java.util.Properties"%>
<div class="footer">
	<div id="ffox">
		<a href='http://www.mozilla.org/firefox?WT.mc_id=aff_en05&WT.mc_ev=click'><img src='http://www.mozilla.org/contribute/buttons/110x32bubble_g.png' alt='Firefox Download Button' border='0' /></a>
	</div>
     <%
     	String version;
     	try {
         	version = SystemProperties.applicationVersion().getVersion() + "." + SystemProperties.applicationVersion().getBuild();
     	} catch (Exception e) {
     		version = "-";
     	}
     %>
     <div class="left">&copy; 2011 Shonaka & SGT. Based on the idea of lakebodom. v<% out.print(version); %>. Times are displayed in UTC-3.</div>
     <div class="clearer"><span></span></div>
 </div>
</div>

<div style="width: 728px; height: 90px; margin: 25px auto 2px;">
<script type="text/javascript"><!--
google_ad_client = "ca-pub-6692965674688630";
/* IPDB */
google_ad_slot = "1813404437";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
</div>
</div>
<script type="text/javascript">
$("#context-loader").hide();
$("#main").fadeIn("slow");
</script>
</body>
</html>
