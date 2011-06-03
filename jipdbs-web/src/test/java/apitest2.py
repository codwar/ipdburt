import xmlrpclib
import time
import datetime

key = "01fecb4676e366f7d934e9e59c026b60e5fffe29"
proxy = xmlrpclib.ServerProxy("http://localhost:8080/xmlrpc2")

print "Update Name"
proxy.updateName(key,"Test API %s" % time.time(), "test")

baninfo = "%s::%s::%s::%s" % ('pb', int(time.time()), -1, 'Test')

players = [('connect', 'Player1-1','guid1', '1', '127.0.0.1', '0'),
           ('connect', 'Player2-1','guid2', '2', '127.0.0.2', '0'),
            ('connect', 'Player3-1','guid3', '3', '127.0.0.3', '1'),
            ('connect', 'Player4-1','guid4', '4', '127.0.0.4', '2'),
            ('connect', 'Player5-1','guid5', '5', '127.0.0.5', '20'),
            ('connect', 'Player6-1','guid6', '6', '127.0.0.1', '0'),
            ('disconnect', 'Player1-2','guid1', '1', '127.0.0.1', '0'),
            ('update', 'Player2-2','guid2', '2', '127.0.0.1', '1'),
            ('addnote', 'Player2-2','guid2', '2', '127.0.0.1', '1', time.time(), "un pt"),
            ('banned', 'Player2-2','guid2', '2', '127.0.0.1', '1', time.time(), baninfo),
            ('banned', 'Player3-2','guid3', '3', '127.0.0.1', '1', time.time(), baninfo),
            ('unbanned', 'Player3-2','guid3', '3', '127.0.0.1', '1'),]

print "Update logs"
proxy.update(key,players)
#proxy.update(key,[])