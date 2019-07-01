import pymongo
import pprint

MONGO_URI = 'mongodb://admin:abc123def456@localhost:27017/admin'
MONGO_DATABASE = 'intent_ape'
MONGO_COLLECTION = 'Assessment'

client = pymongo.MongoClient(MONGO_URI)
db = client[MONGO_DATABASE]
coll = db[MONGO_COLLECTION]

unique_assessment_owners = set()

for assessment in coll.find():
    unique_assessment_owners.add(str(assessment['owner']))

with open("unique_assessment_owners.txt", "w") as outfile:
    for owner in unique_assessment_owners:
        outfile.write(owner + '\n')

pprint.pprint("Total Count: " + str(len(unique_assessment_owners)))
