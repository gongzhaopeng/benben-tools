import scrapy
import pymongo


class SpecCutoffCrawler(scrapy.Spider):
    name = 'spec_cutoff'

    def start_requests(self):
        def generate_url(school, year):
            base_url = \
                "https://gaokao.koolearn.com/college/speline/0/0/" \
                    f"{year}/{school}/0"
            return base_url

        _YEAR_DOMAIN = list(range(2008, 2018))

        mongo_client = pymongo.MongoClient(self.settings.get('MONGO_URI'))
        mongo_db = mongo_client[self.settings.get('MONGO_DATABASE')]
        school_coll = mongo_db[self.settings.get('MONGO_COLLECTION_SCHOOL')]

        for school in school_coll.find():
            for year in _YEAR_DOMAIN:
                req_url = generate_url(school['_id'], year)

                yield scrapy.Request(url=req_url, callback=self.parse)

    def parse(self, response):
        pb60 = response.css('div.wrap div#content div.mt20 div.fl div.pb60')

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
