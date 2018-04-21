$( document ).ready(function() {
   
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
     $("#dlg_btnOK").click(function(){
        ctrl_access("update");
    });                                     
           
    function init_device_info(){
        $.getJSON("/switch_relay/device_info", function(data) {   
            $("#input-dev01").val(data.dev_name[0]);
            $("#input-dev02").val(data.dev_name[1]);
            $("#input-dev03").val(data.dev_name[2]);
            $("#input-dev04").val(data.dev_name[3]);
            $("#input-dev05").val(data.dev_name[4]);
            $('#table-dev_inf > tbody > tr').each(function(i){
                $(this).find("td").eq(1).text(data.dev_name[i]);
            });
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
                    if(ctrl != "update"){
                        ctrl_switch(ctrl,data.result);
                    }else{
                        update_info();
                    }
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
    var json_array_name = new Array();
    function update_info(){
        json_array_name[0] = $("#input-dev01").val();
        json_array_name[1] = $("#input-dev02").val();
        json_array_name[2] = $("#input-dev03").val();
        json_array_name[3] = $("#input-dev04").val();
        json_array_name[4] = $("#input-dev05").val();
        
        $.ajax({
                url: '/switch_relay/update_sw_rly',
                data:{
                  action: 'update_sw_rly',
                  dev_name: json_array_name
                },
                type: 'POST',
                success: function(data){
                    if(data.result == "OK"){
                        $("#Dialog_Setting").modal('toggle');
                        init_device_info();
                    }
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }
});