# Create your views here.
#coding=utf-8
from django.views.decorators.csrf import csrf_exempt
from django.template.loader import get_template
from django.template import Context
from django.shortcuts import render,render_to_response
from django.http import HttpResponse,HttpResponseRedirect
from django.template import RequestContext
from django import forms
from models import *
from django.core import serializers

import json
import datetime
import time


# -------------------------------------------注册函数开始 ----------------------------
# 没有使用模板的注册
# post的表单中含有 username、password 参数
# 注册成功， 返回error为false，errorMs为空
# 注册失败， 返回error为true， errorMs为用户已存在
# 不是post请求，返回error为false， errorMs为请发一个表单请求
@csrf_exempt
def regist(req):
    if req.method == 'POST':
        newUserName = req.POST.get('username')
        user = User.objects.filter(realName__exact = newUserName)
        if user:
            # 已经存在
            error = True
            errorMs = "User's name already exist!"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
        else:
            # 创建新用户
            newPassword = req.POST.get('password')
            newUser = User.objects.create(realName=newUserName, password=newPassword)
            newUser.save()
            error = False
            errorMs = "create user success"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
    else:
        error = False
        errorMs = "Please post a form to registe"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------注册函数结束 ----------------------------
# -------------------------------------------登录函数开始 ----------------------------
# 没有使用模板的登录表单
# post表单中含有参数 username password 参数
# 成功返回 error = False errorMs 和含有cookie的response
# 失败返回 error = True 和errorMs
# 若不是post则返回error = False 和errorMs
# 通过添加@csrf_exempt来去除csrftoken
@csrf_exempt
def login(req):
    if req.method == 'POST':
        username = req.POST.get("username")
        password = req.POST.get("password")
        user = User.objects.filter(realName__exact = username,password__exact = password)
        if user:
            # 登录成功
            error = False
            errorMs = "log in success"
            response = HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
            #将username写入cookie,失效时间为3600
            response.set_cookie('username', username, -1)
            now = datetime.datetime.now()
            nowstamp = time.mktime(now.timetuple())
            response.set_cookie('lastTime', nowstamp, -1)
            response.set_cookie('flag', 0, -1)
            return response
        else:
            # 不存在该用户或密码错误
            error = True
            errorMs = "has no user or wrong password"
            response = HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
            return response
    else:
        error = True
        errorMs = "Please post a form to login"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------登录函数结束 ----------------------------
# -------------------------------------------主页函数开始 ----------------------------
# 没有使用模板的主页函数
# 返回各种数据
# users : 所有的用户
# dinings : 所有的食堂
# meals : 所有的菜式
# windows : 所有的窗口
# orders : 所有状态为0的订单 状态为0 表示还没有被接受、1为已接受、2为已完成
# myself : 我
# myOrders : 我发布的订单
# myAcceptOrders : 我接收的订单
@csrf_exempt
def index(req):
    username = req.COOKIES.get('username','')
    if(username == ''):
        error = True
        errorMs = "You should log in first"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
    error = False
    errorMs = ""
    users    = serializers.serialize("json", User.objects.all())
    dinings  = serializers.serialize("json", DiningRoom.objects.all())
    meals    = serializers.serialize("json", Meal.objects.all())
    windows  = serializers.serialize("json", Window.objects.all())
    orders   = serializers.serialize("json", Order.objects.filter(status=0))
    myself   = User.objects.get(realName=username)
    myselfJson = User.objects.get(realName=username).toJSON()
    myOrders = serializers.serialize("json", Order.objects.filter(postBy=myself))
    myAcceptOrders = serializers.serialize("json", Order.objects.filter(acceptBy=myself))
    return HttpResponse(json.dumps({"error":error, "errorMs":errorMs, "users":users, "dinings":dinings,\
        "meals":meals, "windows":windows, "orders":orders, "myself":myselfJson, "myOrders":myOrders, \
        "myAcceptOrders":myAcceptOrders}),content_type="application/json")
# -------------------------------------------注册函数结束 ----------------------------
# -------------------------------------------登出函数开始 ----------------------------
# 退出登录函数
# 返回删除了cookie的HttpResponse
@csrf_exempt
def logout(req):
    response = HttpResponse()
    response.delete_cookie('username')
    return response
