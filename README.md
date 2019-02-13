# Waldo
#### Dhvani Patel and Mayank Kanoria

## What is Waldo?
Waldo is a suite of microservices based on indoor localization.

## What does Waldo do?
**Helps improve indoor localization** <br /> 
Delivers accurate location data to first responders that existing 
mapping and location services cannot.

**Provides a fully customizable application for events and venues** <br 
/> 
Enhances user experience through custom built interfaces tailored for 
each event. 

**Analytics for valuable insights** <br /> 
Provides unique insights about user behaviours and patterns.

## How does Waldo integrate?

**Businesses Buy** <br /> 
Businesses can buy or product directly from us and get custom built 
interfaces for their users to access and enjoy. These can include 
museums, hackathons, concerts and conferences.

**Businesses Sell** <br /> 
Our clients can choose to sell these interfaces to their users for a 
small fee, like in the case of a museum. Being a mobile application 
offers flexibility and computing power that over devices cannot.

**Businesses Gain** <br /> 
We also deliver impactful data about user behaviour to our clients. 
These can help understand what times are peak hours, which sections of 
the 'event' are more popular and hundreds of others possibilities.

**First Responders Too** <br /> 
In the case of an emergency, we can provide first responders critical 
information based on user's real-time information which can help in 
moments of life and death; for example, Waldo can help a fire chief 
better manage and understand a situation by receiving real time location 
data of all the people involved. 

## How was Waldo built?
Waldo was built using Android Studio for the Android platform. The Waldo 
team surely plans on bringing Waldo to iOS. A picture of our tech stack 
is attached.

## What's next for Waldo
The Waldo team will be contacting museums & other businesses that could 
benefit from this and exploring testing Waldo in a real world 
environment. 

## Running Waldo
To run the Android App, import the project into Android Studio and 
insert your Google Map API key and Navisens key. Also make sure to place 
the google-services.json (Firebase init file) in the app/ folder. 
To change the location, the latitude and longitude coordinates should be 
adjusted accordingly. <br />  <br /> 

To run the Web App, make sure the Android App is running and update the 
Firebase information in the index.html file. <br />  <br /> 

To run the Solace script, update the Firebase and Solace information in 
the Python file. Make sure to run:
pip install paho-mqtt
Note: this was tested using Python 3.7
I would also advise on using a virtualenv and make sure if you're on an 
enterprise network like a University network, that the Solace port is 
not blocked. If you have doubts, run the script on a VPN or using an 
external hotspot. <br />  <br /> 

If anything is not working, feel free to reach out at: 
**dhvani.patel@uwaterloo.ca**

## Screenshots
![Waldo](Screenshots/main_screen.jpg?raw=true "")
![Waldo](Screenshots/first_screen.jpg?raw=true "")
![Waldo](Screenshots/second_screen.jpg?raw=true "")
![Waldo](Screenshots/third_screen.jpg?raw=true "")
![Waldo](Screenshots/fourth_screen.jpg?raw=true "")
![Waldo](Screenshots/emergency_screen.jpg?raw=true "")

<br /> 



