import React, { Component } from "react";
import DataService from "../services/data.service";
import MapContainerComponent from "./map-container";
import Spinner from "./spinner.js";

export default class MapGraph extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: "",
      selectedStations: ["Jordanowska", "Opolska", "Potoczek"],
      selectedParameters: ["air_temperature", "precipitation"],
      apiData: null,
    };
  }

  componentDidMount() {
    DataService.postMapContent({
      selectedStationsCheckboxes: this.state.selectedStations,
      selectedValuesCheckboxes: this.state.selectedParameters
    }).then(
      (response) => {
        this.setState({ 
          apiData: response.data,
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
        });
      }
    );
  }

  render() {
    return (
      <div className="map-graph-pre-container">
        {this.state.apiData === null ? (<Spinner />) : (
          <div className="map-graph-container">
            <div className="div_info block1"><h2>OPIS</h2><p>Sudół lub Sudoł – potok w województwie małopolskim, wypływa w Giebułtowie, przepływa przez Kraków (Tonie, Prądnik Biały). Prawy dopływ rzeki Białuchy. Długość 9 km. Powierzchnia zlewni około 18.3 km<sup>2</sup>. Od roku 2019 na terenie zlewni potoku Sudół Politechnika Krakowska zlokalizowała trzy automatyczne posterunki pomiarowe:
              <li>posterunek Jordanowska</li>
              <li>posterunek Opolska</li>
              <li>posterunek Potoczek</li>
              Wszystkie posterunki posiadają czujniki pracujące w systemie automatycznym pozwalającym na gromadzenia danych: opad, odległości do zwierciadła wody, temperatura powietrza, nasłonecznienie, wilgotność i ciśnienie powietrza oraz prędkość i kierunek wiatru.  
            </p></div>
            <MapContainerComponent data={this.state.apiData} />
            <div className="div_info block4"><h2>JORDANOWSKA</h2><p>Zlokalizowany w okolicy ul. Jordanowskiej. Zamyka zlewnie o powierzchni około 16.4 km<sup>2</sup>.</p>
            <img className="block-img" src="/img/Jordanowska.jpg" alt="jordanowska-img" />
            </div>
            <div className="div_info block5"><h2>OPOLSKA</h2><p>Zlokalizowany w okolicy ul. Opolskiej (bardzo blisko ujścia cieku do Białuchy). Zamyka zlewnie o powierzchni około 18.2 km<sup>2</sup>.</p>
            <img className="block-img" src="/img/Opolska.jpg" alt="opolska-img" />
            </div>
            <div className="div_info block6"><h2>POTOCZEK</h2><p>Zlokalizowany w okolicy ul. Gaik (położony prostopadle do ul. Na Budzyniu). Zamyka zlewnie o powierzchni około 7 km<sup>2</sup>.</p>
            <img className="block-img" src="/img/Potoczek.jpg" alt="potoczek-img" />
            </div>
          </div>
        )}
      </div>
      );
    }
}
