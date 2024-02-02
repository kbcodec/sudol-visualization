import React, { Component } from "react";
import DataService from "../services/data.service";
import Graph from "./graph.component";
import ParameterCheckbox from "./parameter-checkbox.component";
import Spinner from "./spinner";

export default class BoardPublic extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: "",
      selectedStations: ["Jordanowska", "Opolska", "Potoczek"],
      selectedParameters: ["water_state", "pressure", "air_temperature", "air_humidity", "wind_speed_avg", "wind_direction", "solar_radiation", "precipitation", "soil_temperature", "soil_pressure", "soil_humidity"],
      apiData: null,
      isLoading: false
    };
  }

  componentDidMount() {
    this.setState({isLoading: true})
    DataService.postPublicContent({
      selectedStationsCheckboxes: this.state.selectedStations,
      selectedValuesCheckboxes: this.state.selectedParameters
    }).then(
      (response) => {
        this.setState({ 
          apiData: response.data,
          isLoading: false
        })
      },
      (error) => {
        this.setState({
          content:
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString(),
            isLoading: false
        });
      }
    );
  }

  handleCheckboxChange = (type, value) => {
    this.setState((prevState) => ({
      selectedParameters: prevState.selectedParameters.includes(value)
        ? prevState.selectedParameters.filter((param) => param !== value)
        : [...prevState.selectedParameters, value],
    }));
  };

  render() {
    const { selectedParameters} = this.state;

    return (

      <div className="board-container">
        <div className="board-admin-container">
          <div className="admin-menu">
        <div className="menu-row">
          <ParameterCheckbox
              selectedParameters={selectedParameters}
              handleCheckboxChange={this.handleCheckboxChange}
              />
        </div>
        </div>
          <div className="graphs-content">
            {this.state.isLoading ? (
              <Spinner />
            ): (
              <Graph data={this.state.apiData} params={selectedParameters} />
            )}
          </div>
        </div>
      </div>
      );
    }
}

