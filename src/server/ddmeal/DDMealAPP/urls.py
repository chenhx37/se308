from django.conf.urls import patterns, url
from DDMealAPP import views

 
urlpatterns = patterns('',
    url(r'^$', views.login, name='login'),
    url(r'^login/$', views.login, name = 'login'),
    url(r'^regist/$', views.regist, name = 'regist'),
    url(r'^index/$', views.index, name = 'index'),
    url(r'^index/release/$', views.release, name= 'release'),
    url(r'^logout/$', views.logout, name = 'logout'),
    # url(r'^fileUpload/$', views.fileUpload, name='fileUpload'),
    url(r'^index/cancelOrder/$', views.cancelOrder, name='cancelOrder'),
    url(r'^index/finishOrder/$', views.finishOrder, name='finishOrder'),
    url(r'^index/accept/$', views.accept, name='accept'),
    url(r'^index/updatePersonal/$', views.updatePersonal, name='updatePersonal'),
    url(r'^index/allMessage/$', views.allMessage,name='allMessage'),
    url(r'^index/myMessage/$',views.myMessage, name='myMessage'),
    url(r'^index/personalMs/$',views.personalMs, name='personalMs'),
    
)
