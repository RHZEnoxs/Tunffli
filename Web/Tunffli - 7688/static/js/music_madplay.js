$( document ).ready(function() {
    
    
    var Array_playlist = new Array();
    var Array_musiclist = new Array();
    var Array_musicadrs = new Array();
    var Array_filefold = new Array();
    var Array_filefile = new Array();
//    var Array_playlist = ["Default","Girls' Generation"];
//    var Array_musiclist = ["Oh !!!","Mr.taxi"];
//    var Array_filefold = ['Desktop', 'Enoxs', 'RHZ'];
//    var Array_filefile = [['desktop_1.mp3', 'desktop_2.mp3'], ['enoxs_1.mp3'], ['rhz_1.mp3'], 'root_1.mp3', 'root_2.mp3', 'root_3.mp3'];
    init_media();
    $("#media_start").click(function(){
        if(media_flag){
            media_control("pause","");
        }else{
            media_control("play","");
        }
    });
    $("#media_stop").click(function(){
        media_control("stop","");
    });
    $("#media_back").click(function(){
        media_control("back","");
    });
    $("#media_next").click(function(){
        media_control("next","");
    });
    function init_media(){
        media_control("status","");
        init_playlist();
        init_filepath();
    }
    function init_playlist(){
        $.getJSON("/music_madplay/playlist", function(data) {   
            Array_playlist = data.PlayList;
            $("#panel-playlist").html("");
            for(var i=0;i<Array_playlist.length;i++){
                $("#panel-playlist").append(" <a id='ply_lst_"+i+"' href='#' class='list-group-item'>"+Array_playlist[i]+"</a>");
            }
            for(var i=0;i<Array_playlist.length;i++){
                $("#ply_lst_"+i).click(function(){
                    init_musiclist($(this).text());
                });
            }
            init_musiclist(Array_playlist[0]);
        });
    }
    function init_musiclist(which){
        $.ajax({
                url: '/music_madplay/musiclist',
                data:{
                  pylst: which
                },
                type: 'POST',
                success: function(data){
                    Array_musiclist = data.filename;
                    Array_musicadrs = data.filepath;
                    $("#panel-musiclist").html("");
                    for(var i=0;i<Array_musiclist.length;i++){
                        $("#panel-musiclist").append(" <a href='#' class='list-group-item'>"+Array_musiclist[i]+"</a>");
                    }
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }
    function init_filepath(){
        $.getJSON("/music_madplay/filepath", function(data) {   
            Array_filefold = data.fold_list;
            Array_filefile = data.file_list;
            filepath_position(-1);
        });
//        filepath_position(-1);
    }
    
    function filepath_position(position){
        $("#panel-filepath").html("");
        if(position == -1){
            for(var i=0;i<Array_filefold.length;i++){
                $("#panel-filepath").append(" <a id='fold_pos_"+i+"' href='#' class='list-group-item list-group-item-warning'><i class='glyphicon glyphicon-folder-open'></i>&nbsp;<span>"+Array_filefold[i]+"</span></a>");
                $("#fold_pos_"+i).click(function(){
                    fold_position = Array_filefold.indexOf($(this).find("span").text());
                    filepath_position(fold_position);
                });
            }
            for(var i=Array_filefold.length;i<Array_filefile.length;i++){
                $("#panel-filepath").append(" <a id='file_pos_"+i+"' href='#' class='list-group-item'><i class='glyphicon glyphicon-music'></i> &nbsp;<span>"+Array_filefile[i]+"<span></a>");
                $("#file_pos_"+i).click(function(){
                    media_control("play",$(this).find("span").text());
                });
            }
        }else{
            $("#panel-filepath").append(" <a id='fold_pos_"+i+"' href='#' class='list-group-item list-group-item-warning'><i class='glyphicon glyphicon-folder-open'></i>&nbsp;<span>..</span></a>");
            $("#fold_pos_"+i).click(function(){
                filepath_position(-1);
            });
            for(var i=0;i<Array_filefile[position].length;i++){
                $("#panel-filepath").append(" <a id='file_pos_"+i+"' href='#' class='list-group-item'><i class='glyphicon glyphicon-music'></i> &nbsp;<span>"+Array_filefile[position][i]+"</span></a>");
                $("#file_pos_"+i).click(function(){
                    media_control("play",Array_filefold[position] + "/" +$(this).find("span").text());
                });
            } 
        }
        
    }
    var media_flag;
//    status play playlist pause stop next back
    function media_control(ctrl,file){
        $.ajax({
                url: '/music_madplay/media_ctrl',
                data:{
                  action : ctrl,
                  filepath: file
                },
                type: 'POST',
                success: function(data){
                    media_flag = data.result;
                    if(data.result){
                        $("#media_start").html("<i class='glyphicon glyphicon-pause'>");
                    }else{
                        $("#media_start").html("<i class='glyphicon glyphicon-play'>");
                    }
                },
				error: function(xhr) {
      						alert('Ajax request 發生錯誤' );
    					}
                });
    }


});