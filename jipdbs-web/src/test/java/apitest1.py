import xmlrpclib
import time

key = "d87eca9fac12710a1aadab89d5f69ed8b3f31d05"
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

banplayers = [('guid1','baneado1','player1','127.0.0.50'),('guid6','baneado1','player2','127.0.0.50')]                        
print "Update logs"

proxy.updateConnect(key,players)
proxy.updateConnect(key,players2)
proxy.updateDisconnect(key,players3)
print "Update ban info"
proxy.updateBanInfo(key,banplayers)
print "Update connected again"
proxy.updateConnect(key,players2)