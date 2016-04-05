__author__ = 'Simon'

import thread
import MySQLdb
import time
import urllib2
import json
import socket

# https://github.com/PyMySQL/PyMySQL/ <-- Dokumentasjon for db connection

# lager en connection til db for gps_sender_main
db1 = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base

# lager en connection til db for send_ip_main
db2 = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base
rpi_id = 1

def main():
    try:
        thread.start_new_thread(activation_main, ())
        thread.start_new_thread(gps_sender_main, ())
        thread.start_new_thread(send_ip_main, ())
    except:
        print "Unable to start thread"

    while True:
        pass


def activation_main():
    menu = {'1': "Activate alarm", '2': "Deactive alarm", '3': "Read alarm status", '4': "Quit\n"}

    while True:
        options = menu.keys()
        options.sort()
        for entry in options:
            print entry, menu[entry]

        selection = raw_input("Please Select: ")
        if selection == '1':
            setAlarm(1)
            sendNotification(getToken(getUsername()))
        elif selection == '2':
            setAlarm(0)
        elif selection == '3':
            text = "\nAlarm status: %d \n" % getAlarm()
            print text
        elif selection == '4':
            break
        else:
            print "Unknown Option Selected!"


# Henter ut latitude, longtitude og timestamp fra fil.
# Kjorer til filen er lest igjennom.
# Har delay pa 1 sek

def gps_sender_main():

    f = open("coord1.txt", "r")
    tid = 0
    while True:
        h = 0
        lang = 0
        lat = 0


        while True:
            # kode som leser txt-fil
            a = f.readline()

            if a == "":
                f.close()
                break

            if h == 2:
                break

            b = a.split("\"")
            c = b[6].split(":")[1].split(",")[0]
            d = b[3]
            timestamp = b[8].split(":")[1].split("}")[0]

            if d == "latitude" and lat == 0:
                lat = 1
                h += 1
                latitude = c
            elif d == "longitude" and lang == 0:
                lang = 1
                h += 1
                longitude = c

        if a == "":

            f.close()
            break

        #print rpi_id, " ", timestamp, " ", latitude, " ", longitude
        sendLatitudeLogditude(timestamp, longitude, latitude, rpi_id)
        if tid >= 5:
            updateCurrCoor(longitude, latitude)
            #print "Current coordinates updated"
            tid = 0
        time.sleep(1)
        tid += 1

def send_ip_main():

    ip = [l for l in ([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")][:1],
                   [[(s.connect(('8.8.8.8', 53)), s.getsockname()[0], s.close())
                     for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]]) if l][0][0]

    while True:
        setIp(ip)
        #print "ip er oppdatert"
        time.sleep(5)


def sendLatitudeLogditude(time, longitude, latitude, pi_id):
    cur = db1.cursor()
    try:
        cur.execute('''INSERT INTO gps_log (timestamp, longitude, latitude, pi_id) VALUES (%s, %s, %s, %s)''',
                    (time, longitude, latitude, pi_id))
        db1.commit()
    except:
        db1.rollback()

def updateCurrCoor(longtitude, latitude):
    cur = db1.cursor()
    try:
        cur.execute('''UPDATE raspberry_pi SET Coords = '{0}' WHERE rpi_id = 1'''.format(longtitude + "," + latitude))
        db1.commit()
    except:
        db1.rollback()


def sendNotification(token):

    if not (token == None or token == ""):
        url = 'https://gcm-http.googleapis.com/gcm/send'
        postdata = {"data": {"message": getCarName()}, "to": token}

        req = urllib2.Request(url)
        req.add_header('Content-Type', 'application/json')
        req.add_header("Authorization", "key=AIzaSyDp9WZAvpQBkBEsoo_KyWPjkTOeR3lUEXo")
        data = json.dumps(postdata)

        response = urllib2.urlopen(req, data)


def setAlarm(value):
    cur = db1.cursor()
    stringValue = str(value)
    try:
        cur.execute("UPDATE glennchr_nta.raspberry_pi SET alarm =(%s)  WHERE rpi_id = (%s)", (stringValue, rpi_id))
        db1.commit()
    except:
        db1.rollback()


# get-metoder
def getAlarm():
    # lager en cursor som kan kjore sql soringer
    cur = db1.cursor()
    cur.execute('''SELECT alarm FROM raspberry_pi WHERE rpi_id = '{0}' '''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getToken(username):
    # lager en cursor som kan kjore sql soringer
    cur = db1.cursor()
    user = str(username)
    cur.execute('''SELECT token FROM user WHERE username = '{0}' '''.format(username))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getCarName():
    # lager en cursor som kan kjore sql soringer
    cur = db1.cursor()
    cur.execute('''SELECT car_name FROM raspberry_pi WHERE rpi_id = {0}'''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getUsername():
    cur = db1.cursor()
    cur.execute(''' SELECT U.username
                    FROM user U, user_has_rpi UR
                    WHERE {0} = UR.rpi_id AND U.user_id = UR.user_id'''.format(rpi_id))
    return cur.fetchone()[0]


def setIp(rpiIp):
    cur = db2.cursor()
    try:
        cur.execute("""UPDATE raspberry_pi SET IP ='{0}' WHERE rpi_id = 1""".format(rpiIp))
        db2.commit()
    except:
        db2.rollback()

main()