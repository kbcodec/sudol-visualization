import React, { Component } from "react";
import DataService from "../services/data.service";
import EventBus from "../common/EventBus";
import moment from 'moment';
import Graph from "./graph.component";
import DatePickerRange from "./date-picker.component";
import ParameterCheckbox from "./parameter-checkbox.component";
import Spinner from "./spinner";
import AuthService from "../services/auth.service";
import { Navigate } from "react-router-dom";

export default class BoardAdmin extends Component {
  constructor(props) {
    super(props);

    const endDate = moment();
    const startDate = moment(endDate).subtract(6, 'hours');
    const dateStringFormat = 'YYYY-MM-DD HH:mm:ss UTC';
    const endDateStringMoment = endDate.utc().format(dateStringFormat);
    const startDateStringMoment = startDate.utc().format(dateStringFormat);

    this.state = {
      content: "",
      selectedStations: ["Jordanowska", "Opolska", "Potoczek"],
      graphParameters: ["air_temperature", "pressure", "precipitation"],
      selectedParameters: ["air_temperature", "pressure", "precipitation"],
      dateFrom: startDateStringMoment,
      dateTo: endDateStringMoment,
      apiData: null,
      isLoading: false,
      redirect: null,
      dataChanged: false,
      wrongData: false
    };
  }

  componentDidMount = () => {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser)
    {
      alert("Nie masz uprawnień do przeglądania tej zawartości.")
      this.setState({ redirect: "/public" });
    } else if(currentUser.roles.includes("ROLE_TEACHER")) {
      this.setState({currentUser: currentUser})
      this.setState({ redirect: "/teacher" })
    }

    this.setState({ currentUser: currentUser})

