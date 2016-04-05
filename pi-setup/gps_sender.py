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




# Sender til database med SQL
def sendLatitudeLogditude(time, longitude, latitude, pi_id):
    cur = db.cursor()
    try:
        cur.execute('''INSERT INTO gps_log (timestamp, longitude, latitude, pi_id) VALUES (%s, %s, %s, %s)''',
                    (time, longitude, latitude, pi_id))
        db.commit()
    except:
        db.rollback()

def updateCurrCoor(longtitude, latitude):
    cur = db.cursor()
    try:
        cur.execute('''UPDATE raspberry_pi SET Coords = '{0}' WHERE rpi_id = 1'''.format(longtitude + "," + latitude))
        db.commit()
    except:
        db.rollback()

# Henter ut latitude, longtitude og timestamp fra fil.
# Kjorer til filen er lest igjennom.
# Har delay pa 1 sek

def main():

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

        print pi_id, " ", timestamp, " ", latitude, " ", longitude
        sendLatitudeLogditude(timestamp, longitude, latitude, pi_id)
        if tid >= 5:
            updateCurrCoor(longitude, latitude)
            print "Current coordinates updated"
            tid = 0
        time.sleep(1)
        tid += 1

main()