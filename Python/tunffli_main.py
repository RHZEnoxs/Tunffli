#-*-coding:utf-8-*-
from flask import Flask, render_template, request, jsonify,json
import paho.mqtt.client as mqtt
import os
import time
import mraa
import json
import subprocess
app = Flask(__name__)

@app.route('/')
@app.route('/About')
def about():
    return render_template('About.html')

@app.route('/switch_relay')
def switch_relay():
    return render_template('switch_relay.html')
@app.route('/music_madplay')
def music_madplay():
    return render_template('music_madplay.html')
@app.route('/webcam_streamer')
def webcam_streamer():
    return render_template('webcam_streamer.html')
@app.route('/Safty')
def safty():
    return render_template('Safty.html')

@app.route('/switch_relay/device_info', methods=['GET','POST'])
def swrly_devinfo():
    if request.method == 'GET':
		with open('static/json/switch_relay.json', 'r') as f:
			doc_device = json.load(f)
		f.close	
		return jsonify(doc_device)
    
@app.route('/switch_relay/update_sw_rly', methods=['GET','POST'])
def update_sw_rly():
    if request.method == 'POST':
         if request.form['action'] == "update_sw_rly" :
                data = {  'dev_name':request.form.getlist('dev_name[]')
		          }
                with open('static/json/switch_relay.json', 'w') as f:
                    json.dump(data, f)
                f.close
                return jsonify(result = 'OK')
@app.route('/switch_relay/safety_certificate', methods=['GET','POST'])
def swrly_access():
    if request.method == 'POST':
        if request.form['action'] == "access" :
            return jsonify(result = "tunffli_90161728")

@app.route('/switch_relay/ctrl_sw_rly', methods=['GET','POST'])
def swrly_ctrl_sw_rly():
    if request.method == 'POST':
        if request.form['action'] == "ctrl_mraa" :
            if request.form['access'] == "tunffli_90161728" :
                ctrl = int(request.form['dev_id'])
                sta = ctrl_mraa(ctrl)
                return jsonify(result = sta);
            
# --- music_madplay *---
@app.route('/music_madplay/playlist', methods=['GET','POST'])
def palylist():
    if request.method == 'GET':
        with open('static/json/playlist.json', 'r') as f:
			playlist = json.load(f)
        f.close
        return jsonify(playlist)
@app.route('/music_madplay/musiclist', methods=['GET','POST'])
def music_list():
    if request.method == 'POST':
        pylst = request.form['pylst']
        with open('static/json/pylst_' + pylst + '.json', 'r') as f:
            playlist = json.load(f)
        f.close
        return jsonify(playlist)   
path = '/Media/SD-P1/Music/'    
@app.route('/music_madplay/filepath', methods=['GET','POST'])
def fold_file():
    if request.method == 'GET':
        return jsonify(path_media())
media_flag = False
@app.route('/music_madplay/media_ctrl', methods=['GET','POST'])
def media_ctrl():
    global media_flag
    if request.method == 'POST':
        action = request.form['action']
        filepath = request.form['filepath']
        ctrl_media(action,filepath)
        return jsonify(result = media_flag)
    
webcam_flag = False
@app.route('/webcam_streamer/webcam_ctrl', methods=['GET','POST'])
def webcam_ctrl():
    global media_flag
    if request.method == 'POST':
        action = request.form['action']
        if(action == "webcam_on"):
            ctrl_webcam(True);
        if(action == "webcam_off"):
            ctrl_webcam(False);
        return jsonify(result = webcam_flag)  

###############################
MQTT_Server = "127.0.0.1"
MQTT_Pport = 1883
MQTT_Alive = 90
MQTT_Topic = "enoxs"
MQTT_Qos = 2
###############################
def mqtt_setup():
    global client
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    client.connect(MQTT_Server, MQTT_Pport)
    client.loop_start()
    print "MQTT Start"
def on_connect(client, userdata, flags, rc):
	print("Connected with result code "+str(rc))
	client.subscribe(MQTT_Topic)
def on_message(client, userdata, msg):
#    print(msg.topic+" : "+str(msg.payload))
    try:
        msg_json = json.loads(str(msg.payload))
        if msg_json['reply'] == False  :
            if msg_json['id'] == "Relay" :
                ctrl_mraa(int(msg_json['dev_id'].replace("0x","")))
            if msg_json['id'] == "Madplay" :
                if msg_json['action'] == "fold_file":
                    path_media()
                if msg_json['action'] == "media_flag":
                    time.sleep(1)
                    reply_mqtt("music")
                else:
                    ctrl_media(msg_json['action'],msg_json['value'])
                    reply_mqtt("music")
            if msg_json['id'] == "Mjpg" :
                if(msg_json['dev_id'] == "all"):
                    time.sleep(1)
                    reply_mqtt("webcam")
                else:
                    ctrl_webcam(ctrl_webcam(msg_json['value']))
                
    except ValueError, e:
        print " - - - Fail - - - "
