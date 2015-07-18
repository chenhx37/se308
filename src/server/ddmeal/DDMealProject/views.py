# from django.http import HttpResponse
# from django.http import HttpResponseRedirect
# from django.shortcuts import render_to_response

# from DDMealAPP.models import *
# def hello(request):
# 	return HttpResponse("Hello world!")

# def set_cookie_color(request):
# 	if "favorite_color" in request.GET:
# 		# create a HttpResponse object
# 		response = HttpResponse("Your favorite color is now %s" % request.GET["favorite_color"])
# 		# ... and set a cookie on the response
# 		response.set_cookie("favorite_color", request.GET["favorite_color"])
# 		return response
# 	else:
# 		return HttpResponse("You didn't give a favorite color")		

# # some ways to use session:
# # Set a session value:
# # request.session["fav_color"] = "blue"

# # Get a session value -- this could be called in a different view,
# # or many requests later (or both):
# # fav_color = request.session["fav_color"]

# # Clear an item from the session:
# # del request.session["fav_color"]

# # Check if the session has a given key:
# # if "fav_color" in request.session:

# # use session to check if already comment
# def post_comment(request):
# 	if request.method != 'POST':
# 		raise Http404("Only POSTs are allowed")
# 	if 'comment' not in request.POST:
# 		raise Http404("Comment not submit")
# 	if request.session.get("has_commented", False):
# 		return HttpResponse("You have already commented")

# 	c = comments.Comment(comment=request.POST['comment'])
# 	c.save()
# 	request.session['has_commented'] = True
# 	return HttpResponse("Thanks for your comment!")

# # form method='POST' 
# # form input :name=username name=password
# @csrf_protect
# def login(request):
# 	error = False
# 	if request.method !=  'POST':
# 		return render_to_response('login.html', context_instance=RequestContext(request))
# 	try:
# 		m = User.object.get(name=request.POST['username'])
# 		if m.password == request.POST['password']:
# 			request.session['member_id'] = m.member_id
# 			return HttpResponseRedirect('/index')
# 		else:
# 			error = True
# 	except Member.DoesNotExist:
# 		error = True
# 		return render_to_response('login.html',context_instance=RequestContext(request))

# def index(request):
# 	return render_to_response('index.html',context_instance=RequestContext(request))

# def logout(request):
# 	try:
# 		del request.session['member_id']
# 	except KeyError:
# 		pass
# 	return HttpResponse("You're logged out")

# def register(request):
# 	if request.method != 'POST':
# 		raise Http404("Only POSTs are allowed")
# 	try:
# 		post_name = request.POST['username']
# 		post_password = request.POST['password']
# 		user = User.objects.create_user(name=post_name,
# 			password=post_password)
# 		# write into the database
# 		user.save()
# 	except KeyError:
# 		pass
# 	return HttpResponse("You've register,please login")