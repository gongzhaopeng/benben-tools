import json
import logging

logging.basicConfig(level=logging.INFO)

with open('districts.json', 'r') as dfile:
    districts = json.load(dfile)
    for province in districts:
        for city in districts[province]:
            if len(dict(districts[province][city]).keys()) == 0:
                districts[province][city][city] = []

with open('tidied_districts.json', 'w') as out_file:
    json.dump(districts, out_file, ensure_ascii=False, indent=4)