# -------------------------------------------登出函数结束 ----------------------------
# -------------------------------------------发布任务函数开始 ----------------------------
# 发布任务函数
# 成功返回error 和errorMs
@csrf_exempt
def release(req):
    if req.method == 'POST':
        # 获取表单的信息
        username = req.COOKIES.get('username', '')
        if(username == ''):
            error = True
            errorMs = "You should log in first"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
        user = User.objects.get(realName=username)
        postTime = datetime.datetime.now()
        endTime = req.POST.get('endTime', '')
        diningRoomName = req.POST.get('diningRoom', None)
        if diningRoomName:
            print 
            diningRoom = DiningRoom.objects.get(name=diningRoomName)
        else:
            diningRoom = None
        mealPrice = req.POST.get('mealPrice', '')
        description = req.POST.get('description')
        price = req.POST.get('price', '')
        if mealPrice == '' or price == '':
            error = True
            errorMs = "price is null"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
        
        Order.objects.create(postTime=postTime, modifiedTime=postTime, endTime = endTime, \
            diningRoom = diningRoom, mealPrice = mealPrice, description=description,\
            price=price,status=0, postBy=user)
        error = False
        errorMs = "create order success!"
    else:
        error = True
        errorMs = "please post a form to release order"
    return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------发布任务函数结束 ----------------------------
# -------------------------------------------取消任务函数开始 ----------------------------
# 取消订单
# method:post 
# cookie :username
# 当订单为本人发布无人接受时可以取消
@csrf_exempt
def cancelOrder(req):
    if req.method == 'POST':
        username = req.COOKIES.get('username', '')
        if(username == ''):
            error = True
            errorMs = "You should log in first"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")

        postID = req.POST.get('id', False)
        if postID:
            deleteOrder = Order.objects.get(id=postID)
            if deleteOrder:
                if deleteOrder.postBy.realName == username:
                    #不知道能否正常判断状态
                    if deleteOrder.status == 0:
                        deleteOrder.delete()
                        error = False
                        errorMs = 'Cancel order success!'
                    else:
                        error = True
                        errorMs = "Order has been receive, could not delete"
                else:
                    error = True
                    errorMs = "This order not release by you"
            else:
                error = True
                errorMs = "Please post a correct order id"
        else:
            error = True
            errorMs = "Post the id please "
    else:
        error = True
        errorMs="please post a form to delete"
    return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------取消任务函数结束 ----------------------------
# -------------------------------------------完成任务函数开始 ----------------------------
@csrf_exempt
def finishOrder(req):
    if req.method == 'POST':
        postID = req.POST.get('id', False)
        if postID:
            updateOrder = Order.objects.get(id=postID)
            if updateOrder.status == 1:
                updateOrder.status = 2
                updateOrder.save()
                error = False
                errorMs = "finish order success!"
            else:
                error = True
                errorMs = "order not be receive, please delete"
        else:
            error = True
            errorMs = "Please post a id!"
    else:
        error = True
        errorMs = "please post a form to finish order"
    return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------完成任务函数结束 ----------------------------
# -------------------------------------------接受任务函数开始 ----------------------------
# 接受任务函数
# 获得任务的id，当任务状态为0时，表示为接受，改为1的接受状态，然后设置接受人员为我，
# 修改modifiedTime和接受时间为当前时间，返回error和errorMs
@csrf_exempt
def accept(req):
    if req.method == 'POST':
        username = req.COOKIES.get('username','')
        myself   = User.objects.get(realName=username)
        postID = req.POST['id']
        updateOrder = Order.objects.get(id=postID)
        if updateOrder:
            if updateOrder.status == 0:
                updateOrder.status = 1
                updateOrder.save()
                updateOrder.acceptBy = myself
                now = datetime.datetime.now()
                updateOrder.modifiedTime = now
                updateOrder.modifiedTime = now
                updateOrder.save();
                error = False
                errorMs = "order has been receive"
            else:
                error = True
                errorMs = "order already receive, please select another one"
        else:
            error = True
            errorMs = "Please post a right id !"
    return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------接受任务函数结束 ----------------------------
# -------------------------------------------更新个人资料函数开始 ----------------------------
@csrf_exempt
def updatePersonal(req):
    if req.method == 'POST':
        username = req.COOKIES.get('username', '')
        myself = User.objects.get(realName=username)
        nickName = req.POST.get('nickName','')
        phone = req.POST.get('phone','')
        address = req.POST.get('address','')
        netID = req.POST.get('netID','')

        myself.nickName = myself.nickName if nickName == '' else nickName
        myself.phone = myself.phone if phone == "" else phone
        myself.address = myself.address if address == "" else address
        myself.netID = myself.netID if netID == "" else netID
        myself.save()
        error = False
        errorMs = "update message success"
    else:
        error = True
        errorMs = "please post a form to update " 
    return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------更新个人资料函数结束 ----------------------------
# -------------------------------------------返回个人订单函数开始 ----------------------------
@csrf_exempt
def myMessage(req):
    if req.method == 'GET':
        username = req.COOKIES.get('username', '')
        if username == '':
            error = True
            errorMs = "Please log in first"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")

        myself = User.objects.get(realName=username)
        
        myOrders = serializers.serialize('json', Order.objects.filter(postBy=myself))
        decodejson = json.loads(myOrders)
        newmyOrders = orderSetting(decodejson)

        myAcceptOrders = serializers.serialize('json', Order.objects.filter(acceptBy=myself))
        decodejson = json.loads(myAcceptOrders)
        newmyAcceptOrders = orderSetting(decodejson)

        myselfJson = myself.toJSON()
        error = False
        errorMs = "ok"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs, "myself":myselfJson, "myOrders":newmyOrders, "myAcceptOrders":newmyAcceptOrders}),content_type="application/json")
    else:
        error = True
        errorMs = "Please get a request"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------返回个人订单函数结束 ----------------------------
