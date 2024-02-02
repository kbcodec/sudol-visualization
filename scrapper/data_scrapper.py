from urllib.parse import quote
from pymongo import MongoClient
import stations_names_dictionary as stations
import requests
from datetime import datetime, timedelta
from dateutil import parser

client = MongoClient('mongodb://127.0.0.1:33100/SudolDB')
db = client['SudolDB']

def import_data_to_mongodb(endpoint, station_name, field_mapping, channel_id):
    start_date = datetime(2017, 11, 11, 0, 0, 0)
    end_date = datetime.now()

    print(start_date)
    print(end_date)

    last_saved_date = get_last_saved_date(station_name, channel_id)
    if last_saved_date:
        start_date = last_saved_date

    while start_date <= end_date:
        url = f"{endpoint}{channel_id}/feeds.json"
        params = {
            'start': quote(format_date_for_api_param(start_date)),
            'end': quote(format_date_for_api_param(start_date + timedelta(days=30))),
            'limit': 8000
        }

        response = requests.get(url, params=params)

        if response.status_code == 200:
            data = response.json()
            data_records = data['feeds']
            all_records = []

            collection = db[station_name]

            for record in data_records:
                mapped_record = {}
                for db_field, csv_field in field_mapping.items():
                    if csv_field == "date":
                        mapped_record[csv_field] = parser.parse(record[db_field]).strftime("%Y-%m-%d %H:%M:%S UTC")
                    else:
                        value = record[db_field]
                        if value is not None:
                            try:
                                mapped_record[csv_field] = float(value)
                            except ValueError:
                                mapped_record[csv_field] = int(value)
                        else:
                            print(value)
                            mapped_record[csv_field] = None

                mapped_record["channel_id"] = channel_id
                all_records.append(mapped_record)

            try:
                collection.insert_many(all_records)
                print(f"Pobrano dane z endpointu {endpoint}{channel_id} w zakresie od {start_date} do {params['end']} i zapisano do MongoDB dla stacji {station_name}")
            except TypeError as e:
                print(f"Błąd przy dodawaniu rekordów: {str(e)}")

        else:
            print(f"Błąd podczas pobierania danych z endpointu {endpoint}: {response.status_code} {response.text}")

        start_date = start_date + timedelta(days=31)


def get_last_saved_date(station_name, channel_id):
    collection = db[station_name]
    last_record = collection.find_one({"channel_id": channel_id}, sort=[("date", -1)])

    if last_record:
        last_record_date_raw = last_record["date"]
        last_record_date = datetime.strptime(last_record_date_raw, "%Y-%m-%d %H:%M:%S UTC")
        last_record_date += timedelta(seconds=1)
        return last_record_date
    else:
        return None

def format_date_for_api_param(date):
    return date.strftime("%Y-%m-%d %H:%M:%S").replace(" ", "%20")

get_last_saved_date("Jordanowska", 617101)

timestart = datetime.now()

print("Backup start: ", timestart)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Jordanowska", stations.jordanowskaMeteo1, 617101)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Jordanowska", stations.jordanowskaMeteo2, 617103)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Jordanowska", stations.jordanowskaMeteo3, 617107)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Jordanowska", stations.jordanowskaMeteo4, 617093)

import_data_to_mongodb("https://api.thingspeak.com/channels/", "Opolska", stations.opolskaMeteo1, 639606)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Opolska", stations.opolskaMeteo2, 639607)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Opolska", stations.opolskaMeteo3, 639615)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Opolska", stations.opolskaMeteo4, 570456)

import_data_to_mongodb("https://api.thingspeak.com/channels/", "Potoczek", stations.potoczekMeteo1, 639619)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Potoczek", stations.potoczekMeteo2, 639621)
import_data_to_mongodb("https://api.thingspeak.com/channels/", "Potoczek", stations.potoczekMeteo3, 639622)


timeend = datetime.now() - timestart
print('Backup end: ', timeend)

