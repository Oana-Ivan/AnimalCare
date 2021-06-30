# AnimalCare


<img align="right" width="200" height="200" src="https://user-images.githubusercontent.com/49486605/123798395-cd6a2100-d8ef-11eb-92f6-4682f18a36d8.png">


## Description

AnimalCare is a mobile application developed for the Android platform and it provides a software solution for the management of an animal shelter. It uses the Firebase cloud database, the TensorFlow Lite machine learning platform and the Java programming language.


The purpose of an animal shelter is to ensure the well-being of the animals and to find them a home. 
This application follows both the internal needs of such a shelter and the external needs.

The most important parts of the application:

- recommendation system that correlates what a person who wants to adopt an animal can offer with what the animal needs;

- presenting animals in an attractive and informative way so that a user can make a responsible decision:

     - the breed of the animal determined from the picture introduced in the application using a machine learning model;
     
     - the level of attention and care that an animal needs, these levels are calculated by the application;
     
     - an automatically generated set of care information for each animal based on its characteristics;

- introduction of actions for shelter management:

    - scheduling a visit by users (shelter staff will be able to see which animals are of interest to the person who added the visit);
    
    - implementation of a ticket system through which employees can communicate.


# Authentification

The application offers 3-role authentication and user data protection using the hashing procedure through the BCrypt library.

Application roles:

- ADOPTER

- VOLUNTEER

- ADMIN

![app-utilizatori-nelogati](https://user-images.githubusercontent.com/49486605/123797992-71070180-d8ef-11eb-9dee-22af88e81a7b.png)


# Functionalities based on user role


## ADOPTER

The role `ADOPTER` is for the users who are looking to adopt an animal.


<img align="left" width="255" height="430" src="https://user-images.githubusercontent.com/49486605/123943432-d2d57300-d9a4-11eb-873d-2c2a0e7dd013.png">


The people who want to adopt an animal can use the app to see which animals exist in the shelter and to schedule a visit. 


To facilitate the adoption process (connecting those interested with the right animal) the application tries to provide, in addition to the possibility of seeing the animals available in the shelter, a number of **recommendations by linking the information entered by the user with the information related to the characteristics of the animals in the shelter**. 


The reason for implementing this functionality is to increase the retention of adopted animals, to ensure that they integrate well into the new home. 


And to receive this recommendation, those who wish to adopt an animal must respond to a short questionnaire and volunteers (or shelter employees) must fill in some information for each animal new to the shelter. Also, based on the information entered, the application extracts other data that could make the animal more attractive to a person who wants to adopt an animal, for example if a photo is entered for the animal, the app will predict the animal's most likely breed. 


In addition, when a user wants to view details about a particular animal, a list of information about animal care is generated to help the user 
make a responsible decision.


<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123945465-d964ea00-d9a6-11eb-99d7-604461b3ebc5.png">


## Schedule visit

<p align="center">
<img  width="255" height="430" src="https://user-images.githubusercontent.com/49486605/123945312-b0dcf000-d9a6-11eb-8690-11d8c416783b.png">
</p>


<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123932614-4756e480-d99a-11eb-8c12-787531477e40.png">


## Animals

![main adopter](https://user-images.githubusercontent.com/49486605/123945172-8a1eb980-d9a6-11eb-8107-b32d00814843.png)


<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123946172-96efdd00-d9a7-11eb-8b18-f4e6ebc3c670.png">


## Recommended animals

The application generates a list of animals corresponding to the level of care that a user can offer.


![recomended](https://user-images.githubusercontent.com/49486605/123946865-5a70b100-d9a8-11eb-9268-0659074e488b.png)


<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123947418-087c5b00-d9a9-11eb-9b38-fa47276d5108.png">


## Saved animals

The user can save the animals that he is interested in.

<p align="center">
<img  width="255" height="450" src="https://user-images.githubusercontent.com/49486605/123947319-ee427d00-d9a8-11eb-97c2-7539aa2c74b1.png">
</p>


## ADMIN and VOLUNTEER

<p align="center">
  <img width="430" height="500" src="https://user-images.githubusercontent.com/49486605/123931930-a2d4a280-d999-11eb-866f-bfad1e0b830b.png">
</p>


<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123932614-4756e480-d99a-11eb-8c12-787531477e40.png">

## Animals

The users with the role `ADMIN` or `VOLUNTEER` can add animals and based on the data introduced the app will display:

- the race of the animal based on the photo (using a ML model);

- the level of care required for each animal based on the needs of the animal;

- the level of attention required for each animal based on the animal's characteristics;

- personalized care information.


![Animals Admin Volunteers](https://user-images.githubusercontent.com/49486605/123931339-217d1000-d999-11eb-9ca1-126bbb822270.png)


<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123934256-c7317e80-d99b-11eb-8b35-e8a8325a7166.png">

## Shelter management

The application can be used as a **communication platform within the organization between the volunteers or employees** in charge of managing the activity of the shelter.


![tickets](https://user-images.githubusercontent.com/49486605/123935435-e4b31800-d99c-11eb-888d-5ce28176876d.png)



<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123936988-622b5800-d99e-11eb-9f27-5723994aad31.png">

## Visits

The app displays information about all the visits added by the users with the role `ADOPTER` that are not marked as closed. Also, it displays the list of animals that the person who added the visit is interested in.


<p align="center">
  <img width="800" height="600" src="https://user-images.githubusercontent.com/49486605/123936243-aa964600-d99d-11eb-8465-cbcc97dc6420.png">
</p>


## ADMIN 



<img align="left" width="50" height="50" src="https://user-images.githubusercontent.com/49486605/123938007-64da7d00-d99f-11eb-9bb0-285c67e6906f.png">

In addition to the above, the role of administrator has an additional option: adding users as volunteers, editing information about them and deleting them.

![volunteers](https://user-images.githubusercontent.com/49486605/123938950-4923a680-d9a0-11eb-9d8c-0f534b874968.png)


# Technologies


- **Android** platform for development;

- **Firebase** platform for storing data revealed for the application, as well as for storing uploaded images;

- the **TensorFlow platform** for identifying an animal's breed according to the picture entered in the application;

- the code is written in the **Java** programming language.

# Sources

Circle menu

Within the applications for the graphical interface, two activities use the [Circle Menu](https://github.com/Hitomis/CircleMenu) library which provides a way to display the options that a user has in the application.

Image sources

The application uses images to make the graphical interface more attractive. The images used in the application were taken from 2 open-source platforms: [Pixabay](hwww.pixabay.com) and [Pexels](www.pexels.com) which offer images, vector graphics and illustrations for free.
