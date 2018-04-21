$( document ).ready(function() { 
// --- var --- 變數
	var compentence,user_id,user_name,admin;// innformation
	var category_no,category_name,category_show = new Array(),category_item_count = new Array();//Show_CategoryArray
	var Material_Item_No = new Array();//Show_ItemArray
	var Material_Item_Name = new Array();
	var Material_Item_Model = new Array();
	var Material_Item_Package = new Array();
	var Material_Item_Brand = new Array();
	var Material_Item_Remark = new Array();
	var Material_Item_Quantity = new Array();
	var Material_Pick_Flag = new Array();
	var Material_Pick_ItemNo = new Array();
	var Material_Pick_ItemQuan = new Array();
	var Record_Ex_id,Record_Ex_date,Record_Ex_userID,Record_Ex_name,Record_Ex_remark;
	var Record_Ex_ItNo = new Array(),Record_Ex_ItQuan = new Array(),Record_Ex_show = new Array();//Show_RecordArray
	var Record_Im_id,Record_Im_date,Record_Im_userID,Record_Im_name,Record_Im_remark;
	var Record_Im_ItNo = new Array(),Record_Im_ItQuan = new Array(),Record_Im_show = new Array();
	var material_text;//Text
	var i,j,position,select,choice,count;// Int 
	var material_category_count = 0;//category_count
	var material_item_count = 0;//item_count
	var material_pick_count = 0;//pick_count
	var Ex_record_count = 0,Im_record_count = 0;
	var dialog_category_flag = true; //dialog_category: true -> New,false -> Edit
	var dialog_delete_flag = 0; // 0 nothing, 1 category, 2 item 
	var dialog_input_flag = 0; // 0 nothing , 1 new , 2 edit
	var item_view_flag = false;
	var pick_view_flag = false;
	var shopping_cart_flag = false; // True -> Show , False -> Hide
	var shopping_cart_IO = false; // 1 True -> output , 0 False -> input 
	var material_record_ie; // 0 -> inedx , 1 -> import , 2 -> export  
	var main_index = 0; // 0 index 1 import 2 export
// --- init --- 初始化
	init_flag();
// --- Design-UI --- 
//	minigrid('.export-grid', '.export-grid-item');
//	$("#material-import-panel").hide();
//	$("#material-panel").hide();
	$("#item-view").hide();
	$("#pick-view").hide();
	$("#pick-panel").hide();
	$("#btn-dig-rdCancel").hide();
	$("#btn-dig-rdDelete").hide();
	$("#btn-dig-rdEdit").hide();
	$("#btn-dig-rdPrint").hide();
	$("#btn-dig-rdOk").hide();
	$("#btn-item-new").hide();
	$("#btn-category-edit").hide();
	$("#btn-category-delete").hide();
	$("#dia_it_btnDel").hide();
	$("#dia_it_btnOK").hide();
	

//	minigrid('.export-grid', '.export-grid-item');
//	$("#material-export-panel").hide();
//	minigrid('.import-grid', '.import-grid-item');
//	$("#material-import-panel").hide();
	
// --  Nav_href * -- 
	$("#material_all_list").click(function(){
		if(!item_view_flag && !pick_view_flag && !shopping_cart_flag){
			main_index = 0;
			material_category_list();
			$("#material-panel").show(500);
			$("#material-import-panel").hide(500);
			$("#material-export-panel").hide(500);
		}
		
	});
	$("#material_input_record").click(function(){
		if(!item_view_flag && !pick_view_flag && !shopping_cart_flag){
			main_index = 1;
			material_record_list(0);//入庫紀錄
			$("#material-panel").hide(500);
			$("#material-export-panel").hide(500);
			$("#material-import-panel").show(500);
		}
		if(shopping_cart_flag){
			alert("目前狀態無法切換頁面，請先取消庫存單作業。");
		}
	});
	$("#material_output_record").click(function(){
		if(!item_view_flag && !pick_view_flag && !shopping_cart_flag){
			main_index = 2;
			material_record_list(1);//領料紀錄
			$("#material-panel").hide(500);
			$("#material-import-panel").hide(500);
			$("#material-export-panel").show(500);
		}
		if(shopping_cart_flag){
			alert("目前狀態無法切換頁面，請先取消庫存單作業。");
		}
	});
	$("#new_category").click(function(){
		if(main_index == 0 && compentence == 2){
			$("#Dialog_Category").modal('show');
			$("#CategoryLabel").text("新增料件類別");
			dialog_category_flag = true;
		}
	});
	$("#input_material_list").click(function(){
		if(main_index == 0){
			$("#shoppin_cart_title").text("採購入庫清單");
			Alert_View_Info("<strong>入庫單！</strong>　採購人員可於右側圓形按鈕檢視入庫清單,點選類別中的選擇鈕，可將料件加入清單中。");
			$("#pick-panel").show();
			shopping_cart_flag = true;
			shopping_cart_IO = false;
		}
		
	});
	$("#output_material_list").click(function(){
		if(main_index == 0){
			$("#shoppin_cart_title").text("研發生產領料清單");
			Alert_View_Info("<strong>領料單！</strong>　研發、生產等料件使用人員可於右側圓形按鈕檢視領料清單,點選類別中的選擇鈕，可將料件加入清單中。");
			$("#pick-panel").show();
			shopping_cart_flag = true;
			shopping_cart_IO = true;
		}
	});
// -- Item_view * --
	$("#btn-item-new").click(function(){
		$("#dig_it_name").val('');
		$("#dig_it_model").val('');
		$("#dig_it_packpage").val('');
		$("#dig_it_brand").val('');
		$("#dig_it_remark").val('');
		$("#ItemLabel").text("新增料件");
		$("#dia_it_btnDel").hide();
		dialog_input_flag = 1; 
	});
	$("#btn-category-edit").click(function(){
		$("#CategoryLabel").text("修改料件類別");
		dialog_category_flag = false;
		$("#dialog_input").val($("#item-view-title").text());
	});
	$("#btn-category-delete").click(function(){
		dialog_delete_flag = 1;
	});
	$( "#btn_back").button().click(function(){
		Back_View();
	});
	$( "#btn_pick_OK").click(function(){
		if(shopping_cart_IO){
			$("#pick-view-title").text("領料單");
		}else{
			$("#pick-view-title").text("入庫單");
		}
//		alert(material_pick_count);
		pick_view_flag = true;
		material_item_count = 0;
		$("#body-pick-view").html("<table id='table-pick-view' class='table table-bordered material-table-lg'><thead><tr><th>#</th><th>品名</th><th>型號</th><th>封裝</th><th>廠牌</th><th>實際數量</th>		<th>異動數量</th></tr></thead><tbody></tbody></table><h3><label for='message-text' class='control-label'>用途:</label></h3><textarea class='form-control material-table-lg' id='record_remark'  rows='5'></textarea>");
		Material_Pick_ItemQuan = [];
		Material_Pick_ItemNo = [];
		for(var i=0;i<material_pick_count;i++){
			var which = $($('#table-pick > tbody > tr')[i]).find("td:eq(0)").text();
   			count = which.split(",");
			select=(parseInt(count[0])-1);
			choice=(parseInt(count[1])-1);
			Material_Pick_ItemNo[i] = Material_Item_No[select][choice]; 
			Info_Material_Item(Material_Pick_ItemNo[i]);
		}
		$("#material-panel").hide(500);
		$("#material-export-panel").hide(500);
		$("#material-import-panel").hide(500);
		$("#item-view").hide(500);
		$("#pick-panel").hide(500);
		$('#pick-view').show(500);
	});
	$( "#btn_ShopCart_No").click(function(){
		$("record_remark").val('');
		$("#pick-panel").hide(500);
		item_view_flag = false;
		pick_view_flag = false;
		shopping_cart_flag = false;
//		alert(material_item_count + "\n" + material_pick_count);
		for(var i=0;i<Material_Pick_Flag.length;i++){
			for(var j=0;j<Material_Pick_Flag[i].length;j++){
				Material_Pick_Flag[i][j] = false;
			}
		}
//		for(var i=0;i<material_item_count;i++){
//			$("#table-pick-view tr:last").remove();
//		}
		for(var i=0;i<material_pick_count;i++){
			$("#table-pick tr:last").remove();
		}
		material_pick_count = 0;
	});
// -- Pick_View * --
	$("#btn_pick_back").click(function(){
		pick_view_flag = false;
		$("#material-panel").show(500);
		$('#pick-view').hide(500);
		$("#pick-panel").show(500);
//		for(var i=0;i<material_item_count;i++){
//			$("#table-pick-view tr:last").remove();
//		}
	});
	$("#btn_pick_submit").click(function(){
		var record_id = "r" + (new Date()).Format("yyyyMMddhhmmss");
//		material_item_count Material_Pick_ItemNo Material_Pick_ItemQuan 
		Material_Pick_ItemQuan = [];
		for(var i=0;i<material_item_count;i++){
			Material_Pick_ItemQuan[i] = $('#input_pi'+i).val();
		}
		Create_Material_Record(record_id);
	});
	
// -- Dialog UI * -- 
	$("#dia_cate_btnOK").click(function(){//category
		if(dialog_category_flag){
			New_Material_Category();
		}else{
			Edit_Material_Category(category_no[position],position);
		}
	});
	$("#dia_chk_btnOK").click(function(){//category
		switch(dialog_delete_flag){
			case 1:
				Delete_Material_Category(category_no[position]);
				break;
			case 2:
				Delete_Material_Item(Material_Item_No[position][j]);
				break;
		}
	});
	$("#dia_it_btnOK").click(function(){//category
		if($("#dig_it_name").val() != ""){
			switch(dialog_input_flag){
			case 1:
				New_Material_Item(category_no[position]);
				break;
			case 2:
				Edit_Material_Item(Material_Item_No[position][j]);
				break;
			}
		}else{
			alert("料件名稱無法空白");
		}
	});
	$("#dia_it_btnDel").click(function(){
		dialog_delete_flag = 2;
	});
// -- Dialog_Record_View * --
	$("#btn-dig-rdDelete").click(function(){
		Delete_Material_Record();
	});
	$("#btn-dig-rdEdit").click(function(){
		
	});
	$("#btn-dig-rdPrint").click(function(){
		
	});
	$("#btn-dig-rdOk").click(function(){
		Finish_Material_Record();
	});
	

// --- function ---
function Alert_View_Success(show){
	$("#alert_info").html("<div class='alert alert-success alert-dismissible' role='alert'><span class='glyphicon glyphicon-ok-sign' aria-hidden='true'></span>您已成功新增料件<strong>"+show+"</strong>到您的清單中！<button type='button' class='close' data-dismiss='alert'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span></button></div>");
}
function Alert_View_Info(info){
	$("#alert_info").html("<div class='alert alert-info alert-dismissible' role='alert'>"+info+"<button type='button' class='close' data-dismiss='alert'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span></button></div>");
}
function Alert_View_Danger(info){
	$("#alert_info").html("<div class='alert alert-danger alert-dismissible' role='alert'>"+info+"<button type='button' class='close' data-dismiss='alert'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span></button></div>");
}
function init_flag(){ // init
		$.ajax({  
		url: "php/Initialization_Operation.php",  
		type: "POST",
		data: { action : "the_compentence" },
		dataType:"JSON",
		success: function(data){
			if(data.reply == "Error_Info"){
				history.back();
			}else{
				compentence = data.compentence;
				user_id = data.user_id;
				user_name = data.user_name;
				admin = data.admin;
				material_category_list();
				material_record_count(1);//領料單-數量
				material_record_count(0);//入庫單-數量
//				material_record_list(1);//領料紀錄
//				material_record_list(0);//入庫紀錄
				$("#material-export-panel").hide();
				$("#material-import-panel").hide();
				if(compentence == 2){
					$("#btn-dig-rdCancel").show();
					$("#btn-dig-rdOk").show();
					$("#btn-item-new").show();
					$("#btn-category-edit").show();
					$("#btn-category-delete").show();
					$("#dia_it_btnOK").show();
				}else{
					$("#btn-dig-rdCancel").show();
				}
			}
		},
		error: function(xhr) {
      		alert('抱歉，您的請求，發生了一些錯誤');
    	}
	 });
}
function material_category_list(){//創建料件類別
	$.ajax({  
		url: "php/Initialization_Operation.php",  
		type: "POST",
		data: { action : "material_category" },
		dataType:"JSON",
		success: function(data){
			category_no = data.category_no;
			category_name = data.category_name;
			material_category_count = 0;
			$("#material-panel").html('');
			for(var i=0;i<category_name.length;i++){
			category_show[i] = false;
			material_text = "<div class='panel panel-primary' style='height:100%;'>"+
			"<div class='panel-heading'><h3 id='panel_title"+i+"' class='panel-title'>"+(i+1)+". "+category_name[i]+"</h3></div>"+
			"<div class='panel-body grid-item-hidden'><table id='table_list_"+i+"' class='table'>"+
			"<thead><tr><th>"+
			"<button type='button' id='btn_set_"+i+"' value='"+i+"' class='btn btn-xs btn-primary' title='設定'>"+
  			"<span class='glyphicon glyphicon-cog' aria-hidden='true'></span></button>"+
			"</th><th>品名</th><th>實際數量</th><th>可用數量</th><th>"+
			"<button type='button' id='btn_show_"+i+"' value='"+i+"' class='btn btn-xs btn-primary' title='顯示'>"+
  			"<span class='glyphicon glyphicon-fullscreen' aria-hidden='true'></span></button>"+
			"</th></tr></thead>"+
            "<tbody></tbody></table></div></div>";
			$("#material-panel").append("<div id='list_"+i+"' class='grid-item'></div>");
			$("#list_"+i).html(material_text);	//material_text
			$("#btn_set_"+i).click(function(){
				position = $(this).val();
				item_view_flag = true;
				material_item_info(category_no[position],position);
				Info_Material_Category(category_no[position]);
				$("#material-panel" ).hide(500);	
				$("#item-view").show(500);
			});
			$("#btn_show_"+i).click(function(){
				position = $(this).val();
				if(category_show[position]){
					category_show[position] = false;
					$( "#list_"+ position).attr("style","height:30%");
					$( "#btn_show_" + position).html("<span class='glyphicon glyphicon-fullscreen' aria-hidden='true'>	</span>");
				}else{
					category_show[position] = true;
					$( "#list_"+ position).attr("style","height:auto");
					$( "#btn_show_" + position).html("<span class='glyphicon glyphicon-remove' aria-hidden='true'></span>");
				}
				minigrid('.grid', '.grid-item');
			});	
				material_item_list(category_no[i],i);
				material_category_count++;
			}
			minigrid('.grid', '.grid-item');
		},
		error: function(xhr) {
      		alert('抱歉，您的請求，發生了一些錯誤');
    	}
	 });
}
function material_item_list(no_id,position){//創建 各 類別 -> 料件 表
 	$.ajax({  
		url: "php/Initialization_Operation.php",  
		type: "POST",
		data: { action : "material_item",
			    category_no : no_id
			  },
		dataType:"JSON",
		success: function(data){
			Material_Item_No[position] = new Array();
			Material_Item_Name[position] = new Array();
			Material_Item_Quantity[position] = new Array();
			Material_Pick_Flag[position] = new Array();
			Material_Item_No[position] = data.item_no;
			Material_Item_Name[position] = data.item_name;
			Material_Item_Quantity[position] = data.item_quantity;
			if(data.item_no == null){
				category_item_count[position] = 0;
			}else{
				category_item_count[position] = Material_Item_No[position].length;
			}
			material_pick_count = 0;
			for(var i=0;i<category_item_count[position];i++){
				Material_Pick_Flag[position][i] = false;
				$("#table_list_"+position).append("<tr><td>"+(i+1)+"</td><td>"+Material_Item_Name[position][i]+"</td><td>"+Material_Item_Quantity[position][i]+"</td><td>"+Material_Item_Quantity[position][i]+"</td><td><button type='button' id='btn_l_"+position+"i_"+i+"' value='"+position+","+i+"' class='btn btn-xs btn-primary'  title='選擇'><span class='glyphicon glyphicon-record' aria-hidden='true'></span></button></td>");
				$("#btn_l_"+position+"i_"+i).click(function(){
					count = $(this).val().split(",");
					select=parseInt(count[0]);
					choice=parseInt(count[1]);
					if(shopping_cart_flag){
						if(!Material_Pick_Flag[select][choice]){
							var show = Material_Item_Name[select][choice];
							Alert_View_Success(show);
							var count = $($('#table_list_'+select+' > tbody > tr ')[choice]).find("td:eq(3)").text();
							$("#table-pick").append("<tr><td>"+(select+1)+","+(choice+1)+"</td><td>"+Material_Item_Name[select][choice]+"</td><td>"+Material_Item_Quantity[select][choice]+"</td><td>"+count+"</td><td align='center'><button id='btn_cart_"+material_pick_count+"' value='"+(select+1)+","+(choice+1)+"' type='button' class='btn btn-danger'　title='刪除'><span class='glyphicon glyphicon-minus' aria-hidden='true'></span></button></td></tr>");
							$("#btn_cart_"+material_pick_count).click(function(){
								var which = $(this).val();
								for(var i=0;i<material_pick_count;i++){
									var show = $($('#table-pick > tbody > tr')[i]).find("td:eq(0)").text();
    								if(which == show){
										$($('#table-pick > tbody > tr')[i]).remove();
									}
								}
								count = which.split(",");
								select=(parseInt(count[0])-1);
								choice=(parseInt(count[1])-1);
								Material_Pick_Flag[select][choice] = false;
								material_pick_count--;
							});
							Material_Pick_Flag[select][choice] = true;
							material_pick_count++;
						}else{
							var show = "<span class='glyphicon glyphicon-info-sign' aria-hidden='true'></span><strong>注意！</strong>料件選擇重複！";
							Alert_View_Danger(show);
						}
					}else{
						compute_item_record(Material_Item_No[select][choice]);
					}
				});	
			}
			if(position == category_name.length-1){
				compute_record_quantity();
			}
		},
		error: function(xhr) {
      		alert('抱歉，您的請求，發生了一些錯誤');
    	}
	 });
}
function compute_record_quantity(){
	$.ajax({  
			url: "php/Initialization_Operation.php",  
			type: "POST",
			data: { action : "compute_record_quantity"
			  	},
			dataType:"JSON",
			success: function(data){
				var quantity;
				for(var i=0;i<data.category_no.length;i++){
					select = category_no.indexOf(data.category_no[i]);
					choice = Material_Item_No[select].indexOf(data.item_no[i]);
					quantity = parseInt($($('#table_list_'+select+' > tbody > tr ')[choice]).find("td:eq(3)").text());
					$($('#table_list_'+select+' > tbody > tr ')[choice]).find("td:eq(3)").text(quantity-data.quantity[i]);
				}
				
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function compute_item_record(no_id){
	$.ajax({  
			url: "php/Initialization_Operation.php",  
			type: "POST",
			data: { action : "compute_item_record",
				    item_no : no_id
			  	},
			dataType:"JSON",
			success: function(data){
				material_text = "<table id= 'dlg_itrcd_table' class='table table-bordered'><thead><tr><th>日期</th>				<th>人員</th><th>數量</th></tr></thead><tbody></tbody></table>";
				$('#dlg_itrcd_body').html(material_text);
				var show;
				if(data.record_date != null){
					for(var i=0;i<data.record_date.length;i++){
						if(data.material_action[i] == 1){
							show = " - ";
						}else if(data.material_action[i] == 0){
							show = " + ";
						}
						$('#dlg_itrcd_table').append("<tr><td>"+data.record_date[i]+"</td><td>"+data.user_name[i]+"</td><td>"+show+data.quantity[i]+"</td></tr>");
					}
				}else{
					$('#dlg_itrcd_table').append("<tr><td colspan='3'> No Data</td></tr>");
				}
				
				$('#Dialog_Item_Record').modal('show');
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function material_record_count(material_action){//取得庫存單數量
	$.ajax({  
			url: "php/Initialization_Operation.php",  
			type: "POST",
			data: { action : "material_record_count",
			  		material_action : material_action
			  	},
			dataType:"JSON",
			success: function(data){
				if(material_action == 1){
					$("#record_out_count").text(data.count);
				}else if(material_action == 0){
					$("#record_in_count").text(data.count);
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function material_record_list(material_action){
	$.ajax({  
			url: "php/Initialization_Operation.php",  
			type: "POST",
			data: { action : "material_record_list",
				  	material_action : material_action
				  },
			dataType:"JSON",
			success: function(data){
				if(data.user_name != null){
				var IE;
				if(material_action == 1){
					IE = "export";
					Record_Ex_id = data.record_id;
					Record_Ex_date = data.record_date;
					Record_Ex_userID = data.user_id;
					Record_Ex_name = data.user_name;
					Record_Ex_remark = data.remark;
					Ex_record_count = 0;
				}else if(material_action == 0){
					IE = "import";
					Record_Im_id = data.record_id;
					Record_Im_date = data.record_date;
					Record_Im_userID = data.user_id;
					Record_Im_name = data.user_name;
					Record_Im_remark = data.remark;
					Im_record_count = 0;
				}
				$("#material-"+IE+"-panel").html('');
				for(var i=0;i<data.user_name.length;i++){
					if(material_action == 1){
						Record_Ex_show [i] = false;
					}else if(material_action == 0){
						Record_Im_show [i] = false;
					}
					material_text = "<div class='panel panel-primary' style='height:100%;'>"+
					"<div class='panel-heading'><h3 id='panel_"+IE+"_title"+i+"' class='panel-title'>"+(i+1)+". "+data.user_name[i]+"</h3></div>"+
					"<div class='panel-body grid-item-hidden'><table id='table_"+IE+"_list_"+i+"' class='table material-table-lg'>"+
					"<thead><tr><th>"+
					"<button type='button' id='btn_set_"+IE+i+"' value='"+i+"' class='btn btn-sm btn-primary' title='資訊'>"+
  					"<span class='glyphicon glyphicon-ok-sign' aria-hidden='true'></span></button>"+
					"</th><th>品名</th><th>實際數量</th><th>異動數量</th><th>剩餘數量</th><th>"+
					"<button type='button' id='btn_show_"+IE+i+"' value='"+i+"' class='btn btn-sm btn-primary' title='顯示'>"+
  					"<span class='glyphicon glyphicon-fullscreen' aria-hidden='true'></span></button>"+
					"</th></tr></thead>"+
            		"<tbody></tbody></table></div></div>";
					$("#material-"+IE+"-panel").append("<div id='"+IE+"_list_"+i+"' class='"+IE+"-grid-item'></div>");
					$("#"+IE+"_list_"+i).html(material_text);//material_text
					$("#btn_set_"+IE+i).click(function(){
						position = $(this).val();
						if(material_action == 1){
							if(user_id == Record_Ex_userID[position]){
								$("#btn-dig-rdDelete").show();
							}else{
								$("#btn-dig-rdDelete").hide();
							}
							$("#dig_rd_title").text("領料單:");
							$("#dig_rd_date").text(Record_Ex_date[position]);
							$("#dig_rd_name").text(Record_Ex_name[position]);
							$("#dig_rd_remark").val(Record_Ex_remark[position]);
							material_record_ie = 1;
						}else if(material_action == 0){
							if(user_id == Record_Im_userID[position]){
								$("#btn-dig-rdDelete").show();
							}else{
								$("#btn-dig-rdDelete").hide();
							}
							$("#dig_rd_title").text("入庫單:");
							$("#dig_rd_date").text(Record_Im_date[position]);
							$("#dig_rd_name").text(Record_Im_name[position]);
							$("#dig_rd_remark").val(Record_Im_remark[position]);
							material_record_ie = 0;
						}
						
						$('#Dialog_Record').modal('show'); 
					});
					$("#btn_show_"+IE+i).click(function(){
						position = $(this).val();
						if(material_action == 1){
							if(Record_Ex_show[position]){
								$( "#"+IE+"_list_"+position).attr("style","height:50%");
								$( "#btn_show_"+IE+position).html("<span class='glyphicon glyphicon-fullscreen' aria-hidden='true'></span>");
								Record_Ex_show [position] = false;
							}else{
								$( "#"+IE+"_list_"+position).attr("style","height:auto");
								$( "#btn_show_"+IE+position).html("<span class='glyphicon glyphicon-remove' aria-hidden='true'></span>");
								Record_Ex_show [position] = true;
							}	
							minigrid('.export-grid', '.export-grid-item');
						}else if(material_action == 0){
							if(Record_Im_show[position]){
								$( "#"+IE+"_list_"+position).attr("style","height:50%");
								$( "#btn_show_"+IE+position).html("<span class='glyphicon glyphicon-fullscreen' aria-hidden='true'></span>");
								Record_Im_show [position] = false;
							}else{
								$( "#"+IE+"_list_"+position).attr("style","height:auto");
								$( "#btn_show_"+IE+position).html("<span class='glyphicon glyphicon-remove' aria-hidden='true'></span>");
								Record_Im_show [position] = true;
							}	
							minigrid('.import-grid', '.import-grid-item');	
						}	
					});	
					material_record_item(data.record_id[i],i,IE);
				}
					if(material_action == 1){
						minigrid('.export-grid', '.export-grid-item');
//						$("#material-export-panel").hide();
					}else if(material_action == 0){
						minigrid('.import-grid', '.import-grid-item');
//						$("#material-import-panel").hide();
					}
			}else{
				if(material_action == 1){
					IE = "export";
				}else if(material_action == 0){
					IE = "import";
				}
				$("#material-"+IE+"-panel").html("<div class='alert alert-info alert-dismissible' role='alert'><h1>無紀錄!</h1></div>");
			}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function material_record_item(no_id,position,IE){//取得庫存單數量	
	$.ajax({  
			url: "php/Initialization_Operation.php",  
			type: "POST",
			data: { action : "material_record_item",
			  		record_id : no_id
			  	},
			dataType:"JSON",
			success: function(data){
				if(IE == "import"){
					Record_Im_ItNo[Im_record_count] = new Array();
					Record_Im_ItQuan[Im_record_count] = new Array();
					Record_Im_ItNo[Im_record_count] = data.item_no;
					for(var i=0;i<data.item_name.length;i++){
						Record_Im_ItQuan[Im_record_count][i] = parseInt(data.item_quantity[i])+parseInt(data.quantity[i]);
						$("#table_"+IE+"_list_"+position).append("<tr><td>"+(i+1)+"</td><td>"+data.item_name[i]+"</td><td>"+data.item_quantity[i]+"</td><td>"+data.quantity[i]+"</td><td colspan='2'>"+Record_Im_ItQuan[Im_record_count][i]+"</td>");
					}
					Im_record_count ++;
				}else if(IE == "export"){
					Record_Ex_ItNo[Ex_record_count] = new Array();
					Record_Ex_ItQuan[Ex_record_count] = new Array();
					Record_Ex_ItNo[Ex_record_count] = data.item_no;
					for(var i=0;i<data.item_name.length;i++){
						Record_Ex_ItQuan[Ex_record_count][i] = 
						parseInt(data.item_quantity[i]) - parseInt(data.quantity[i]);
						$("#table_"+IE+"_list_"+position).append("<tr><td>"+(i+1)+"</td><td>"+data.item_name[i]+"</td><td>"+data.item_quantity[i]+"</td><td>"+data.quantity[i]+"</td><td colspan='2'>"+Record_Ex_ItQuan[Ex_record_count][i]+"</td>");
					}
					Ex_record_count ++;
			    }
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function material_item_info(no_id,position){//設定料件資訊
	$.ajax({  
		url: "php/Initialization_Operation.php",  
		type: "POST",
		data: { action : "material_item_info",
			    category_no : no_id
			  },
		dataType:"JSON",
		success: function(data){
			Material_Item_No[position] = new Array();
			Material_Item_Name[position] = new Array();
			Material_Item_Model[position] = new Array();
			Material_Item_Package[position] = new Array();
			Material_Item_Brand[position] = new Array();
			Material_Item_Remark[position] = new Array();
			Material_Item_Quantity[position] = new Array();
			Material_Item_No[position] = data.item_no;
			Material_Item_Name[position] = data.item_name;
			Material_Item_Model[position] = data.item_model;
			Material_Item_Package[position] = data.item_package;
			Material_Item_Brand[position] = data.item_brand;
			Material_Item_Remark[position] = data.item_remark;
			Material_Item_Quantity[position] = data.item_quantity;
			material_item_count = 0;
			material_item_count = Material_Item_No[position].length;
			for(var i=0;i<material_item_count;i++){
				$("#table-category").append("<tr><td>"+(i+1)+"</td><td>"+Material_Item_Name[position][i]+"</td><td>"+Material_Item_Model[position][i]+"</td><td>"+Material_Item_Package[position][i]+"</td><td>"+Material_Item_Brand[position][i]+"</td><td>"+Material_Item_Quantity[position][i]+"</td><td align='center'><button id='btn_iv_"+i+"' type='button' value='"+i+"' class='btn btn-primary'><span class='glyphicon glyphicon-cog' aria-hidden='true'></span></button></td></tr>");
				$("#btn_iv_"+i).click(function(){
					j = $(this).val();
					$('#Dialog_Item').modal('show'); 
					$("#dig_it_name").val(Material_Item_Name[position][j]);
					$("#dig_it_model").val(Material_Item_Model[position][j]);
					$("#dig_it_packpage").val(Material_Item_Package[position][j]);
					$("#dig_it_brand").val(Material_Item_Brand[position][j]);
					$("#dig_it_remark").val(Material_Item_Remark[position][j]);
					dialog_input_flag = 2;
					if(compentence == 2){
						$("#dia_it_btnDel").show();
					}
				});	
			}
		},
		error: function(xhr) {
      		alert('抱歉，您的請求，發生了一些錯誤');
    	}
	 });
}
function Back_View(){
	for(var i=0;i<material_item_count;i++){
			$("#table-category tr:last").remove();
		}
	$("#item-view").hide(500);
	$( "#material-panel" ).show(500);
	item_view_flag = false;
}
	
function Info_Material_Category(no_id){ // 料件類別 動作
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Info_Material_Category",
			  		category_no : no_id
			  	},
			dataType:"JSON",
			success: function(data){
				$("#item-view-title").text(data.category_name);
				category_name[position] = data.category_name;
				$("#panel_title"+position).text((parseInt(position)+1)+"."+category_name[position]);
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function Info_Material_Item(no_id){ // 料件類別 動作
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Info_Material_Item",
			  		item_no : no_id
			  	},
			dataType:"JSON",
			success: function(data){
				$("#table-pick-view").append("<tr><td>"+(material_item_count+1)+"</td><td>"+data.item_name+"</td><td>"+data.item_model+"</td><td>"+data.item_package+"</td><td>"+data.item_brand+"</td><td>"+data.item_quantity+"</td><td align='center'><div class='input-group number-spinner spinner'><span class='input-group-btn'><button class='btn btn-primary' id='btn_sub_pi"+material_item_count+"' value='"+material_item_count+"'><span class='glyphicon glyphicon-minus'></span></button></span><input id='input_pi"+material_item_count+"' type='text' class='form-control text-center' value='1'><span class='input-group-btn'><button class='btn btn-primary' id='btn_add_pi"+material_item_count+"' value='"+material_item_count+"'><span class='glyphicon glyphicon-plus'></span></button></span></div></td>");
				$('#btn_sub_pi'+material_item_count).on('click', function() {
					var which = $(this).val(); 
					if($('#input_pi'+which).val() > 1){
    					$('#input_pi'+which).val( parseInt($('#input_pi'+which).val(), 10) -1 );
					}
  				});
  				$('#btn_add_pi'+material_item_count).on('click', function() {
					var which = $(this).val();
					var max = parseInt($($('#table-pick-view > tbody > tr')[which]).find("td:eq(5)").text());
					if($('#input_pi'+which).val() < max || !shopping_cart_IO){
						$('#input_pi'+which).val( parseInt($('#input_pi'+which).val(), 10) + 1);
					}
  				});
				material_item_count ++;
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function New_Material_Category(){ // 
	if($("#dialog_input").val() != ""){
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "New_Material_Category",
			  		category_name : $("#dialog_input").val() 	
			  	},
			dataType:"JSON",
			success: function(data){
				$("#dialog_input").val("");
				if(data.reply == "add_category_OK"){
					$('#Dialog_Category').modal('toggle');
					$("#material-panel").html("");
					material_category_list();
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
	}else{
		alert("類別名稱無法空白");
	}
}
function Edit_Material_Category(no_id,position){ // init
	if($("#dialog_input").val() != ""){
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Edit_Material_Category",
				   	category_no : no_id,
			  		category_name : $("#dialog_input").val() 	
			  	},
			dataType:"JSON",
			success: function(data){
				$("#dialog_input").val("");
				if(data.reply == "edit_category_OK"){
					$('#Dialog_Category').modal('toggle');
					Info_Material_Category(no_id);
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
	}else{
		alert("類別名稱無法空白");
	}
}
function Delete_Material_Category(no_id){ // 料件類別 動作
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Delete_Material_Category",
			  		category_no : no_id
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "delete_category_OK"){
					$('#Dialog_Check').modal('toggle');
					$("#material-panel").html("");
					material_category_list();
					Back_View();
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function New_Material_Item(no_id){ // 料件類別 動作
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "New_Material_item",
				    category_no: no_id,
				   	item_name: $("#dig_it_name").val(),
				    item_model: $("#dig_it_model").val(),
				    item_package: $("#dig_it_packpage").val(),
				    item_brand: $("#dig_it_brand").val(), 
				   	item_remark: $("#dig_it_remark").val()
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "new_item_OK"){
					$('#Dialog_Item').modal('toggle');
					for(var i=0;i<material_item_count;i++){
						$("#table-category tr:last").remove();
					}
					for(var i=0;i<category_item_count[position];i++){
						$("#table_list_"+position+" tr:last" ).remove();
					}
					material_item_info(category_no[position],position);
					material_item_list(category_no[position],position);
//					Info_Material_Category(category_no[position]);
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function Edit_Material_Item(no_id){ // 料件類別 動作
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Edit_Material_item",
				   	item_no: no_id,
				   	item_name: $("#dig_it_name").val(),
				    item_model: $("#dig_it_model").val(),
				    item_package: $("#dig_it_packpage").val(),
				    item_brand: $("#dig_it_brand").val(), 
				   	item_remark: $("#dig_it_remark").val()
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "edit_item_OK"){
					$('#Dialog_Item').modal('toggle');
					for(var i=0;i<material_item_count;i++){
						$("#table-category tr:last").remove();
					}
					for(var i=0;i<category_item_count[position];i++){
						$("#table_list_"+position+" tr:last" ).remove();
					}
					material_item_info(category_no[position],position);
					material_item_list(category_no[position],position);
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}	
function Delete_Material_Item(no_id){ // 料件類別 動作
		$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Delete_Material_item",
				    item_no: no_id
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "delete_item_OK"){
					$('#Dialog_Item').modal('toggle');
					$('#Dialog_Check').modal('toggle');
					for(var i=0;i<material_item_count;i++){
						$("#table-category tr:last").remove();
					}
					for(var i=0;i<category_item_count[position];i++){
						$("#table_list_"+position+" tr:last" ).remove();
					}
					material_item_info(category_no[position],position);
					material_item_list(category_no[position],position);
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}	
function Create_Material_Record(no_id){
	var io = 0;
	if(shopping_cart_IO){
		io = 1;
	}
	$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Create_Material_Record",
				    record_id: no_id,
				   	user_name: user_name,
				    remark: $("#record_remark").val(),
				   	material_action: io,
				    item_no_arr: Material_Pick_ItemNo,
				    quantity_arr: Material_Pick_ItemQuan
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "Creat_Record_OK"){
					$("record_remark").val('');
					$("#material-panel").show(500);
					$('#pick-view').hide(500);
//					for(var i=0;i<material_item_count;i++){
//						$("#table-pick-view tr:last").remove();
//					}
					for(var i=0;i<material_pick_count;i++){
						$("#table-pick tr:last").remove();
					}
					item_view_flag = false;
					pick_view_flag = false;
					shopping_cart_flag = false;
					
//					material_record_list(io);//領料紀錄
					material_record_count(io);
					material_category_list();
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function Finish_Material_Record(){
	var no_id,rcd_no,rcd_qu;
	if(material_record_ie == 1){
		no_id = Record_Ex_id[position];
		rcd_no = Record_Ex_ItNo[position];
		rcd_qu = Record_Ex_ItQuan[position];
	}else if(material_record_ie == 0){
		no_id = Record_Im_id[position];
		rcd_no = Record_Im_ItNo[position];
		rcd_qu = Record_Im_ItQuan[position];
	}
	$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Finish_Material_Record",
				    record_id: no_id,
				    item_no_arr: rcd_no,
				    quantity_arr: rcd_qu
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "Finish_Record_OK"){
					$('#Dialog_Record').modal('toggle');
					material_record_count(material_record_ie);
					material_record_list(material_record_ie);//領料紀錄
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}
function Delete_Material_Record(){
	if(material_record_ie == 1){
		no_id = Record_Ex_id[position];
	}else if(material_record_ie == 0){
		no_id = Record_Im_id[position];
	}
	$.ajax({  
			url: "php/Process_Operation.php",  
			type: "POST",
			data: { action : "Delete_Material_Record",
				    record_id: no_id
			  	},
			dataType:"JSON",
			success: function(data){
				if(data.reply == "delete_record_OK"){
					$('#Dialog_Record').modal('toggle');
					material_record_count(material_record_ie);
					material_record_list(material_record_ie);//領料紀錄
				}
			},
			error: function(xhr) {
      			alert('抱歉，您的請求，發生了一些錯誤');
    		}
	 	});
}	
	
// --- grid_view ---
$(function () {
  $('[data-toggle="popover"]').popover()
});

});
//--- time ---
	Date.prototype.Format = function (fmt) {
  	var o = {
    	"y+": this.getFullYear(),
    	"M+": this.getMonth() + 1,                 //月份
    	"d+": this.getDate(),                    //日
		"h+": this.getHours(),                   //小时
    	"m+": this.getMinutes(),                 //分
    	"s+": this.getSeconds(),                 //秒
    	"q+": Math.floor((this.getMonth() + 3) / 3), //季度
    	"S+": this.getMilliseconds()             //毫秒
  	};
  	for (var k in o) {
    	if (new RegExp("(" + k + ")").test(fmt)){
      		if(k == "y+"){
        		fmt = fmt.replace(RegExp.$1, ("" + o[k]).substr(4 - RegExp.$1.length));
      	}
      	else if(k=="S+"){
        	var lens = RegExp.$1.length;
        	lens = lens==1?3:lens;
        	fmt = fmt.replace(RegExp.$1, ("00" + o[k]).substr(("" + o[k]).length - 1,lens));
      	}
      	else{
        	fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      	}
    	}
  	}
  	return fmt;
	}