import json
import logging

logging.basicConfig(level=logging.INFO)

with open('districts.json', 'r') as dfile:
    with open('school.json', 'r') as sfile:
        districts = json.load(dfile)
        schools = json.load(sfile)

        logging.info(districts)
        logging.info(schools)

        out_schools = dict(districts)

        logging.info("Begin generating...")

        for province in schools:
            for city in schools[province]:
                for county in schools[province][city]:
                    try:
                        out_schools[province][city][county] = \
                            schools[province][city][county]
                    except Exception:
                        logging.info(f"Exception for {province}:{city}:{county}")

        with open('school_dict.json', 'w') as out_file:
            json.dump(out_schools, out_file, ensure_ascii=False, indent=4)
