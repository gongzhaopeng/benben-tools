import scrapy
import pymongo
import logging
import random


class SpecCutoffCrawler(scrapy.Spider):
    name = 'spec_cutoff'

    # def start_requests(self):
    #     def generate_url(school, year):
    #         base_url = \
    #             "https://gaokao.koolearn.com/college/speline/0/0/" \
    #                 f"{year}/{school}/0"
    #         return base_url
    #
    #     _YEAR_DOMAIN = list(range(2008, 2018))
    #
    #     mongo_client = pymongo.MongoClient(self.settings.get('MONGO_URI'))
    #     mongo_db = mongo_client[self.settings.get('MONGO_DATABASE')]
    #     school_coll = mongo_db[self.settings.get('MONGO_COLLECTION_SCHOOL')]
    #
    #     self.sc_fail_link_coll = mongo_db[self.settings.get('MONGO_COLLECTION_SPEC_CUTOFF_FAIL_LINK')]
    #
    #     all_school = [s['_id'] for s in school_coll.find()]
    #     random.shuffle(all_school)
    #     # all_school = ['广东财经大学华商学院']
    #
    #     logging.info(f'Year Domain: {_YEAR_DOMAIN}')
    #     logging.info(f'School Count: {len(all_school)}')
    #     logging.info(f'School Domain: {all_school}')
    #
    #     for school in all_school:
    #         year_domain = list(_YEAR_DOMAIN)
    #         random.shuffle(year_domain)
    #         for year in year_domain:
    #             req_url = generate_url(school, year)
    #
    #             yield scrapy.Request(url=req_url, callback=self.parse)

    def start_requests(self):
        mongo_client = pymongo.MongoClient(self.settings.get('MONGO_URI'))
        mongo_db = mongo_client[self.settings.get('MONGO_DATABASE')]

        self.sc_fail_link_coll = mongo_db[self.settings.get('MONGO_COLLECTION_SPEC_CUTOFF_FAIL_LINK')]

        self.all_fail_links = set([fl['_id'] for fl in self.sc_fail_link_coll.find()])

        logging.info(f'Fail Link Count: {len(self.all_fail_links)}')

        fail_links = set(self.all_fail_links)
        while len(fail_links) > 0:
            for fl in fail_links:
                yield scrapy.Request(url=fl, callback=self.parse)
            fail_links = set(self.all_fail_links)

    def parse(self, response):
        pb60 = response.css('div.wrap div#content div.mt20 div.fl div.pb60')

        if len(pb60) > 0:
            try:
                self.sc_fail_link_coll.delete_one({'_id': response.url})
            except Exception:
                pass
            try:
                self.all_fail_links.remove(response.url)
            except Exception:
                pass
        else:
            try:
                self.sc_fail_link_coll.insert_one({'_id': response.url})
            except Exception:
                pass
            try:
                self.all_fail_links.add(response.url)
            except Exception:
                pass

        try:
            school_table = list(pb60.css('table.tab2 tr'))[1:]
            for school_item in school_table:
                try:
                    school_tds = school_item.css('td')
                    yield {
                        '_id': {
                            'spec': school_tds[0].css('a::text').get().strip(),
                            'school': school_tds[1].css('::text').get().strip(),
                            'area': school_tds[2].css('::text').get().strip(),
                            'subject': school_tds[3].css('::text').get().strip(),
                            'year': school_tds[5].css('::text').get().strip(),
                            'offer_batch': school_tds[4].css('::text').get().strip()
                        },
                        'average_score': school_tds[6].css('::text').get().strip(),
                        'max_score': school_tds[7].css('::text').get().strip(),
                        'type': 'spec_cutoff'
                    }
                except Exception:
                    pass
        except Exception:
            pass

        try:
            pages = list(pb60.css('div.page a'))
            next_page = [p.css('::attr(href)').get() for p in pages if p.css('::text').get().strip() == '下一页'][0]
            yield response.follow(url=next_page, callback=self.parse)
        except Exception:
            pass
