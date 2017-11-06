#!flask/bin/python
from flask import Flask
import json,random

def senddummy():
	i = random.randint(0,9)
	if i%2 == 0:
		return True;
	return False

def getData() :
    somecondition = True

    if somecondition:
        light = senddummy()
    if somecondition:
        fans = senddummy()
    if somecondition:
        isoccupied = senddummy()
    if somecondition:
        temperature = random.randint(0,100)
    if somecondition:
        locationid = "Room #"+str(random.randint(5030,5050))+" CC-3"
    mydictionary = {'lights':light, 'fans':fans,'isOccupied':isoccupied,'temperature':temperature, 'location_id':locationid}
    return json.dumps(mydictionary)

app = Flask(__name__)
@app.route('/query/')
def index():
    return "["+getData()+","+getData()+","+getData()+","+getData()+","+getData()+","+getData()+","+getData()+"]"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=3000)