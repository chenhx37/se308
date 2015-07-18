import httplib, urllib
params = urllib.urlencode({'username':"cjs", 'password':"cjs"})
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
conn = httplib.HTTPConnection("127.0.0.1:8000")
conn.request("POST", "/ddmeal/login/", params, headers)
response = conn.getresponse()
print response.status
data = response.read()
conn.close()

import httplib, urllib
params = urllib.urlencode({'username':"cjs", 'password':"cjs"})
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
conn = httplib.HTTPConnection("didimeal.sinaapp.com")
conn.request("POST", "/ddmeal/login/", params, headers)
response = conn.getresponse()
print response.status
data = response.read()
conn.close()

# index
import httplib, urllib
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
conn = httplib.HTTPConnection("didimeal.sinaapp.com")
conn.request("GET", "/ddmeal/index/")
conn.add_header('cookie', 'username=cjs')
response = conn.getresponse()
print response.status
data = response.read()
conn.close()

import urllib2
opener = urllib2.build_opener()
opener.addheaders.append(('Cookie', 'username=cjs'))
f = opener.open("http://didimeal.sinaapp.com")

# 模拟注册
import httplib, urllib
params = urllib.urlencode({'username':"czz", 'password':"czz"})
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
conn = httplib.HTTPConnection("didimeal.sinaapp.com")
conn.request("POST", "/ddmeal/regist/", params, headers)
response = conn.getresponse()
print response.status
data = response.read()
conn.close()

#模拟登录去index

import urllib
import httplib2
http = httplib2.Http()
url = "http://www.didimeal.sianapp.com/ddmeal/login/"
body = {'username':'cjs', 'password':'cjs'}
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
response, content = http.request(url, 'POST', headers = headers, body=urllib.urlencode(body))
headers = {'cookie':response['set-cookie']}
url = "http://www.didimeal.sianapp.com/ddmeal/index/"
response, content = http.request(url, 'GET', headers = headers)

# ok= 模拟 allMessage
import httplib, urllib
params = urllib.urlencode({'flag':"0", 'lastTime':"0"})
headers = {"Cookie":"username=cjs", "Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
conn = httplib.HTTPConnection("ddmeal.sinaapp.com")
conn.request("POST", "/ddmeal/index/allMessage/", params, headers)
response = conn.getresponse()
print response.status
data = response.read()
conn.close()

# import urllib2
# opener = urllib2.build_opener()
# opener.addheaders.append(('Cookie', 'username=cjs'))
# f = opener.open("http://didimeal.sinaapp.com/ddmeal/index/")
# import httplib, urllib
# headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
# conn = httplib.HTTPConnection("didimeal.sinaapp.com")
# conn.request("GET", "/ddmeal/index/", headers)
# response = conn.getresponse()
# print response.status
# data = response.read()
# conn.close()

# import urllib2
# opener = urllib2.Request("http://didimeal.sinaapp.com/ddmeal/index")
# opener.add_header("Content-type","application/x-www-form-urlencoded")
# opener.add_header("Accept","text/plain")
# opener.add_header("Cookie","username=cjs")
# req = urllib2.urlopen(opener)
# resInfo = resp.info()

import urllib2
cookies = urllib2.HTTPCookieProcessor()
opener = urllib2.build_opener(cookies)
url = "http://www.didimeal.sinaapp.com/ddmeal/login/"
params = {'username':'cjs', 'password':'cjs'}
postData = urllib.urlencode(params)
f = opener.open(url,postData)
url = "http://www.didimeal.sinaapp.com/ddmeal/index/"
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
d = urllib2.Request(url=url, headers=headers)
opener.open(d)

import urllib2
import urllib
cookies = urllib2.HTTPCookieProcessor()
opener = urllib2.build_opener(cookies)
url = "http://www.didimeal.sinaapp.com/ddmeal/login/"
params = {'username':'cjs', 'password':'cjs'}
postData = urllib.urlencode(params)
f = opener.open(url,postData)
print f.read()
url = "http://www.didimeal.sinaapp.com/ddmeal/index/"
headers = {'Content-Type' : 'text/xml'}
d = urllib2.Request(url=url, headers=headers)
opener.open(d)
res = opener.open(d)
print res.info()

# 
import urllib2
opener = urllib2.build_opener()
opener.addheaders.append(('Cookie','username=cjs'))
opener.addheaders.append(('Content-Type','text/xml'))
opener.addheaders.append(('Accept','text/plain'))
f = opener.open("http://www.didimeal.sinaapp.com/ddmeal/index/")

# third
# first url request
import urllib2
import cookielib
import urllib
loginurl = "http://didimeal.sinaapp.com/ddmeal/login/";
cj = cookielib.CookieJar();
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj));
urllib2.install_opener(opener);
para = {'username':'cjs', 'password':'cjs'}
postData = urllib.urlencode(para)
req = urllib2.Request(loginurl, postData)
req.add_header('Content-Type', 'application/x-www-form-urlencoded')
req.add_header('Connection', 'Keep-Alive')
resp = urllib2.urlopen(req)
print resp.read()
indexUrl = "http://didimeal.sinaapp.com/ddmeal/index/"
resp = urllib2.urlopen(indexUrl)
respInfo = resp.info()

#
import urllib2
import cookielib
import urllib
loginurl = "http://127.0.0.1:8000/ddmeal/login/";
cj = cookielib.CookieJar();
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj));
urllib2.install_opener(opener);
para = {'username':'cjs', 'password':'cjs'}
postData = urllib.urlencode(para)
req = urllib2.Request(loginurl, postData)
req.add_header('Content-Type', 'application/x-www-form-urlencoded')
req.add_header('Connection', 'Keep-Alive')
resp = urllib2.urlopen(req)
print resp.read()
print resp
indexUrl = "http://127.0.0.1:8000/ddmeal/index/"
resp = urllib2.urlopen(indexUrl)
respInfo = resp.info()

# post a request to cancel a order

import urllib
import httplib2
http = httplib2.Http()
url = "http://www.didimeal.sianapp.com/ddmeal/index/allMessage/"
body = {'lastTime':'0', 'flag':0}
headers = {"Content-type":"application/x-www-form-urlencoded", "Accept":"text/plain"}
response, content = http.request(url, 'POST', headers = headers, body=urllib.urlencode(body))
headers = {'cookie':response['set-cookie']}
url = "http://www.didimeal.sianapp.com/ddmeal/index/"
response, content = http.request(url, 'GET', headers = headers)

import urllib2
opener = urllib2.build_opener()
opener.addheaders.append(('Cookie','username=cjs'))
opener.addheaders.append(('Content-Type','application/x-www-form-urlencoded'))
opener.addheaders.append(('Accept','text/plain'))
f = opener.open("http://www.ddmeal.sinaapp.com/ddmeal/index/")
