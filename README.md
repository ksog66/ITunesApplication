# ITunesApplication
This Application retrieve data from iTunes API and show it in Grid view.


# API Module Description

  The project has "api" named module which has all the retrofit Client, Call,Response.
  API module is pure kotlin-java library free from android.

# APP Module Description

  App module contains all the android files for Room database, Activity, ViewModel, Repository.
  This app module is free from retrofit call and response , it use API module to call retrofit client.
  View Model has all the business logic of the application.
  Repository Provides data to ViewModel, either from the room database or from iTunes api response.
  ViewModel check if there is internet Connection available or not, then it decide, from where it has to get data. 
  


# UI Description
  The application has SearchView used In Toolbar, A RecyclerView to show Certain layout in a GridViewLayout.
   ItemView has one ImageView to Show artWork, Two TextView to show TrackName and CollectionName.
   

# Testing
  One Test is done to check we are getting response from retrofit in API module
  One Test is done to get reponse from retrofit and insert in Room database and then checking we are getting data from room database or not.