    this.setState({isLoading: true});
    DataService.postAdminContent({
      selectedStationsCheckboxes: this.state.selectedStations,
      selectedValuesCheckboxes: this.state.selectedParameters,
      startDate: this.state.dateFrom,
      endDate: this.state.dateTo,
    }).then(
        (response) => {
          this.setState({
            apiData: response.data,
            isLoading: false,
            dataChanged: false
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

          if (error.response && error.response.status === 401) {
            EventBus.dispatch("logout");
          }
        }
    );
  }

  handleRequestSubmit = () => {
    const { selectedStations, selectedParameters, dateFrom, dateTo} = this.state;

    if(dateFrom && dateTo && (dateFrom < dateTo)) {
      this.setState({isLoading:true})
      DataService.postAdminContent({
        selectedStationsCheckboxes: selectedStations,
        selectedValuesCheckboxes: selectedParameters,
        startDate: dateFrom,
        endDate: dateTo,
      }).then(
          (response) => {
            this.setState({
              apiData: response.data,
              graphParameters: selectedParameters,
              isLoading: false,
              dataChanged: false
            })
          }
      ).catch(error => {
        this.setState({isLoading: false})
      });
    } else {
      alert("Wybierz datę początkową i datę końcową przed wizualizacją.")
    }

  };

  handleCheckboxChange = (type, value) => {
    this.setState((prevState) => {
      let updatedState = {};

      if (type === "station") {
        updatedState = {
          selectedStations: prevState.selectedStations.includes(value)
              ? prevState.selectedStations.filter((station) => station !== value)
              : [...prevState.selectedStations, value],
          dataChanged: true,
        };
      }
      if (type === "parameter") {
        updatedState = {
          selectedParameters: prevState.selectedParameters.includes(value)
              ? prevState.selectedParameters.filter((param) => param !== value)
              : [...prevState.selectedParameters, value],
          dataChanged: true,
        };
      }

      return {
        ...updatedState,
        wrongData: this.checkDataIsValid({ ...prevState, ...updatedState }),
      };
    });
  };

  checkDataIsValid = ({ selectedParameters, selectedStations }) => {
    return selectedParameters.length === 0 || selectedStations.length === 0;
  };

  handleDatesSelected = (selectedDates) => {
    this.setState({
      dateFrom: selectedDates.startDate,
      dateTo: selectedDates.endDate,
      dataChanged: true
    })
  }

  handleDownloadCsv = () => {
    const { selectedStations, selectedParameters, dateFrom, dateTo, dataChanged} = this.state;
    if(dateFrom && dateTo && !dataChanged) {
      DataService.downloadCsv({
        selectedStationsCheckboxes: selectedStations,
        selectedValuesCheckboxes: selectedParameters,
        startDate: dateFrom,
        endDate: dateTo
      }).then((response) => {
        const blob = new Blob([response.data], { type: 'text/csv'});
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'data.csv';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      }).catch((error) => {
        console.error('Error downloading CSV: ', error);
      })
    }
  }

  handleDownloadInstruction = () => {
    DataService.downloadInstruction()
        .then((response) => {
          const blob = new Blob([response.data], { type: 'application/pdf'});
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'instrukcja.pdf';
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        }).catch((error) => {
      console.error('Error downloading PDF: ', error)
    })
  }

  render() {
    const { selectedStations, selectedParameters} = this.state;

    if (this.state.redirect) {
      return <Navigate to={this.state.redirect} />
    }

    return (

        <div className="board-container">
          <div className="board-admin-container">
            <div className="admin-menu">
              <div className="menu-row">
                <form className="menu-stations-form">
                  <label>
                    <input
                        type="checkbox"
                        name="station"
                        value="Jordanowska"
                        className="chb-station"
                        id="jordanowska-chb"
                        checked={selectedStations.includes("Jordanowska")}
                        onChange={() => this.handleCheckboxChange("station", "Jordanowska")}
                    />
                    <span className="clickable-span" id="jordanowska-span">Jordanowska</span>
                  </label>
                  <label>
                    <input
                        type="checkbox"
                        name="station"
                        value="Potoczek"
                        className="chb-station"
                        id="potoczek-chb"
                        checked={selectedStations.includes("Potoczek")}
                        onChange={() => this.handleCheckboxChange("station", "Potoczek")}
                    />
                    <span className="clickable-span" id="potoczek-span">Potoczek</span>
                  </label>
                  <label>
                    <input
                        type="checkbox"
                        name="station"
                        value="Opolska"
                        className="chb-station"
                        id="opolska-chb"
                        checked={selectedStations.includes("Opolska")}
                        onChange={() => this.handleCheckboxChange("station", "Opolska")}
                    />
                    <span className="clickable-span" id="opolska-span">Opolska</span>
                  </label>
                </form>
              </div>
              <div className="menu-row">
                <ParameterCheckbox
                    selectedParameters={selectedParameters}
                    handleCheckboxChange={this.handleCheckboxChange}
                />
              </div>
              <div className="menu-row">
                <DatePickerRange onDatesSelected={this.handleDatesSelected}/>
              </div>
              <div className="menu-row">
                <button type="button" disabled={this.state.dataChanged} className= "btn-menu" id="btn-download" onClick={this.handleDownloadCsv}>
                  Pobierz dane CSV
                </button>
                <button type="button" className= "btn-menu" id="btn-download-instruction" onClick={this.handleDownloadInstruction}>
                  Pobierz instrukcję
                </button>
                <button type="button" disabled={this.state.wrongData} className="btn-menu" id="btn-handle-request" onClick={this.handleRequestSubmit}>
                  Wyślij żądanie
                </button>
              </div>
              <div className="menu-row">
                {this.state.dataChanged ? (
                    <h4 className="wrong-data-header">Pobranie pliku CSV możliwe po zaktualizowaniu wykresów</h4>
                ) : (
                    <h4></h4>
                )}
              </div>
              <div className="menu-row">
                {this.state.wrongData ? (
                    <h4 className="wrong-data-header">Wybierz co najmniej jedną stację i jeden parametr</h4>
                ) : (
                    <h4>Wartości reprezentujące czas dla każdego z odczytów zaokrąglone są do pełnych 10 minut</h4>
                )}


              </div>
            </div>
            <div className="graphs-content">
              {this.state.isLoading ? (
                  <Spinner />
              ): (
                  <Graph data={this.state.apiData} params={this.state.graphParameters} />
              )}
            </div>
          </div>
        </div>
    );
  }
}
