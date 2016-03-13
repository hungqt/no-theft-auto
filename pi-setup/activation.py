import MySQLdb

# https://github.com/PyMySQL/PyMySQL/ <-- Dokumentasjon for db connection

# lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                     user="glennchr_nta",  # your username
                     passwd="nta123",  # your password
                     db="glennchr_nta")  # name of the data base


cur = db.cursor()
cur.execute('''INSERT INTO gps_log (timestamp, longitude, latitude, pi_id) VALUES (%s, %s, %s, %s)''',
            ('0', '123', '321', 1))

# kode som leser txt-fil
f = open("coord1.txt", "r")

while True:
    a = f.readline()

    if a == "":
        print "lol"
        f.close()
        break

    b = a.split("\"")
    c = b[6].split(":")[1].split(",")[0]
    print c

# setter opp RaspberryPi

def getAlarm():
    # lager en cursor som kan kjore sql soringer
    cur = db.cursor()
    cur.execute("SELECT alarm FROM raspberry_pi WHERE rpi_id = 1")
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value

def getLatitudeLogditude():
    cur = db.cursor()
    cur.execute("INSERT INTO gps_log (timestamp, longitude, latitude, pi_id) VALUES (0000, 667, 668, 1)")


def setAlarm(value):
    cur = db.cursor()
    stringValue = str(value)
    cur.execute("UPDATE glennchr_nta.raspberry_pi SET alarm=" + stringValue + " WHERE rpi_id = 1")

menu = {'1': "Activate alarm", '2': "Deactive alarm", '3': "Read alarm status", '4': "Quit\n"}
while True:
    options = menu.keys()
    options.sort()
    for entry in options:
        print entry, menu[entry]

    selection = raw_input("Please Select:")
    if selection == '1':
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
