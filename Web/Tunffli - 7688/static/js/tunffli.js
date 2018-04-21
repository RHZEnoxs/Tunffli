$( document ).ready(function() {
    //   --- Init * --- 
//    $("#signin-view").hide();
    $("#index-view").hide();
    $("#NetSysInfo-view").hide();
    
//    $("#ap_mode_view").show();
//    $("#sta_mode_view").hide();
    
    if(getCookie("Tunffli_Session") != null){
		$("#index-view").slideToggle(1000);
        $("#signin-view").slideToggle(1000);
		session = getCookie("Tunffli_Session");
		$("#Signin_NewPasswd").val(getCookie("Tunffli_Password"));
		rpc_netstate_load("wan");
		rpc_system_load();
		rpc_wifi_load();
		rpc_wifi_ap_scan();
//		$("#sys-info").html(session);
	}
    
    
    //  --- Index-View *---
    $("#btn-login,#btn-logout" ).click(function(){
        if($("#index-view").is(":hidden")){
            rpc_login("root", $("#Signin_Password").val());
        }else{
            clear_Cookie("Tunffli_Session");
            clear_Cookie("Tunffli_Password");
            rpc_logout();
        }
	});
    
    $("#Card-NetSysInfo,#btn-back-nsi").click(function(){
        $("#index-view").slideToggle(1000);
        $("#NetSysInfo-view").slideToggle(1000);
	});
    
    $("#btn-sw-rly").click(function(){//ip_address
        window.open("http://"+document.domain+":5000/switch_relay");
    });
    
    $("#btn-msc-play").click(function(){
        window.open("http://"+document.domain+":5000/music_madplay");
    });
    
    $("#btn-wcm-mjpg").click(function(){
        window.open("http://"+document.domain+":5000/webcam_streamer");
    });
    
    $("#btn-rsa-mqtt").click(function(){
        window.open("http://"+document.domain+":5000/Safty");
    });
    
    $("#btn-light" ).click(function(){
        if($("#canv").is(":hidden")){
            $("#light-sta").text("ON");
        }else{
            $("#light-sta").text("OFF");
        }
        $("#canv").fadeToggle(500);
	});
    $("#btn-about").click(function(){ 
        window.open("templates/About.html");
    });
    
    $(".div-card-outside").hover(function(){
				$(this).addClass('div-card-inside');	
                $(this).children(".btn-circle-outside").addClass('btn-circle-inside');
			},function(){
				$(this).removeClass('div-card-inside');	
                $(this).children(".btn-circle-outside").removeClass('btn-circle-inside');
			}
		);
    
    // --- UI Design *---
    // --- Network System Information ---
    $( 'input[name="wifi_mode"]:radio' ).change(function(){
        if(this.value == "ap"){
            $("#ap_mode_view").show();
            $("#sta_mode_view").hide();
            rpc_wifi_mode("ap");
        }
        else if(this.value == "sta"){
            $("#ap_mode_view").hide();
            $("#sta_mode_view").show();
            rpc_wifi_mode("sta");
        }
    });
    $("#btn-config-hostname").click(function(){
        rpc_hostname($("#Hostname").val());
    });
    $("#btn-config-password").click(function(){
        rpc_passwd($("#Signin_NewPasswd").val()); 
        $("#index-view").show();
        $("#NetSysInfo-view").hide();
		clear_Cookie("Tunffli_Session");
		clear_Cookie("Tunffli_Password");
		rpc_logout(); 
    });
    $("#btn-scan-wifi").click(function(){
        rpc_wifi_ap_scan();
    });
    $("#btn-config-network").click(function(){
        $("#index-view").show();
        $("#NetSysInfo-view").hide();
        var wifi_mode = $('input[name=wifi_mode]:checked').val();
        alert(wifi_mode);
        if(wifi_mode == "sta"){
            alert($("#Scan_NetWork").val() + ":" + $("#sta_key").val());
			rpc_wifi("sta", $("#Scan_NetWork").val(),$("#sta_key").val());
		}else{
            alert($("#ap_ssid").val() + ":" + $("#ap_key").val());
			rpc_wifi("ap", $("#ap_ssid").val(), $("#ap_key").val());
		}
    });
    
});