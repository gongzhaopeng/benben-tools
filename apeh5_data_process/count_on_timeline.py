import pymongo
import pprint
import time
import csv


def str_date(ms_timestamp):
    return time.strftime('%Y%m%d', time.localtime(ms_timestamp/1000))


MONGO_URI = 'mongodb://admin:abc123def456@localhost:27017/admin'
MONGO_DATABASE = 'intent_ape'
MONGO_COLLECTION = 'User'

client = pymongo.MongoClient(MONGO_URI)
db = client[MONGO_DATABASE]
coll = db[MONGO_COLLECTION]

counts_by_date = {}


def get_date_count_obj(date):
    try:
        counts_by_date[date]
    except Exception:
        counts_by_date[date] = {'reg': 0, 'finish': 0}
    return counts_by_date[date]


for user in coll.find():
    reg_date = str_date(user['createTime'])
    get_date_count_obj(reg_date)['reg'] += 1

    try:
        finish_date = str_date(user['assessments'][0]['createTime'])
        get_date_count_obj(finish_date)['finish'] += 1
    except Exception:
        pass

with open('counts_on_timeline.csv', 'w') as f_cot:
    writer = csv.writer(f_cot)
    header_row = ['timespan', 'reg', 'finish']
    writer.writerow(header_row)
    for date in sorted(counts_by_date.keys()):
        out_date = date + " 00:00:00-23:59:59"
        writer.writerow([
            out_date,
            counts_by_date[date]['reg'],
            counts_by_date[date]['finish']
        ])
