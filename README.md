REST Item
========

This is a simple rest impl for sample item services.

Services
========

GetAuth : This is a convenience service which retrieves a digest for later REST authentications

CreateItem : This service creates (an) item(s) in the datasource. Only PUT/POST are supported

GetItemDetails : This service retrieves items from DB based on the location of request. 

SystemStatus : This convenience service would tell you if the system is up, if success = 0


Before you start
================

The test cases are entirely focused on REST aspect of application. So to run your tests, please download a snapshot war from repo below and deploy it on Tomcat.

Configurations on Tomcat :

Please note that there is a fallback mechanism for all configs mentioned here. But to override them, please set these env vars on Tomcat.

e.g -DdataSource=db


dataSource {db(default),cms}

restServer {localhost(default), restitem.gautamnc.cloudbees.net ...}

overridePort {8080, 7070(default) ...}

privateKey {any-private-key-you-like (look in ResourceUtility class)} // very much not supposed to be here!!

locale {"South Bay" (default), "San Francisco"}

I do personally think that all of these settings/configs are not supposed to be here, should be in secured NFS or configuration system.


End Points And Details
======================

GetAuth

URI : http://xxxxxx:zzzz/restitem/services/security/authToken

Headers Required : appId(identity of your app), timestamp(a timestamp of request), uri(attempted uri)

CreateItem

URI : http://xxxxxx:zzzz/restitem/services/items/createItems

Headers Required : appIdKey(authentication digest, use getAuth service to get one), appId(identity of your app), timestamp(a timestamp of request), uri(attempted uri), content-type (generally application/json for json data)

GetItemDetails

URI : http://xxxxxx:zzzz/restitem/services/items/AAA101

Headers Required : appIdKey(authentication digest, use getAuth service to get one), appId(identity of your app), timestamp(a timestamp of request), uri(attempted uri), content-type (generally application/json for json data)

Enhancements and TODOs
======================
Improve auth by adding timestamp check, rather timeout of say 5 mins, and there by denying reply attacks and middle man attack.

Configure app to set and get props from properties bundle, configuration server as fall back from system env

Add an in-mem Data Store like HSQLDB, Derby or H2

Implement CMS api to read from JSON/XML repository

Implementing and using SSL to secure HTTP traffic

Testing
=======

Implement Test scripts for following scenarios

Test http get limitations on uri too long to handle to yield HTTP 414

Test http get limitations on header size limit of default 8K

Test HTTP POST/PUT limitations of maxPostSize on server (default 2M)

Test CreateItem service with excess size of item JSON array

Try serices with incorrect REST 

Try endpoints with special chars in url

Try services with incorrect auth appIdKey

Test all services for idempotent nature

Test for ACID nature of services

Test for continuity of services

Try endpoints with special chars in header

Try endpoints with Empty/No header

Try endpoints with special chars in body

Try endpoints with Empty/No json in POST body

Try endpoints with invalid json in POST body
~

Try CreateItem with no item id in request, where itemId is mandatory field

Try endpoints with no content-type specified

Try endpoints with faulty content type

Try endpoints with incorrect/faulty method

Try to fetch item when there is no item in data source

Try timestamp with alphanumeric values

Try accessing non-exposed services/random URIs to yield HTTP 404

Check and verify all JSON expected services are returning with Content-Type as application/json in header
