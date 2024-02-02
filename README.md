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



