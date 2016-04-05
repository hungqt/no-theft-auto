__author__ = 'Simon'

import MySQLdb
import urllib2
import json
import socket

# lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                     user="glennchr_nta",  # your username
                     passwd="nta123",  # your password
                     db="glennchr_nta")  # name of the data base

ip = [l for l in ([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")][:1],
                   [[(s.connect(('8.8.8.8', 53)), s.getsockname()[0], s.close())
                     for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]]) if l][0][0]



def sendCoordinates(token):

    if not (token == None or token == ""):
        url = 'https://gcm-http.googleapis.com/gcm/send'
        postdata = {"data": {"message": getCarName()}, "to": token}

        req = urllib2.Request(url)
        req.add_header('Content-Type', 'application/json')
        req.add_header("Authorization", "key=AIzaSyDp9WZAvpQBkBEsoo_KyWPjkTOeR3lUEXo")
        data = json.dumps(postdata)

        response = urllib2.urlopen(req, data)

def sendCoordinates():

    TCP_IP = '78.91.8.239'
    TCP_PORT = 9009
    BUFFER_SIZE = 1024
    MESSAGE = "Hello"

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((TCP_IP, TCP_PORT))
    s.send("Hello")
    s.send("Hello")
    print "yo"
    #data = s.recv(BUFFER_SIZE)
    s.close()

    #print "received data:", data

def hey():
    HOST = "localhost"
    PORT = 8080

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((HOST, PORT))

    sock.sendall("Hello\n")
    data = sock.recv(1024)
    print "1)", data

    if ( data == "olleH\n" ):
        sock.sendall("Bye\n")
        data = sock.recv(1024)
        print "2)", data

        if (data == "eyB}\n"):
            sock.close()
            print "Socket closed"


def getPhoneIp():
    # lager en cursor som kan kjore sql soringer
    cur = db.cursor()
    cur.execute("SELECT IP FROM user WHERE username = '{0}".format(getUsername(rpi_id)))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return str(value)
