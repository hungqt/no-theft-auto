__author__ = 'Simon'

import thread
import MySQLdb
import time
import urllib2
import json
import RPi.GPIO as GPIO
import sys


rpi_id = 1


#GPIO leds og knapper setup
GreenLed = 11
RedLed = 7
left_button = 10
right_button = 8

GPIO.setmode(GPIO.BOARD)
GPIO.setup(RedLed, GPIO.OUT)
GPIO.setup(GreenLed, GPIO.OUT)




#cur = db1.cursor()
# cur.execute("UPDATE glennchr_nta.raspberry_pi SET update=1  WHERE rpi_id=1")
# db1.commit()
# print("hooo")


def main():

    try:
        thread.start_new_thread(activation_main2, ())
        thread.start_new_thread(gps_sender_main, ())
    except:
        print "Unable to start thread"

    while True:
        pass


def activation_main():
    menu = {'1': "Activate alarm", '2': "Deactive alarm", '3': "Read alarm status", '4': "Quit\n"}

    while True:
        alarmstatus = getAlarm()
        if alarmstatus == 1:
            GPIO.output(RedLed, 1)
            GPIO.output(GreenLed, 0)
        else:
            GPIO.output(GreenLed, 1)
            GPIO.output(RedLed, 0)

        options = menu.keys()
        options.sort()
        for entry in options:
            print entry, menu[entry]

        selection = raw_input("Please Select: ")
        if selection == '1':
            setAlarm2(1)
            sendNotification(getToken(getUsername()))

            alarmstatus = getAlarm()
            if alarmstatus == 1:
                GPIO.output(RedLed, 1)
                GPIO.output(GreenLed, 0)
        elif selection == '2':
            setAlarm2(0)

            alarmstatus = getAlarm()
            if alarmstatus == 0:
                GPIO.output(RedLed, 1)
                GPIO.output(GreenLed, 0)
        elif selection == '3':
            text = "\nAlarm status: %d \n" % getAlarm()
            print text
        elif selection == '4':
            sys.exit()
        else:
            print "Unknown Option Selected!"


def activation_main2():

    GPIO.setup(right_button, GPIO.IN, GPIO.PUD_UP)
    GPIO.setup(left_button, GPIO.IN, GPIO.PUD_UP)

    try:
        while True:

            alarmstatus = getAlarm()
            if alarmstatus == 1:
                GPIO.output(RedLed, 1)
                GPIO.output(GreenLed, 0)
            else:
                GPIO.output(GreenLed, 1)
                GPIO.output(RedLed, 0)

            if GPIO.input(right_button) == False:
                setAlarm2(1)
                sendNotification(getToken(getUsername()))

            if GPIO.input(left_button) == False:
                setAlarm2(0)
                sendNotification(getToken(getUsername()))

    except KeyboardInterrupt:
        GPIO.cleanup()


##def activation_main2():
##    GPIO.setup(11, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
##    a = 0
##
##    try:
##        while True:
##            if GPIO.input(11) == 1:
##                print "feit"
##                if a == 0:
##                    setAlarm2(1)
##                    sendNotification(getToken(getUsername()))
##                    a = 1
##                    time.sleep(1)
##                else:
##                    setAlarm2(0)
##                    a = 0
##                    time.sleep(1)
##            time.sleep(0.1)
##
##    except KeyboardInterrupt:
##        GPIO.cleanup()


# Henter ut latitude, longtitude og timestamp fra fil.
# Kjorer til filen er lest igjennom.
# Har delay pa 1 sek
def gps_sender_main():

    f = open("/home/pi/no-theft-auto/pi-setup/coord1.txt", "r")
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

        # if getUpdateCurrCoor() == 1:
        #print "updating"
        updateCurrCoor(longitude, latitude)
        #print "Current coordinates updated"

        time.sleep(1)


