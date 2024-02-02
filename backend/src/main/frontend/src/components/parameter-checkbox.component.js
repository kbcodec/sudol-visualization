import React, { Component } from "react";

class ParameterCheckbox extends Component {
    renderCheckbox = (label, value) => (
        <label key={value}>
          <input
            type="checkbox"
            name="parameter"
            className="chb-value"
            value={value}
            checked={this.props.selectedParameters.includes(value)}
            onChange={() => this.props.handleCheckboxChange("parameter", value)}
          />
          <span className="clickable-span meteo-param">{label}</span>
        </label>
      );
    
      render() {
        return (
          <form className="values-form">
            {this.renderCheckbox("Odległość [cm]", "water_state")}
            {this.renderCheckbox("Ciśnienie [hPa]", "pressure")}
            {this.renderCheckbox("Temperatura powietrza [°C]", "air_temperature")}
            {this.renderCheckbox("Wilgotność powietrza [%]", "air_humidity")}
            {this.renderCheckbox("Prędkość wiatru średnia [km/h]", "wind_speed_avg")}
            {this.renderCheckbox("Kierunek wiatru [°]", "wind_direction")}
            {this.renderCheckbox("Prom. słoneczne [W/m2]", "solar_radiation")}
            {this.renderCheckbox("Opad [mm]", "precipitation")}
            {this.renderCheckbox("Temp. wewnętrzna [°C]", "soil_temperature")}
            {this.renderCheckbox("Wilgotność gleby [%Vol]", "soil_humidity")}
            {this.renderCheckbox("Ciśnienie wewnętrzne [hPa]", "soil_pressure")}
          </form>
        );
      }
    }

export default ParameterCheckbox;