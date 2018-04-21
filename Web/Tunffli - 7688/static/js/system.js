// --- init ---
var UBUS_STATUS_OK = 0;
var UBUS_STATUS_INVALID_COMMAND = 1;
var UBUS_STATUS_INVALID_ARGUMENT = 2;
var UBUS_STATUS_METHOD_NOT_FOUND = 3;
var UBUS_STATUS_NOT_FOUND = 4;
var UBUS_STATUS_NO_DATA = 5;
var UBUS_STATUS_PERMISSION_DENIED = 6;
var UBUS_STATUS_TIMEOUT = 7;
var UBUS_STATUS_NOT_SUPPORTED = 8;
var UBUS_STATUS_UNKNOWN_ERROR = 9;
var UBUS_STATUS_CONNECTION_FAILED = 10;

function do_ajax(data, success) {
	$.ajax({
		url: "/ubus",
		type: "post",
		data: JSON.stringify(data),
		success: function(reply) {
			success(reply);
		},
	});
}

var id = 1;
var session;
function uci_commit() {
	alert("Commit");
	var commit = { "jsonrpc": "2.0", "id": id++, "method": "call",
		"params": [session, "uci", "apply", { "commit": true }]};
	do_ajax(commit, function(reply) {
		if (!reply.result && reply.result[0] == UBUS_STATUS_NO_DATA) {
			alert("config has not changed");
			return;
		}
		if (!reply.result || reply.result[0] != 0) {
			alert("failed to commit config");
			return;
		}
		clear_Cookie("Tunffli_Session");
		clear_Cookie("Tunffli_Password");
		rpc_logout(); 
		alert("changes were comitted");
		var reload = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [session, "uci", "reload_config", { "commit": true }]};
		do_ajax(reload, function(reply) {
			if (!reply.result || reply.result[0] != 0) {
				alert("failed to reload config");
				return;
			}
			alert("changes were applied");
		});
	});
}
function rpc_logout()
{
	var login = { "jsonrpc": "2.0", "id": id++, "method": "call",
		"params": [ session, "session", "destroy", { "dummy": 0} ]};
	session = "";
	do_ajax(login, function(reply) {
		if (!reply.result || reply.result[0] != 0) {
			alert("failed to logout");
            $("#index-view").slideToggle(1000);
            $("#signin-view").slideToggle(1000);
			return;
		}
		$("#index-view").slideToggle(1000);
        $("#signin-view").slideToggle(1000);
	});
}

