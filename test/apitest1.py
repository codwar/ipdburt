import xmlrpclib
import time

KEY = "3b80a782e7a402c21a6a316a08cd9e9a81f6c222"
proxy = xmlrpclib.ServerProxy("http://localhost:8888/xmlrpc")

print "Update Name"

proxy.updateName(key,"Test API %s" % time.time())

players = [('Player1','127.0.0.1','guid1'),
            ('Player2','127.0.0.2','guid2'),
            ('Player3','127.0.0.3','guid3'),
            ('Player4','127.0.0.4','guid4'),
            ('Player5','127.0.0.5','guid5'),
            ('Player6','127.0.0.6','guid6')]

players2 = [('Player1-alias','127.0.0.1','guid1'),
            ('Player2-alias','127.0.0.2','guid2')]

players3 = [('Player4-alias','127.0.0.1','guid4'),
            ('Player6-alias','127.0.0.2','guid6')]

banplayers = [('guid1','baneado1'),('guid2','baneado1')]                        
print "Update logs"

proxy.updateConnect(key,players)
proxy.updateConnect(key,players2)
proxy.updateDisconnect(key,players3)

print "Update ban info"
proxy.updateBanInfo(key,banplayers)
print "Update connected again"
proxy.updateConnect(key,players)