# -------------------------------------------返回所有订单函数开始 ----------------------------
@csrf_exempt
def allMessage(req):
    if req.method == 'POST':
        username = req.COOKIES.get('username', '')
        # lastTime = req.COOKIES.get('lastTime', '')
        # flag = req.COOKIES.get('flag', '')
        lastTime = req.POST.get('lastTime')
        flag = req.POST.get('flag')
        myself = User.objects.get(realName=username)
         
        nowtime = datetime.datetime.now()
        # print "nowtime", nowtime
        nowstamp = time.mktime(nowtime.timetuple())
        diningRooms = serializers.serialize('json',DiningRoom.objects.all())
        if not flag or not lastTime or float(flag) < 0 or float(lastTime) < 0:
            error = True
            errorMs = 'could not get the flag or lastTime' 
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}), content_type="application/json")
        elif float(flag) == 0:
            orders = Order.objects.filter(status=0)
        else: 
            lt = time.localtime(float(lastTime))
            lastStr = time.strftime("%Y-%m-%d %H:%M:%S",lt)
            orders = Order.objects.filter(status=0, modifiedTime__gt = lastStr)
            print Order.objects.filter(status=0, modifiedTime__gt = lastStr)[0].id
        
        ordersJson = serializers.serialize('json', orders)
        decodejson = json.loads(ordersJson)
        # for dj in decodejson:
        #     # add diningRoomName
        #     if dj['fields']['diningRoom']:
        #         _drname =  DiningRoom.objects.get(id=dj['fields']['diningRoom']).name
        #         dj['fields'].setdefault("diningRoomName", _drname)
        #     else :
        #         dj['fields'].setdefault("diningRoomName", None)
            
        #     # add postUserName
        #     _postUserName = User.objects.get(id=dj['fields']['postBy']).realName
        #     dj['fields'].setdefault("postUserName", _postUserName)
            
        #     # meal
        #     if dj['fields']['meal']:
        #         _mealname = Meal.objects.get(id=dj['fields']['meal']).name
        #         dj['fields'].setdefault("mealName", _mealname)
        #     else :
        #         dj['fields'].setdefault("diningRoomName", None)
        neworders = orderSetting(decodejson)
        return HttpResponse(json.dumps({"diningRooms":diningRooms, "orders":neworders,"lastTime":nowstamp,"flag":1}),content_type="application/json")
    else:
        error = False
        errorMs = "Please post a request"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------返回所有订单函数结束 ----------------------------

# def fileUpload(req):
#     username = req.COOKIES.get('username', '')
#     if (username == ''):
#         return HttpResponseRedirect('/')
#     if (req.method == 'POST'):
#         form = FileForm(req.POST, req.FILES)
#         if form.is_valid():
#             handle_uploaded_file(req.FILES['file'])
#             return HttpResponseRedirect('/success/url')
#     else:
#         form = FileForm()
#     return render_to_response('fileUpload.html', {'form':form},context_instance=RequestContext(req))


# -------------------------------------------返回个人信息函数开始 ----------------------------
@csrf_exempt
def personalMs(req):
    if req.method == 'GET':
        username = req.COOKIES.get('username', '')
        if username == '':
            error = True
            errorMs = "Please log in first"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
        myself = User.objects.get(realName=username)
        if myself:      
            myselfJson = myself.toJSON()
            error = False
            errorMs = "return personal message"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs, "myself":myselfJson}),content_type="application/json")
        else:
            error = True
            errorMs = "Wrong username in cookie!"
            return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
    else:
        error = True
        errorMs = "Please get a request"
        return HttpResponse(json.dumps({"error":error, "errorMs":errorMs}),content_type="application/json")
# -------------------------------------------返回个人信息函数结束 ----------------------------

def orderSetting(decodejson):
    for dj in decodejson:
        # add diningRoomName
        if dj['fields']['diningRoom']:
            _drname =  DiningRoom.objects.get(id=dj['fields']['diningRoom']).name
            dj['fields'].setdefault("diningRoomName", _drname)
        else :
            dj['fields'].setdefault("diningRoomName", None)
        
        # add postUserName
        _postUserName = User.objects.get(id=dj['fields']['postBy']).realName
        dj['fields'].setdefault("postUserName", _postUserName)
        
        # meal
        if dj['fields']['meal']:
            _mealname = Meal.objects.get(id=dj['fields']['meal']).name
            dj['fields'].setdefault("mealName", _mealname)
        else :
            dj['fields'].setdefault("diningRoomName", None)

    return decodejson