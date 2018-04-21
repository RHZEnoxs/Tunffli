#-*- coding:utf8 -*-
import os

#path = '/Media/SD-P1/Music'
path = '/Media/SD-P1/Music/'    
#path = 'E:/Enoxs_Develop/3.專案項目/Turn off the lights(tunfli/程式碼/BackUp'
fold_list = []
file_list = []
for root,dirs,files in os.walk(path): 
    for dir in dirs:
        fold_list.append(dir)
        new = []
        file_list.append(new)
    for file in files: 
        Arr = os.path.join(root,file).replace(path,"").split("/")
        print Arr
        if(len(Arr) == 1):
            file_list.append(Arr[0])
        if(len(Arr) == 2):
            file_list[fold_list.index(Arr[0])].append(Arr[1])
print fold_list
print file_list

