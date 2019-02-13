import paho.mqtt.client as mqtt
import ssl
import sys
import pyrebase


config = {
    "apiKey": "{PUT IN YOUR FIREBASE INFO HERE}",
    "authDomain":  "{PUT IN YOUR FIREBASE INFO HERE}",
    "databaseURL":  "{PUT IN YOUR FIREBASE INFO HERE}",
    "projectId":  "{PUT IN YOUR FIREBASE INFO HERE}",
      "storageBucket":  "{PUT IN YOUR FIREBASE INFO HERE}",
    "messagingSenderId":  "{PUT IN YOUR FIREBASE INFO HERE}",
}

firebase = pyrebase.initialize_app(config)

def onConnect(client, userdata, flags, rc):
	print('connected!')
	print('subscribing to topic')
	client.subscribe('location')
	print('publishing to topic')
	client.publish('location', 'latitude: -43.471737;longitude: -80.543640')

def onDisconnect(client, userdata, rc):
	print('disconnected!')

def onMessage(client, userdata, message):
	# print(typeof(message.payload))
	mess = str(message.payload)
	print('got message: ' + mess)
	db = firebase.database()
	data = {"latitude": mess[mess.find(":")+2:mess.find(";")], "longitude": mess[mess.find(";")+12:-1]}
	db.child("users").child("1").child("location").set(data)

client = mqtt.Client(transport="websockets")
client.tls_set(ca_certs=None, certfile=None, keyfile=None, cert_reqs=ssl.CERT_REQUIRED, tls_version=ssl.PROTOCOL_TLS, ciphers=None)
client.on_connect = onConnect
client.on_disconnect = onDisconnect
client.on_message = onMessage

client.username_pw_set('solace-cloud-client', password='{PUT IN YOUR SOLACE PASSWORD HERE}')
client.connect('{PUT IN YOUR SOLACE LINK HERE}', {PUT IN YOUR SOLACE PORT HERE}, 20)
client.loop_forever()


