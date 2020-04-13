# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html


import pymongo


class EduJobuiComPipeline(object):
    def process_item(self, item, spider):
        return item


class MongoPipeline(object):

    def __init__(self, mongo_uri, mongo_db,
                 mongo_coll_spec_job_benke,
                 mongo_coll_spec_job_zhuanke):
        self.mongo_uri = mongo_uri
        self.mongo_db = mongo_db
        self.mongo_coll_spec_job_benke = mongo_coll_spec_job_benke
        self.mongo_coll_spec_job_zhuanke = mongo_coll_spec_job_zhuanke

        self.client = None
        self.db = None

    @classmethod
    def from_crawler(cls, crawler):
        return cls(
            mongo_uri=crawler.settings.get('MONGO_URI'),
            mongo_db=crawler.settings.get('MONGO_DATABASE'),
            mongo_coll_spec_job_benke=crawler.settings.get('MONGO_COLLECTION_SPEC_JOB_BENKE'),
            mongo_coll_spec_job_zhuanke=crawler.settings.get('MONGO_COLLECTION_SPEC_JOB_ZHUANKE')
        )

    def open_spider(self, spider):
        self.client = pymongo.MongoClient(self.mongo_uri)
        self.db = self.client[self.mongo_db]

    def close_spider(self, spider):
        self.client.close()

    def process_item(self, item, spider):
        edu_level = item['eduLevel']
        if 'benke' == edu_level:
            mongo_coll = self.mongo_coll_spec_job_benke
        elif 'zhuanke' == edu_level:
            mongo_coll = self.mongo_coll_spec_job_zhuanke
        else:
            raise ValueError(f'Unknown edu level: {edu_level}')

        self.db[mongo_coll].update_one(
            {'url': item['url']},
            {"$set": item},
            upsert=True)
        # self.db[self.mongo_coll].insert_one(item)
        return item
