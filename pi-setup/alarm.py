import RPi.GPIO as GPIO
import MySQLdb

#https://github.com/PyMySQL/PyMySQL/ <-- Dokumentasjon for db connection

#lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",    # your host, usually localhost
		user="glennchr",         # your username
		passwd="mysql123",  # your password
		db="glennchr_app")        # name of the data base

#setter opp RaspberryPi
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

led = 4
alarm_button = 14
reset_button = 15


GPIO.setup(led, GPIO.OUT)
GPIO.setup(alarm_button, GPIO.IN, GPIO.PUD_UP)
GPIO.setup(reset_button, GPIO.IN, GPIO.PUD_UP)

def alarm():
  #lager en cursor som kan kjøre sql spørringer
	cur = db.cursor()
	cur.execute("SELECT alarm FROM alarm WHERE user_email = %s", ("glaar90@gmail.com"))
  #henter ut verdier fra fetchone
	values = cur.fetchone()
	value = values[0]
	return value


def setAlarm(value):
	cur = db.cursor()
	stringValue = str(value)
	cur.execute("UPDATE glennchr_app.alarm SET alarm="+stringValue+" WHERE user_email = %s", ("glaar90@gmail.com"))
	
while True:
	if GPIO.input(alarm_button) == False:
		GPIO.output(led, 1)

		if alarm() == 0:
			setAlarm(1)
			print("set to 1")
		else:
			setAlarm(0)
			print("set to 0")
		
	if GPIO.input(alarm_button) == True:
		GPIO.output(led, 0)

	if GPIO.input(reset_button) == False:
		GPIO.output(led, 1)

		cur = db.cursor()
		cur.execute("SELECT alarm FROM alarm WHERE user_email = %s", ("glaar90@gmail.com"))
		values = cur.fetchone()
		value = values[0]
		print (value)

	if GPIO.input(reset_button) == True:
		GPIO.output(led, 0)


	if GPIO.input(reset_button) == False and GPIO.input(alarm_button) == False:
		print ("Closing Program")
		db.close()
		break
	
GPIO.cleanup()



