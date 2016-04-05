__author__ = 'Simon'

import MySQLdb

# lager en connection til db
db = MySQLdb.connect(host="mysql.stud.ntnu.no",  # your host, usually localhost
                     user="glennchr_nta",  # your username
                     passwd="nta123",  # your password
                     db="glennchr_nta")  # name of the data base

def setIp(rpiIp):
    cur = db.cursor()
    try:
        cur.execute("""UPDATE raspberry_pi SET IP ='{0}' WHERE rpi_id = 1""".format(rpiIp))
        db.commit()
        print "hey"
    except:
        db.rollback()


def getPhoneIp():
    # lager en cursor som kan kjore sql soringer
    cur = db.cursor()
    cur.execute("SELECT IP FROM user WHERE username = 'tb'")
    # henter ut verdier fra fetchone
    values = cur.fetchone()
    value = values[0]
    return str(value)


