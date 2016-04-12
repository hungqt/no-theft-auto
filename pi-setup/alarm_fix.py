__author__ = 'Simon'

import thread
import MySQLdb
import time
import urllib2
import json
import RPi.GPIO as GPIO


rpi_id = 1

def getAlarm():

	# lager en cursor som kan kjore sql soringer

	db = MySQLdb.connect(host="mysql.stud.ntnu.no",
					user="glennchr_nta",
					passwd="nta123",
					db="glennchr_nta")

	cur = db.cursor()


	cur.execute('''SELECT alarm FROM raspberry_pi WHERE rpi_id = '{0}' '''.format(rpi_id))
	# henter ut verdier fra fetchone
	values = cur.fetchone()
	value = values[0]

	db.close()
	return value


def setAlarm():
	
	db = MySQLdb.connect(host="mysql.stud.ntnu.no",
					user="glennchr_nta",
					passwd="nta123",
					db="glennchr_nta")

	cur = db.cursor()

	try:
		cur.execute('''UPDATE raspberry_pi SET alarm=1 WHERE rpi_id = '{0}' '''.format(rpi_id))
		db.commit()
	except:
		print("det gikk ikke")
		db.rollback()

	db.close()


print(getAlarm())
setAlarm()
print(getAlarm())
