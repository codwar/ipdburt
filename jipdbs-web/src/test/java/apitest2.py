import xmlrpclib
import time
import datetime
import socket

keys= ['0759890c22075f7d2e473817a468a1c80c9670f6','b20553616be35a6ad03067e29a8667d8e7d85fd3','cf3d578c7828d1abecec2b74562121bd8179063f','073b99edd082f46bb9586f85281be05a02291938']

for  key in keys:
    proxy = xmlrpclib.ServerProxy("http://localhost:8080/xmlrpc2")
    socket.setdefaulttimeout(10)
    
    print "Update Name"
    
    try:
        proxy.updateName(key,"T%s" % key.replace("<", "&lt;").replace(">","&gt;"), "test")
    except xmlrpclib.ProtocolError, protocolError:
        print "1"
    except xmlrpclib.Fault, applicationError:
        print "2"
    except socket.timeout, timeoutError:
        print "timedout"
    except socket.error, x:
        print x
        print "conn error"
    except Exception, e:
        raise e
    socket.setdefaulttimeout(None)
    
    baninfo = "%s::%s::%s::%s" % ("pb", int(time.time()), -1, "adverte: por gil")
    
    baninfo = "%s::%s::%s::%s" % ('pb', int(time.time()), -1, 'Test')
    
    players = [('connect', 'Player1-1','guid1', '1', '127.0.0.1', '0'),
               ('connect', 'Player2-1','guid2', '2', '127.0.0.2', '0'),
                ('connect', 'Player3-1','guid3', '3', '127.0.0.3', '1'),
                ('connect', 'Player4-1','guid4', '4', '127.0.0.4', '2'),
                ('connect', 'Player5-1','guid5', '5', '127.0.0.5', '20'),
                ('connect', 'Player6-1','guid6', '6', '127.0.0.1', '0'),
                ('disconnect', 'Player1-2','guid1', '1', '127.0.0.1', '0'),
                ('update', 'Player2-2','guid2', '2', '127.0.0.1', '1'),
                #('addnote', 'Player2-2','guid2', '2', '127.0.0.1', '1', int(time.time()), "un pt"),
                #('banned', 'Player2-2','guid2', '2', '127.0.0.1', '1', int(time.time()), baninfo),
                #('banned', 'Player3-2','guid3', '3', '127.0.0.1', '1', int(time.time()), baninfo),
                ('unbanned', 'Player3-2','guid3', '3', '127.0.0.1', '1'),]
    
    print "Update logs"
    proxy.update(key,players)
