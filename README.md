REST Item
========

This is a simple rest impl for sample item services.

Services
========
GetAuth : This is a convenience service which retrieves a digest for later REST authentications
CreateItem : This service creates (an) item(s) in the datasource. Only PUT/POST are supported
GetItemDetails : This service retrieves items from DB based on the location of request. 

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

Try serices with incorrect REST URI
Try serices with incorrect auth appIdKey
Test http get limitations on uri too long to handle to yield HTTP 414
Test http get limitations on header size limit of default 8K
Test HTTP POST/PUT limitations of maxPostSize on server (default 2M)
Test CreateItem service with excess size of item JSON array
Test all services for idempotent nature
Test for ACID nature of services
Test for continuity of services
Try endpoints with special chars in header
Try endpoints with special chars in body
Try endpoints with Empty/No json in POST body
Try endpoints with invalid json in POST body
Try CreateItem with no item id in request, where itemId is mandatory field
Try endpoints with no content-type specified
Try endpoints with faulty content type
Try endpoints with incorrect/faulty method
Try to fetch item when there is no item in data source
Try timestamp with alphanumeric values
Try accessing non-exposed services/random URIs to yield HTTP 404