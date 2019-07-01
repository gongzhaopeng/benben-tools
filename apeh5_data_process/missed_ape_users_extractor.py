import csv
import pprint
import base64

ape_user_ids = set()
with open('ytkuserid_grade_score.csv') as f_ape_users:
    reader = csv.reader(f_ape_users)
    header_row = next(reader)
    for row in reader:
        ape_user_ids.add(row[0])

pprint.pprint("ape users count: " + str(len(ape_user_ids)))

assessment_owners = set()
with open('unique_assessment_owners.txt') as f_assessment_owners:
    for row in f_assessment_owners:
        try:
            assessment_owners.add(base64.b64decode(row[:-1]))
        except Exception:
            pass

pprint.pprint('valid assessment owners count: ' + str(len(assessment_owners)))

matched_assessment_owners = assessment_owners & ape_user_ids

pprint.pprint('matched assessment owners count: ' + str(len(matched_assessment_owners)))
pprint.pprint('matched assessment owners:')
pprint.pprint(matched_assessment_owners)

with open('matched_assessment_owners_to_ape.csv', 'w') as f_mao:
    writer = csv.writer(f_mao)
    with open('ytkuserid_grade_score.csv', 'r') as f_ape_users:
        reader = csv.reader(f_ape_users)
        header_row = next(reader)
        header_row.insert(1, 'inner_id')
        writer.writerow(header_row)
        for row in reader:
            ape_id = row[0]
            if ape_id in matched_assessment_owners:
                row.insert(1, base64.b64encode(ape_id))
                writer.writerow(row)