function rpc_login(user, pass)
{
	var login = { "jsonrpc": "2.0", "id": id++, "method": "call",
		"params": [ "00000000000000000000000000000000","session","login",{
			"username":user,"password":pass,
			}
		]};
	session = "";
	do_ajax(login, function(reply) {
		if (!reply.result || reply.result[0] != 0) {
			alert("密碼錯誤");
			return;
		}
		session = reply.result[1].ubus_rpc_session;
		var grant = { "jsonrpc": "2.0", "id": id++, "method": "call",
				"params": [ session, "session", "grant",
					{ "scope": "uci", "objects": [ [ "*", "read" ], [ "*", "write" ] ] }
				]};
		do_ajax(grant, function(reply) {
			if (!reply.result || reply.result[0] != 0) {
				alert("權限錯誤");
				return;
			}
			rpc_netstate_load("wan");
//			rpc_netstate_load("wan");
			rpc_wifi_load();
			rpc_system_load();
			rpc_wifi_ap_scan();
            $("#index-view").slideToggle(1000);
            $("#signin-view").slideToggle(1000);
			setCookie("Tunffli_Session",session,1);
			setCookie("Tunffli_Password",pass,1);
			$("#Signin_NewPasswd").val(pass);
			
//			$("#dt-info").html(session);
			/*$("#Session").val(session);
			$("#fwSession").val(session);
			rpc_board_load();
			rpc_network_load();
			rpc_netstate_load("lan");
			rpc_netstate_load("wan");*/
		});
	});
}
var myHostname;
function rpc_system_load() {
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "uci", "get", { "config": "system", "type": "system"  }]
		};
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0)
			return;
		var res = reply.result[1].values;
		for (k in res) if (res[k].hostname) {
			$("#Hostname").val(res[k].hostname);
			myHostname = res[k].hostname;
//			$("#sys-info").html("Boot loader version" + res[k].loader_version + "<br>");
//			$("#sys-info").append("Firmware version" + res[k].firmware_version  + "<br>");
			return
		}
	});
}
function rpc_hostname(hostname) {
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "uci", "set", {
				"config": "system",
				"section": "@system[0]",
				values: { "hostname": hostname }}
			]
		};
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0) {
			alert("failed to change hostname");
			return;
		}
		alert("hostname set to " + hostname);
		myHostname = hostname;
	});
}
function rpc_passwd(pass) {
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "rpc-sys", "password_set", { "user": "root", "password": pass, } ] };
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0)
			alert("failed to set password");
		else
			alert("password changed");
	});
}
var ap_newwork = null;
var wifi_mode ="";
function rpc_wifi_load() {
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "uci", "get", { "config": "wireless" }]
		};
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0)
			return;
		var res = reply.result[1].values;

		$("#ap_ssid").val(res.ap.ssid);
		$("#ap_key").val(res.ap.key);
		ap_newwork = res.sta.ssid;
		$("#sta_key").val(res.sta.key);
		if (res.sta.disabled == 1){
			rpc_netstate_load("lan");
			wifi_mode = "ap";
            $('input[name=wifi_mode]').filter('[value=ap]').prop('checked', true);
            $("#lable_ap").addClass("active");
            $("#ap_mode_view").show();
            $("#sta_mode_view").hide();
		}
		else{
			wifi_mode = "sta";
//			rpc_netstate_load("wan");
            $('input[name=wifi_mode]').filter('[value=sta]').prop('checked', true);
            $("#lable_sta").addClass("active");
            $("#ap_mode_view").hide();
            $("#sta_mode_view").show();
		}
	});
}
function rpc_wifi_ap_scan() {
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "iwinfo", "scan", { "device": "ra0", } ] };
	$("#Scan_NetWork").empty();
	do_ajax(config, function(reply) {
//		if (!reply.result || reply.result[0] != 0){
//			alert("failed to scan");
//		}
//		else{
//			alert("scan complete");
//		}
		$.each(reply.result[1].results, function (i, item) {
			$('#Scan_NetWork').append($('<option>', { 
				value: item.ssid,
				text : item.ssid + " - " + item.quality + "%", 
			}));
		});
		if(ap_newwork != null){
			$('#Scan_NetWork').val(ap_newwork);
		}else{
			$('#Scan_NetWork').selectedIndex = 0;
		}
		$('#Scan_NetWork').selectmenu("refresh");
	});
}
function rpc_wifi_mode(mode) {
	var disabled = 1;

	if (mode == "sta")
		disabled = 0;

	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "uci", "set", {
				"config": "wireless",
				"section": "sta",
				values: {
					"disabled": disabled
				}}
			]
		};
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0) {
			alert("failed to apply config");
			return;
		}
//		alert("wifi mode set to " + mode);
	});
}
function rpc_wifi(section, ssid, key) {
	var enc = "none";
	if (key.length > 1)
		enc = "psk2";
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "uci", "set", {
				"config": "wireless",
				"section": section,
				values: {
					"ssid": ssid,
					"key": key,
					"encryption": enc,
				}}
			]
		};
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0) {
			alert("failed to apply config");
			return;
		}
		alert(section + " settings were changed");
		uci_commit();
	});
}
//var ip_address;
function rpc_netstate_load(iface) {
	config = { "jsonrpc": "2.0", "id": id++, "method": "call",
			"params": [ session, "network.interface", "status", { "interface": iface }]
		};
	do_ajax(config, function(reply) {
		if (!reply.result || reply.result[0] != 0)
			return;
		var res = reply.result[1];

		if (res["ipv4-address"]){
//            ip_address = res["ipv4-address"][0].address;
			$("#equip_ip").val(res["ipv4-address"][0].address);
        }
		else{
			$("#equip_ip").val("");
        }
	});
}
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
} 
function clear_Cookie(cname) {
    var d = new Date();
    d.setTime(d.getTime() -1 );
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=;" + expires + ";path=/";
} 
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length,c.length);
        }
    }
    return null;
} 