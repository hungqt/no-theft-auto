import MySQLdb
import urllib2
import json

# https://github.com/PyMySQL/PyMySQL/ <-- Dokumentasjon for db connection
# lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                     user="glennchr_nta",  # your username
                     passwd="nta123",  # your password
                     db="glennchr_nta")  # name of the data base
rpi_id = 1


# setter opp RaspberryPi
def getAlarm():
    # lager en cursor som kan kjore sql soringer
    cur = db.cursor()
    cur.execute('''SELECT alarm FROM raspberry_pi WHERE rpi_id = '{0}' '''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def getToken(username):
    # lager en cursor som kan kjore sql soringer
    cur = db.cursor()
    user = str(username)
    cur.execute('''SELECT token FROM user WHERE username = '{0}' '''.format(username))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def sendNotification(token):

    if not (token == None or token == ""):
        url = 'https://gcm-http.googleapis.com/gcm/send'
        postdata = {"data": {"message": getCarName()}, "to": token}

        req = urllib2.Request(url)
        req.add_header('Content-Type', 'application/json')
        req.add_header("Authorization", "key=AIzaSyDp9WZAvpQBkBEsoo_KyWPjkTOeR3lUEXo")
        data = json.dumps(postdata)

        response = urllib2.urlopen(req, data)


def getCarName():
    # lager en cursor som kan kjore sql soringer
    cur = db.cursor()
    cur.execute('''SELECT car_name FROM raspberry_pi WHERE rpi_id = {0}'''.format(rpi_id))
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return value


def setAlarm(value):
    cur = db.cursor()
    stringValue = str(value)
    try:
        cur.execute("UPDATE glennchr_nta.raspberry_pi SET alarm =(%s)  WHERE rpi_id = (%s)", (stringValue, rpi_id))
        db.commit()
    except:
        db.rollback()


def getUsername(rpi_id):
    cur = db.cursor()
    cur.execute(''' SELECT U.username
                    FROM user U, user_has_rpi UR
                    WHERE {0} = UR.rpi_id AND U.user_id = UR.user_id'''.format(rpi_id))
    return cur.fetchone()[0]


def main():
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

main()
