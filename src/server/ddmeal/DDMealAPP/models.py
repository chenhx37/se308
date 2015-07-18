from django.db import models

# Create your models here.
class User(models.Model):
	email    = models.CharField(max_length=60,blank = True,null = True)
	realName = models.CharField(max_length=30)
	nickName = models.CharField(max_length=30,blank = True,null = True)
	password = models.CharField(max_length=32)
	phone    = models.CharField(max_length=20,blank = True,null = True)
	address  = models.CharField(max_length=50,blank = True,null = True)
	netID    = models.CharField(max_length=30,blank = True,null = True)
	def __unicode__(self):
		return u'email:%s, realName:%s, \
		password:%s, netID:%s' \
		% (self.email, self.realName, \
		self.password, self.netID)
	def toJSON(self):
		import json
		return json.dumps(dict([(attr, getattr(self, attr)) for attr in [f.name for f in self._meta.fields]]))
class DiningRoom(models.Model):
	name = models.CharField(max_length=30)
	"""docstring for DiningRoom"""
	def __unicode__(self):
		return u'name:%s' % self.name
	def toJSON(self):
		import json
		return json.dumps(dict([(attr, getattr(self, attr)) for attr in [f.name for f in self._meta.fields]]))

class Window(models.Model):
	name       = models.CharField(max_length=30)
	diningRoom = models.ForeignKey(DiningRoom)
	"""docstring for Window"""
	def __unicode__(self):
		return u'name:%s' % self.name
	def toJSON(self):
		import json
		return json.dumps(dict([(attr, getattr(self, attr)) for attr in [f.name for f in self._meta.fields]]))

class Meal(models.Model):
	name   = models.CharField(max_length=30)
	price  = models.FloatField()
	diningRoom = models.ForeignKey(DiningRoom)
	"""docstring for Meal"""
	def __unicode__(self):
		return u'name:%s, price:%s' % (self.name, self.price)
	def toJSON(self):
		import json
		return json.dumps(dict([(attr, getattr(self, attr)) for attr in [f.name for f in self._meta.fields]]))

class Order(models.Model):
	postBy       = models.ForeignKey("User",related_name="postUser")
	acceptBy     = models.ForeignKey("User",related_name="acceptUser",blank = True,null = True)
	postTime     = models.DateTimeField()
	endTime      = models.DateTimeField(blank = True,null = True)
	acceptTime   = models.DateTimeField(blank = True,null = True)
	modifiedTime = models.DateTimeField(blank = True,null = True)
	status       = models.IntegerField()
	meal         = models.ForeignKey(Meal,blank = True,null = True)
	diningRoom   = models.ForeignKey(DiningRoom, blank = True, null = True)
	mealPrice    = models.FloatField()
	description  = models.TextField(blank = True,null = True)
	price        = models.FloatField()

	def __unicode__(self):
		return u'postTime:%s, \
		endTime:%s, acceptTime:%s, modifiedTime:%s, \
		status:%s, meal:%s, description:%s, price:%s' \
		% (self.postTime, \
		self.endTime, self.acceptTime, self.modifiedTime, \
		self.status, self.meal, self.description, self.price)
	def toJSON(self):
		import json
		return json.dumps(dict([(attr, getattr(self, attr)) for attr in [f.name for f in self._meta.fields]]))




		
		

