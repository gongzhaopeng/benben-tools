import pymongo
import pprint
import csv


READING_ID_1 = "58ca92865bb5c32c64fd9c6b"
READING_ID_2 = "58ca92865bb5c32c64fd9d39"
RECOGNITION_ID = "5947bb416813090f08eee942"
PARAGRAPH_ID = "59c4cde6accc7b7dff6bf242"
VOCABULARY_ID = "5a0908b6accc7b33d7942891"


MONGO_URI = 'mongodb://root:F0rgzfkcs@xquiz.benbenedu.cn:27017/xquiz3'
MONGO_DATABASE = 'xquiz3'
USER_COLLECTION = 'asmt_readingUser'
RESULT_COLLECTION = 'asmt_result'

schools = [
    u"文海实验学校",
    # u"广安友谊中学实验学校",
    # u"郑州丽水外国语学校",
    # u"通州区第二中学",
    # u"杭州市西溪实验学校"
]

client = pymongo.MongoClient(MONGO_URI)
db = client[MONGO_DATABASE]
user_coll = db[USER_COLLECTION]
result_coll = db[RESULT_COLLECTION]


def generate_exception_info(school):
    e_table = []

    users = list(user_coll.find({'schoolName': school, 'only': '1'}))
    for user in users:
        open_id = user['openid']
        user_test_ids = [result['testId'] for result in result_coll.find({'takerId': open_id})]

        user_row = [open_id, user['name'], user['grade'], "X", "X", "X", "X"]
        if READING_ID_1 in user_test_ids or READING_ID_2 in user_test_ids:
            user_row[3] = 'O'
        if RECOGNITION_ID in user_test_ids:
            user_row[4] = 'O'
        if PARAGRAPH_ID in user_test_ids:
            user_row[5] = 'O'
        if VOCABULARY_ID in user_test_ids:
            user_row[6] = 'O'

        e_table.append(user_row)

    return e_table


for school in schools:
    exception_table = generate_exception_info(school)

    with open(u'{}异常学生信息表.csv'.format(school), 'w') as f_cot:
        writer = csv.writer(f_cot)
        header_row = ['OPEN_ID', "NAME", 'GRADE', 'READING', 'RECOGNITION', 'PARAGRAPH', 'VOCABULARY']
        writer.writerow(header_row)
        for user_row in exception_table:
            writer.writerow(user_row)
