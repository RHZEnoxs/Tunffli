$( document ).ready(function() {
    //   --- Init * --- 
//    $("#signin-view").hide();
//    $( "#radio-sta" ).prop( "checked", "checked");
//    $('input[name=wifi_mode]').filter('[value=sta]').prop('checked', true);
     $('input[name=wifi_mode]').filter('[value=sta]').prop('checked', true);
    init_network();
    function init_network(){
        show = $('input[name=wifi_mode]:checked').val();
//        alert(show);
        if(show == "ap"){
            $("#lable_ap").addClass("active");
        }
        else if(show == "sta"){
            $("#lable_sta").addClass("active");
        }
    }
    
    $("#index-view").hide();
    $("#NetSysInfo-view").hide();
    
    $("#ap_mode_view").show();
    $("#sta_mode_view").hide();
    //  --- Index-View *---
    $("#btn-login,#btn-logout" ).click(function(){
        $("#index-view").slideToggle(1000);
        $("#signin-view").slideToggle(1000);
	});
    
    $("#Card-NetSysInfo,#btn-back-nsi").click(function(){
        $("#index-view").slideToggle(1000);
        $("#NetSysInfo-view").slideToggle(1000);
	});
    
    $("#btn-sw-rly").click(function(){
        window.open("templates/switch_relay.html");
    });
    
    $("#btn-msc-play").click(function(){
        window.open("templates/music_madplay.html");
    });
    
    $("#btn-wcm-mjpg").click(function(){
        window.open("templates/webcam_streamer.html");
    });
    
    $("#btn-rsa-mqtt").click(function(){
        window.open("templates/Safty.html");
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
    // --- NetSysInfo ---
    $( 'input[name="wifi_mode"]:radio' ).change(function(){
        if(this.value == "ap"){
            $("#ap_mode_view").show();
            $("#sta_mode_view").hide();
        }
        else if(this.value == "sta"){
            $("#ap_mode_view").hide();
            $("#sta_mode_view").show();
        }
    });
    $("#btn-network-config").click(function(){
        ctrl_access(1);
//       ctrl_switch(1,"tunffli_90161728");
    });
    function ctrl_access(ctrl){
        $.ajax({
                url: 'http://192.168.1.7:5000/safety_certificate',
                data:{
                  action: 'access'
                },
                type: 'POST',
                success: function(data){
                    ctrl_switch(ctrl,data.result);
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }
    function ctrl_switch(ctrl,access){
        $.ajax({
                url: 'http://192.168.1.7:5000/ctrl_sw_rly',
                data:{
                  action: 'ctrl_mraa',
                  dev_id: ctrl,
                  access: access    
                },
                type: 'POST',
                success: function(data){
//                    alert(data.result);
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }
    
});