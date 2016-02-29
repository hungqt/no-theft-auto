import MySQLdb

#https://github.com/PyMySQL/PyMySQL/ <-- Dokumentasjon for db connection

#lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",    # your host, usually localhost
		user="glennchr",         # your username
		passwd="mysql123",  # your password
		db="glennchr_app")        # name of the data base

#setter opp RaspberryPi

def getAlarm():
  #lager en cursor som kan kjore sql soringer
	cur = db.cursor()
	cur.execute("SELECT alarm FROM alarm WHERE user_email = %s", ["glaar90@gmail.com"])
  #henter ut verdier fra fetchone
	values = cur.fetchone()
	value = values[0]
	return value


def setAlarm(value):
	cur = db.cursor()
	stringValue = str(value)
	cur.execute("UPDATE glennchr_app.alarm SET alarm="+stringValue+" WHERE user_email = %s", ["glaar90@gmail.com"])


menu = {}
menu['1']="Activate alarm" 
menu['2']="Deactive alarm"
menu['3']="Read alarm status"
menu['4']="Quit"
while True: 
	options=menu.keys()
	options.sort()
	for entry in options: 
		print entry, menu[entry]

	selection=raw_input("Please Select:") 
	if selection =='1': 
		setAlarm(1)
	elif selection == '2': 
		setAlarm(0)
	elif selection == '3': 
		text = "\nAlarm status: %d \n" % getAlarm()
		print text
	elif selection == '4':
		break
	else: 
		print "Unknown Option Selected!" 
