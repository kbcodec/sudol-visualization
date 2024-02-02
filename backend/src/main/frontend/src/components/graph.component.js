import React, { Component } from 'react';
import Plot from 'react-plotly.js';
import * as localeDictionary from 'plotly.js/lib/locales/pl.js'
import moment from 'moment-timezone';


const paramTranslations = {
  water_state: "Odległość [cm]",
  pressure: "Ciśnienie [hPa]",
  air_temperature: "Temperatura powietrza [°C]",
  air_humidity: "Wilgotność powietrza [%]",
  wind_speed_avg: "Prędkość wiatru średnia [km/h]",
  wind_direction: "Kierunek wiatru [°]",
  solar_radiation: "Prom. słoneczne [W/m2]",
  precipitation: "Opad [mm]",
  soil_temperature: "Temp. wewnętrzna [°C]",
  soil_humidity: "Wilgotność gleby [%Vol]",
  soil_pressure: "Ciśnienie wewnętrzne [hPa]"
}

class Graph extends Component {

    renderCharts() {
        const { data, params } = this.props;
    
        if (!data || data.length === 0 || !params || params.length === 0) {
            return null;
          }
              
        return params.map(param => (
          <div key={param}>
            <h2 className="unique-graph-header">{paramTranslations[param]}</h2>
            <Plot
              data={this.generateChartData(data, param)}
              layout={
                {
                  height: 250,
                  margin: { l: 50, t: 20, r: 45, b: 50 },
                  showlegend: false,
                  xaxis: {
                    linecolor: '#636363',
                    linewidth: 2,
                    tickcolor: '#636363',
                    ticklen: 10,
                    tickwidth: 1,
                    ticks: 'outside',
                    showgrid: true,
                    gridwidth: 1, 
                    gridcolor: '#cdcdcd',
                    tickfont: {
                      size: 14
                    }
                  },
                  yaxis: {
                    fixedrange: true,
                    linecolor: '#636363',
                    linewidth: 2,
                    tickcolor: '#636363',
                    ticklen: 10,
                    tickwidth: 1,
                    ticks: 'outside',
                    showgrid: true,
                    gridwidth: 1, 
                    gridcolor: '#cdcdcd',
                    tickfont: {
                      size: 14
                    }
                  },
                  hovermode: 'x'
                }
                }
              config={{responsive: true, locales: { 'pl': localeDictionary },
              locale: 'pl'}}
            />
          </div>
        ));
      }
    
      generateChartData(data, param) {
        return data.map(station => {
          const stationData = station.data
            .filter(entry => typeof entry[param] !== 'undefined')
            .map(entry => ({
              x: moment.utc(entry.date).tz('Europe/Warsaw').format(),
              y: entry[param],
            }));
      
          const chartType = param === 'precipitation' ? 'bar' : 'lines';
          let lineColor = 'rgb(0, 0, 0)';
          if(station.groupName === 'Jordanowska') {
            lineColor = 'rgb(0, 124, 186)'
          } else if (station.groupName === 'Opolska') {
            lineColor = 'rgb(241, 92, 92)'
          } else {
            lineColor = 'rgb(98, 197, 86)'
          }
          return {
            x: stationData.map(entry => entry.x),
            y: stationData.map(entry => entry.y),
            type: chartType,
            name: station.groupName,
            line: {
              color: lineColor,
              width: 2
            },
            marker: {
              color: lineColor
            }
          };
        });
      }
    
      render() {
        return (
          <div className="graphs-container">
            {this.renderCharts()}
          </div>
        );
      }
    }
    
    export default Graph;