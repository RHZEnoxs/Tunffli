$( document ).ready(function() {
    
//    $("#table-dev_inf > tbody > tr").eq(1).find("td").eq(2).text("hello");
    
    $('#table-dev_inf > tbody > tr').each(function(i){
        $(this).find("td").eq(1).text("device - " + i);
        }
    );
    
    
    init_device_info();
    $("#btn_ctrl_1").click(function(){
        ctrl_access(1);
    });
    $("#btn_ctrl_2").click(function(){
        ctrl_access(2);
    }); 
    $("#btn_ctrl_3").click(function(){
        ctrl_access(3);
    }); 
    $("#btn_ctrl_4").click(function(){
        ctrl_access(4);
    }); 
    $("#btn_ctrl_5").click(function(){
        ctrl_access(5);
    }); 
    function init_device_info(){
        $.getJSON("/switch_relay/device_info", function(data) {
				alert(data.dev_01);
				alert(data.dev_02);
				alert(data.dev_03);
				alert(data.dev_04);
				alert(data.dev_05);
			});	
    }
    function ctrl_access(ctrl){
        $.ajax({
                url: '/switch_relay/safety_certificate',
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
                url: '/switch_relay/ctrl_sw_rly',
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