#client.publish(Topic_0_Config,json.dumps(doc_config), MQTT_Qos)
# --- init_mraa ---    
pin_01 = mraa.Gpio(14)
pin_01.dir(mraa.DIR_OUT)
pin_01.write(0)

pin_02 = mraa.Gpio(15)
pin_02.dir(mraa.DIR_OUT)
pin_02.write(0)

pin_03 = mraa.Gpio(16)
pin_03.dir(mraa.DIR_OUT)
pin_03.write(0)

pin_04 = mraa.Gpio(17)
pin_04.dir(mraa.DIR_OUT)
pin_04.write(0)

pin_05 = mraa.Gpio(19)
pin_05.dir(mraa.DIR_OUT)
pin_05.write(0)
def ctrl_mraa(ctrl):
    if(ctrl == 1):
        if(pin_01.read() == 0):
            pin_01.write(1)
        else:
            pin_01.write(0)
        return pin_01.read()
    if(ctrl == 2):
        if(pin_02.read() == 0):
            pin_02.write(1)
        else:
            pin_02.write(0)
        return pin_02.read()
    if(ctrl == 3):
        if(pin_03.read() == 0):
            pin_03.write(1)
        else:
            pin_03.write(0)
        return pin_03.read()
    if(ctrl == 4):
        if(pin_04.read() == 0):
            pin_04.write(1)
        else:
            pin_04.write(0)
        return pin_04.read()
    if(ctrl == 5):
        if(pin_05.read() == 0):
            pin_05.write(1)
        else:
            pin_05.write(0)
        return pin_05.read()
    
def path_media():
    global fold_list
    global file_list
    fold_list = []
    file_list = []
    for root,dirs,files in os.walk(path): 
        for dir in dirs:
            fold_list.append(dir)
            new = []
            file_list.append(new)
        for file in files: 
            Arr = os.path.join(root,file).replace(path,"").split("/")
            if(len(Arr) == 1):
                file_list.append(Arr[0])
            if(len(Arr) == 2):
                file_list[fold_list.index(Arr[0])].append(Arr[1])
    data = {
        'id' : 'Madplay',
        'reply': True,
        'action': 'fold_file',
   	    'fold_list' : fold_list,
        'file_list' : file_list
    }
    client.publish(MQTT_Topic,json.dumps(data), 0)
    return data

def ctrl_media(ctrl,file):
    global media_flag
    global media
    global path
    print ctrl,":",file
    if(ctrl == "play"):
        if(media_flag):
            media.kill()
        media = subprocess.Popen(['madplay', path+file])
        media_flag = True
    if(ctrl == "pause"):
        if(media_flag):
            media.kill()
        media_flag = False
    if(ctrl == "stop"):
        if(media_flag):
            media.kill()
        media_flag = False
    if(ctrl == "back"):
        media_flag = True
    if(ctrl == "next"):
        media_flag = True
def ctrl_webcam(ctrl):
    global webacm
    global webcam_flag
    if(ctrl == True):
        if(not webcam_flag):
            webacm = subprocess.Popen(['mjpg_streamer','-i',"input_uvc.so -f 20 -r 1280*720 -d /dev/video0" ,'-o', "output_http.so"])
        webcam_flag = True
    if(ctrl == False):
        if(webcam_flag):
            webacm.kill()
        webcam_flag = False
    reply_mqtt("webcam")
def reply_mqtt(reply):
    global webcam_flag
    global media_flag
    global fold_list
    global file_list
    if(reply == "music"):
        data = {
            'id' : 'Madplay',
            'reply': True,
            'action': 'media_flag',
   			'value' : media_flag
		}
    if(reply == "webcam"):
        data = {
            'id' : 'Mjpg',
            'reply': True,
            'dev_id': '0x01',
   			'value' : webcam_flag
		}
    client.publish(MQTT_Topic,json.dumps(data), 0)
        
        
if __name__ == '__main__':
    try:
        mqtt_setup()
        app.run(host='0.0.0.0')
    except KeyboardInterrupt:
        client.disconnect()        
        print "","Interrupt","disconnect"
    
    