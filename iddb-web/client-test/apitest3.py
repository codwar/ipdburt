import xmlrpclib
import time
import datetime
import socket

keys= ['3483c7a89bc3eb257bb54d74121315b17aa2ee33']

for  key in keys:
    proxy = xmlrpclib.ServerProxy("http://www.iddb.com.ar/api/v3/xmlrpc")
    socket.setdefaulttimeout(10)
    
    print "Update Name"
    
    try:
        proxy.updateName(key,"Server-%s" % key, ["test", 5])
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
    
    baninfo = ["pb", int(time.time()), 0, "Ban Api 3", "El Admin", "guid2"]
    noteinfo = [int(time.time()), "Una Nota", "El Admin", "guid2"]
    
    players = [('connect', 'Player1-1','guid1', '1', '127.0.0.1', '0'),
               ('connect', 'Player2-1','guid2', '2', '127.0.0.2', '0'),
                ('connect', 'Player3-1','guid3', '3', '127.0.0.3', '1'),
                ('connect', 'Player4-1','guid4', '4', '127.0.0.4', '2'),
                ('connect', 'Player5-1','guid5', '5', '127.0.0.5', '20'),
                ('connect', 'Player6-1','guid6', '6', '127.0.0.1', '0'),
                ('disconnect', 'Player1-2','guid1', '1', '127.0.0.1', '0'),
                ('update', 'Player2-2','guid2', '2', '127.0.0.1', '1'),
                ('banned', 'Player2-2','guid2', '2', '127.0.0.1', '1', int(time.time()), baninfo),
                ('banned', 'Player3-2','guid3', '3', '127.0.0.1', '1', int(time.time()), baninfo),
                ('addnote', 'Player3-2','guid3', '3', '127.0.0.1', '1', int(time.time()), noteinfo),
                ('unbanned', 'Player2-2','guid2', '2', '127.0.0.1', '1'),]
    
    print "Update logs"
    proxy.update(key,players)
