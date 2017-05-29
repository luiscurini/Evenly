# Evenly
Evenly Coding Challenge (Min API level 23 Marshmallow) 

This app will retrieve the list of NearBy venues using the FoursquareAPI (hardcoded WGS84 Coordinates 52.500342, 13.425170) and shows it in a RecyclerView. 

It's possible to click on any of the list items, a venue detail page will be presented. This will show minimal information, Venue name, category,
picture gallery, address and telephone numer (all of them if available). Also it's possible to like/dislike the venue and get directions from the hardcoded coordinates
described above to the venue. 

To get the directions I'm using the google API which will open the Google Maps apps if installed or will redirect to the browser and open google maps website. 

It's currently using the OAUTH token of my personal profile to get venue details and like/dislike it but it's possible to replace with any token. 
To do so replace OAUTHTOKEN inside: app/src/main/java/com/luisrubenrodriguez/evenly/service/FoursquareAPI.java

# Libraries used

* Dagger2
* ButterKnife
* Retrofit2
* RxJava
* Glide
* jUnit
* Mockito
* Relex Circle Indicator

# How to run the project

Clone or Download the repository, unzip if needed. Import the project on Android Studio, wait for it to build and run it on your preffered device or emulator.

# What's missing
* Implementation of the Favorite Venues.
* Parceleable Model Classes. 
* More UnitTest.
* SaveInstance for VenueActivity. 
* Application branding. 
* Refactor of some code.
* More meaninful error handling. 
* Avoid hardcoded parameters for API calls. 
