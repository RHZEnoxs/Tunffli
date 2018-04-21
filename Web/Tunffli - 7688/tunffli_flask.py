#-*-coding:utf-8-*-
from flask import Flask, render_template, request, jsonify,json
#import mraa
import os
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
                data = {
   			      'dev_name':request.form.getlist('dev_name[]')
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
path = 'Music_Path'    
@app.route('/music_madplay/filepath', methods=['GET','POST'])
def fold_file():
    if request.method == 'GET':
        fold_list = []
        file_list = []
        for root,dirs,files in os.walk(path): 
            for dir in dirs:
                fold_list.append(dir)
                new = []
                file_list.append(new)
            for file in files: 
                Arr = os.path.join(root,file).replace(path+"\\","").split("\\")
                if(len(Arr) == 1):
                    file_list.append(Arr[0])
                if(len(Arr) == 2):
                    file_list[fold_list.index(Arr[0])].append(Arr[1])
        data = {
   			'fold_list' : fold_list,
            'file_list' : file_list
		}       
        return jsonify(data)

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
        if(action == "webcam_on" or action == "webcam_off"):
            ctrl_webcam(action);
        return jsonify(result = webcam_flag)  
# --- init_mraa ---    
#pin_01 = mraa.Gpio(14)
#pin_01.dir(mraa.DIR_OUT)
#pin_01.write(0)
#
#pin_02 = mraa.Gpio(15)
#pin_02.dir(mraa.DIR_OUT)
#pin_02.write(0)
#
#pin_03 = mraa.Gpio(16)
#pin_03.dir(mraa.DIR_OUT)
#pin_03.write(0)
#
#pin_04 = mraa.Gpio(17)
#pin_04.dir(mraa.DIR_OUT)
#pin_04.write(0)
#
#pin_05 = mraa.Gpio(19)
#pin_05.dir(mraa.DIR_OUT)
#pin_05.write(0)
def ctrl_mraa(ctrl):
    print("Ctrl:",ctrl)
def ctrl_media(ctrl,file):
    global media_flag
    print ctrl,":",file
    if(ctrl == "play"):
        media_flag = True
    if(ctrl == "pause"):
        media_flag = False
    if(ctrl == "stop"):
        media_flag = False
    if(ctrl == "back"):
        media_flag = True
    if(ctrl == "next"):
        media_flag = True
def ctrl_webcam(ctrl):
    global webacm
    global webcam_flag
    if(ctrl == "webcam_on"):
#        if(!webcam_flag):
#            webacm = subprocess.Popen(['mjpg_streamer','-i',"input_uvc.so -f 20 -r 1280*720 -d /dev/video0" ,'-o', "output_http.so"])
        webcam_flag = True
    if(ctrl == "webcam_off"):
#        if(webcam_flag):
#            webacm.kill()
        webcam_flag = False
if __name__ == '__main__':
    app.run(host='0.0.0.0')