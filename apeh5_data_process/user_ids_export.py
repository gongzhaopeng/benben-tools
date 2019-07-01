import pymongo
import pprint

MONGO_URI = 'mongodb://admin:abc123def456@localhost:27017/admin'
MONGO_DATABASE = 'intent_ape'
MONGO_COLLECTION = 'User'

client = pymongo.MongoClient(MONGO_URI)
db = client[MONGO_DATABASE]
coll = db[MONGO_COLLECTION]

with open("user_ids.txt", "w") as outfile:
    for user in coll.find():
        outfile.write(str(user['_id']) + '\n')

pprint.pprint("Total Count: " + str(coll.count()))
