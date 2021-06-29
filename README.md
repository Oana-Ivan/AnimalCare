<img align="right" width="300" height="300" src="https://user-images.githubusercontent.com/49486605/123798395-cd6a2100-d8ef-11eb-92f6-4682f18a36d8.png">

# AnimalCare

## Description

AnimalCare is a mobile application developed for the Android platform and it provides a software solution for the management of an animal shelter.


The purpose of an animal shelter is to ensure the well-being of the animals and to find them a home. 
This application follows both the internal needs of such a shelter and the external needs.


## Authentification

The application has 3 roles:

- ADOPTER

- VOLUNTEER

- ADMIN

![app-utilizatori-nelogati](https://user-images.githubusercontent.com/49486605/123797992-71070180-d8ef-11eb-9dee-22af88e81a7b.png)


## Functionalities


### Comunication

The first category of functionalities concerns animal well-being, the application can be used as a **communication platform within the organization 
between volunteers or employees** in charge of managing the activity of the shelter.

![tickets](https://user-images.githubusercontent.com/49486605/123802384-eb398500-d8f3-11eb-88c6-29af5e710663.png)

It also keeps track of:

- the number of animals existing;

- the details of the health of each animal;

- the age and other information necessary to provide the best possible care.


### Finding a home for all animals

The second category of functionalities refers to finding a home for animals inside the shelter. 

The people who want to adopt an animal can use the 
app to see which animals exist in the shelter and to schedule a visit. 

To facilitate the adoption process (connecting those interested with the right 
animal) the application tries to provide, in addition to the possibility of seeing the animals available in the shelter, a number of recommendations 
by linking the information entered by the user with the information related to the characteristics of the animals in the shelter. The reason for 
implementing this functionality is to increase the retention of adopted animals, to ensure that they integrate well into the new home. And to receive 
this recommendation, those who wish to adopt an animal must respond to a short questionnaire and volunteers (or shelter employees) must fill in some 
information for each animal new to the shelter. 

Also, based on the information entered, the application extracts other data that could make the animal 
more attractive to a person who wants to adopt an animal, for example if a photo is entered for the animal, the app will predict the animal's most likely 
breed. 

In addition, when a user wants to view details about a particular animal, a list of information about animal care is generated to help the user 
make a responsible decision.


## Technologies


- Android platform for development;

- Firebase platform for storing data revealed for the application, as well as for storing uploaded images;

- the TensorFlow platform for identifying an animal's breed according to the picture entered in the application;

- the code is written in the Java programming language.

