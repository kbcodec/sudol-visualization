# sudol-visualization

Application created for Engineering Thesis. It is used to visualize data from hydro-meteorological stations. Information from the station is downloaded via the thingpeak API as JSON objects, transformed into another format and then saved in the database. Charts are created based on the information stored in the database.

## stack
- Java 17
- SpringBoot 3
    - Lombok
    - Spring Security
- React
    - Leaflet
    - Plotly-react
- MongoDB
- Python 

## actions
The application allows use for unlogged users and logged in users. Login is handled by the Spring Security library using the JWT token. 

An unlogged user has access to the last 150 readings.

A logged in user with the teacher role has access to changing the date range and parameters displayed on the charts.

A logged in user with the administrator role can also download a csv file with the data currently presented in the chart and instructions as pdf file.

## pages
A link to visit the website will appear soon. Meanwhile, the website view is shown below.
### main page
![main_page](https://github.com/kbcodec/sudol-visualization/assets/103029426/be3cdf3e-d593-419b-a3d2-db4e009e37bb)

### login page
![login_page](https://github.com/kbcodec/sudol-visualization/assets/103029426/7bef0af9-7304-4050-a096-cd4208479885)

### guest graphs page
![guest_view](https://github.com/kbcodec/sudol-visualization/assets/103029426/09bfcbd5-8297-4f04-8ba4-2092adbfb823)

### teacher graphs page
![teacher_view](https://github.com/kbcodec/sudol-visualization/assets/103029426/bcbf2271-68ff-4c30-b86c-908a4338f903)

### admin graphs page
![admin_view](https://github.com/kbcodec/sudol-visualization/assets/103029426/b60c4de0-d2a7-4bf2-ab13-5ed13b5a687e)


