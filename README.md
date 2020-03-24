## CoolShopTest Application
- This application have 2 Screens first you will see Sign In screen if you enter email and password then i will go to next screen if where you will find you profile detail link avatar and email password displayed

- In this application I used MVVM architecture and repository file for network call for adding another abstraction layer.
- In this application for transection I used navigation editor from on fragment to another
- For network call Retrofit and for Server side i used Local Server XAMPP and create some PHP API Files to access MySQl server.
- For  asynchronous I used Coroutines of Kotlin
## This application have main 6 packages
- managers I have Persistence Manager to manage shared prefs and stat of application and Media manager to manage images and size bitmaps.
- Api have endpoint file and module for Retrofit singleton
- repository contains singleton class for network call.
- utils have different class to manage network stats and call back method interface.
- view contains fragments and activity.
- viewmodel contains viewmodel for this application.
## Question Asked
# What frameworks or supporting libraries did you use to make the task simpler and/or easier to accomplish?
# Answer:
- I used ViewModel and LiveData libraries to achieve clear architecture.
- Used Coroutines of kotlin for asynchronous task and network calls.
- Retrofit client for network call.
- Gson to convert JSON to class objects.
- Google navigation editor libraries to navigation between fragments.
- circleimageview library for round image view.
- Glide to load data from http URL link to circleimageview.
# How did you ensure that the display of the avatar image (from a remote URL) gave the best user experience?
# Answer:
- I used Glide library to download image from URL i has ability to store image as cache also so It won't download every time application opened.
- I used  silhouette avatar as placeholder so if anything happen to loading image from URL this will appear so circleimageview won't be blank.
# How did you set up the app so that it was automatically logged in for the user on subsequent uses?
# Answer:
- I used shared prefs to store session of application. When user is logged in session is created in shared prefs which includes token and id.
- when Main Activity created i check if session is available then directly go to profile otherwise stay on the Sign screen.
# How did you cope with building the sample app without having access to the real back-end API.
# Answer:
- To achieve there was 2  options one is to  use local host services like XAMPP and create api PHP based other was to use local data base to for storage and calling.
- I implement both so I can show you that I am comfortable with both.
- When application will start you will see dialog box to chose your backend you can choose local or web.

## API BASED USING PHP
- To use this just copy folder name coolshoptest from root of this repository. Then import file coolshoptest.sql from DB folder which is inside this folder.
- Change BASE_URL from Android file ApiUtils.kt form api package and then you can test it.
- I used XAMPP local server and created PHP files Api files that access the MySQl data base.
- I include the folder of server side coding.
- DB folder have coolshoptest.Sql file you can import that to check db.
- Main folder have init.php that established database connection.
- Session folder have file new.php which is used to sign in and register this file is accessed using post method and have two variables email and password if email password is exist then it will return registered user's id and token after creating fresh token if email and password don't match then will create new user and return id and token in JSON format.
- Users and another folder inside named Uploads where your photos will be uploaded.
- avatar.php file is which upload file after decoding from base64 this have Header named Authorization which includes Bearer token if token is verified then proceed to next.
- user.php uses get method it also uses Bearer Token Authorization and then give you user detail.
## LOCAL DATA BASE ROOM
- For local database i used room library for storage.
- Every operation is done on local data base Authorization is implement
- In this I don't uplaod picture to server just save in database in Base64 format and when get convert that into bitmap
# What testing did you do, and why?
- I did manual testing I have never used automated testing in my previous jobs QA team does all the testing they did manual. I know about espresso, JUnit and mockito but I never used in my project so I didn't take the risk in this short time