def sendLatitudeLogditude(time, longitude, latitude, pi_id):
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                         user="glennchr_nta",  # your username
                         passwd="nta123",  # your password
                         db="glennchr_nta")  # name of the data base

    cur = db.cursor()

    if not cur:
        cur.close()
    try:
        cur.execute('''INSERT INTO gps_log (timestamp, longitude, latitude, pi_id) VALUES (%s, %s, %s, %s)''',
                     (time, longitude, latitude, pi_id))
        db.commit()
    except:
        db.rollback()


def updateCurrCoor(longtitude, latitude):
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                         user="glennchr_nta",  # your username
                         passwd="nta123",  # your password
                         db="glennchr_nta")  # name of the data base

    cur = db.cursor()

    if not cur:
        cur.close()
    try:
        value = getAlarm()
        name = getCarName1()
        gps_string = (longtitude +","+ latitude)
        cur.execute('''DELETE FROM raspberry_pi WHERE rpi_id = {0}'''.format(rpi_id))
        cur.execute("INSERT INTO raspberry_pi (rpi_id, alarm, car_name, Coords) VALUES (%s, %s, %s, %s)",
                     (rpi_id, value, name, gps_string))

        db.commit()
    except:

        db.rollback()



def setAlarm2(value):
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",
                         user="glennchr_nta",
                         passwd="nta123",
                         db="glennchr_nta")

    cur = db.cursor()

    try:
        cur.execute('''UPDATE raspberry_pi SET alarm = {0} WHERE rpi_id = '{1}' '''.format(value, rpi_id))
        db.commit()
    except:
        print("det gikk ikke")
        db.rollback()

    db.close()


def sendNotification(token):

    if not (token == None or token == ""):
        url = 'https://gcm-http.googleapis.com/gcm/send'
        postdata = {"data": {"message": getCarName1()}, "to": token}

        req = urllib2.Request(url)
        req.add_header('Content-Type', 'application/json')
        req.add_header("Authorization", "key=AIzaSyDp9WZAvpQBkBEsoo_KyWPjkTOeR3lUEXo")
        data = json.dumps(postdata)

        response = urllib2.urlopen(req, data)


# get-metoder


def getAlarm():
    # lager en cursor som kan kjore sql soringer

    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base

    cur = db.cursor()

    if not cur:
        cur.close()

    cur.execute('''SELECT alarm FROM raspberry_pi WHERE rpi_id = '{0}' '''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getToken(username):
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base

    cur = db.cursor()

    if not cur:
        cur.close()
    # lager en cursor som kan kjore sql soringer
    user = str(username)
    cur.execute('''SELECT token FROM user WHERE username = '{0}' '''.format(username))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getCarName2():
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base

    cur = db.cursor()

    if not cur:
        cur.close()
    cur.execute('''SELECT car_name FROM raspberry_pi WHERE rpi_id = {0}'''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value

def getCarName1():
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base
    cur = db.cursor()
    if not cur:
        cur.close()
    cur.execute('''SELECT car_name FROM raspberry_pi WHERE rpi_id = {0}'''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getUsername():
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base
    cur = db.cursor()

    if not cur:
        cur.close()
    cur.execute(''' SELECT U.username
                    FROM user U, user_has_rpi UR
                    WHERE {0} = UR.rpi_id AND U.user_id = UR.user_id'''.format(rpi_id))
    return cur.fetchone()[0]


def getUpdateCurrCoor():
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base
    cur = db.cursor()

    if not cur:
        cur.close()
    cur.execute('''SELECT update
                   FROM raspberry_pi
                   WHERE rpi_id = {0} '''.format(rpi_id))
    # henter ut verdier fra fetchone
    value = cur.fetchone()
    return value


def getLongLat():
    db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                      user="glennchr_nta",  # your username
                      passwd="nta123",  # your password
                      db="glennchr_nta")  # name of the data base
    cur = db.cursor()

    if not cur:
        cur.close()
    cur.execute('''SELECT Coords FROM raspberry_pi WHERE rpi_id = {0}'''.format(rpi_id))
    value = cur.fetchone()
    return value

main()
