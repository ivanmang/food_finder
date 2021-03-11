# food_finder
A project for Udacity's Android Kotlin Developer Nanodegree course
# Overview
It might be a daily struggle for many people to find a restaurant nearby. So, I built an app which shows restaurants within 100m. The nearby restaurants can be displayed via Google map or a list. Users are able to explore different restaurants
and saved it to the app if they liked that restaurant. They are able to share the name and address of that restaurant to friends too.

# Features
- Accees user's current location and find the restaurants nearby using the Google Place Api
- Display the detail of the restaurant inclufing pictures and vicinty
  - Able to like and save the selected restaurant
  - Able to share this restaurants via text

# Milestone delivery targets (Each milestone took about a week)
#### Milestone 1
Build a single page app that can display all the restaurant nearby
#### Milestone 2
Able to display the restaurnat nearby in google map
#### Milestone 3
Able to selecte a restaurant and show its detail including images and share its name to friends
#### Milestone 4
Able to save the restaurants that are "liked" and display it on another page


# Instruction
Once you enter the discover page (google map) and allow the app you access your location, press the refresh button on the tool bar. The restaurants nearby will be shown on the map. You can press the marker for more details

<img src="https://user-images.githubusercontent.com/35868876/110740931-ca7af000-826e-11eb-9ec6-73dc1bde8d4b.gif" alt="Discover gif" width="300"/>

For the browse page, press the refresh button to obtain restaurants nearby and click into the restaurants interested. You can share the name and address of the restaurants to your friends via share button. Also, you can like and save this restaurant for the future.

<img src="https://user-images.githubusercontent.com/35868876/110742782-eb911000-8271-11eb-8772-534412c16140.gif" alt="Browse gif" width="300"/>

# App structure
- Discover
  - Google Map shows nearby restaurant
- Browse
  - List of restaurants that are nearby
- Restaurant Detail
  - Show the details (name, image, vicinity) of the selected restaurant
  - Able to like and save this restaurant
- Liked
  - List all the restaurants you liked


# Project Criteria
## Android UI/UX
#### Build a navigable interface consisting of multiple screens of functionality and data.
It use navifation controller to switch between four different screen. We used application bundle to pass the selected place between restaurant detail fragment and its view model.

#### Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.
It used ConstraintLayout, RecyclerView to hold and arrange UI elements. The resourcess are store appropriately in res folders.

#### Animate UI components to better utilize screen real estate and create engaging content.
The motion layout are used in the restaurant detail screen. The restaurant name will fade in 
when the view is scrolled
<img src="https://user-images.githubusercontent.com/35868876/110744895-6b6ca980-8275-11eb-984c-b418ba9852ff.gif" alt="motion gif" width="300"/>

## Local and Network data
#### Connect to and consume data from a remote data source such as a RESTful API.
It connects to [Google Place API](https://developers.google.com/maps/documentation/places/web-service/overview?hl=vi) using **Retrofit**. It contain a parser which parse the JSON response into class *Place*

#### Load network resources, such as Bitmap Images, dynamically and on-demand.
**Glide** are used when loading the image of the restaurant. Placeholder and error image are used to inform users failed network request.

#### Store data locally on the device for use between application sessions and/or offline use.
**Room** are used to store the restaurants list received from the API. To reduce 
unnecessary API calls. There contain a restaurant database to store restaurants nearby and alos a liked restaurant database to store those restaurants the user liked.

## Android system and hardware integration
#### Architect application functionality using MVVM.
It used MVVM pattern with repository to control database and API requests.


#### Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.
Location permssion are required at the start of the app. Also, users can switch to another app when sharing their loved restaurants to others by pressing the share button on the detail page.

#### Utilize system hardware to provide the user with advanced functionality and features.
It used GPS location from the phone and behaviors accessed only permissions are ganted.
