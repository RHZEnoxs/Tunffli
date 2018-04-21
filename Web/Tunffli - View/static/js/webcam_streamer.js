$( document ).ready(function() {
    var webcam_flag = false;
//    webcam_control("status");
    $("#btn_view").click(function(){
        window.open("../static/img/flask_camer.png");
    });
    $("#btn_webcam").click(function(){
        if(webcam_flag){
            $("#btn_webcam").text("Start");
            $("#btn_webcam").removeClass("btn-danger");
            $('#webcam-view').attr('src','../static/img/flask_camer.png');
            webcam_flag = false;
        }else{
            $("#btn_webcam").text("Stop");
            $("#btn_webcam").addClass("btn-danger");
            $('#webcam-view').attr('src','../static/img/flask_music.png');
            webcam_flag = true;
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
                    if(ctrl == "status"){
                        alert(data.result);
                    }
                    
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }
 });