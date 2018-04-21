$( document ).ready(function() {
    var webcam_flag = false;
    webcam_control("status");
    $("#btn_view").click(function(){
        window.open("http://"+document.domain+":8080?action=stream");
    });
    $("#btn_webcam").click(function(){
        if(webcam_flag){
            webcam_control("webcam_off");
        }else{
            webcam_control("webcam_on");
        }
        
    });
    function webcam_control(ctrl){
        $.ajax({
                url: '/webcam_streamer/webcam_ctrl',
                data:{
                  action : ctrl
                },
                type: 'POST',
                success: function(data){
                    webcam_flag = data.result;
                    if(webcam_flag){
                        $("#btn_webcam").text("Stop");
                        $("#btn_webcam").addClass("btn-danger");
                        $('#webcam-view').attr('src',"http://"+document.domain+":8080?action=stream");
                    }else{
                        $("#btn_webcam").text("Start");
                        $("#btn_webcam").removeClass("btn-danger");
                        $('#webcam-view').attr('src','../static/img/flask_camer.png');
                    }
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }
 });