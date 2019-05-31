# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html

import pymongo


class GaokaoKoolearnComCrawlingPipeline(object):
    def process_item(self, item, spider):
        return item


class MongoPipeline(object):

    def __init__(self, mongo_uri, mongo_db, mongo_coll_school, mongo_coll_spec_cutoff):
        self.mongo_uri = mongo_uri
        self.mongo_db = mongo_db
        self.mongo_coll_school = mongo_coll_school
        self.mongo_coll_spec_cutoff = mongo_coll_spec_cutoff

        self.client = None
        self.db = None

    @classmethod
    def from_crawler(cls, crawler):
        return cls(
            mongo_uri=crawler.settings.get('MONGO_URI'),
            mongo_db=crawler.settings.get('MONGO_DATABASE'),
            mongo_coll_school=crawler.settings.get('MONGO_COLLECTION_SCHOOL'),
            mongo_coll_spec_cutoff=crawler.settings.get('MONGO_COLLECTION_SPEC_CUTOFF')
        )

    def open_spider(self, spider):
        self.client = pymongo.MongoClient(self.mongo_uri)
        self.db = self.client[self.mongo_db]

    def close_spider(self, spider):
        self.client.close()

    def process_item(self, item, spider):
        if item['type'] == 'school':
            coll = self.db[self.mongo_coll_school]
        elif item['type'] == 'spec_cutoff':
            coll = self.db[self.mongo_coll_spec_cutoff]
            del item['type']
        else:
            return item

        item_to_save = dict(item).copy()
        item_id = item_to_save['_id']
        del item_to_save['_id']
        self.db[coll].update_one(
            {'_id': item_id},
            {"$set": item_to_save},
            upsert=True)
        return item
