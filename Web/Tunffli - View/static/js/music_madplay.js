$( document ).ready(function() {
    
    
//    var Array_playlist = new Array();
//    var Array_musiclist = new Array();
//    var Array_musicadrs = new Array();
    var Array_playlist = ["Default","Girls' Generation"];
    var Array_musiclist = ["Oh !!!","Mr.taxi"];
    var Array_musicadrs = new Array();
    var Array_filefold = ['Desktop', 'Enoxs', 'RHZ'];
    var Array_filefile = [['desktop_1.mp3', 'desktop_2.mp3'], ['enoxs_1.mp3'], ['rhz_1.mp3'], 'root_1.mp3', 'root_2.mp3', 'root_3.mp3'];
    
    init_playlist();
    init_filepath();
    function init_playlist(){
//        $.getJSON("/music_madplay/playlist", function(data) {   
//            Array_playlist = data.PlayList;
//            $("#panel-playlist").html("");
//            for(var i=0;i<Array_playlist.length;i++){
//                $("#panel-playlist").append(" <a id='ply_lst_"+i+"' href='#' class='list-group-item'>"+Array_playlist[i]+"</a>");
//            }
//            for(var i=0;i<Array_playlist.length;i++){
//                $("#ply_lst_"+i).click(function(){
//                    init_musiclist($(this).text());
//                });
//            }
//            init_musiclist(Array_playlist[0]);
//        });	
        //   --- demo-View ---
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
        
    }
    function init_musiclist(which){
//        $.ajax({
//                url: '/music_madplay/musiclist',
//                data:{
//                  pylst: which
//                },
//                type: 'POST',
//                success: function(data){
//                    Array_musiclist = data.filename;
//                    Array_musicadrs = data.filepath;
//                    $("#panel-musiclist").html("");
//                    for(var i=0;i<Array_musiclist.length;i++){
//                        $("#panel-musiclist").append(" <a href='#' class='list-group-item'>"+Array_musiclist[i]+"</a>");
//                    }
//                },
//				error: function(xhr) {
//      						alert('Ajax request 發生錯誤' );
//    					}
//                });
        //   --- demo-View ---
        $("#panel-musiclist").html("");
                for(var i=0;i<Array_musiclist.length;i++){
                    $("#panel-musiclist").append(" <a href='#' class='list-group-item'>"+Array_musiclist[i]+"</a>");
        }  
        
    }
    function init_filepath(){
        filepath_position(-1);
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
                $("#panel-filepath").append(" <a id='file_pos_"+i+"' href='#' class='list-group-item'><i class='glyphicon glyphicon-music'></i> &nbsp;"+Array_filefile[i]+"</a>");
                $("#file_pos_"+i).click(function(){
                    alert($(this).text());
                });
            }
        }else{
            $("#panel-filepath").append(" <a id='fold_pos_"+i+"' href='#' class='list-group-item list-group-item-warning'><i class='glyphicon glyphicon-folder-open'></i>&nbsp;<span>..</span></a>");
            $("#fold_pos_"+i).click(function(){
                filepath_position(-1);
            });
            for(var i=0;i<Array_filefile[position].length;i++){
                $("#panel-filepath").append(" <a id='file_pos_"+i+"' href='#' class='list-group-item'><i class='glyphicon glyphicon-music'></i> &nbsp;"+Array_filefile[position][i]+"</a>");
                $("#file_pos_"+i).click(function(){
                    alert($(this).text());
                });
            } 
        }
        
    }


});