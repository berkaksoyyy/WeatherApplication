# WeatherApp
A simple weather app which shows current weather and 5 day forecast and has current location and another location options.

In this project, I developed the architecture with MVVM which I feel most comfortable and find it easy to read/write. 

Also, I decided to go with Jetpack Compose for the UI because it's the most recent and useful one which Google recommends. I did not want to use any other outdated method.

I used OpenWeatherMap which is a free and useful option for weather and forecast data and has a clear documentation. 

I decided to use Google Places for selecting place and receive latitude, longitude values in order to get weather for desired location because Google is a field giant also in maps related technologies and has the most reliable data in this field.

I went with Hilt when using dependency injection because of its ease of use and understandability.

I used Coroutines for async development and Retrofit for API communication.

I developed Unit tests with Junit5. 

Application is only active with Internet connection and it has a control in the opening.

I wanted to use fullscreen in portrait mode so the application does not have landscape support and action bar.

I let users to choose their own desired options whether use their current location or select another one from the list which also provided with Google Places API.

When user changes the option for location, application starts and shows the user their location, current weather conditions and 5 days forecast.

The application also lets the user go back and select another location without closing the application. 

I chose the colors of a clear sky in the application because in my opinion it creates a positive atmosphere and a happy experience.

Finally, I decided to go with Gifs rather than images while expressing current situation of the weather because I wanted app to be more dynamic. 

Reminder: Keys are not in the project file and cannot be reachable right now. Please contact if you need them.











