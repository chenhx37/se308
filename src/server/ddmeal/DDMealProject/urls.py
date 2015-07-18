from django.conf.urls import patterns, include, url
from DDMealProject.views import *
# from django.contrib.auth.views import login, logout
# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',

	# (r'^hello/$', hello),
	# (r'^$', login),
	# (r'^accounts/login/$', login),
	# (r'^accounts/logout/$', logout),
	# (r'^index/$', index),

    # Examples:
    # url(r'^$', 'DDMealProject.views.home', name='home'),
    # url(r'^DDMealProject/', include('DDMealProject.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
    # url(r'^$', include('DDMealAPP.urls')),
    url(r'^ddmeal/', include('DDMealAPP.urls')),
)
