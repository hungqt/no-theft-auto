__author__ = 'Simon'

import MySQLdb
import time

# https://github.com/PyMySQL/PyMySQL/ <-- Dokumentasjon for db connection

# lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                     user="glennchr_nta",  # your username
                     passwd="nta123",  # your password
                     db="glennchr_nta")  # name of the data base

pi_id = 1

f = open("coord1.txt", "r")


def sendLatitudeLogditude(time, longitude, latitude, pi_id):
    cur = db.cursor()
    try:
        cur.execute('''INSERT INTO gps_log (timestamp, longitude, latitude, pi_id) VALUES (%s, %s, %s, %s)''',
                    (time, longitude, latitude, pi_id))
        db.commit()
    except:
        db.rollback()

while True:
    h = 0
    long = 0
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
        elif d == "longitude" and long == 0:
            long = 1
            h += 1
            longitude = c

    if a == "":
            f.close()
            break
    print pi_id, " ", timestamp, " ", latitude, " ", longitude
    sendLatitudeLogditude(timestamp, longitude, latitude, pi_id)
    time.sleep(1)
